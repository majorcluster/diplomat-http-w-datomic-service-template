(ns bakery.ports.core
  (:require [bakery.ports.http.core :as http.c]
            [bakery.ports.datomic.core :as d.c]))

(defn start-ports-dev
  []
  (d.c/start)
  (http.c/start-dev))

(defn start-ports
  []
  (d.c/start)
  (http.c/start))
