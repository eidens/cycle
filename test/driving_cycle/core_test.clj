(ns driving-cycle.core-test
  (:require [clojure.test :as test]
            [driving-cycle.core :as core]))

(deftest proxy-with-prev-result-test
  (testing "stateful functions"
    (let [func (fn [prev] (* prev -1))
          testfunc (core/proxy-with-prev-result func 1)]
      (test/is (= -1 (testfunc)) "first invocation: 1 * -1")
      (test/is (= 1 (testfunc)) "second invocation: 1 * -1 * 1")
      (test/is (= -1 (testfunc)) "second invocation: 1 * -1 * 1 * -1")
     )
    )
  )
