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
  "Calculates a new velocity given a previous velocity and
  acceleration and a lower and upper bound (-20 and 100 respectively).

  It's assumed that the given acceleration is the change in velocity
  from the previous velocity to the new, to be calculated, one.

  Thus the new velocity is simply the sum of the old velocity and the
  acceleration."
  [old-velocity acceleration]
  (let [lower -20
        upper 100]
    (bounds lower (+ old-velocity acceleration) upper)))

(defn- new-acceleration
  "Returns a new, random, acceleration that is equal to, or 1 below or
  above the previous one.

  The previous velocity is not taken into account."
  [old-velocity acceleration]
  (let [lower -10
        upper 20]
    (bounds lower (+ acceleration (rand-around-zero 1)) upper)))

(defn- next-data-point
  "Return the next data point given the previous one.

  The supplied functions to calculate the new velocity and
  acceleration receive as arguments the velocity and acceleration of
  the previous point."
  [prev next-velocity next-acceleration]
  {:v (next-velocity (:v prev) (:a prev)),
   :a (next-acceleration (:v prev) (:a prev))}
  )

(defn drive-walk
  "Generate a driving cycle consisting of a chain of data points with
  the velocity and acceleration at that point."
  []
  (walk/generate #(next-data-point % new-velocity new-acceleration)
                 {:v 0 :a 0}))
