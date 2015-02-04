(ns driving-cycle.common
  (:gen-class))

(defn proxy-with-prev-result
  "Returns an arg-less function that calls the given function with the
  result of its previous invocation (or the given initial value, if
  it's the first invocation)."
  [func initial]
  ; prev is result of the previous invocation
  ; using ref for thread safety
  (let [prev (ref initial)]
    (fn []
      (dosync (alter prev func))))
  )
