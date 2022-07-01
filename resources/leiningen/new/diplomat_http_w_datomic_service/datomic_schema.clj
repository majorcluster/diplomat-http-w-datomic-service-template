(ns {{namespace}}.ports.datomic.schema)

(defonce specs
         [;!!BREADS!!
          {:db/ident :breads/name
           :db/valueType :db.type/string
           :db/cardinality :db.cardinality/one
           :db/doc "Bread name"}
          {:db/ident :breads/price
           :db/valueType :db.type/bigdec
           :db/cardinality :db.cardinality/one
           :db/doc "Bread price"}
          {:db/ident :breads/id
           :db/valueType :db.type/uuid
           :db/cardinality :db.cardinality/one
           :db/unique :db.unique/identity
           :db/doc "Bread id"}
          {:db/ident :breads/unit-grams
           :db/valueType :db.type/long
           :db/cardinality :db.cardinality/one
           :db/doc "Bread grams per unit"}])