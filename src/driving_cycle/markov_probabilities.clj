(ns driving-cycle.markov-probabilities
  (:require [driving-cycle.markov-frequencies :as frequencies]))

(defn- transform-map
  [map func]
  (into {} (for [[k v] map] [k (func v)])))

(defn- calc-probabilities
  [frequencies]
  (let [total (reduce + 0 (vals frequencies))]
    (transform-map frequencies (partial * (/ 1 total)))))

(defn matrix
  [order walk]
  (let [frequency-matrix (frequencies/matrix order walk)]
    (transform-map frequency-matrix calc-probabilities)))
