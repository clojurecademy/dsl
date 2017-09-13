(ns clojurecademy.dsl.validator.chapter-validation-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.validator :as v]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.validator.maps :as maps]))

(def course-chapter-with-no-name (util/update-val maps/course :chapters [(util/update-val maps/chapter :name nil)]))
(def course-chapter-with-no-title (util/update-val maps/course :chapters [(util/update-val maps/chapter :title nil)]))
(def course-chapter-with-no-sub-chapters (util/update-val maps/course :chapters [(util/update-val maps/chapter :sub-chapters nil)]))
(def course-chapter-with-empty-sub-chapters (util/update-val maps/course :chapters [(util/update-val maps/chapter :sub-chapters [])]))
(def course-chapter-with-additional-key (util/update-val maps/course :chapters [(util/update-val maps/chapter :try-me 1)]))
(def course-chapter-with-same-name-sub-chapters (util/update-val maps/course :chapters [(util/update-val maps/chapter :sub-chapters [maps/sub-chapter
                                                                                                                                     maps/sub-chapter
                                                                                                                                     maps/sub-chapter])]))
(def course-chapter-with-sub-chapter-dif-key (util/update-val maps/course :chapters [(util/update-val maps/chapter :sub-chapters [(util/update-val maps/sub-chapter :try 1)])]))

(fact (v/validate course-chapter-with-no-name) => (throws IllegalArgumentException))
(fact (v/validate course-chapter-with-no-title) => (throws IllegalArgumentException))
(fact (v/validate course-chapter-with-no-sub-chapters) => (throws IllegalArgumentException))
(fact (v/validate course-chapter-with-empty-sub-chapters) => (throws IllegalArgumentException))
(fact (v/validate course-chapter-with-additional-key) => (throws IllegalArgumentException))
(fact (v/validate course-chapter-with-same-name-sub-chapters) => (throws IllegalArgumentException))
(fact (v/validate course-chapter-with-sub-chapter-dif-key ) => (throws IllegalArgumentException))