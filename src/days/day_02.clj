(ns days.day-02
  (:require [utils.core :refer [read-input]]))

(def passwords
  (read-input "resources/day-02-input"))

(def r #"^(\d+)-(\d+)\s(\w):\s(\w+)$")


(defn parse-password [inp]
  (let [[_ min max c password] (re-matches r inp)]
    {:min (Integer/parseInt min)
    :max (Integer/parseInt max)
     :c (.charAt c 0)
    :password password
    :counts (frequencies password)}))


(defn valid-char-count? [p]
  (<= (:min p)
      (get (:counts p) (:c p) 0)
      (:max p)))


(defn valid-char-posn? [p]
  (let [posn-1 (dec (:min p))
        posn-2 (dec (:max p))
        c (:c p)
        password (:password p)
        matches [(= c (.charAt password posn-1))
                 (= c (.charAt password posn-2))]]

    (true? (and (some true? matches)
                (not (every? true? matches))))))


(defn day-2-part-1-answer []
  (->> passwords
       (map parse-password)
       (filter valid-char-count?)
       count))

(defn day-2-part-2-answer []
  (->> passwords
       (map parse-password)
       (filter valid-char-posn?)
       count))
