(ns days.day-06
  (:require [utils.core :refer [read-input]]
            [clojure.string :as str]))

(def boarding-passes
  (read-input "resources/day-06-input"))

(defn group-fields [px]
  (->> (partition-by #{""} px)
       (filter (complement #{'("")}))
       (map (partial str/join ""))))

(defn group-fields-party [px]
  (->> (partition-by #{""} px)
       (filter (complement #{'("")}))
       (map (juxt count (partial str/join "")))))

(defn count-group-answers [ax]
  (->> ax
       (map (partial into #{}))
       (map count)))


(defn answer-counts [[group-count answers]]
  (->> answers
       frequencies
       (filter #(= group-count (val %)))
       count))

(defn count-party-answers [ax]
  (->> ax
       group-fields-party
       (map answer-counts)
       (apply +)))

(defn day-6-part-1-answer []
  (->> boarding-passes
       group-fields
       count-group-answers
       (apply +)))

(defn day-6-part-2-answer []
  (count-party-answers boarding-passes))
