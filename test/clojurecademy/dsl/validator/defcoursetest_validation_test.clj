(ns clojurecademy.dsl.validator.defcoursetest-validation-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.validator :as v]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.validator.maps :as maps]))

(defmacro defcoursetest-mock
  [name route-vec & body]
  `(do
     (def ~(vary-meta name assoc :course-test-var true)
       {:route-vec '~route-vec
        :body      '~body})))

(defcoursetest-mock valid-test-map
                    [chapter-one sub-chapter-2 subject ins sub-ins]
                    (reduce + [1 2 3])
                    (println "Hello, World!"))

(defcoursetest-mock valid-test-map-2
                    [chapter-one sub-chapter-2 subject ins sub-ins]
                    (reduce + [1 2 3]))

(defcoursetest-mock wrong-route-data-type
                    1
                    (reduce + [1 2 3]))

(defcoursetest-mock wrong-route-data-type-2
                    [chapter-one sub-chapter-2 subject 2 sub-ins]
                    (reduce + [1 2 3]))

(defcoursetest-mock wrong-route-data-type-3
                    "try us"
                    (reduce + [1 2 3]))

(defcoursetest-mock wrong-route-data-type-4
                    nil
                    (reduce + [1 2 3]))

(defcoursetest-mock empty-body
                    [chapter-one sub-chapter-2 subject ins sub-ins])

(fact (v/test? #'valid-test-map) => true)
(fact (v/test? #'valid-test-map-2) => true)
(fact (v/test? #'wrong-route-data-type) => false)
(fact (v/test? #'wrong-route-data-type-2) => false)
(fact (v/test? #'wrong-route-data-type-3) => false)
(fact (v/test? #'wrong-route-data-type-4) => false)
(fact (v/test? #'empty-body) => false)
