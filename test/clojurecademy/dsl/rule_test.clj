(ns clojurecademy.dsl.rule-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "Rule can be nil"
      (rule) => nil)

(fact "Rule has 3 attributes"
      (rule :only-use-one-fn? true
            :required-fns ['reduce 'do]
            :restricted-fns ['eval]) => {:only-use-one-fn? true
                                         :required-fns     ['reduce 'do]
                                         :restricted-fns   ['eval]})

(fact ":only-use-one-fn? default value is false"
      (rule :restricted-fns ['eval]) => {:only-use-one-fn? false
                                         :required-fns     nil
                                         :restricted-fns   ['eval]})

(fact ":required-fns could be used when :only-use-one-fn? true otherwise fails"
      (rule :required-fns ['reduce 'do]
            :only-use-one-fn? false) => (throws IllegalArgumentException))

(fact (rule :only-use-one-fn? true) => {:only-use-one-fn? true
                                        :required-fns     nil
                                        :restricted-fns   nil})

(fact "required-fns and restricted-fns have to be vector"
      (rule :only-use-one-fn? true
            :required-fns 1
            :restricted-fns ['eval]) => (throws IllegalArgumentException)
      (rule :only-use-one-fn? true
            :required-fns ['eval]
            :restricted-fns 1) => (throws IllegalArgumentException)
      (rule :only-use-one-fn? true
            :required-fns 1
            :restricted-fns 1) => (throws IllegalArgumentException))

(fact "every fn has to be symbol"
      (rule :only-use-one-fn? true
            :restricted-fns ['eval 1 "reduce"]) => (throws IllegalArgumentException))