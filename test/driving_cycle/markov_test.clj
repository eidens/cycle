(ns driving-cycle.markov-test
  (:require [clojure.test :refer :all]
            [driving-cycle.markov :refer :all]))

(deftest markov-matrix-test
  (testing "generation of a basic markov matrix"
    (let [walk '(0 1 0 )
          matrix {'(0) {1 1}
                  '(1) {0 1}}]
      (is (= matrix (gen-matrix 1 walk))
          "markov matrix should be correctly generated"))))
