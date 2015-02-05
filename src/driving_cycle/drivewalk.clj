(ns driving-cycle.drivewalk
  (:require [driving-cycle.walk :as walk]))

(defn- data-point
  "Return a data point for the given values."
  [velocity acceleration]
  {:v velocity, :a acceleration}
  )

(defn drive-walk
  "Generate a driving cycle as a markov chain."
  []
  (walk/generate (constantly (data-point 0 0))
                 (data-point 0 0)))
