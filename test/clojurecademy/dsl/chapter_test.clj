(ns clojurecademy.dsl.chapter-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "name can not be nil"
      (chapter nil "Title" (subject 'subject-1 "Title" (learn (text "Hi")) nil)) => (throws IllegalArgumentException))

(fact "name has to be symbol"
      (chapter "ch-intro-clojure" "Title" (subject 'subject-1 "Title" (learn (text "Hi")) nil)) => (throws IllegalArgumentException)
      (chapter 1 "Title" (subject 'subject-1 "Title" (learn (text "Hi")) nil)) => (throws IllegalArgumentException)
      (chapter true "Title" (subject 'subject-1 "Title" (learn (text "Hi")) nil)) => (throws IllegalArgumentException)
      (chapter \a "Title" (subject 'subject-1 "Title" (learn (text "Hi")) nil)) => (throws IllegalArgumentException)
      (chapter 1.2 "Title" (subject 'subject-1 "Title" (learn (text "Hi")) nil)) => (throws IllegalArgumentException))

(fact "subject has to be proper map format"
      (chapter 'ch-1-intro-clojure "Title" {:a 1 :b 2}) => (throws IllegalArgumentException))

(fact "Chapter map form"
      (chapter 'ch-1-intro-clojure
               "1st Clojure Chapter"
               (sub-chapter 'sub-ch-clojure-intro
                            "Title of sub chapter"
                            (subject 'subject-scalars
                                     "Title"
                                     (learn (text (p "Hi,there")))
                                     (instruction 'ins-type-scalar
                                                  (run-pre-tests? false)
                                                  (initial-code (= 1 1))
                                                  (rule :only-use-one-fn? true)
                                                  (sub-instruction 'my-sub-ins-1
                                                                   (text (p "Clojure is cool!"))
                                                                   (testing (is (= 1 0)))))
                                     'my-ns)))
      => {:name         'ch-1-intro-clojure
          :title        "1st Clojure Chapter"
          :sub-chapters [{:name     'sub-ch-clojure-intro
                          :title    "Title of sub chapter"
                          :subjects [{:name        'subject-scalars
                                      :title       "Title"
                                      :learn       {:texts [{:p     true
                                                             :texts [{:normal-text true
                                                                      :text        "Hi,there"}]}]}
                                      :instruction {:name             'ins-type-scalar
                                                    :run-pre-tests?   false
                                                    :initial-code     {:form "(= 1 1)"}
                                                    :rule             {:only-use-one-fn? true
                                                                       :required-fns     nil
                                                                       :restricted-fns   nil}
                                                    :sub-instructions [{:name             'my-sub-ins-1
                                                                        :instruction-text [{:p     true
                                                                                            :texts [{:normal-text true
                                                                                                     :text        "Clojure is cool!"}]}]
                                                                        :testing          {:is [{:form          "(= 1 0)"
                                                                                                 :error-message nil
                                                                                                 :error-message-type :simple}]}}]}
                                      :ns          'my-ns}]}]}

      (chapter 'ch-1-intro-clojure
               "1st Clojure Chapter"
               (sub-chapter 'sub-ch-clojure-intro
                            "Title of sub chapter"
                            (subject 'subject-scalars
                                     "Title"
                                     (learn (text (p "Hi,there")))
                                     (instruction 'ins-type-scalar
                                                  (run-pre-tests? false)
                                                  (initial-code (= 1 1))
                                                  (rule :only-use-one-fn? true)
                                                  (sub-instruction 'my-sub-ins-1
                                                                   (text (p "Clojure is cool!"))
                                                                   (testing (is (= 1 0)))))
                                     'my-ns-1)
                            (subject 'subject-scalars-2
                                     "Title"
                                     (learn (text (p "Hi,there")))
                                     (instruction 'ins-type-scalar
                                                  (run-pre-tests? false)
                                                  (initial-code (= 1 1))
                                                  (rule :only-use-one-fn? true)
                                                  (sub-instruction 'my-sub-ins-1
                                                                   (text (p "Clojure is cool!"))
                                                                   (testing (is (= 1 0)))))
                                     'my-ns-2)
                            (subject 'subject-scalars-3
                                     "Title"
                                     (learn (text (p "Hi,there")))
                                     (instruction 'ins-type-scalar
                                                  (run-pre-tests? false)
                                                  (initial-code (= 1 1))
                                                  (rule :only-use-one-fn? true)
                                                  (sub-instruction 'my-sub-ins-1
                                                                   (text (p "Clojure is cool!"))
                                                                   (testing (is (= 1 0)))))
                                     'my-ns-3)))
      => {:name         'ch-1-intro-clojure
          :title        "1st Clojure Chapter"
          :sub-chapters [{:name     'sub-ch-clojure-intro
                          :title    "Title of sub chapter"
                          :subjects [{:name        'subject-scalars
                                      :title       "Title"
                                      :learn       {:texts [{:p     true
                                                             :texts [{:normal-text true
                                                                      :text        "Hi,there"}]}]}
                                      :instruction {:name             'ins-type-scalar
                                                    :run-pre-tests?   false
                                                    :initial-code     {:form "(= 1 1)"}
                                                    :rule             {:only-use-one-fn? true
                                                                       :required-fns     nil
                                                                       :restricted-fns   nil}
                                                    :sub-instructions [{:name             'my-sub-ins-1
                                                                        :instruction-text [{:p     true
                                                                                            :texts [{:normal-text true
                                                                                                     :text        "Clojure is cool!"}]}]
                                                                        :testing          {:is [{:form          "(= 1 0)"
                                                                                                 :error-message nil
                                                                                                 :error-message-type :simple}]}}]}
                                      :ns          'my-ns-1}
                                     {:name        'subject-scalars-2
                                      :title       "Title"
                                      :learn       {:texts [{:p     true
                                                             :texts [{:normal-text true
                                                                      :text        "Hi,there"}]}]}
                                      :instruction {:name             'ins-type-scalar
                                                    :run-pre-tests?   false
                                                    :initial-code     {:form "(= 1 1)"}
                                                    :rule             {:only-use-one-fn? true
                                                                       :required-fns     nil
                                                                       :restricted-fns   nil}
                                                    :sub-instructions [{:name             'my-sub-ins-1
                                                                        :instruction-text [{:p     true
                                                                                            :texts [{:normal-text true
                                                                                                     :text        "Clojure is cool!"}]}]
                                                                        :testing          {:is [{:form          "(= 1 0)"
                                                                                                 :error-message nil
                                                                                                 :error-message-type :simple}]}}]}
                                      :ns          'my-ns-2}
                                     {:name        'subject-scalars-3
                                      :title       "Title"
                                      :learn       {:texts [{:p     true
                                                             :texts [{:normal-text true
                                                                      :text        "Hi,there"}]}]}
                                      :instruction {:name             'ins-type-scalar
                                                    :run-pre-tests?   false
                                                    :initial-code     {:form "(= 1 1)"}
                                                    :rule             {:only-use-one-fn? true
                                                                       :required-fns     nil
                                                                       :restricted-fns   nil}
                                                    :sub-instructions [{:name             'my-sub-ins-1
                                                                        :instruction-text [{:p     true
                                                                                            :texts [{:normal-text true
                                                                                                     :text        "Clojure is cool!"}]}]
                                                                        :testing          {:is [{:form          "(= 1 0)"
                                                                                                 :error-message nil
                                                                                                 :error-message-type :simple}]}}]}
                                      :ns          'my-ns-3}]}]})


(fact "chapters' sub-chapters can't have the same name"
      (chapter 'ch-1-intro-clojure
               "1st Clojure Chapter"
               (sub-chapter 'sub-ch-clojure-intro
                            "Title of sub chapter"
                            (subject 'subject-scalars
                                     "Title"
                                     (learn (text (p "Hi,there")))
                                     (instruction 'ins-type-scalar
                                                  (run-pre-tests? false)
                                                  (initial-code (= 1 1))
                                                  (rule :only-use-one-fn? true)
                                                  (sub-instruction 'my-sub-ins-1
                                                                   (text "Clojure is cool!")
                                                                   (testing (is (= 1 0)))))
                                     'who-knows))
               (sub-chapter 'sub-ch-clojure-intro
                            "Title of sub chapter"
                            (subject 'subject-scalars
                                     "Title"
                                     (learn (text (p "Hi,there")))
                                     (instruction 'ins-type-scalar
                                                  (run-pre-tests? false)
                                                  (initial-code (= 1 1))
                                                  (rule :only-use-one-fn? true)
                                                  (sub-instruction 'my-sub-ins-1
                                                                   (text "Clojure is cool!")
                                                                   (testing (is (= 1 0)))))
                                     'who-knows))) => (throws IllegalArgumentException))