(ns driving-cycle.core
  (use [markov.core :as markov])
  (use [clojure.tools.trace :as trace])
  (:require [driving-cycle.common :as common])
  (:gen-class))


(defn ^:dynamic filter-outside
  [lower upper coll]
  (filter (fn [item] (<= lower item upper))
          coll))

(defn ^:dynamic range-around
  [val]
  (let [distance 1 step 1]
    (range (- val distance) ; start
           (+ val distance step) ; end of range is exclusive
           step)
    )
)

(defn ^:dynamic rand-item
  [coll]
  (first (shuffle coll))
  )

; returns a function that takes a value prev and returns
; a new value in prev's vicinity, but within the given bounds
(defn ^:dynamic next-item
  [lower upper]
  (fn [prev]
    (rand-item (filter-outside lower
                               upper
                               (range-around prev))))
  )

(defn ^:dynamic rand-cycle
  [lower upper]
  (common/proxy-with-prev-result (next-item lower upper)
                                 lower)
  )

(defn ^:dynamic cycle-data
  [lower upper]
  (repeatedly (rand-cycle lower upper))
  )

(defn -main
  [& args]
  (let [training-data (take 1000 (cycle-data 0 100))
        matrix (build-from-coll training-data)
        walk (generate-walk matrix)]
    (println training-data)
    (println matrix)
    (println (take 1000 walk))
    )
)
