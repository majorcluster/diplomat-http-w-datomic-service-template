(ns bakery.adapters.breads
  (:require [clj-data-adapter.core :as data-adapter]))

(defn convert-inbound-values
  [k v]
  (cond (= :price k) (when v (bigdec v))
        :else v))

(defn convert-inbound
  [m]
  (->> m
       (data-adapter/transform-values convert-inbound-values)
       (data-adapter/transform-keys (partial data-adapter/kebab-key->namespaced-key "breads"))))

(defn convert-outbound
  [m]
  (cond (nil? m) m
        (map? m) (->> (:breads/id m)
                      str
                      (assoc m :breads/id)
                      (data-adapter/transform-keys data-adapter/namespaced-key->kebab-key))
        :else (map #(convert-outbound %) m)))
