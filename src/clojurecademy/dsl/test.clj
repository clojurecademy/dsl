(ns clojurecademy.dsl.test
  (:require [clojurecademy.dsl.assert :as assert]))

(defmacro defcoursetest
  [name route-vec & body]
  `(do
     ~(assert/defcoursetest route-vec body)
     (def ~(vary-meta name assoc :course-test-var true)
       {:route-vec '~route-vec
        :body      '~body})))