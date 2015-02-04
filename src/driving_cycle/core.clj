(ns driving-cycle.core
  (:require [driving-cycle.common :as common]
            [driving-cycle.inputdata :as input]
            [markov.core :as markov])
  (:gen-class))


(defn filter-outside
  [lower upper coll]
  (filter (fn [item] (<= lower item upper))
          coll))

(defn range-around
  [val]
  (let [distance 1 step 1]
    (range (- val distance) ; start
           (+ val distance step) ; end of range is exclusive
           step)
    )
)

(defn rand-item
  [coll]
  (first (shuffle coll))
  )

; returns a function that takes a value prev and returns
; a new value in prev's vicinity, but within the given bounds
(defn next-item
  [lower upper]
  (fn [prev]
    (rand-item (filter-outside lower
                               upper
                               (range-around prev))))
  )

(defn simple-training-data
  [lower upper]
  (input/generate (next-item lower upper)
                  lower))

(defn -main
  [& args]
  (let [training-data (take 1000 (simple-training-data 0 100))
        matrix (markov/build-from-coll training-data)
        walk (markov/generate-walk matrix)]
    (println training-data)
    (println matrix)
    (println (take 1000 walk))
    ))
