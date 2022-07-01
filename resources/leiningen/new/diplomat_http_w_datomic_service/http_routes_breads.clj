(ns {{namespace}}.ports.http.routes.breads
  (:require [{{namespace}}.controllers.breads :as c.breads]
            [{{namespace}}.ports.http.routes.utils :refer :all]
            [pedestal-api-helper.params-helper :as ph])
  (:import (clojure.lang ExceptionInfo)))

(defn get-breads
  [_]
  (let [result {:breads (c.breads/get-all)}]
    {:status 200 :headers json-header :body result}))

(defn get-bread
  [request]
  (let [result (-> (:path-params request)
                   :id
                   (c.breads/get-by-id))
        not-found? (nil? result)]
    (cond not-found? {:status 404 :headers json-header :body {}}
          :else {:status 200 :headers json-header :body result})))

(defn post-bread
  [request]
  (try
    (let [bread-id (->  (get request :json-params {})
                        (ph/validate-and-mop!!
                            ["name","unit-grams","price"]
                            ["name","unit-grams","price"])
                        (c.breads/post))]
      {:status 201
       :headers (merge json-header {"Location" (str "http://localhost:8080/breads/" bread-id)})
       :body {:id bread-id}})
    (catch ExceptionInfo e
      (->> (.getData e)
          :validation-messages
          (assoc-in {:status 400 :headers json-header :body {:validation-messages []}}
                    [:body :validation-messages])))))

(defn patch-bread
  [request]
  (try
    (let [bread (get request :json-params {})
          bread (->  bread
                        (ph/validate-and-mop!!
                          ["id","name","unit-grams","price"]
                          ["id","name","unit-grams","price"])
                        (c.breads/patch (:id bread)))]
      {:status 200
       :headers json-header
       :body bread})
    (catch ExceptionInfo e
      (->> (.getData e)
           :validation-messages
           (assoc-in {:status 400 :headers json-header :body {:validation-messages []}}
                     [:body :validation-messages])))))

(defn delete-bread
  [request]
  (try
    (c.breads/delete-by-id (get-in request [:path-params :id]))
    {:status 200}
    (catch Exception _
      {:status 404})))
