(ns clojurecademy.dsl.is-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "Form has to be list data structure"
      (is "Deneme") => (throws IllegalArgumentException)
      (is true) => (throws IllegalArgumentException)
      (is 1) => (throws IllegalArgumentException)
      (is []) => (throws IllegalArgumentException)
      (is {}) => (throws IllegalArgumentException))

(fact "Is is a map"
      (is (+ 1 2)) => map?)

(fact (is (+ 1 2)) => {:form               "(+ 1 2)"
                       :error-message      nil
                       :error-message-type :simple})

(fact
  (is (+ 1 2) "Output has to be 3") => {:form               "(+ 1 2)"
                                        :error-message      "Output has to be 3"
                                        :error-message-type :simple})

(fact
  (is (= 1 2) "") => (throws IllegalArgumentException))

(fact "checking string type for message"
      (is (= 1 2) 1) => (throws IllegalArgumentException)
      (is (= 1 2) \a) => (throws IllegalArgumentException)
      (is (= 1 2) 'as) => (throws IllegalArgumentException)
      (is (= 1 2) 21.2) => (throws IllegalArgumentException))

(fact "fail scenarios with err-msg-keys"
      (is (true? true) "Dememe" :unkown-key) => (throws IllegalArgumentException)
      (is (true? true) nil :none) => (throws IllegalArgumentException)
      (is (true? true) "  " :none) => (throws IllegalArgumentException))

(fact
  (is (true? true) "Your test failed!" :none) => map?
  (is (true? true) "Your test failed!" :simple) => map?
  (is (true? true) "Your test failed!" :advanced) => map?)