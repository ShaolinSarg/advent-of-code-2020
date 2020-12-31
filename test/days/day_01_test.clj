(ns days.day-01-test
  (:require [clojure.test :refer [is deftest testing]]
            [days.day-01 :as sut]))

(deftest day-01-tests
  (testing "Should return the tuples"
    (is (= #{}     (sut/find-tuple [] 2020 2)))
    (is (= #{2000 20} (sut/find-tuple [2000 20] 2020 2)))
    (is (= #{1721 299} (sut/find-tuple [1721 979 366 299 675 1456] 2020 2)))
    (is (= #{979 366 675} (sut/find-tuple [1721 979 366 299 675 1456] 2020 3)))))
