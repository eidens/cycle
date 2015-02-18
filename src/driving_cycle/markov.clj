(ns driving-cycle.markov)

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

(defn- split-into-cause-and-effect
  [partitioned-walk]
  (map #(hash-map (butlast %) (take-last 1 %))
       partitioned-walk))

(defn- merge-with-concat
  [map other-map]
  (merge-with concat map other-map))

(defn- group-by-cause
  [causes-and-effects]
  (reduce merge-with-concat causes-and-effects))

(defn- aggregate-effects
  [entry]
  (hash-map (key entry)
            (frequencies (val entry))))

(defn matrix
  [order walk]
  (let [partitioned-walk (partition-walk order walk)
        causes-and-effects (split-into-cause-and-effect partitioned-walk)
        effects-by-causes (group-by-cause causes-and-effects)]
    (reduce merge (map aggregate-effects effects-by-causes))))
