(ns leiningen.new.diplomat-http-w-datomic-service
  (:require [leiningen.new.templates :as tmpl]
            [leiningen.core.main :as main]))

(def render (tmpl/renderer "diplomat_http_w_datomic_service"))

(defn diplomat-http-w-datomic-service
  [name]
  (let [main-ns (tmpl/sanitize-ns name)
        data {:raw-name name
              :name (tmpl/project-name name)
              :namespace main-ns
              :sanitized (tmpl/name-to-path name)}]
    (main/info "Generating fresh 'lein new' diplomat-http-w-datomic-service project.")
    (tmpl/->files data
                  ["resources/dev-config.edn" (render "dev-config.edn" data)]
                  ["resources/test-config.edn" (render "test-config.edn" data)]
                  ["src/{{sanitized}}/server.clj" (render "server.clj" data)]
                  ["src/{{sanitized}}/configs.clj" (render "configs.clj" data)]
                  ["src/{{sanitized}}/adapters/breads.clj" (render "adapters_breads.clj" data)]
                  ["src/{{sanitized}}/controllers/breads.clj" (render "controllers_breads.clj" data)]
                  ["src/{{sanitized}}/ports/datomic/core.clj" (render "datomic_core.clj" data)]
                  ["src/{{sanitized}}/ports/datomic/schema.clj" (render "datomic_schema.clj" data)]
                  ["src/{{sanitized}}/ports/http/routes/breads.clj" (render "http_routes_breads.clj" data)]
                  ["test/{{sanitized}}/ports/http/routes/breads_integration_test.clj" (render "breads_integration_test.clj" data)]
                  ["test/{{sanitized}}/adapters/breads_test.clj" (render "adapters_breads_test.clj" data)]
                  ["src/{{sanitized}}/ports/http/routes/core.clj" (render "http_routes_core.clj" data)]
                  ["src/{{sanitized}}/ports/http/routes/interceptors.clj" (render "http_routes_interceptors.clj" data)]
                  ["src/{{sanitized}}/ports/http/routes/utils.clj" (render "http_routes_utils.clj" data)]
                  ["src/{{sanitized}}/ports/http/core.clj" (render "http_core.clj" data)]
                  ["src/{{sanitized}}/ports/core.clj" (render "ports_core.clj" data)]
                  ["test/core_test.clj" (render "core_test.clj" data)]
                  ["README.md" (render "README.md" data)]
                  ["project.clj" (render "project.clj" data)]
                  ["Dockerfile" (render "Dockerfile" data)]
                  [".gitignore" (render "gitignore" data)]
                  ["config/logback.xml" (render "logback.xml" data)])))

