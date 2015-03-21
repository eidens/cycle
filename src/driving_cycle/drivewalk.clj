(ns driving-cycle.drivewalk
  (:require [driving-cycle.walk :as walk]
            [clojure.math.numeric-tower :as math]))

(defn- data-point
  [velocity acceleration rudder]
  {:v velocity,
   :a acceleration,
   :r rudder})

(def min-velocity -20)
(def max-velocity 160)

(def min-acceleration -10)
(def max-acceleration 30)

(def min-rudder -100)
(def max-rudder 100)

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

(defn- close-to
  "Returns true if the given numbers are close to each other,
  close meaning within 10% of the first number."
  [number other-number]
  (let [range (if (= number 0)
                5
                (math/abs (* number 1/10)))]
    (< (- number range)
       other-number
       (+ number range))))

(defn- new-velocity
  "Calculates a new velocity given a previous velocity and
  acceleration and a lower and upper bound.

  It's assumed that the given acceleration is the change in velocity
  from the previous velocity to the new, to be calculated, one.

  Thus the new velocity is simply the sum of the old velocity and the
  acceleration."
  [old-velocity acceleration]
  (let [lower min-velocity
        upper max-velocity]
    (bounds lower (+ old-velocity acceleration) upper)))

(defn- new-acceleration
  "Returns a new, random, acceleration that is equal to, or 2 below or
  above the previous one."
  [old-velocity acceleration]
  (let [lower (if (close-to old-velocity min-velocity)
                0
                min-acceleration)
        upper (if (close-to old-velocity max-velocity)
                0
                max-acceleration)]
    (bounds lower (+ acceleration (rand-around-zero 2)) upper)))

(defn- new-rudder
  [old-velocity acceleration old-rudder]
  (let [lower min-rudder
        upper max-rudder]
    (bounds lower (+ old-rudder (rand-around-zero 10)) upper)))

(defn- next-data-point
  "Return the next data point given the previous one.

  The supplied functions to calculate the new velocity and
  acceleration receive as arguments the velocity and acceleration of
  the previous point."
  [prev next-velocity next-acceleration next-rudder]
  (data-point (next-velocity (:v prev) (:a prev))
              (next-acceleration (:v prev) (:a prev))
              (next-rudder (:v prev) (:a prev) (:r prev))))

(defn drive-walk
  "Generate a driving cycle consisting of a chain of data points with
  the velocity and acceleration at that point."
  []
  (walk/generate #(next-data-point % new-velocity new-acceleration new-rudder)
                 {:v 0 :a 0 :r 0}))

(defn all-possible-data-points
  "Returns a lazy sequence of all possible data points."
  []
  (for [velocity (range min-velocity max-velocity)
        acceleration (range min-acceleration max-acceleration)
        rudder (range min-rudder max-rudder)]
    (data-point velocity acceleration rudder)))
