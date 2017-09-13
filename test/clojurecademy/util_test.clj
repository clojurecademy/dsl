(ns clojurecademy.util-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.util :as util]))

(fact "ns can not be nil"
      (util/ns? nil) => false)

(fact "ns has to be symbol"
      (util/ns? "try me") => false
      (util/ns? 1) => false
      (util/ns? true) => false
      (util/ns? false) => false
      (util/ns? \a) => false)

(fact "ns has to start with characters"
      (util/ns? '-abc) => false
      (util/ns? '.abc) => false
      (util/ns? '+abc) => false)

(fact "ns has to end either letters or numbers"
      (util/ns? 'my-name-space--) => false
      (util/ns? 'my-name-space_) => false
      (util/ns? 'my-name-space*) => false
      (util/ns? 'my-name-space&) => false)

(fact "ns's letters have to be a-z & A-Z and it can contain numbers, also it can have '-'"
      (util/ns? 'asd-12-&&-ab) => false
      (util/ns? 'asd_ab) => false)

(fact "ns can't have 2 dots"
      (util/ns? 'my-package.clojure..chapter) => false
      (util/ns? 'my-package.clojure.-.chapter) => false)

(fact
  (util/ns? 'my-packaage) => true
  (util/ns? 'my-packaage.ertus.clojure) => true
  (util/ns? 'my-packaage123.ertus.clojure) => true
  (util/ns? 'my-packaage123.ertus.clojure123) => true
  (util/ns? 'my-ns-2) => true)