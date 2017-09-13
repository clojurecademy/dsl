(ns clojurecademy.dsl.testing-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "Testing should have at least one is statement"
      (testing) => (throws IllegalArgumentException))

(fact "Testing has to take is as parameter and is is a map"
      (testing []) => (throws IllegalArgumentException)
      (testing [] 1 false true \a) => (throws IllegalArgumentException))

(fact "Testing's is has form and message keys"
      (testing {:some-key 1}) => (throws IllegalArgumentException)
      (testing {:form "(+ 1 2)" :error-message "asda" :error-message-type :simple :unkown-key 1}) => (throws IllegalArgumentException))

(fact "Testing is map"
      (testing (is (+ 1 2 3))) => map?)

(fact "Testing takes is statement"
      (testing (is (+ 1 2))) => {:is [{:form               "(+ 1 2)"
                                       :error-message      nil
                                       :error-message-type :simple}]})

(fact "Testing can have multiple is statements"
      (testing (is (= (+ 1 2) 3))
               (is (= (reduce + [1 2 3]) 6))
               (is (map? {}))) => {:is [{:form               "(= (+ 1 2) 3)"
                                         :error-message      nil
                                         :error-message-type :simple}
                                        {:form               "(= (reduce + [1 2 3]) 6)"
                                         :error-message      nil
                                         :error-message-type :simple}
                                        {:form               "(map? {})"
                                         :error-message      nil
                                         :error-message-type :simple}]})

(fact "Is statements with error messages"
      (testing (is (= (+ 1 2) 3) "Output has to be 3")
               (is (= (reduce + [1 2 3]) 6) "Output has to be 6")
               (is (map? {}) "Output has to be map")) => {:is [{:form               "(= (+ 1 2) 3)"
                                                                :error-message      "Output has to be 3"
                                                                :error-message-type :simple}
                                                               {:form               "(= (reduce + [1 2 3]) 6)"
                                                                :error-message      "Output has to be 6"
                                                                :error-message-type :simple}
                                                               {:form               "(map? {})"
                                                                :error-message      "Output has to be map"
                                                                :error-message-type :simple}]})