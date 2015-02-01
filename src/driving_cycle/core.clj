(ns driving-cycle.core
  (use [markov.core :as markov])
  (use [clojure.tools.trace :as trace])
  (:gen-class))



(defn ^:dynamic inside
  [item lower upper]
  (= true ; necessary? can both even be false? 
     (<= lower item)
     (<= item upper))
  )

(defn ^:dynamic filter-outside
  [lower upper coll]
  (filter (fn [item] (inside item lower upper))
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
    (fn [] (dosync (alter prev function))))
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
;  (let [training-data (build-from-coll args)]
;    (println training-data)
;    (println (take 10 (generate-walk training-data)))
;  )
  (dotrace [range-around] 
           (range-around 10))
  (dotrace [inside] 
           (inside 9 9 11))
  (dotrace [filter-outside] 
           (filter-outside 10 20 (range 0 30)))
  (dotrace [rand-item]
           (rand-item (range 0 10)))
  (println (dotrace [next-item]
                    ((next-item 0 10) 0)))
  (println (dotrace [rand-cycle]
                    ((rand-cycle 0 10))))
  )

