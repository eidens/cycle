(ns driving-cycle.core
  (use [markov.core :as markov])
  (:gen-class))

(defn -main
  [& args]
  (let [training-data (build-from-coll args)]
    (println training-data)
    (println (take 10 (generate-walk training-data)))
  )
)

