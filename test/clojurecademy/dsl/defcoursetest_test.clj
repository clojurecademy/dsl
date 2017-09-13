(ns clojurecademy.dsl.defcoursetest-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.test :refer :all]
            [clojure.string :as str]))

(fact "route vec has to be vector"
      (defcoursetest my-first-test
                     {} nil) => (throws IllegalArgumentException))

(fact "route vals have to be symbols"
      (defcoursetest my-second-teest
                     [chapter-one sub-chapter-2 subject false]
                     nil) => (throws IllegalArgumentException))


(fact "test body can not be empty"
      (defcoursetest my-fourth-test-2
                     [chapter-one sub-chapter-2 subject ins sub-ins]) => (throws IllegalArgumentException))

(fact
  (defcoursetest my-fourth-test
                 [chapter-one sub-chapter-2 subject ins sub-ins]
                 (reduce + [1 2 3])
                 (println "Hello, World!")) => var?)

(fact
  my-fourth-test => {:route-vec  '[chapter-one sub-chapter-2 subject ins sub-ins]
                     :body       '((reduce + [1 2 3])
                                    (println "Hello, World!"))})