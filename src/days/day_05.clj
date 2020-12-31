(ns days.day-05
  (:require [utils.core :refer [read-input]]))

(def boarding-passes
  (read-input "resources/day-05-input"))

(defn split-rows-columns [input]
  (split-at 7 input))

(def chop {\F take
           \B drop
           \R drop
           \L take})

(defn chop-space [instructions search-size]
  (first (reduce (fn [acc item]
                    (let [n (/ (count acc) 2)]
                      ((get chop item) n acc)))
                  (range search-size)
                  instructions)))


(defn find-seat-id [[row-instructions col-instructions]]
  (let [row (chop-space row-instructions 128)
        col (chop-space col-instructions 8)]
    (+ col (* 8 row))))

(defn day-05-part-1-answer []
  (->> boarding-passes
       (map split-rows-columns)
       (map find-seat-id)
       (apply max)))

(defn day-05-part-2-answer []
  (let [ordered-cards (->> boarding-passes
                          (map split-rows-columns)
                          (map find-seat-id)
                          (sort))]

       (loop [cards ordered-cards
              missing 0]

         (if ((complement zero?) missing)
           missing
           (recur (rest cards)
                  (if (= (inc (first cards)) (second cards))
                    0
                    (inc (first cards))))))))


