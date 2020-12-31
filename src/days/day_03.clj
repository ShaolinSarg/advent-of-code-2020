(ns days.day-03
  (:require [utils.core :refer [read-input]]))

(def trees
  (read-input "resources/day-03-input"))


(defn tree-hit? [row index]
  (let [space (first (drop index (cycle row)))]
    (= \# space)))


(defn tree-hit-count [field right down]
  (loop [tx (drop down field)
         count 0
         index right]

    (if (seq tx)
      (let [row (first tx)
            tree? (tree-hit? row index)]
        (recur (drop down tx)
               (if tree? (inc count) count)
               (+ index right)))

      count)))


(defn day-3-part-1-answer []
  (tree-hit-count trees 3 1))

(defn day-3-part-2-answer []
  (* (tree-hit-count trees 1 1)
     (tree-hit-count trees 3 1)
     (tree-hit-count trees 5 1)
     (tree-hit-count trees 7 1)
     (tree-hit-count trees 1 2)))
