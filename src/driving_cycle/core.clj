(ns driving-cycle.core
  (use [markov.core :as markov])
  (use [clojure.tools.trace :as trace])
  (:gen-class))



(defn ^:dynamic inside-bounds
  [item lower upper]
  (= true ; necessary? can both even be false? 
     (<= lower item)
     (<= item upper))
  )

(defn ^:dynamic filter-outside
  [lower upper coll]
  (filter (fn [item] (inside-bounds item lower upper))
          coll)
  )

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

(defn ^:dynamic remember-last-result
  [function initial]
  (let [prev (ref initial)] ; prev is result of the previous invocation (ref for thread safety)
    (fn []
      (println "dosync, prev: " prev)
      (dosync (alter prev function))
      (deref prev)))
  )


(defn ^:dynamic rand-cycle
  [lower upper]
  (remember-last-result (next-item lower upper)
                        lower)
  )

(defn ^:dynamic cycle-data
  [lower upper]
  (repeatedly (rand-cycle lower upper))
  )

(defn -main
  [& args]
  (let [training-data (build-from-coll args)]
    (println training-data)
    (println (take 10 (generate-walk training-data)))
    )
)
