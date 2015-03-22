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

(deftest prob-matrix-test
  (testing "generation of a more complex markov matrix"
    (let [walk '(0 1 1 1 0 0)
          matrix {'(0) {'(0) 1/2, '(1) 1/2}
                  '(1) {'(0) 1/3, '(1) 2/3}}]
      (is (= matrix (probabilities/matrix 1 walk))
          "markov matrix should be correctly generated"))))

(deftest complex-prob-matrix-test
  (testing "generation of a complex markov matrix"
    (let [walk '(0 1 1 2 1 2 0 2 0 3 0 0)
          matrix {'(0) {'(0) 1/4, '(1) 1/4, '(2) 1/4, '(3) 1/4}
                  '(1) {'(1) 1/3, '(2) 2/3}
                  '(2) {'(0) 2/3, '(1) 1/3}
                  '(3) {'(0) 1}}]
      (is (= matrix (probabilities/matrix 1 walk))
          "markov matrix should be correctly generated"))))
