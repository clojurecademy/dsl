(ns clojurecademy.dsl.validator.sub-chapter-validation-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.validator :as v]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.validator.maps :as maps]))

(defn update-sub-chapter
  [key val]
  (util/nested-update-val [maps/course :chapters
                           maps/chapter] :sub-chapters [(util/update-val maps/sub-chapter key val)]))

(def sub-chapter-with-no-name (update-sub-chapter :name nil))
(def sub-chapter-with-no-title (update-sub-chapter :title nil))
(def sub-chapter-with-no-subject (update-sub-chapter :subjects nil))
(def sub-chapter-with-empty-subject (update-sub-chapter :subjects []))
(def sub-chapter-with-additional-key (update-sub-chapter :try "me"))
(def sub-chapter-subject-with-additional-key (update-sub-chapter :subjects [(util/update-val maps/subject :try-me 1)]))
(def sub-chapter-with-same-name-subjects (update-sub-chapter :subjects [maps/subject
                                                                        maps/subject
                                                                        maps/subject]))

(fact (v/validate sub-chapter-with-no-name) => (throws IllegalArgumentException))
(fact (v/validate sub-chapter-with-no-title) => (throws IllegalArgumentException))
(fact (v/validate sub-chapter-with-no-subject) => (throws IllegalArgumentException))
(fact (v/validate sub-chapter-with-empty-subject) => (throws IllegalArgumentException))
(fact (v/validate sub-chapter-with-additional-key) => (throws IllegalArgumentException))
(fact (v/validate sub-chapter-subject-with-additional-key) => (throws IllegalArgumentException))
(fact (v/validate sub-chapter-with-same-name-subjects) => (throws IllegalArgumentException))