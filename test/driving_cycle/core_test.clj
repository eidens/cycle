(ns driving-cycle.core-test
  (:require [clojure.test :refer :all]
            [driving-cycle.core :refer :all]))

(deftest proxy-with-prev-result-test
  (testing "stateful functions"
    (let [func (fn [prev] (* prev -1))
          testfunc (proxy-with-prev-result func 1)]
      (is (= -1 (testfunc)) "first invocation: 1 * -1")
      (is (= 1 (testfunc)) "second invocation: 1 * -1 * 1")
      (is (= -1 (testfunc)) "second invocation: 1 * -1 * 1 * -1")
     )
    )
  )
