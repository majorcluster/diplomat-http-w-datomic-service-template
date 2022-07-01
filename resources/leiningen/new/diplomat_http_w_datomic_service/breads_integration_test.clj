(ns {{namespace}}.ports.http.routes.breads-integration-test
  (:require [clojure.test :refer :all]
            [core-test :refer :all]
            [clojure.data.json :as cjson]
            [io.pedestal.test :refer :all]
            [{{namespace}}.controllers.breads :as c.b]
            [clojure.string :as cstr]))

(use-fixtures :each test-fixture)

(defn get-id-from-resp
  [resp]
  (get (-> (:body resp)
           (cjson/read-str))
       "id"))

(deftest get-breads
  (let [croissant {:name "Croissant"
                   :unit-grams 200
                   :price 5.4M}]
    (testing "empty breads are returned once no entry is on db"
      (is (= "{\"breads\":[]}"
             (:body (response-for service :get "/breads")))))
    (testing "once breads are on db they are returned"
      (let [id-1 (c.b/post croissant)
            resp (response-for service :get "/breads")]
        (is (= (format "{\"breads\":[{\"name\":\"Croissant\",\"price\":5.4,\"id\":\"%s\",\"unit-grams\":200}]}" id-1)
               (:body resp)))
        (is (= 200
               (:status resp)))
        (let [id-2 (c.b/post croissant)
              resp (response-for service :get "/breads")]
          (is (= (format "{\"breads\":[{\"name\":\"Croissant\",\"price\":5.4,\"id\":\"%s\",\"unit-grams\":200},{\"name\":\"Croissant\",\"price\":5.4,\"id\":\"%s\",\"unit-grams\":200}]}" id-1 id-2)
                 (:body resp)))
          (is (= 200
                 (:status resp))))))))

(deftest get-bread
  (let [croissant {:name "Croissant"
                   :unit-grams 200
                   :price 5.4M}
        id-1 (c.b/post croissant)
        id-2 (c.b/post croissant)]
    (testing "once valid id is sent, bread is returned"
      (let [resp (response-for service :get (str "/breads/" id-1))]
        (is (= (format "{\"name\":\"Croissant\",\"price\":5.4,\"id\":\"%s\",\"unit-grams\":200}" id-1)
               (:body resp)))
        (is (= 200
               (:status resp))))
      (let [resp (response-for service :get (str "/breads/" id-2))]
        (is (= (format "{\"name\":\"Croissant\",\"price\":5.4,\"id\":\"%s\",\"unit-grams\":200}" id-2)
               (:body resp)))
        (is (= 200
               (:status resp)))))
    (testing "once invalid id is sent, 404 is returned"
      (let [resp (response-for service :get "/breads/3")]
        (is (= 404
               (:status resp))))
      (let [resp (response-for service :get "/breads/473a4e4a-b4ec-44ea-a2be-ac67b9d9e205")]
        (is (= 404
               (:status resp)))))))

(deftest post-bread
  (testing "missing fields are validated with 400"
    (let [resp (response-for service  :post "/breads"
                             :headers json-header
                             :body "")]
      (is (= "{\"validation-messages\":[{\"field\":\"name\",\"message\":\"Field :name is not present\"},{\"field\":\"unit-grams\",\"message\":\"Field :unit-grams is not present\"},{\"field\":\"price\",\"message\":\"Field :price is not present\"}]}"
             (:body resp)))
      (is (= 400
             (:status resp))))
    (let [resp (response-for service  :post "/breads"
                             :headers json-header
                             :body "{\"name\":\"Baguette\"}")]
      (is (= "{\"validation-messages\":[{\"field\":\"unit-grams\",\"message\":\"Field :unit-grams is not present\"},{\"field\":\"price\",\"message\":\"Field :price is not present\"}]}"
             (:body resp)))
      (is (= 400
             (:status resp)))))
  (testing "when mandatory fields are present, 201 with location is returned and bread is placed on db"
    (let [new-bread {:name "Baguette", :unit-grams 400, :price 8.69M}
          bread-json (cjson/write-str new-bread)
          resp (response-for service  :post "/breads"
                             :headers json-header
                             :body bread-json)
          id (get-id-from-resp resp)
          db-found (c.b/get-by-id id)]
      (is (= (format "{\"id\":\"%s\"}" id)
             (:body resp)))
      (is (= 201
             (:status resp)))
      (is (cstr/ends-with?
            (get-in resp [:headers "Location"])
            (str "/breads/" id)))
      (is (= (assoc new-bread :id id)
             db-found)))))

(deftest patch-bread
  (testing "missing fields are validated with 400"
    (let [resp (response-for service
                             :patch "/breads"
                             :headers json-header
                             :body "")]
      (is (= "{\"validation-messages\":[{\"field\":\"id\",\"message\":\"Field :id is not present\"},{\"field\":\"name\",\"message\":\"Field :name is not present\"},{\"field\":\"unit-grams\",\"message\":\"Field :unit-grams is not present\"},{\"field\":\"price\",\"message\":\"Field :price is not present\"}]}"
             (:body resp)))
      (is (= 400
             (:status resp)))))
  (testing "when mandatory fields are present, 200 is returned and bread is updated on db"
    (let [croissant {:name "Croissant"
                     :unit-grams 200
                     :price 5.4M}
          id (c.b/post croissant)
          croissant {:id id :name "Baguette", :unit-grams 500, :price 8.89M}
          bread-json (cjson/write-str croissant)
          resp (response-for service
                             :patch "/breads"
                             :headers json-header
                             :body bread-json)
          db-found (c.b/get-by-id id)]
      (is (= bread-json
             (:body resp)))
      (is (= 200
             (:status resp)))
      (is (= croissant
             db-found)))))

(deftest delete-bread
  (testing "not present bread delete returns 200"
    (let [resp (response-for service
                             :delete "/breads/1")]
      (is (= 200
             (:status resp)))))
  (testing "when id is present, bread is deleted"
    (let [croissant {:name "Croissant"
                     :unit-grams 200
                     :price 5.4M}
          id (c.b/post croissant)
          db-found-before? (= (merge {:id id} croissant) (c.b/get-by-id id))
          resp (response-for service
                             :delete (str "/breads/" id))
          db-found-after (c.b/get-by-id id)]
      (is (= 200
             (:status resp)))
      (is db-found-before?)
      (is (= (nil? db-found-after))))))
