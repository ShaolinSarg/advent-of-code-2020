(ns days.day-08
  (:require [utils.core :refer [read-input]]))

(def instructions
  (read-input "resources/day-08-input"))

(defn parse-instruction [instruction]
  (let [[_ op amount] (re-find #"(nop|acc|jmp) (.+$)" instruction)]
    [(keyword op) (Integer/parseInt amount)]))


(defn init-state [instructions]
  {:instructions (conj (into [] (map parse-instruction instructions)) [:trm 0])
   :instruction-counts {}
   :accumulator 0
   :current-instruction 0
   :terminate false})


(defn jmp [m amount]
  (if (zero? amount)
    (nop m)
    (update m :current-instruction + amount)))

(defn nop [m]
  (jmp m 1))

(defn acc [m amount]
  (-> m
      (update :accumulator + amount)
      nop))

(defn trm [m]
  (assoc m :terminate true))


(defn init-code [s]
  (-> s
      (assoc :nop (fn [_] (nop s)))
      (assoc :jmp (fn [amount] (jmp s amount)))
      (assoc :acc (fn [amount] (acc s amount)))
      (assoc :trm (fn [_] (trm s)))))


(defn nxt [m]
  (let [current-index (:current-instruction m)
        [op amount] (get (:instructions m) current-index)
        s ((op m) amount)
        instruction-count (inc (get (:instruction-counts m) current-index 0))]

        (-> s
          (assoc-in [:instruction-counts current-index] instruction-count)
          init-code)))


(defn int-code [instructions]
  (-> instructions
      init-state
      init-code))

(defn nop-or-jump? [instruction]
  (case (first instruction)
    :acc false
    :trm false
         true))

(defn flip-nop-jmp [instruction]
  (case (first instruction)
    :nop [:jmp (second instruction)]
    :jmp [:nop (second instruction)]
    instruction))

(defn run-result [int-code]
  (loop [m int-code]
    (if (true? (:terminate m))
      {:success (:accumulator m)}
      (if (every? #{1} (vals (:instruction-counts m)))
        (recur (nxt m))
        {:fail (:accumulator m)}))))

(defn day-8-part-1-answer []
  (loop [m (int-code instructions)
         s (nxt m)]
    (if (every? #{1} (vals (:instruction-counts s)))
      (recur s (nxt s))
      (:accumulator m))))

(defn day-8-part-2-answer []
  (let [int-code (int-code instructions)
        instruction-count (count (:instructions int-code))]

    (loop [index 0]

      (if (= index instruction-count)
        0
        (if (nop-or-jump? (get-in int-code [:instructions index]))
          (let [result (run-result (init-code (update-in int-code [:instructions index] flip-nop-jmp)))]
            (if (contains? result :success)
              (:success result)
              (recur (inc index))))
          (recur (inc index)))))))
