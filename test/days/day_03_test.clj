(ns days.day-03-test
  (:require [clojure.test :refer [is deftest testing]]
            [days.day-03 :as sut]))

(def test-field
  ["..##......."
  "#...#...#.."
  ".#....#..#."
  "..#.#...#.#"
  ".#...##..#."
  "..#.##....."
  ".#.#.#....#"
  ".#........#"
  "#.##...#..."
  "#...##....#"
  ".#..#...#.#"])


(deftest day-03-tests
  (testing "tree-hit"
    (testing "should indicate if a tree is hit for the given index in the row"
      (is (= true  (sut/tree-hit? ".#.#" 3)))
      (is (= true  (sut/tree-hit? ".#.#" 1)))
      (is (= false (sut/tree-hit? ".#.#" 2))))
    (testing "should indicate if a tree is hit for the given index outside the row"
      (is (= true  (sut/tree-hit? ".#.#" 5)))
      (is (= true  (sut/tree-hit? ".#.#" 11)))
      (is (= false (sut/tree-hit? ".#.#" 12)))))

  (testing "tree-hit-count"
    (testing "should return the right count of trees"
      (is (= 2 (sut/tree-hit-count test-field 1 1)))
      (is (= 7 (sut/tree-hit-count test-field 3 1)))
      (is (= 3 (sut/tree-hit-count test-field 5 1)))
      (is (= 4 (sut/tree-hit-count test-field 7 1)))
      (is (= 2 (sut/tree-hit-count test-field 1 2))))))


