(ns driving-cycle.core
  (:require [driving-cycle.walk]
            [driving-cycle.drunkwalk]
            [driving-cycle.drivewalk]
            [driving-cycle.markov-frequencies]
            [driving-cycle.markov-probabilities]
            [markov.core :as markov]
            [clojure.tools.cli :as cli]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(def cli-options
  [["-f" "--outfile OUTFILE"
    :id :output-file]
   ["-i" "--infile INFILE"
    :id :input-file]
   ["-l" "--walk-length LENGTH"
    :id :walk-length
    :default 10
    :parse-fn #(Integer/parseInt %)]])

(defn- to-file [output-file seq]
  (log/info "writing data to file" output-file)
  (time (with-open [wrtr (io/writer output-file)]
          (.write wrtr (str seq)))))

(defn- gen-drivewalk [options]
  (let [output-file (:output-file options)
        walk-length (:walk-length options)
        walk (take walk-length (driving-cycle.drivewalk/drive-walk))]
    (log/info "generating drive-walk with" walk-length "data points")
    (if output-file
      (to-file output-file walk)
      (println walk))))

(defn -main
  [& args]
  (let [cli-args (cli/parse-opts args cli-options)
        options (:options cli-args)
        command (first (:arguments cli-args))]
    (case command
      "drivewalk" (gen-drivewalk options))))
