(ns clojurecademy.dsl.validator.testing-validation-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.validator :as v]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.validator.maps :as maps]))

(defn update-testing
  [key val]
  (util/nested-update-val [maps/course :chapters
                           maps/chapter :sub-chapters
                           maps/sub-chapter :subjects
                           maps/subject] :instruction
                          (util/update-val maps/instruction
                                           :sub-instructions [(util/update-val maps/sub-instruction :testing
                                                                               (util/update-val maps/testing key val))])))
(def testing-nil (update-testing :is nil))
(def testing-empty (update-testing :is []))
(def testing-wrong-is-form (update-testing :is [{:form          1
                                                 :error-message nil
                                                 :error-message-type :simple}]))
(def testing-wrong-is-error-message-form (update-testing :is [{:form          "(+ 1 2 3)"
                                                               :error-message 1
                                                               :error-message-type :simple}]))
(def testing-wrong-is-form-lisp (update-testing :is [{:form          "(+ 1 2 3]"
                                                      :error-message nil
                                                      :error-message-type :simple}]))
(def testing-wrong-is-form-that-empty (update-testing :is [{:form          ""
                                                            :error-message nil
                                                            :error-message-type :simple}]))
(def testing-wrong-is-form-that-not-unique (update-testing :is [{:form          "1"
                                                                 :error-message nil
                                                                 :error-message-type :simple}]))
(def testing-with-additional-key (update-testing :try :me))

(fact (v/validate testing-nil) => (throws IllegalArgumentException))
(fact (v/validate testing-empty) => (throws IllegalArgumentException))
(fact (v/validate testing-wrong-is-form) => (throws IllegalArgumentException))
(fact (v/validate testing-wrong-is-error-message-form) => (throws IllegalArgumentException))
(fact (v/validate testing-wrong-is-form-lisp) => (throws IllegalArgumentException))
(fact (v/validate testing-wrong-is-form-that-empty) => (throws IllegalArgumentException))
(fact (v/validate testing-wrong-is-form-that-not-unique) => (throws IllegalArgumentException))
(fact (v/validate testing-with-additional-key) => (throws IllegalArgumentException))