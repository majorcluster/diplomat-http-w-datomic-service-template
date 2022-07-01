(ns {{namespace}}.ports.http.routes.core
  (:require [io.pedestal.http :as http]
    [io.pedestal.http.body-params :as body-params]
    [{{namespace}}.ports.http.routes.interceptors :as i]
    [{{namespace}}.ports.http.routes.breads :as r.breads]))

(def common-interceptors [(body-params/body-params)
                          http/html-body])

(def json-interceptors [(body-params/body-params)
                        (i/json-out)
                        http/html-body])

(def specs #{["/breads" :get (conj json-interceptors `r.breads/get-breads)]
             ["/breads/:id" :get (conj json-interceptors `r.breads/get-bread)]
             ["/breads" :post (conj json-interceptors `r.breads/post-bread)]
             ["/breads" :patch (conj json-interceptors `r.breads/patch-bread)]
             ["/breads/:id" :delete (conj common-interceptors `r.breads/delete-bread)]})
