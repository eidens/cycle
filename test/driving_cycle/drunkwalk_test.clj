(ns driving-cycle.drunkwalk-test
  (:require [clojure.test :as test]
            [driving-cycle.drunkwalk :as drunkwalk]))

(deftest filter-outside-test
  (testing "the basics"
    (test/is (= '(5 6) (drunkwalk/filter-outside 0 10 '(5 6)))
             "nothing to filter")
    (test/is (= '(6) (drunkwalk/filter-outside 0 10 '(6 12)))
             "filter value above upper bound")
    (test/is (= '(6) (drunkwalk/filter-outside 3 10 '(1 6)))
             "filter value below lower bound")
    )
  (testing "edge cases"
    (test/is (= '(5 6) (drunkwalk/filter-outside 5 6 '(5 6)))
             "values on bounds")
    (test/is (= '() (drunkwalk/filter-outside 0 10 '(61 12)))
             "all values above upper bound")
    (test/is (= '() (drunkwalk/filter-outside 10 20 '(1 6)))
             "all values below lower bound")
    )
  )

(deftest range-around-test
  (testing "the basics"
    (test/is (= '(1 2 3) (range-around 2))
             "basic example")
    (test/is (= '(1999 2000 2001) (range-around 2000))
             "basic example with slightly higher numbers")
    )
  )
