(ns clojurecademy.dsl.sub-instruction-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "name can not be nil"
      (sub-instruction nil
                       (text (p "Clojure Sub Chapter"))
                       (testing
                         (is (+ 1 2)))) => (throws IllegalArgumentException))

(fact "text and testing can not be nil"
      (sub-instruction 'my-sub-ins-1 nil nil) => (throws IllegalArgumentException)
      (sub-instruction 'my-sub-ins-1 nil 1) => (throws IllegalArgumentException)
      (sub-instruction 'my-sub-ins-1 1 nil) => (throws IllegalArgumentException))

(fact "text and testing have to be maps"
      (sub-instruction 'my-sub-ins-1 1 1) => (throws IllegalArgumentException)
      (sub-instruction 'my-sub-ins-1 true "sada") => (throws IllegalArgumentException)
      (sub-instruction 'my-sub-ins-1 true \a) => (throws IllegalArgumentException)
      (sub-instruction 'my-sub-ins-1 {:a 1} \a) => (throws IllegalArgumentException))

(fact "text and testing have to be proper map forms"
      (sub-instruction 'my-sub-ins-1 {:do 1} {:yeah 2}) => (throws IllegalArgumentException)
      (sub-instruction 'my-sub-ins-1 (text "Try") {:yeah 2}) => (throws IllegalArgumentException)
      (sub-instruction 'my-sub-ins-1 {:do 1} (testing (is (+ 1 2)))) => (throws IllegalArgumentException))

(fact "optional name identifier"
      (sub-instruction 'sub-ch-clojure
                       (text (p "Clojure Sub Chapter"))
                       (testing
                         (is (+ 1 2)))) => {:name             'sub-ch-clojure
                                            :instruction-text [{:p     true
                                                                :texts [{:normal-text true
                                                                         :text        "Clojure Sub Chapter"}]}]
                                            :testing          {:is [{:form          "(+ 1 2)"
                                                                     :error-message nil
                                                                     :error-message-type :simple}]}})

(fact "name identifier has to be symbol"
      (sub-instruction "sub-ch-clojure"
                       (text (p "Clojure Sub Chapter"))
                       (testing
                         (is (+ 1 2)))) => (throws IllegalArgumentException))

(fact
  (sub-instruction 'my-sub-ins-1
                   (text (p "Clojure!"))
                   (testing (is (+ 1 2)))) => {:name             'my-sub-ins-1
                                               :instruction-text [{:p     true
                                                                   :texts [{:normal-text true
                                                                            :text        "Clojure!"}]}]
                                               :testing          {:is [{:form          "(+ 1 2)"
                                                                        :error-message nil
                                                                        :error-message-type :simple}]}})

(fact
  (sub-instruction 'my-sub-ins-1
                   (text (p "More Clojure Code: " " is " (bold "wrong")))
                   (testing (is (= 1 1) :default)
                            (is (= 1 2) "Those vals are not equal"))) => {:name             'my-sub-ins-1
                                                                          :instruction-text [{:p     true
                                                                                              :texts [{:normal-text true
                                                                                                       :text        "More Clojure Code: "}
                                                                                                      {:normal-text true
                                                                                                       :text        " is "}
                                                                                                      {:bold true
                                                                                                       :text "wrong"}]}]
                                                                          :testing          {:is [{:form          "(= 1 1)"
                                                                                                   :error-message nil
                                                                                                   :error-message-type :simple}
                                                                                                  {:form          "(= 1 2)"
                                                                                                   :error-message "Those vals are not equal"
                                                                                                   :error-message-type :simple}]}})
