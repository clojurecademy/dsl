(ns clojurecademy.dsl.initial-code-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))


(fact "Initial-code's form takes "
      (fact "Either strings"
            (initial-code "(+ 1 2 3)"))
      (fact "Or Lists"
            (initial-code (+ 1 2 3))))

(fact "Initial code looks like code but has only :form attribute"
      (initial-code (+ 1 2)) => {:form "(+ 1 2)"}
      (initial-code "(+ 1 2 3) ") => {:form "(+ 1 2 3) "})