(ns clojurecademy.dsl.code-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))


(fact "Code's form takes "
      (fact "Either strings"
            (code "(- 3 1)"))
      (fact "Or Lists"
            (code (- 3 1))))

(fact "Code is map"
      (code (+ 1 2)) => map?)

(fact "To string form"
      (code (+ 1 2)) => {:code true
                         :form "(+ 1 2)"
                         :lang "clojure"})

(fact "To string form from string"
      (code "(+ 1 2)") => {:code true
                           :form "(+ 1 2)"
                           :lang "clojure"})

(fact "unsupported languages"
      (code "dart" "void main() {\n  print(new Die(n: 12).roll());\n}") => (throws IllegalArgumentException))