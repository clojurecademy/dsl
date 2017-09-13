(ns clojurecademy.dsl.sub-chapter-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "name can not be nil"
      (sub-chapter nil "Title" (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException))

(fact "name has to be symbol"
      (sub-chapter "sub-ch-intro-to-clojure" "Title" (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException)
      (sub-chapter 1 "Title" (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException)
      (sub-chapter true "Title" (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException)
      (sub-chapter 1.2 "Title" (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException)
      (sub-chapter \a "Title" (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException))

(fact "title can not be nil"
      (sub-chapter 'sub-ch-intro-clojure nil (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException))

(fact "title can not be blank"
      (sub-chapter 'sub-ch-intro-clojure "" (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException)
      (sub-chapter 'sub-ch-intro-clojure "   " (subject 'subject-1 "Title" (learn (text (p "Hi"))) nil)) => (throws IllegalArgumentException))

(fact "subject has to be proper map format"
      (sub-chapter 'sub-ch-intro "Hey there!" {:some-key 1}) => (throws IllegalArgumentException))

(fact
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
                        'my-ns))

  => {:name     'sub-ch-clojure-intro
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
                  :ns          'my-ns}]})

(fact "with multi sub-instructions"
      (sub-chapter 'sub-ch-clojure-intro
                   "Title of sub chapter"
                   (subject 'subject-scalars
                            "Title"
                            (learn (text (p "Hi,there")))
                            (instruction 'ins-type-scalar
                                         (run-pre-tests? false)
                                         (initial-code (ns ertus))
                                         (rule :only-use-one-fn? true)
                                         (sub-instruction 'my-sub-ins-1
                                                          (text (p "Clojure is cool!"))
                                                          (testing (is (= 1 0))))
                                         (sub-instruction 'my-sub-ins-2
                                                          (text (p "Clojure is cool!"))
                                                          (testing (is (= 1 0)))))))

      => {:name     'sub-ch-clojure-intro
          :title    "Title of sub chapter"
          :subjects [{:name        'subject-scalars
                      :title       "Title"
                      :learn       {:texts [{:p     true
                                             :texts [{:normal-text true
                                                      :text        "Hi,there"}]}]}
                      :instruction {:name             'ins-type-scalar
                                    :run-pre-tests?   false
                                    :initial-code     {:form "(ns ertus)"}
                                    :rule             {:only-use-one-fn? true
                                                       :required-fns     nil
                                                       :restricted-fns   nil}
                                    :sub-instructions [{:name             'my-sub-ins-1
                                                        :instruction-text [{:p     true
                                                                            :texts [{:normal-text true
                                                                                     :text        "Clojure is cool!"}]}]
                                                        :testing          {:is [{:form          "(= 1 0)"
                                                                                 :error-message nil
                                                                                 :error-message-type :simple}]}}
                                                       {:name             'my-sub-ins-2
                                                        :instruction-text [{:p     true
                                                                            :texts [{:normal-text true
                                                                                     :text        "Clojure is cool!"}]}]
                                                        :testing          {:is [{:form          "(= 1 0)"
                                                                                 :error-message nil
                                                                                 :error-message-type :simple}]}}]}
                      :ns          nil}]})

(fact "title can not be longer then 80 chars"
      (sub-chapter 'sub-ch-clojure-intro
                   "Title of sub chapter Title of sub chapter  Title of sub chapter  Title of sub chapter "
                   (subject 'subject-scalars
                            "Title"
                            (learn (text "Hi,there"))
                            (instruction 'ins-type-scalar
                                         (run-pre-tests? true)
                                         (initial-code (= 1 1))
                                         (rule :only-use-one-fn? true)
                                         (sub-instruction 'my-sub-ins-1
                                                          (text "Clojure is cool!")
                                                          (testing (is (= 1 0))))
                                         (sub-instruction 'my-sub-ins-2
                                                          (text "Clojure is cool!")
                                                          (testing (is (= 1 0))))))) => (throws IllegalArgumentException))

(fact "sub-chapters' subjects can't have the same name"
      (sub-chapter 'sub-ch-clojure-intro
                   "Title"
                   (subject 'subject-scalars
                            "Title"
                            (learn (text "Hi,there"))
                            (instruction 'ins-type-scalar
                                         (run-pre-tests? true)
                                         (initial-code (= 1 1))
                                         (rule :only-use-one-fn? true)
                                         (sub-instruction 'my-sub-ins-1
                                                          (text "Clojure is cool!")
                                                          (testing (is (= 1 0))))
                                         (sub-instruction 'my-sub-ins-2
                                                          (text "Clojure is cool!")
                                                          (testing (is (= 1 0)))))
                            'my-ns)
                   (subject 'subject-scalars
                            "Title"
                            (learn (text "Hi,there"))
                            (instruction 'ins-type-scalar
                                         (run-pre-tests? true)
                                         (initial-code (= 1 1))
                                         (rule :only-use-one-fn? true)
                                         (sub-instruction 'my-sub-ins-1
                                                          (text "Clojure is cool!")
                                                          (testing (is (= 1 0))))
                                         (sub-instruction 'my-sub-ins-2
                                                          (text "Clojure is cool!")
                                                          (testing (is (= 1 0)))))
                            'my-ns)) => (throws IllegalArgumentException))