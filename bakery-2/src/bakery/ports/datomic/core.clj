(ns bakery.ports.datomic.core
  (:require [datomic.api :as d]
            [outpace.config :refer [defconfig]]
            [bakery.ports.datomic.schema :refer [specs]]))


(defconfig db-host)
(defconfig db-name)

(defonce db-uri (str db-host "/" db-name))

(defn create-db! []
  (d/create-database db-uri))

(defn connect! []
  (d/connect db-uri))

(defn create-schema!
  [conn]
  (d/transact conn specs))

(defn erase-db!
  "test use only!!!"
  []
  (println "ERASING DB!!!!!!!")
  (d/delete-database db-uri))

(defn start
  []
  (create-db!)
  (let [conn (connect!)]
    (create-schema! conn)))
