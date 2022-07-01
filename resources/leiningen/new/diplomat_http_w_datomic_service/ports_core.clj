(ns {{namespace}}.ports.core
  (:require [{{namespace}}.ports.http.core :as http.c]
            [{{namespace}}.ports.datomic.core :as d.c]))

(defn start-ports-dev
  []
  (d.c/start)
  (http.c/start-dev))

(defn start-ports
  []
  (d.c/start)
  (http.c/start))
