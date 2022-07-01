(ns core-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [{{namespace}}.ports.http.core :as service]
            [{{namespace}}.ports.datomic.core :as d.c]))

(def json-header
  {"Content-Type" "application/json"})

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(defn setup
  []
  (d.c/start))

(defn teardown
  []
  (d.c/erase-db!))

(defn test-fixture [f]
  (setup)
  (f)
  (teardown))
