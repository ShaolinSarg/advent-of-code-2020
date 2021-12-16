(ns days.day-10-test
  (:require [clojure.test :refer [deftest testing is]]))

(def test-adapters
  [16
   10
   15
   5
   1
   11
   7
   19
   6
   12
   4])


(defn tool-joltage [adapters]
  (->> adapters
       (apply max)
       (+ 3)))


(tool-adapter test-adapters)

