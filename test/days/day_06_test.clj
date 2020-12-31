(ns days.day-06-test
  (:require [clojure.test :refer [is deftest testing]]
            [days.day-06 :as sut]))

(def test_answers ["abc"
                   ""
                   "a"
                   "b"
                   "c"
                   ""
                   "ab"
                   "ac"
                   ""
                   "a"
                   "a"
                   "a"
                   "a"
                   ""
                   "b"])


(def grouped-answers
  ["abc" "abc" "abac" "aaaa" "b"])

(deftest day-06-test
  (testing "group-fields"
    (testing "should return all of a parties answers"
      (is (= grouped-answers (sut/group-fields test_answers)))))

  (testing "count answers"
    (testing "should return the correct counts for a group's answers"
      (is (= '(3 3 3 1 1) (sut/count-group-answers grouped-answers)))))

  (testing "count all yes"
    (testing "should return the count for all questions the party answered yes to"
      (is (= 6 (sut/count-party-answers test_answers)))))

  (testing "answer-counts"
    (is (= 3 (sut/answer-counts [1 "abc"])))))
