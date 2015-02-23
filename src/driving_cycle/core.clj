(ns driving-cycle.core
  (:require [driving-cycle.walk]
            [driving-cycle.drunkwalk]
            [driving-cycle.drivewalk]
            [driving-cycle.markov-frequencies]
            [driving-cycle.markov-probabilities]
            [markov.core :as markov]))

(defn -main
  [& args]
  (let [training-data (take 1000 (driving-cycle.drivewalk/drive-walk))
        matrix (driving-cycle.markov-probabilities/matrix 3 training-data)
        walk (markov/generate-walk matrix)]
    (println "training-data: " training-data)
    (println "matrix: " matrix)
    (println "walk: " (take 1000 walk))))
