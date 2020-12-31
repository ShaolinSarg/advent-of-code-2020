(ns days.day-02-test
  (:require [clojure.test :refer [is deftest testing]]
            [days.day-02 :as sut]))

(deftest day-02-tests
  (testing "valid-char-posn?"
    (testing "Should return validation status"
      (is (= true  (sut/valid-char-posn? {:min 1 :max 3 :c \a :password "abcde"})))
      (is (= false (sut/valid-char-posn? {:min 2 :max 9 :c \c :password "ccccccccc"})))
      (is (= false (sut/valid-char-posn? {:min 1 :max 3 :c \b :password "cdefg"}))))))
