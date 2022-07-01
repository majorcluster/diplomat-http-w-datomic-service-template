(ns {{namespace}}.adapters.breads
  (:require [clj-data-adapter.core :refer [transform-keys
                                           namespaced-key->kebab-key
                                           kebab-key->namespaced-key]]))

(defn convert-inbound
  [m]
  (->> (:price m)
       (bigdec)
       (assoc m :breads/price)
       (transform-keys (partial kebab-key->namespaced-key "breads"))))

(defn convert-outbound
  [m]
  (cond (nil? m) m
        (map? m) (->> (:breads/id m)
                      (.toString)
                      (assoc m :breads/id)
                      (transform-keys namespaced-key->kebab-key))
        :else (map #(convert-outbound %) m)))
