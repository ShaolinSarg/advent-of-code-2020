(ns days.day-04
  (:require [utils.core :refer [read-input]]
            [clojure.string :as str]))

(def passports
  (read-input "resources/day-04-input"))

(def birth-year-regex      #"byr:(\d{4})(?:\s|$)")
(def issue-year-regex      #"iyr:(\d{4})(?:\s|$)")
(def expiration-year-regex #"eyr:(\d{4})(?:\s|$)")
(def height-regex          #"hgt:(\d+(?:cm|in))(?:\s|$)")
(def hair-colour-regex     #"hcl:(#[0-9a-f]{6})(?:\s|$)")
(def eye-colour-regex      #"ecl:(amb|blu|brn|gry|grn|hzl|oth)(?:\s|$)")
(def passport-id-regex     #"pid:(\d{9})(?:\s|$)")
(def country-id-regex      #"cid:(\S+)(?:\s|$)")

(defn group-fields [px]
  (->> (partition-by #{""} px)
       (filter (complement #{'("")}))
       (map (partial str/join " "))))

(defn key-value [passport rx]
  (let [k (str/join (take-while #(not= \: %) (str rx)))]
    {(keyword k) (second (re-find rx passport))}))

(defn create-passport [passport]
  (merge
   (key-value passport birth-year-regex)
   (key-value passport issue-year-regex)
   (key-value passport expiration-year-regex)
   (key-value passport height-regex)
   (key-value passport hair-colour-regex)
   (key-value passport eye-colour-regex)
   (key-value passport passport-id-regex)
   (key-value passport country-id-regex)))

(defn in-range? [min max field-value]
  (<= min (Integer/parseInt field-value) max))

(defn- valid-field? [passport key p?]
  (if-let [field (not-empty (key passport))]
    (p? field)
    false))

 (defn valid-birth-year? [passport]
   (valid-field? passport :byr (partial in-range? 1920 2002)))

(defn valid-issue-year? [passport]
  (valid-field? passport :iyr (partial in-range? 2010 2020)))

(defn valid-expiry-year? [passport]
  (valid-field? passport :eyr (partial in-range? 2020 2030)))


(defn valid-hair-colour? [passport]
  (valid-field? passport :hcl (fn [field] true ;mandatory pattern check only no further checks
                                )))

(defn valid-eye-colour? [passport]
  (valid-field? passport :ecl (fn [field] true ;mandatory pattern check only no further checks
                                )))

(defn valid-passport-id? [passport]
  (valid-field? passport :pid (fn [field] true ;mandatory pattern check only no further checks
                                )))

(defn- valid-inches? [height-in-inches]
  (in-range? 59 76 height-in-inches))

(defn- valid-cms? [height-in-cms]
  (in-range? 150 193 height-in-cms))

(defn valid-height? [passport]
  (valid-field? passport :hgt (fn [field]
                                (let [[_ value unit] (re-find #"(\d+)(in|cm)" field)]
                                  (case unit
                                    "cm" (valid-cms? value)
                                    "in" (valid-inches? value))))))

(defn valid-passport? [passport]
  (let [validation-rules [(valid-birth-year? passport)
                          (valid-issue-year? passport)
                          (valid-expiry-year? passport)
                          (valid-height? passport)
                          (valid-hair-colour? passport)
                          (valid-eye-colour? passport)
                          (valid-passport-id? passport)]]

    (every? true? validation-rules)))


(defn day-4-answer []
  (->> passports
       group-fields
       (map create-passport)
       (filter valid-passport?)
       count))
