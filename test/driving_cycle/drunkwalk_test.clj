(ns driving-cycle.drunkwalk-test
  (:require [clojure.test :refer :all]
            [driving-cycle.drunkwalk :refer :all]))

(deftest filter-outside-test
  (testing "the basics"
    (is (= '(5 6) (filter-outside 0 10 '(5 6)))
        "nothing to filter")
    (is (= '(6) (filter-outside 0 10 '(6 12)))
        "filter value above upper bound")
    (is (= '(6) (filter-outside 3 10 '(1 6)))
        "filter value below lower bound")
    )
  (testing "edge cases"
    (is (= '(5 6) (filter-outside 5 6 '(5 6)))
        "values on bounds")
    (is (= '() (filter-outside 0 10 '(61 12)))
        "all values above upper bound")
    (is (= '() (filter-outside 10 20 '(1 6)))
        "all values below lower bound")
    )
  )

(deftest range-around-test
  (testing "the basics"
    (is (= '(1 2 3) (range-around 2))
        "basic example")
    (is (= '(1999 2000 2001) (range-around 2000))
        "basic example with slightly higher numbers")
    )
  )
