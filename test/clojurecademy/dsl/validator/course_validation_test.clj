(ns clojurecademy.dsl.validator.course-validation-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.validator :as v]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.validator.maps :as maps]))

(def course-with-no-title (util/update-val maps/course :title nil))
(def course-with-no-desc (util/update-val maps/course :description nil))
(def course-with-no-chapter (util/update-val maps/course :chapters []))
(def course-chapters-with-additional-key (util/update-val maps/course :chapters [{:name         'denem
                                                                                  :title        "aaa"
                                                                                  :sub-chapters (-> maps/course :chapters first :sub-chapters)
                                                                                  :deneme       1}]))
(def course-chapters-with-same-name (util/update-val maps/course :chapters [maps/chapter maps/chapter maps/chapter]))

(fact (v/validate course-with-no-title) => (throws IllegalArgumentException))
(fact (v/validate course-with-no-desc) => (throws IllegalArgumentException))
(fact (v/validate course-with-no-chapter) => (throws IllegalArgumentException))
(fact (v/validate course-chapters-with-additional-key) => (throws IllegalArgumentException))
(fact (v/validate course-chapters-with-same-name) => (throws IllegalArgumentException))