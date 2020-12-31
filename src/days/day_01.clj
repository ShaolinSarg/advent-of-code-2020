(ns days.day-01
  (:require [utils.core :refer [read-input]]
            [clojure.math.combinatorics :refer [combinations]]))

(def expenses
  (map #(Integer/parseInt %) (read-input "resources/day-01-input")))

(defn find-tuple [ex target-value nums]
  (->> (combinations ex nums)
       (filter #(= target-value (apply + %)))
       (first)
       (into #{})))

(defn day-1-part-1-answer []
  (apply * (find-tuple expenses 2020 2)))

(defn day-1-part-2-answer []
  (apply * (find-tuple expenses 2020 3)))
