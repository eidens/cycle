(ns driving-cycle.io
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec {:subprotocol "mysql"
              :subname "//127.0.0.1:3306/driving_cycle"
              :user "driving_cycle"
              :password "driving_cycle"})

(def frequency-constraints
  (str " WHERE prev_velocity = ?"
       " AND prev_acceleration = ?"
       " AND prev_rudder = ?"
       " AND next_velocity = ?"
       " AND next_acceleration = ?"
       " AND next_rudder = ?"))

(defn- frequency-matrix-columns []
  (vec :prev-velocity
       :prev_acceleration
       :prev_rudder
       :next_velocity
       :next_acceleration
       :next_rudder))


(defn- frequency-matrix-values
  [prev-point next-point]
  (vec (:v prev-point)
       (:a prev-point)
       (:r prev-point)
       (:v next-point)
       (:a next-point)
       (:r next-point)))


(def select-frequency
  (str "SELECT frequency FROM frequency_matrix"
       frequency-constraints))

(def update-frequency
  (str "UPDATE frequency_matrix SET frequency = (frequency + ?)"
       frequency-constraints))

(defn- insert-or-update-frequency
  [prev-point next-point frequency]
  (jdbc/with-db-transaction [t-con db-spec]
    (let [values (frequency-matrix-constraints prev-point next-point)
          result (jdbc/execute! t-con (conj update-frequency
                                            frequency
                                            values))]
      (if (zero? (first result))
        (j/insert! t-con
                   'frequency_matrix
                   (frequency-matrix-columns)
                   values)
        result))))

(defn- get-frequency
  [prev-point next-point]
  (jdbc/query! db-spec (conj select-frequency
                             (frequency-matrix-constraints prev-point next-point))))

(defn- persist-data-points
  [db-spec data-points]
  (apply (partial jdbc/insert! db-spec 'data_point [:velocity
                                                    :acceleration
                                                    :rudder])
         (map #(list (:v %) (:a %) (:r %)) data-points)))


(defn- persist-frequencies
  [frequency-matrix]
  )

(defn persist
  [data-points]
  (persist-data-points db-spec data-points))
