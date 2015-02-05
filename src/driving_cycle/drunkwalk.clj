(ns driving-cycle.drunkwalk
  (:require [driving-cycle.core :as core]
            [markov.core :as markov])
  (:gen-class))

(defn filter-outside
  "Returns a new collection without the values strictly smaller than
  the given lower or strictly greater than the given upper bound."
  [lower upper coll]
  (filter (fn [item] (<= lower item upper))
          coll))

(defn range-around
  "Returns the range of numbers from val - 1 to val + 1 with a step of
  1."
  [val]
  (let [distance 1 step 1]
    (range (- val distance) ; start
           (+ val distance step) ; end of range is exclusive
           step)
    )
)

(defn next-step
  [lower upper prev]
  (filter-outside lower
                  upper
                  (range-around prev))
  )

(defn drunk
  [coll]
  (first (shuffle coll))
  )

(defn drunk-walker
  [lower upper]
  (fn [prev]
    (drunk (next-step lower upper prev))))

(defn drunk-walk
  "Generate a Markov chain of numbers within the given bounds.

  The number after the current number n is with equal probability
  either n, n - 1 or n + 1, but always within the given lower and
  upper bound.

  The first number is equal to the lower bound.

  example output for (drunk-walk 0 2): (0 1 1 2 1 0)"
  [lower upper]
  (core/generate (drunk-walker lower upper)
                  lower))

(defn -main
  [& args]
  (let [training-data (take 1000 (drunk-walk 0 100))
        matrix (markov/build-from-coll training-data)
        walk (markov/generate-walk matrix)]
    (println training-data)
    (println matrix)
    (println (take 1000 walk))
    ))
