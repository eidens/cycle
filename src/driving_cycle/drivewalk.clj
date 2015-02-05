(ns driving-cycle.drivewalk
  (:require [driving-cycle.walk :as walk]))

(defn- bounds
  "Returns the given number, if it is within the given lower and upper
  bound. Otherwise it returns the value of the bound to which the
  number is closest."
  [lower number upper]
  (max lower (min upper number)))

(defn- rand-around-zero
  "Returns a random whole number from the range 0 +/- distance."
  [distance]
  (- distance
     (rand-int (+ 1 ; upper bound of rand-int is exclusive
                  (* distance 2)))))

(defn- new-velocity
  [old-velocity acceleration]
  (let [lower -20
        upper 100]
    (bounds lower (+ old-velocity acceleration) upper)))

(defn- new-acceleration
  [old-velocity acceleration]
  (let [lower -10
        upper 20]
    (bounds lower (+ acceleration (rand-around-zero 1)) upper)))

(defn- next-data-point
  "Return the next data point given the previous one."
  [prev next-velocity next-acceleration]
  {:v (next-velocity (:v prev) (:a prev)),
   :a (next-acceleration (:v prev) (:a prev))}
  )

(defn drive-walk
  "Generate a driving cycle as a markov chain."
  []
  (walk/generate #(next-data-point % new-velocity new-acceleration)
                 {:v 0 :a 0}))
