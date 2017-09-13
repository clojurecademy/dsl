(ns clojurecademy.dsl.hi-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "Hi takes string as parameter otherwise it fails"
      (hi 1) => (throws IllegalArgumentException)
      (hi true) => (throws IllegalArgumentException)
      (hi false) => (throws IllegalArgumentException)
      (hi 1/2) => (throws IllegalArgumentException)
      (hi \a) => (throws IllegalArgumentException))

(fact "Hi's text can not be blank either"
      (hi "") => (throws IllegalArgumentException)
      (hi " ") => (throws IllegalArgumentException)
      (hi "    ") => (throws IllegalArgumentException))

(fact "Hi is map"
      (hi "Hey there I'm Hi") => map?)

(fact (hi "Yep Clojure is cool") => {:hi   true
                                     :text "Yep Clojure is cool"})