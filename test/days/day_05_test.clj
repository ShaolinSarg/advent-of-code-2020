(ns days.day-05-test
  (:require [clojure.test :refer [is deftest testing]]
            [days.day-05 :as sut]))

(deftest day-05-test
  (testing "split-rows-columns"
    (testing "should return the row and column instructions"
      (is (= ['(\F \B \F \B \F \B \F) '(\L \R \L)] (sut/split-rows-columns "FBFBFBFLRL")))))

  (testing "chop-space"
    (testing "should return the right value for the given space and instructions"
      (is (= 1 (sut/chop-space '(\F \B) 4)))
      (is (= 0 (sut/chop-space '(\F \F \F \F \F \F \F) 128)))
      (is (= 127 (sut/chop-space '(\B \B \B \B \B \B \B) 128)))
      (is (= 7 (sut/chop-space '(\R \R \R) 8))))))
