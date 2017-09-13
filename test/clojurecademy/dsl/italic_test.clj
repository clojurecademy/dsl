(ns clojurecademy.dsl.italic-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))


(fact "Italic takes string as parameter otherwise it fails"
      (italic 1) => (throws IllegalArgumentException)
      (italic true) => (throws IllegalArgumentException)
      (italic false) => (throws IllegalArgumentException)
      (italic 1/2) => (throws IllegalArgumentException)
      (italic \a) => (throws IllegalArgumentException))

(fact "Italic's text can not be blank either"
      (italic "") => (throws IllegalArgumentException)
      (italic " ") => (throws IllegalArgumentException)
      (italic "    ") => (throws IllegalArgumentException))

(fact "Italic is map"
      (italic "Hey there I'm Italic") => map?)

(fact (italic "Yep Clojure is cool") => {:italic true
                                         :text   "Yep Clojure is cool"})