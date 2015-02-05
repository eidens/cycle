(ns driving-cycle.drivewalk-test
  (:require [clojure.test :refer :all]
            [driving-cycle.drivewalk :refer :all]))

(deftest drive-walker-test
  (testing "the basics"
    (let [data-point {:v 0, :a 0}]
      (is (every? #(= data-point %) (take 100 (drive-walk)))
          "should always be the same data point at the moment"))))
