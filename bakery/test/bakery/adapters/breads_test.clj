(ns bakery.adapters.breads-test
  (:require [clojure.test :refer :all]
            [bakery.adapters.breads :refer :all]))

(deftest convert-inbound-test
  (testing "price is converted"
    (is (= {:breads/name "Croissant" :breads/unit-grams 200 :breads/price 4M}
           (convert-inbound {:name "Croissant" :unit-grams 200 :price 4})))
    (is (= {:breads/name "Croissant" :breads/unit-grams 200 :breads/price 4M}
           (convert-inbound {:name "Croissant" :unit-grams 200 :price 4.0})))
    (is (= {:breads/name "Croissant" :breads/unit-grams 200 :breads/price 4M}
           (convert-inbound {:name "Croissant" :unit-grams 200 :price 4M})))))

(deftest convert-outbound-test
  (testing "uuid is converted"
    (is (= {:id "7c17ee44-2328-4a8e-8241-4e27b64f4677" :name "Croissant" :unit-grams 200 :price 4M}
           (convert-outbound {:breads/id #uuid "7c17ee44-2328-4a8e-8241-4e27b64f4677"
                              :breads/name "Croissant"
                              :breads/unit-grams 200
                              :breads/price 4M}))))
  (testing "uuid is converted for coll"
    (is (= [{:id "7c17ee44-2328-4a8e-8241-4e27b64f4677" :name "Croissant" :unit-grams 200 :price 4M}
            {:id "7c17ee44-2328-4a8e-8241-4e27b64f4678" :name "Baguette" :unit-grams 400 :price 6M}]
           (convert-outbound [{:breads/id #uuid "7c17ee44-2328-4a8e-8241-4e27b64f4677"
                               :breads/name "Croissant"
                               :breads/unit-grams 200
                               :breads/price 4M}
                              {:breads/id #uuid "7c17ee44-2328-4a8e-8241-4e27b64f4678"
                               :breads/name "Baguette"
                               :breads/unit-grams 400
                               :breads/price 6M}]))))
  (testing "nil returns nil"
    (is (nil? (convert-outbound nil)))))
