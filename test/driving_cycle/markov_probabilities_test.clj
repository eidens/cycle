(ns driving-cycle.markov-probabilities-test
  (:require [clojure.test :refer :all]
            [driving-cycle.markov-probabilities :as probabilities]))

(deftest basic-prob-matrix-test
  (testing "generation of a basic markov matrix"
    (let [walk '(0 1 0)
          matrix {'(0) {'(1) 1}
                  '(1) {'(0) 1}}]
      (is (= matrix (probabilities/matrix 1 walk))
          "markov matrix should be correctly generated"))))
