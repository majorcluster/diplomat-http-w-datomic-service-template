(ns {{namespace}}.controllers.breads
  (:require [datomic-helper.entity :as d.ent]
            [{{namespace}}.ports.datomic.core :as d.c]
            [pedestal-api-helper.params-helper :as p.h]
            [{{namespace}}.adapters.breads :as a.breads])
  (:import (java.util UUID)))

(defn get-all
  []
  (->> (d.ent/find-all (d.c/connect!) :breads/id)
       (a.breads/convert-outbound)))

(defn get-by-id
  [id]
  (->> (p.h/extract-field-value :id {:id id})
       (d.ent/find-by-id (d.c/connect!) :breads/id)
       (a.breads/convert-outbound)))

(defn post
  [m]
  (let [id (UUID/randomUUID)]
    (->> (assoc m :breads/id id)
         (a.breads/convert-inbound)
         (d.ent/upsert! (d.c/connect!) [:breads/id id]))
    (.toString id)))

(defn patch
  [m id]
  (->>  (a.breads/convert-inbound m)
        (d.ent/upsert! (d.c/connect!) [:breads/id (p.h/extract-field-value :id {:id id})]))
  (assoc m :id (.toString (:id m))))

(defn delete-by-id
  [id]
  (d.ent/delete! (d.c/connect!) :breads/id id))
