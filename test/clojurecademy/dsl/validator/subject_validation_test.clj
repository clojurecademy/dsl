(ns clojurecademy.dsl.validator.subject-validation-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.validator :as v]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.validator.maps :as maps]))

(defn nested-update-val
  [key val]
  (util/nested-update-val [maps/course :chapters
                           maps/chapter :sub-chapters
                           maps/sub-chapter] :subjects [(util/update-val maps/subject key val)]))

(def subject-with-no-name (nested-update-val :name nil))
(def subject-with-name-dif-type (nested-update-val :name 12))
(def subject-with-no-title (nested-update-val :title nil))
(def subject-with-no-learn (nested-update-val :learn nil))
(def subject-with-additional-key (nested-update-val :try :me))
(def subject-with-learn-additional-key (nested-update-val :learn {:do :me}))
(def subject-with-learn-wrong-normal-text-type (nested-update-val :learn {:text [{:normal-text 1 :text "try"}]}))
(def subject-with-learn-wrong-code-type (nested-update-val :learn {:text [{:code 1 :form [""] :lang :clojure}]}))
(def subject-with-learn-wrong-code-type-with-empty-form (nested-update-val :learn {:text [{:code 1 :form [""] :lang nil}]}))
(def subject-with-learn-wrong-hi-type (nested-update-val :learn {:text [{:hi 1 :text nil}]}))
(def subject-with-learn-wrong-italic-type (nested-update-val :learn {:text [{:italic 1 :text nil}]}))
(def subject-with-learn-wrong-bold-type (nested-update-val :learn {:text [{:bold 1 :text nil}]}))
(def subject-with-learn-wrong-link-type (nested-update-val :learn {:text [{:link 1 :src nil :title nil}]}))
(def subject-with-instruction-additional-key (nested-update-val :instruction (util/update-val maps/instruction :try :me)))

(fact (v/validate subject-with-no-name) => (throws IllegalArgumentException))
(fact (v/validate subject-with-name-dif-type) => (throws IllegalArgumentException))
(fact (v/validate subject-with-no-title) => (throws IllegalArgumentException))
(fact (v/validate subject-with-no-learn) => (throws IllegalArgumentException))
(fact (v/validate subject-with-additional-key) => (throws IllegalArgumentException))
(fact (v/validate subject-with-learn-additional-key) => (throws IllegalArgumentException))
(fact (v/validate subject-with-learn-wrong-normal-text-type) => (throws IllegalArgumentException))
(fact (v/validate subject-with-learn-wrong-code-type) => (throws IllegalArgumentException))
(fact (v/validate subject-with-learn-wrong-code-type-with-empty-form) => (throws IllegalArgumentException))
(fact (v/validate subject-with-learn-wrong-hi-type) => (throws IllegalArgumentException))
(fact (v/validate subject-with-learn-wrong-italic-type) => (throws IllegalArgumentException))
(fact (v/validate subject-with-learn-wrong-bold-type) => (throws IllegalArgumentException))
(fact (v/validate subject-with-learn-wrong-link-type) => (throws IllegalArgumentException))
(fact (v/validate subject-with-instruction-additional-key) => (throws IllegalArgumentException))