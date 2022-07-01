(ns {{namespace}}.configs
  (:require [outpace.config :refer [defconfig]]))

(defconfig env)

(defn env-test?
  []
  (= "test" env))
