(ns utils.answers
  (:require [days.day-01 :refer [day-1-part-1-answer
                                 day-1-part-2-answer]]
            [days.day-02 :refer [day-2-part-1-answer
                                 day-2-part-2-answer]]
            [days.day-03 :refer [day-3-part-1-answer
                                 day-3-part-2-answer]]
            [days.day-04 :refer [day-4-answer]]
            [days.day-05 :refer [day-5-part-1-answer
                                 day-5-part-2-answer]]
            [days.day-06 :refer [day-6-part-1-answer
                                 day-6-part-2-answer]]
            [days.day-07 :refer [day-7-part-1-answer
                                 day-7-part-2-answer]]))


(defn -main [& args]
  (println)
  (println "# Sarg's answers to the advent of code 2020")
  (println)
  (println (str "Day 1 part 1 [" (day-1-part-1-answer) "]"))
  (println (str "Day 1 part 2 [" (day-1-part-2-answer) "]"))
  (println)
  (println (str "Day 2 part 1 [" (day-2-part-1-answer) "]"))
  (println (str "Day 2 part 2 [" (day-2-part-2-answer) "]"))
  (println)
  (println (str "Day 3 part 1 [" (day-3-part-1-answer) "]"))
  (println (str "Day 3 part 2 [" (day-3-part-2-answer) "]"))
  (println)
  (println (str "Day 4 part 1 [247]"))
  (println (str "Day 4 part 2 [" (day-4-answer) "]"))
  (println)
  (println (str "Day 5 part 1 [" (day-5-part-1-answer) "]"))
  (println (str "Day 5 part 2 [" (day-5-part-2-answer) "]"))
  (println)
  (println (str "Day 6 part 1 [" (day-6-part-1-answer) "]"))
  (println (str "Day 6 part 2 [" (day-6-part-2-answer) "]"))
  (println)
  (println (str "Day 7 part 1 [" (day-7-part-1-answer) "]"))
  (println (str "Day 7 part 2 [" (day-7-part-2-answer) "]"))
  (println))
