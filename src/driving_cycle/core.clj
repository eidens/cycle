(ns driving-cycle.core
  (:require [driving-cycle.walk]
            [driving-cycle.drunkwalk]
            [driving-cycle.drivewalk]
            [driving-cycle.markov-frequencies]
            [driving-cycle.markov-probabilities]
            [markov.core :as markov]
            [clojure.tools.cli :as cli]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [clojure.math.numeric-tower :as math]))

(def cli-options
  [["-f" "--outfile OUTFILE"
    :id :output-file]
   ["-i" "--infile INFILE"
    :id :input-file]
   ["-l" "--walk-length LENGTH"
    :id :walk-length
    :default 10
    :parse-fn #(Integer/parseInt %)]
   ["-o" "--markov-order ORDER"
    :id :markov-order
    :default 1
    :parse-fn #(Integer/parseInt %)]
   ["-p" "--parallel NUM"
    :id :parallel
    :default 1
    :parse-fn #(Integer/parseInt %)]])

(defn- write-seq [output-file seq]
  (log/info "writing data to file" output-file)
  (time (with-open [wrtr (io/writer output-file)]
          (doseq [item seq]
            (.write wrtr (str item))
            (.newLine wrtr)))))

(defn- read-seq [input-file]
  (log/info "reading sequence from file" input-file)
  (time (with-open [rdr (io/reader input-file)]
          (reduce conj [] (map read-string (line-seq rdr))))))

(defn- generate-walk [length]
  (log/info "generating drive-walk with" length "data points")
  (time (take length (driving-cycle.drivewalk/drive-walk))))

(defn- drivewalk [options]
  (let [output-file (:output-file options)
        walk-length (:walk-length options)
        walk (generate-walk walk-length)]
    (if output-file
      (write-seq output-file walk)
      (println walk))))

(defn- generate-frequencies [order walk]
  (log/info "generating frequency matrix with order" order)
  (time (driving-cycle.markov-frequencies/matrix order walk)))

(defn- merge-frequencies
  [map other-map]
  (merge-with (partial merge-with +) map other-map))

(defn- parallel-frequencies [order walk partition-size]
  (log/info "using pmap for frequency generation with partition size of " partition-size)
  (let [partitioned-walk (partition partition-size walk)
        matrices (pmap (partial generate-frequencies order) partitioned-walk)]
    (log/info "merging frequency matrices")
    (time (reduce merge-frequencies matrices))))

(defn- markov-frequencies [options]
  (let [output-file (:output-file options)
        input-file (:input-file options)
        walk-length (:walk-length options)
        markov-order (:markov-order options)
        parallel (:parallel options)
        partition-size (max (math/round (/ walk-length parallel)) 1000)
        walk (if input-file
               (read-seq input-file)
               (generate-walk walk-length))
        matrix (if (> parallel 1)
                 (time (parallel-frequencies markov-order walk partition-size))
                 (generate-frequencies markov-order walk))]
    (if output-file
      (write-seq output-file matrix)
      (println matrix))))

(defn -main
  [& args]
  (let [cli-args (cli/parse-opts args cli-options)
        options (:options cli-args)
        command (first (:arguments cli-args))]
    (case command
      "drivewalk" (drivewalk options)
      "frequencies" (markov-frequencies options))))
