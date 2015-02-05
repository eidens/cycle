(ns driving-cycle.drivewalk-test
  (:require [clojure.test :refer :all]
            [driving-cycle.drivewalk :refer :all]))

(deftest drive-walker-test
  (testing "the basics"
    (is (every? #(= true (number? (% :v)) (number? (% :a)))
                (take 100 (drive-walk)))
        "should always return a data point with velocity and acceleration")))
