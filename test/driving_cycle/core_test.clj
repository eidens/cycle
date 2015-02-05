(ns driving-cycle.core-test
  (:require [clojure.test :refer :all]
            [driving-cycle.core :refer :all]))

(deftest proxy-with-prev-result-test
  (testing "stateful functions"
    (let [func (fn [prev] (* prev -1))
          testfunc (proxy-with-prev-result func 1)]
      (is (= -1 (testfunc)) "first invocation: 1 * -1")
      (is (= 1 (testfunc)) "second invocation: 1 * -1 * 1")
      (is (= -1 (testfunc)) "second invocation: 1 * -1 * 1 * -1"))))

(deftest generate-test
  (testing "generation of a lazy sequence"
    (let [add-one #(+ 1 %)
          same-number #(+ 0 %)]
      (is (= (take 100 (generate add-one 0))
             (range 1 101 1))
          "should add 1 to the previous number")
      (is (every? #(= 3 %) (take 100 (generate same-number 3)))
          "should always be the same number"))))
