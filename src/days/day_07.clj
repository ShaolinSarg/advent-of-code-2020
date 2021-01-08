(ns days.day-07
  (:require [instaparse.core :as insta]
            [clojurewerkz.ogre.core :as ogre]
            [utils.core :refer [read-input]]
            [clojure.set :refer [difference union]])

  (:import [org.apache.tinkerpop.gremlin.tinkergraph.structure TinkerGraph]
           [org.apache.tinkerpop.gremlin.process.computer.traversal.step.map ShortestPath]
           [org.apache.tinkerpop.gremlin.structure Direction]))

(def bag-rules
  (read-input "resources/day-07-input"))


(def bag-parser (insta/parser
           "S = BAG_COLOUR <CONTAINS> (<NO_BAGS> | INNER_BAGS) <'.'>
            <BAG_COLOUR> = #'\\w+ \\w+'
            INNER_BAGS = (ANOTHER_BAG <', '>?)+
            CONTAINS = ' bags contain '
            NO_BAGS = 'no other bags'
            ANOTHER_BAG = #'\\d+' <' '> BAG_COLOUR <#' bags?'>"))

(defn parse-input [input]
  (map bag-parser input))

(defn create-bag [[_ bag-colour & inner-bags]]
  {bag-colour (into {}
                    (map (fn [[_ count colour]] {colour (Integer/parseInt count)})
                         (rest (first inner-bags))))})

(defn create-bags [rules] (into {} (map create-bag
                                 (parse-input rules))))

(defn add-bags [g bags]
  (doseq [colour (keys bags)]
    (println (str "adding (:bag {colour: " colour "})"))
    (ogre/traverse g
                   (ogre/add-V :bag)
                   (ogre/property :colour colour)
                   (ogre/iterate!))))

(defn add-edge [g from to count]
  (do
    (ogre/traverse g
                   ogre/V
                   (ogre/has :colour from)
                   (ogre/add-E :contains)
                   (ogre/to (ogre/__ (ogre/V) (ogre/has :colour to)))
                   (ogre/property :count count)
                   (ogre/iterate!))

    (ogre/traverse g
                   ogre/V
                   (ogre/has :colour to)
                   (ogre/add-E :contained-in)
                   (ogre/to (ogre/__ (ogre/V) (ogre/has :colour from)))
                   (ogre/iterate!))))

(defn add-contains-edges [g bags]
  (doseq [[bag-colour inner-bags] bags]

    (doseq [[inner-bag-colour count] inner-bags]
      (println (str "adding (" bag-colour ")--[:contains {count:" count "}]-->(" inner-bag-colour ")"))
      (println (str "adding (" inner-bag-colour ")--[:contained-in]-->(" bag-colour ")"))
      (add-edge g bag-colour inner-bag-colour count))))

(defn create-graph [bags]
  (let [g (ogre/traversal (TinkerGraph/open))]
    (add-bags g bags)
    (add-contains-edges g bags)
    g))


(defn containing-bags [g colour]
  (ogre/traverse g
                 ogre/V
                 (ogre/has :colour colour)
                 (ogre/out :contained-in)
                 (ogre/values :colour)
                 (ogre/into-set!)))


(defn find-all-containing-bags [g colour]
  (loop [new-bags (containing-bags g colour)
         bags #{}]
    (if (seq new-bags)
      (recur (into #{} (mapcat (partial containing-bags g) (difference new-bags bags)))
             (union new-bags bags))
      bags)))


(defn bag-contents [g bag]
  (map vals (ogre/traverse g
                           ogre/V
                           (ogre/has :colour bag)
                           (ogre/outE :contains) (ogre/as :e)
                           (ogre/inV) (ogre/as :v)
                           (ogre/select :e :v )
                           (ogre/by :count)
                           (ogre/by :colour)
                           (ogre/into-set!))))

(defn count-contents [g bag]
  (let [contents (bag-contents g bag)
        count 0]
    (if (seq contents)
      (apply + (map #(+ (first %)
                        (* (first %)
                           (count-contents g (second %))))
                    contents))
      0)))

(defn day-7-part-1-answer []
  (let [g (create-graph (create-bags bag-rules))]
    (->> (find-all-containing-bags g "shiny gold")
         count)))

(defn day-7-part-2-answer []
  (let [g (create-graph (create-bags bag-rules))]
    (count-contents g "shiny gold")))
