(ns clojurecademy.dsl.validator.instruction-validation-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.validator :as v]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.validator.maps :as maps]))

(defn update-instruction
  [key val]
  (util/nested-update-val [maps/course :chapters
                           maps/chapter :sub-chapters
                           maps/sub-chapter :subjects
                           maps/subject] :instruction (util/update-val maps/instruction key val)))

(def instruction-with-no-name (update-instruction :name nil))
(def instruction-with-dif-type (update-instruction :name 12))
(def instruction-with-run-pre-tests-dif-type (update-instruction :run-pre-tests? "ertus"))
(def instruction-with-wrong-initial-code (update-instruction :initial-code {:form [""]}))
(def instruction-with-wrong-initial-code-2 (update-instruction :initial-code 1))
(def instruction-with-rule-wrong-only-use-one-fn-data-type (update-instruction :rule {:only-use-one-fn? 1231
                                                                                      :required-fns     nil
                                                                                      :restricted-fns   nil}))
(def instruction-with-rule-required-fn-wrong-form (update-instruction :rule {:only-use-one-fn? true
                                                                             :required-fns     [""]
                                                                             :restricted-fns   nil}))

(def instruction-with-rule-restricted-fn-wrong-form (update-instruction :rule {:only-use-one-fn? true
                                                                               :required-fns     nil
                                                                               :restricted-fns   ["as"]}))

(def instruction-with-rule-required-fn-only-use-with-only-one-fn (update-instruction :rule {:only-use-one-fn? false
                                                                                            :required-fns     ['clojure.string/blank?]
                                                                                            :restricted-fns   nil}))

(def instruction-with-sub-instruction-additional-key (update-instruction :sub-instructions [(util/update-val maps/sub-instruction :try :me)]))
(def instruction-with-sub-instruction-wrong-form (update-instruction :sub-instructions 1))


(fact (v/validate instruction-with-no-name) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-dif-type) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-run-pre-tests-dif-type) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-wrong-initial-code) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-wrong-initial-code-2) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-rule-wrong-only-use-one-fn-data-type) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-rule-required-fn-wrong-form) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-rule-restricted-fn-wrong-form) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-rule-required-fn-only-use-with-only-one-fn) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-sub-instruction-additional-key) => (throws IllegalArgumentException))
(fact (v/validate instruction-with-sub-instruction-wrong-form) => (throws IllegalArgumentException))