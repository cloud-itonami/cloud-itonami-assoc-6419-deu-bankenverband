(ns association.facts-test
  (:require [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest bankenverband-has-spec-basis
  (let [sb (facts/spec-basis "bankenverband")]
    (is (= 2 (count sb)))
    (is (every? #(= "6419" (:association-rule/isic %)) sb))
    (is (every? #(= "DEU" (:association-rule/country %)) sb))))

(deftest unknown-association-has-no-spec-basis
  (is (nil? (facts/spec-basis "keidanren")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["bankenverband" "keidanren"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["keidanren"] (:missing-associations c)))))

(deftest by-topic-filters
  (is (= ["bankenverband.deposit-protection-fund-bylaws"]
         (mapv :association-rule/id (facts/by-topic "bankenverband" :deposit-protection))))
  (is (empty? (facts/by-topic "bankenverband" :labor)))
  (is (empty? (facts/by-topic "keidanren" :governance))))
