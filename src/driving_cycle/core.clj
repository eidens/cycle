(ns driving-cycle.core
  (:require [driving-cycle.drunkwalk :as walk]
            [markov.core :as markov]))

(defn -main
  [& args]
  (let [training-data (take 1000 (walk/drunk-walk 0 100))
        matrix (markov/build-from-coll training-data)
        walk (markov/generate-walk matrix)]
    (println training-data)
    (println matrix)
    (println (take 1000 walk))
    ))
