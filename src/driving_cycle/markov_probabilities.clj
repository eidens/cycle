(ns driving-cycle.markov-probabilities
  (:require [driving-cycle.markov-frequencies :as frequencies]))

(defn- prob-map
  [total map freq]
  (merge map
         (hash-map (key freq)
                   (/ (val freq) total))))

(defn- calc-probabilities
  [prob-matrix entry-freq-matrix]
  (let [frequencies (second entry-freq-matrix)
        total (reduce + 0 (vals frequencies))
        probabilities (reduce (partial prob-map total) {} frequencies)]
    (merge prob-matrix
           (hash-map (first entry-freq-matrix)
                     probabilities))))

(defn matrix
  [order walk]
  (let [frequency-matrix (frequencies/matrix order walk)]
    (reduce calc-probabilities {} frequency-matrix)))
