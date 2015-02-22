(ns driving-cycle.markov-probabilities
  (:require [driving-cycle.markov-frequencies :as frequencies]))

(defn- prob-map
  [total map freq]
  (merge map
         (hash-map (key freq)
                   (/ (val freq) total))))

(defn- calc-probabilities
  [matrix frequencies]
  (let [total (reduce + 0 (vals frequencies))
        probabilities (reduce (partial prob-map total) {} frequencies)]
    (merge matrix probabilities)))

(defn matrix
  [order walk]
  (let [frequency-matrix (frequencies/matrix order walk)]
    (reduce calc-probabilities {} frequency-matrix)))
