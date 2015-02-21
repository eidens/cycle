(ns driving-cycle.markov
  (:require [clojure.tools.trace :refer :all]))

(defn- partition-walk
  "Partition the given collection of data points into collections with
  order + 1 data points. The resulting collections are
  overlapping (one item apart)."
  [order walk]
  (let [size-prev order
        size-next 1]
    (partition (+ size-prev size-next)
               size-next
               walk)))

(defn- split-cause-effect
  [partition]
  (let [cause (butlast partition)
        effect (hash-map (take-last 1 partition) 1)]
    (hash-map cause effect)))

(defn- causes-and-effects
  [partitioned-walk]
  (map split-cause-effect partitioned-walk))

(defn- merge-with-addition
  [map other-map]
  (merge-with + map other-map))

(defn- merge-causes-effects
  [map other-map]
  (merge-with merge-with-addition map other-map))

(defn- ^:dynamic effect-frequencies
  [causes-and-effects]
  (reduce merge-causes-effects causes-and-effects))

(defn matrix
  [order walk]
  (let [partitioned-walk (partition-walk order walk) ; lazy
        causes-and-effects (causes-and-effects partitioned-walk)] ; still lazy
    (dotrace [effect-frequencies]
             (effect-frequencies causes-and-effects))))
