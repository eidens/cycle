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
          (doseq [item seq]
            (.write wrtr (str item))
            (.newLine wrtr)))))

(defn- gen-drivewalk [options]
  (let [output-file (:output-file options)
        walk-length (:walk-length options)
        walk (take walk-length (driving-cycle.drivewalk/drive-walk))]
    (log/info "generating drive-walk with" walk-length "data points")
    (if output-file
      (to-file output-file walk)
      (println walk))))

(defn- gen-markov-frequencies [options]
  (let [output-file (:output-file options)
        input-file (:input-file options)
        walk-length (:walk-length options)
        walk (if input-file
               (with-open [rdr (clojure.java.io/reader input-file)]
                 (log/info "generating markov frequencies with data from file" input-file)
                 (reduce conj [] (map read-string (line-seq rdr))))
               (take walk-length (driving-cycle.drivewalk/drive-walk)))
        matrix (driving-cycle.markov-frequencies/matrix 1 walk)]
    (if output-file
      (to-file output-file matrix)
      (println matrix))))

(defn -main
  [& args]
  (let [cli-args (cli/parse-opts args cli-options)
        options (:options cli-args)
        command (first (:arguments cli-args))]
    (case command
      "drivewalk" (gen-drivewalk options)
      "frequencies" (gen-markov-frequencies options))))
