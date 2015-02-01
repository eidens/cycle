(ns driving-cycle.core-test
  (:require [clojure.test :refer :all]
            [driving-cycle.core :refer :all]))

(deftest inside-bounds-test
  (testing "the basics"
    (is (= true (inside-bounds 4 3 5)) "number within bounds")
    (is (= false (inside-bounds 1 3 5)) "number below lower bound")
    (is (= false (inside-bounds 6 3 5)) "number above upper bound")
    )
  (testing "edge cases"
    (is (= true (inside-bounds 3 3 5)) "number on lower bound")
    (is (= true (inside-bounds 5 3 5)) "number on upper bound")
    )
  )

(deftest filter-outside-test
  (testing "the basics"
    (is (= '(5 6) (filter-outside 0 10 '(5 6))) "nothing to filter")
    (is (= '(6) (filter-outside 0 10 '(6 12))) "filter value above upper bound")
    (is (= '(6) (filter-outside 3 10 '(1 6))) "filter value below lower bound")
    )
  (testing "edge cases"
    (is (= '(5 6) (filter-outside 5 6 '(5 6))) "values on bounds")
    (is (= '() (filter-outside 0 10 '(61 12))) "all values above upper bound")
    (is (= '() (filter-outside 10 20 '(1 6))) "all values below lower bound")
    )
  )

(deftest range-around-test
  (testing "the basics"
    (is (= '(1 2 3) (range-around 2)) "basic example")
    (is (= '(1999 2000 2001) (range-around 2000)) "basic example with slightly higher numbers")
    )
  )

(deftest remember-last-result-test
  (testing "stateful functions"
    (let [func (fn [prev] (* prev -1))
          testfunc (remember-last-result func 1)]
      (is (= -1 (testfunc)) "first invocation: 1 * -1")
      (is (= 1 (testfunc)) "second invocation: 1 * -1 * 1")
      (is (= -1 (testfunc)) "second invocation: 1 * -1 * 1 * -1")
     )
    )
  )

(deftest rand-cycle-test
  (testing "rand-cycle"
    (let [func (rand-cycle 1 3)]
      (for [x (range 10000)]
        (is (contains? '(1 2 3) (func)))  
        )
      )
    )
  )

(deftest cycle-data-test
  (testing "repeatedly rand-cycle"
    (let [seq (cycle-data 1 3)
          generated (take 10000 seq)]
      (is (empty? (filter (fn [val] (not (or (= val 1)
                                             (= val 2)
                                             (= val 3))))
                          generated)))
      )
    )
  )
