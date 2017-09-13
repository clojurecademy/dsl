(ns clojurecademy.dsl.validator.sub-instruction-validation-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.validator :as v]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.validator.maps :as maps]))

(defn update-sub-instruction
  [key val]
  (util/nested-update-val [maps/course :chapters
                           maps/chapter :sub-chapters
                           maps/sub-chapter :subjects
                           maps/subject] :instruction (util/update-val maps/instruction :sub-instructions [(util/update-val maps/sub-instruction key val)])))


(def sub-instruction-with-name-wrong-data-type (update-sub-instruction :name 1))
(def sub-instruction-with-insturction-text-wrong-format (update-sub-instruction :instruction-text [{:normal-text 1
                                                                                                    :text        "Clojure is cool!"}]))
(def sub-instruction-with-insturction-text-wrong-format-2 (update-sub-instruction :instruction-text [{:normal-text 1
                                                                                                      :text        "Clojure is cool!"
                                                                                                      :try         :me}]))
(def sub-instruction-with-insturction-text-wrong-format-3 (update-sub-instruction :instruction-text 1))
(def sub-instruction-with-testing-wrong-data-type (update-sub-instruction :testing 1))
(def sub-instruction-with-testing-with-additional-key (update-sub-instruction :testing {:is  [{:form          "(+ 1 2 3)"
                                                                                               :error-message nil
                                                                                               :error-message-type :simple}]
                                                                                        :try :me}))

(fact (v/validate sub-instruction-with-name-wrong-data-type) => (throws IllegalArgumentException))
(fact (v/validate sub-instruction-with-insturction-text-wrong-format) => (throws IllegalArgumentException))
(fact (v/validate sub-instruction-with-insturction-text-wrong-format-2) => (throws IllegalArgumentException))
(fact (v/validate sub-instruction-with-insturction-text-wrong-format-3) => (throws IllegalArgumentException))
(fact (v/validate sub-instruction-with-insturction-text-wrong-format-3) => (throws IllegalArgumentException))
(fact (v/validate sub-instruction-with-testing-wrong-data-type) => (throws IllegalArgumentException))
(fact (v/validate sub-instruction-with-testing-with-additional-key) => (throws IllegalArgumentException))