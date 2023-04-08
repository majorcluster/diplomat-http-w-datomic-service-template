(defproject org.clojars.majorcluster/lein-template.diplomat-http-w-datomic-service "0.2.4"
  :description "Diplomat architecture-pedestal styled template with datomic db for leiningen generation"
  :url "https://github.com/majorcluster/diplomat-http-w-datomic-service-template"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :deploy-repositories [["clojars" {:url "https://repo.clojars.org"
                                    :username :env/clojars_username
                                    :password :env/clojars_password}]]
  :eval-in-leiningen true)
