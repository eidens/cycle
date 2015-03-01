(ns driving-cycle.core
  (:require [driving-cycle.walk]
            [driving-cycle.drunkwalk]
            [driving-cycle.drivewalk]
            [driving-cycle.markov-frequencies]
            [driving-cycle.markov-probabilities]
            [markov.core :as markov]))

(defn -main
  [& args]
  (let [training-data (take 100 (driving-cycle.drivewalk/drive-walk))
        matrix (driving-cycle.markov-frequencies/matrix 1 training-data)]
    (println training-data)
    (println matrix)))
