(ns days.day-10
  (:require [neo4j-clj.core :as neo]
            [neo4j-clj.in-memory :as neo-mem]
            [utils.core :refer [read-input]])
  (:import java.net.URI))


(def adapters
  (map #(Integer/parseInt %)
       (read-input "resources/day-10-input")))

(def local-db
  (neo/connect (URI. "bolt://localhost:7687")
              "neo4j"
              "s3cr3t"))

#_(def local-db
  (neo-mem/create-in-memory-connection))

(neo/defquery delete-all-nodes-and-edges
  "MATCH (n) DETACH DELETE n")

(neo/defquery create-adapter
  "CREATE (a:adapter $adapter)")

(neo/defquery create-edge
  "MATCH (f:adapter {joltage: $props.fromAdapter})
   MATCH (t:adapter {joltage: $props.toAdapter})
   MERGE (f)-[e:next {difference: $props.difference}]->(t)")

(neo/defquery create-min-spanning-tree
  "MATCH (n:adapter {joltage: $props.joltage})
  CALL gds.alpha.spanningTree.minimum.write({
  startNodeId: id(n),
  nodeProjection: 'adapter',
  relationshipProjection: {
    EROAD: {
      type: 'next',
      properties: 'difference',
      orientation:  'UNDIRECTED'
    }
  },
  relationshipWeightProperty: 'difference',
  writeProperty: 'MINST',
  weightWriteProperty: 'cost'
  })
  YIELD createMillis, computeMillis, writeMillis, effectiveNodeCount
  RETURN createMillis, computeMillis, writeMillis, effectiveNodeCount;")


(neo/defquery min-spanning-tree
  "MATCH path = (n:adapter {joltage: $joltage})-[:MINST*]-()
  WITH relationships(path) AS rels
  UNWIND rels AS rel
  WITH DISTINCT rel AS rel
  RETURN startNode(rel).joltage AS src,
  endNode(rel).joltage AS dst,
  toInteger(rel.cost) AS difference;")


(neo/defquery bfs
  "MATCH (wall:adapter {joltage: $wallJoltage}), (tool:adapter {joltage: $toolJoltage})
  WITH id(wall) AS startNode, [id(tool)] AS targetNodes
  CALL gds.alpha.bfs.stream('Advent of code', {startNode: startNode, targetNodes: targetNodes})
  YIELD path
  RETURN path;")

(neo/defquery create-index
  "CREATE INDEX ON :adapter(joltage)")

(neo/defquery get-adapter
  "MATCH (a:adapter {joltage: $joltage}) RETURN a")

(neo/defquery get-paths
  "MATCH p = (w:adapter {joltage: 0})-[:next*]->(t: {joltage: $toolJoltage}) RETURN p")

(def test-adapters 
  [16 10 15 5 1 11
   7 19 6 12 4])


(def test-adapters-bigger
  [28 33 18 42 31 14 46 20 48 47
   24 23 49 45 19 38 39 11 1 32
   25 35 8 17 7 9 4 2 34 10 3])

(defn tool-joltage [adapters]
  (->> adapters
       (apply max)
       (+ 3)))

(defn all-adapters [adapters]
  (conj adapters 0 (tool-joltage adapters)))


(defn add-edge! [from to difference]
  (with-open [session (neo/get-session local-db)]
    (create-edge session {:props {:fromAdapter from
                                     :difference difference
                                     :toAdapter to}})))

(defn next-adapters [adapter adapters]
  (filter #(<= (+ 1 adapter) % (+ 3 adapter)) adapters))

(defn add-edges-for-adapter [adapter adapters]
  (let [neighbours (next-adapters adapter adapters)]
    (doseq [neighbour neighbours]
      (add-edge! adapter neighbour (- neighbour adapter)))))


(defn add-adapters! [adapters]
  (doseq [adapter adapters]
    (with-open [session (neo/get-session local-db)]
      (create-adapter session {:adapter {:joltage adapter}}))))

(defn add-connections! [adapters]
  (doseq [adapter adapters]
    (add-edges-for-adapter adapter adapters)))

(defn reset-db! []
  (with-open [session (neo/get-session local-db)]
    (delete-all-nodes-and-edges session)))

(defn create-graph [adapters]
  (let [socket-and-tool-and-adapters (all-adapters adapters)]
    (reset-db!)
    (add-adapters! socket-and-tool-and-adapters)
    (add-connections! socket-and-tool-and-adapters)))

#_(create-graph test-adapters-bigger)


(defn create-path-for-all! [start]
  (with-open [session (neo/get-session local-db)]
    (create-min-spanning-tree session {:props {:joltage start}})))

(defn get-adapter-by-joltage [joltage]
  (let [return (atom nil)]
    (with-open [session (neo/get-session local-db)]
      (doseq [x (get-adapter session {:joltage joltage})]
        (swap! return conj x)))
    return))

(defn get-path-for-all [joltage]
  (let [return (atom nil)]
    (with-open [session (neo/get-session local-db)]
      (doseq [x (min-spanning-tree session {:joltage joltage})]
        (swap! return conj x)))
    return))

(defn get-all-paths [adapters]
  (let [return (atom nil)]
    (with-open [session (neo/get-session local-db)]
      (doseq [x (bfs session {:wallJoltage 0 :toolJoltage (tool-joltage adapters)})]
        (swap! return conj x)))
    return))

(defn init-db! []
  (do
    (reset-db!)
    #_(create-index)
    (create-graph adapters)))

(defn day-10-part-1-answer []
  (do
    (init-db!)
    (create-path-for-all! 0)
    (->> @(get-path-for-all 0)
         (group-by :difference)
         (map #(count (val %)))
         (apply *))))


(defn day-10-part-2-answer []
  (do
    (init-db!)
    (get-all-paths adapters)))


(defn find-all-paths
  ([node nodes]
   (find-all-paths node nodes [] []))

  ([node nodes path paths]
   (if (= (apply max nodes) node)
     (conj paths (conj path node))
     (let [branch-nodes (next-adapters node nodes)]
       (if (= 1 (count branch-nodes))
         (find-all-paths (first branch-nodes)
                nodes
                (conj path node)
                paths)

         (mapcat #(find-all-paths % nodes (conj path node) paths)
                 branch-nodes))))))

#_(count (find-all-paths 0 (all-adapters test-adapters)))
#_(count (find-all-paths 0 (all-adapters test-adapters-bigger)))
#_(count (find-all-paths 0 (all-adapters adapters)))
