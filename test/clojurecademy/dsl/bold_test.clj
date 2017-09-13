(ns clojurecademy.dsl.bold-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "Bold takes string as parameter otherwise it fails"
      (bold 1) => (throws IllegalArgumentException)
      (bold true) => (throws IllegalArgumentException)
      (bold false) => (throws IllegalArgumentException)
      (bold 1/2) => (throws IllegalArgumentException)
      (bold \a) => (throws IllegalArgumentException))

(fact "Bold's text can not be blank either"
      (bold "") => (throws IllegalArgumentException)
      (bold " ") => (throws IllegalArgumentException)
      (bold "    ") => (throws IllegalArgumentException))

(fact "Bold is map"
      (bold "Hey there I'm bold") => map?)

(fact (bold "Yep Clojure is cool") => {:bold true
                                       :text "Yep Clojure is cool"})