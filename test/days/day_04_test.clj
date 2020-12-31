(ns days.day-04-test
  (:require [clojure.test :refer [is deftest testing]]
            [days.day-04 :as sut]))

(def batch-test
  ["ecl:gry pid:860033327 eyr:2020 hcl:#fffffd"
   "byr:1937 iyr:2017 cid:147 hgt:183cm"
   ""
   "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884"
   "hcl:#cfa07d byr:1929"
   ""
   "hcl:#ae17e1 iyr:2013"
   "eyr:2024"
   "ecl:brn pid:760753108 byr:1931"
   "hgt:179cm"
   ""
   "hcl:#cfa07d eyr:2025 pid:166559648"
   "iyr:2011 ecl:brn hgt:59in"])


(def batch-test-2
  ["ecl:hzl iyr:2017 eyr:2020 hcl:z byr:2003 hgt:163"
   ""
   "hcl:39615c iyr:1934 byr:2009 pid:7752456272 hgt:191cm"
   "eyr:2024 ecl:#5b7a58"])

(def record-1
  "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm")

(def record-2
  "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884 hcl:#cfa07d byr:1929")

(def record-3
  "hcl:#ae17e1 iyr:2013 eyr:2024 ecl:brn pid:760753108 byr:1931 hgt:179cm")

(def record-4
  "hcl:#cfa07d eyr:2025 pid:166559648 iyr:2011 ecl:brn hgt:59in")

(def record-1-2
  "ecl:hzl iyr:2017 eyr:2020 hcl:z byr:2003 hgt:163")

(def record-2-2
  "hcl:39615c iyr:1934 byr:2009 pid:7752456272 hgt:191cm eyr:2024 ecl:#5b7a58")

(def passport-1
  {:ecl "gry"
   :pid "860033327"
   :eyr "2020"
   :hcl "#fffffd"
   :byr "1937"
   :iyr "2017"
   :cid "147"
   :hgt "183cm"})

(def passport-2
  {:ecl "amb"
   :pid "028048884"
   :eyr "2023"
   :hcl "cfa07d"
   :byr "1929"
   :iyr "2013"
   :cid "350"})

(def passport-3
  {:ecl "brn"
   :pid "760753108"
   :eyr "2024"
   :hcl "#ae17e1"
   :byr "1931"
   :iyr "2013"
   :hgt "179cm"})

(def passport-4
  {:ecl "brn"
   :pid "166559648"
   :eyr "2025"
   :hcl "#cfa07d"
   :iyr "2011"
   :hgt "59in"})

(deftest day-04-tests
  (testing "group-fields"
    (testing "should join all passport details into one string"
      (let [result (sut/group-fields batch-test)]
        (is (= record-1 (first result)))
        (is (= record-2 (second result)))
        (is (= record-3 (nth result 2)))
        (is (= record-4 (nth result 3)))
        (is (= 2 (count (sut/group-fields batch-test-2))))
        (is (= record-1-2 (first (sut/group-fields batch-test-2))))
        (is (= record-2-2 (second (sut/group-fields batch-test-2)))))))

  (testing "key-value"
    (testing "should return a key value pair for a given passport and regex"
      (is (= {:byr "1937"} (sut/key-value record-1 sut/birth-year-regex)))
      (is (= {:iyr "2013"} (sut/key-value record-3 sut/issue-year-regex)))
      (is (= {:eyr "2023"} (sut/key-value record-2 sut/expiration-year-regex)))
      (is (= {:hgt "59in"} (sut/key-value record-4 sut/height-regex)))
      (is (= {:hgt "179cm"} (sut/key-value record-3 sut/height-regex)))
      (is (= {:hcl "#fffffd"} (sut/key-value record-1 sut/hair-colour-regex)))
      (is (= {:ecl "brn"} (sut/key-value record-4 sut/eye-colour-regex)))
      (is (= {:pid "028048884"} (sut/key-value record-2 sut/passport-id-regex)))
      (is (= {:cid "350"} (sut/key-value record-2 sut/country-id-regex)))))

  (testing "create-passport"
    (testing "should return a passport map"
      (is (= passport-1 (sut/create-passport record-1)))))

  (testing "valid-passport?"
    (testing "should return true when all mandatory fields are present"
      (is (= true (sut/valid-passport? passport-1)))
      (is (= true (sut/valid-passport? passport-3))))
    (testing "should return false when all mandatory fields are not present"
      (is (= false (sut/valid-passport? passport-2)))
      (is (= false (sut/valid-passport? passport-4)))))

  (testing "valid-birth-year?"
    (testing "should return false for a year out of range"
      (is false? (sut/valid-birth-year? {:byr "1919"}))
      (is false? (sut/valid-birth-year? {:byr "2003"})))
    (testing "should return false when no value is present"
      (is false? (sut/valid-birth-year? {:by "1919"}))
      (is false? (sut/valid-birth-year? {:byr ""})))
    (testing "should return true for a valid year"
      (is true? (sut/valid-birth-year? {:byr "1920"}))
      (is true? (sut/valid-birth-year? {:byr "1925"}))
      (is true? (sut/valid-birth-year? {:byr "2002"}))
      )))
