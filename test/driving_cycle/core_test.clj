(ns driving-cycle.core-test
  (:require [clojure.test :refer :all]
            [driving-cycle.walk :refer :all]))

(deftest proxy-with-prev-result-test
  (testing "stateful functions"
    (let [func (fn [prev] (* prev -1))
          testfunc (proxy-with-prev-result func 1)]
      (is (= -1 (testfunc)) "first invocation: 1 * -1")
      (is (= 1 (testfunc)) "second invocation: 1 * -1 * 1")
      (is (= -1 (testfunc)) "second invocation: 1 * -1 * 1 * -1"))))

(deftest generate-test
  (testing "generation of a lazy sequence"
    (is (= (take 100 (generate #(+ 1 %) 0))
           (range 1 101 1))
        "should add 1 to the previous number")
    (is (every? #(= 3 %) (take 100 (generate (constantly 3) 3)))
        "should always be the same number")))
