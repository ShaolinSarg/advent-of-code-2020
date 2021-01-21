(ns days.day-08
  (:require [clojure.set :refer [union]]
            [utils.core :refer [read-input]]))

(def stream
  (map #(Long/parseLong %)
       (read-input "resources/day-09-input")))

(def preamble 25)

(defn chunk [stream]
  (->> stream
       (partition (inc preamble) 1)
       (map (partial split-at preamble))))


(defn sums [members]
  (loop [nums members
         sums #{}]
    (if (seq nums)
      (recur (rest nums)
             (union sums (set (map (partial + (first nums)) (rest nums)))))
      sums)))

(defn invld? [[nums [v & _]]]
  (if (nil? ((sums nums) v))
    v
    nil))

(defn day-9-part-1-answer []
  (->> stream
       chunk
       (map invld?)
       (drop-while nil?)
       (first)))


(defn find-run [target nums]
  (loop [[x & xs :as all] nums
         run nil]
    (if (seq all)
      (let [sum (+ x (apply + run))]
        (cond
          (and (seq run) (= target sum)) (conj run x)
          (> target sum) (recur xs nil)
          :else (recur xs (conj run x))))
      run)))

(defn find-run [target nums]
  (loop [xs nums
         run nil]
    (cond
      (empty? xs) run
      (and (seq run)
           (= target (+ (first xs)
                        (apply + run)))) (conj run (first xs))
      (< target (+ (first xs)
                   (apply + run))) (recur (rest xs) nil)
      :else (recur (rest xs) (conj run (first xs))))))

(defn runs [target nums]
  (loop [xs nums]
    (if-let [run (find-run target xs)]
      run
      (recur (rest xs)))))


(defn day-9-part-2-answer []
  (->> stream
       (runs (day-9-part-1-answer))
       ((juxt (partial apply min) (partial apply max)))
       (apply +)))
