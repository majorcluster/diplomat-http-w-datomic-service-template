(ns bakery.ports.http.routes.breads
  (:require [bakery.controllers.breads :as c.breads]
            [bakery.adapters.breads :as a.breads]
            [bakery.ports.http.routes.utils :refer :all]
            [pedestal-api-helper.params-helper :as ph])
  (:import (clojure.lang ExceptionInfo)
           [java.util UUID]))

(defn get-breads
  [_]
  (let [result {:breads (c.breads/get-all)}]
    {:status 200 :headers json-header :body result}))

(defn get-bread
  [request]
  (cond (ph/is-uuid (-> (:path-params request)
                        :id)) (let [result (-> request :path-params :id
                                               UUID/fromString
                                               c.breads/get-by-id)
                                    not-found? (nil? result)]
                                (cond not-found? {:status 404 :headers json-header :body {}}
                                      :else {:status 200 :headers json-header :body result}))
        :else {:status 404 :headers json-header :body {}}))

(defn post-bread
  [request]
  (try
    (let [bread-id (->  (get request :json-params {})
                        (ph/validate-and-mop!!
                         ["name","unit-grams","price"]
                         ["name","unit-grams","price"])
                        a.breads/convert-inbound
                        c.breads/post)]
      {:status 201
       :headers (merge json-header {"Location" (str "http://localhost:8080/breads/" bread-id)})
       :body {:id bread-id}})
    (catch ExceptionInfo e
      (->> e
           ex-data
           :validation-messages
           (assoc-in {:status 400 :headers json-header :body {:validation-messages []}}
                     [:body :validation-messages])))))

(defn patch-bread
  [request]
  (try
    (let [bread (get request :json-params {})
          bread-id (->  bread
                        (ph/validate-and-mop!!
                         ["id"]
                         ["id","name","unit-grams","price"])
                        a.breads/convert-inbound
                        (c.breads/patch (-> bread :id UUID/fromString)))]
      {:status 200
       :headers json-header
       :body {:id bread-id}})
    (catch ExceptionInfo e
      (->> (.getData e)
           :validation-messages
           (assoc-in {:status 400 :headers json-header :body {:validation-messages []}}
                     [:body :validation-messages])))))

(defn delete-bread
  [request]
  (try
    (c.breads/delete-by-id (-> request :path-params :id))
    {:status 200}
    (catch Exception _
      {:status 404})))
