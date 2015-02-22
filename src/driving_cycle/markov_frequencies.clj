(ns driving-cycle.markov-frequencies)

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

(defn- merge-causes-effects
  [map other-map]
  (merge-with (partial merge-with +) map other-map))

(defn matrix
  [order walk]
  (let [partitioned-walk (partition-walk order walk) ; lazy
        causes-effects (map split-cause-effect partitioned-walk)] ; still lazy
    (reduce merge-causes-effects causes-effects)))
