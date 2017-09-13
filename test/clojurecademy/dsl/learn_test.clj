(ns clojurecademy.dsl.learn-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "Learn can not be nil otherwise fails"
      (learn nil) => (throws IllegalArgumentException))

(fact "Learn's text param can not be other map form otherwise fails"
      (learn {:some-key 'and-its-value}) => (throws IllegalArgumentException))

(fact "Learn takes text as a parameter"
      (learn (text (p "Hi this is the learning part"))) => {:learn {:texts [{:p     true
                                                                             :texts [{:normal-text true
                                                                                      :text        "Hi this is the learning part"}]}]}})