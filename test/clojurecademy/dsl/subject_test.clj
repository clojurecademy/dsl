(ns clojurecademy.dsl.subject-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "name can not be nil"
      (subject nil "Title" (learn (text (p "Learn text!!!"))) nil) => (throws IllegalArgumentException))

(fact "name has to be symbol"
      (subject "name-of-subject" "Title" (learn (text (p "Learn text!!!"))) nil) => (throws IllegalArgumentException))

(fact "learn can not be nil"
      (subject 'my-first-subject "This is title for subject" nil nil) => (throws IllegalArgumentException))

(fact "instructions can be nil"
      (subject 'my-first-subject "Title for me" (learn (text (p "Learn text!!!"))) nil) => map?)

(fact "with ns"
      (subject 'my-first-subject
               "Title"
               (learn (text (p "Hi,there")))
               (instruction 'ins-my-first-instruction
                            (run-pre-tests? false)
                            (initial-code (= 1 1))
                            (rule :only-use-one-fn? true)
                            (sub-instruction 'my-sub-ins-1
                                             (text (p "Clojure is cool!"))
                                             (testing (is (= 1 0)))))
               'my-ns.clojure)
      => {:name        'my-first-subject
          :title       "Title"
          :learn       {:texts [{:p     true
                                 :texts [{:normal-text true
                                          :text        "Hi,there"}]}]}
          :instruction {:name             'ins-my-first-instruction
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
          :ns          'my-ns.clojure})

(fact "with ns in initial code both are acceptable"
      (subject 'my-first-subject
               "Title"
               (learn (text (p "Hi,there")))
               (instruction 'ins-my-first-instruction
                            (run-pre-tests? false)
                            (initial-code (ns initial-code-ns-name))
                            (rule :only-use-one-fn? true)
                            (sub-instruction 'my-sub-ins-1
                                             (text (p "Clojure is cool!"))
                                             (testing (is (= 1 0))))))
      => {:name        'my-first-subject
          :title       "Title"
          :learn       {:texts [{:p     true
                                 :texts [{:normal-text true
                                          :text        "Hi,there"}]}]}
          :instruction {:name             'ins-my-first-instruction
                        :run-pre-tests?   false
                        :initial-code     {:form "(ns initial-code-ns-name)"}
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
          :ns          nil})

(fact "title has to be string"
      (subject 'my-first-subject 1 (learn (text (p "Hi"))) nil) => (throws IllegalArgumentException)
      (subject 'my-first-subject false (learn (text (p "Hi"))) nil) => (throws IllegalArgumentException)
      (subject 'my-first-subject 1/3 (learn (text (p "Hi"))) nil) => (throws IllegalArgumentException)
      (subject 'my-first-subject 'title (learn (text (p "Hi"))) nil) => (throws IllegalArgumentException)
      (subject 'my-first-subject \a (learn (text (p "Hi"))) nil) => (throws IllegalArgumentException)
      (subject 'my-first-subject {:some-key 1} (learn (text (p "Hi"))) nil) => (throws IllegalArgumentException))

(fact "title can not be blank"
      (subject 'my-first-subject "" (learn (text (p "Hi"))) nil) => (throws IllegalArgumentException)
      (subject 'my-first-subject "   " (learn (text (p "Hi"))) nil) => (throws IllegalArgumentException))

(fact "learn has to be proper map format"
      (subject 'my-first-subject "Title" {:some-key 1} nil) => (throws IllegalArgumentException)
      (subject 'my-first-subject "Title" 12 nil) => (throws IllegalArgumentException))

(fact "instruction has to be proper map format"
      (subject 'my-first-subject "Title again" (learn (text (p "Here!!!"))) {:some-key 12}) => (throws IllegalArgumentException)

      (subject 'my-first-subject "Title again" (learn (text (p "Here!!!"))) {:some-key         1
                                                                             :run-pre-tests?   true
                                                                             :initial-code     {:form "(= 1 1)"}
                                                                             :rule             {:only-use-one-fn? true
                                                                                                :required-fns     nil
                                                                                                :restricted-fns   nil}
                                                                             :sub-instructions [{:text    [{:normal-text true
                                                                                                            :text        "Clojure is cool!"}]
                                                                                                 :testing {:is [{:form          "(= 1 0)"
                                                                                                                 :error-message nil
                                                                                                                 :error-message-type :simple}]}}]}) => (throws IllegalArgumentException))

(fact "ns has to be symbol"
      (subject 'my-first-subject "Title again" (learn (text (p "Here!!!"))) nil "clojure.core") => (throws IllegalArgumentException)
      (subject 'my-first-subject "Title again" (learn (text (p "Here!!!"))) nil 1) => (throws IllegalArgumentException)
      (subject 'my-first-subject "Title again" (learn (text (p "Here!!!"))) nil true) => (throws IllegalArgumentException)
      (subject 'my-first-subject "Title again" (learn (text (p "Here!!!"))) nil \a) => (throws IllegalArgumentException)
      (subject 'my-first-subject "Title again" (learn (text (p "Here!!!"))) nil 1.2) => (throws IllegalArgumentException))

(fact "ns is a symbol and it is optional"
      (subject 'my-first-subject "Title again" (learn (text (p "Here!!!"))) nil 'my-ns.subject1) => {:name        'my-first-subject
                                                                                                     :title       "Title again"
                                                                                                     :learn       {:texts [{:p     true
                                                                                                                            :texts [{:normal-text true
                                                                                                                                     :text        "Here!!!"}]}]}
                                                                                                     :instruction nil
                                                                                                     :ns          'my-ns.subject1})

(fact "learn's character long can not be longer than 2000 chars long."
      (subject 'my-first-subject
               "Title again"
               (learn (text (apply str (repeat 2001 "a"))))
               nil) => (throws IllegalArgumentException))

(fact "ns has to be provided in some way."
      (subject 'my-first-subject
               "Title"
               (learn (text (p "Hi,there")))
               (instruction 'ins-first
                            (run-pre-tests? true)
                            (initial-code (= 1 1))
                            (rule :only-use-one-fn? true)
                            (sub-instruction 'my-sub-ins-1
                                             (text "Clojure is cool!")
                                             (testing (is (= 1 0)))))) => (throws IllegalArgumentException))