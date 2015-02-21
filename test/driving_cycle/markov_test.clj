(ns driving-cycle.markov-test
  (:require [clojure.test :refer :all]
            [driving-cycle.markov :as markov]))

(deftest basic-markov-matrix-test
  (testing "generation of a basic markov matrix"
    (let [walk '(0 1 0)
          matrix {'(0) {'(1) 1}
                  '(1) {'(0) 1}}]
      (is (= matrix (markov/freq-matrix 1 walk))
          "markov matrix should be correctly generated"))))

(deftest complex-markov-matrix-test
  (testing "generation of a slightly more complex markov matrix"
    (let [walk '(0 1 0 2 1 1 2 3)
          matrix {'(0) {'(1) 1, '(2) 1}
                  '(1) {'(0) 1, '(1) 1, '(2) 1}
                  '(2) {'(1) 1, '(3) 1}}]
      (is (= matrix (markov/freq-matrix 1 walk))
          "markov matrix should be correctly generated"))))

(deftest non-numbers-markov-matrix-test
  (testing "generation of a markov matrix from non-numeric input"
    (let [walk '((0 0) {1 1} (0 0) 1)
          matrix {'((0 0)) {'({1 1}) 1, '(1) 1}
                  '({1 1}) {'((0 0)) 1}}]
      (is (= matrix (markov/freq-matrix 1 walk))
          "markov matrix should be correctly generated"))))
