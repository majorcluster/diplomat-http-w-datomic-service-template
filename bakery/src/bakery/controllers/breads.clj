(ns bakery.controllers.breads
  (:require [datomic-helper.entity :as d.ent]
            [bakery.ports.datomic.core :as d.c])
  (:import (java.util UUID)))

(defn get-all
  []
  (d.ent/find-all (d.c/connect!) :breads/id))

(defn get-by-id
  [id]
  (d.ent/find-by-id (d.c/connect!) :breads/id id))

(defn post
  [m]
  (let [id (UUID/randomUUID)]
    (d.ent/insert! (d.c/connect!)
                   (assoc m :breads/id id)
                   #(d.ent/check-unique! (d.c/connect!)
                                         :breads/id
                                         :breads/name (:breads/name m)))
    id))

(defn patch
  [m id]
  (d.ent/upsert! (d.c/connect!) [:breads/id id] m)
  id)

(defn delete-by-id
  [id]
  (d.ent/delete! (d.c/connect!) :breads/id id))
