(ns days.day-07-test
  (:require [days.day-07 :as sut]
            [clojure.test :refer [deftest testing is]]))

(def test-rules ["light red bags contain 1 bright white bag, 2 muted yellow bags."
                 "dark orange bags contain 3 bright white bags, 4 muted yellow bags."
                 "bright white bags contain 1 shiny gold bag."
                 "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags."
                 "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags."
                 "dark olive bags contain 3 faded blue bags, 4 dotted black bags."
                 "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags."
                 "faded blue bags contain no other bags."
                 "dotted black bags contain no other bags."])

(def rule-no-bags "light red bags contain no other bags.")

(def rule-1-single-bag "light red bags contain 1 bright white bag.")

(def rule-2-single-bags "light red bags contain 24 bright white bags.")

(def rule-multi-bags "light red bags contain 1 bright white bag, 2 muted yellow bags.")

(def parsed-bag [:S
                 "light red"
                 [:INNER_BAGS
                  [:ANOTHER_BAG "1" "bright white"]
                  [:ANOTHER_BAG "2" "muted yellow"]]])

(def parsed-empty-bag [:S "faded blue"])

(def bag {"light red" {"bright white" 1
                       "muted yellow" 2}})

(def empty-bag {"faded blue" {}})

(deftest day-07-test
  (testing "create-bag"
    (is (= bag (sut/create-bag parsed-bag)))
    (is (= empty-bag (sut/create-bag parsed-empty-bag)))))

