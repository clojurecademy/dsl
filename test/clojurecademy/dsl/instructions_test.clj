(ns clojurecademy.dsl.instructions-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "has to take at least one sub-instruction"
      (instruction 'ins-my-first-instruction
                   (run-pre-tests? true)
                   (initial-code (= 1 1))
                   (rule :only-use-one-fn? true)
                   nil) => (throws IllegalArgumentException))

(fact "single & multi sub-instructions"
      (fact "single sub-instruction"
            (instruction 'ins-my-first-instruction
                         (run-pre-tests? false)
                         (initial-code (= 1 1))
                         (rule :only-use-one-fn? true)
                         (sub-instruction 'my-sub-ins-1
                                          (text (p "Clojure is cool!"))
                                          (testing (is (= 1 0)))))
            => {:name             'ins-my-first-instruction
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
                                                             :error-message-type :simple}]}}]})

      (fact "multiple sub-instructions"
            (instruction 'ins-multi-name
                         (run-pre-tests? false)
                         (initial-code (= 1 1))
                         (rule :only-use-one-fn? true
                               :required-fns ['reduce 'do]
                               :restricted-fns ['eval 'resolve])
                         (sub-instruction 'my-sub-ins-1
                                          (text (p "Clojure is cool!"))
                                          (testing (is (= 1 0))))
                         (sub-instruction 'my-sub-ins-2
                                          (text (p "Yeah it is!"))
                                          (testing (is (= 1 1))))
                         (sub-instruction 'my-sub-ins-3
                                          (text (p "Really?"))
                                          (testing (is (= 1 0) "They are not equal"))))
            => {:name             'ins-multi-name
                :run-pre-tests?   false
                :initial-code     {:form "(= 1 1)"}
                :rule             {:only-use-one-fn? true
                                   :required-fns     ['reduce 'do]
                                   :restricted-fns   ['eval 'resolve]}
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
                                                                 :text        "Yeah it is!"}]}]
                                    :testing          {:is [{:form          "(= 1 1)"
                                                             :error-message nil
                                                             :error-message-type :simple}]}}
                                   {:name             'my-sub-ins-3
                                    :instruction-text [{:p     true
                                                        :texts [{:normal-text true
                                                                 :text        "Really?"}]}]
                                    :testing          {:is [{:form          "(= 1 0)"
                                                             :error-message "They are not equal"
                                                             :error-message-type :simple}]}}]}))

(fact "run-pre-tests?, initial-code and rule can be nil"
      (instruction 'ins-without-rules
                   false
                   nil
                   nil
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Really?"))
                                    (testing (is (= 1 0) "They are not equal"))))
      => {:name             'ins-without-rules
          :run-pre-tests?   false
          :initial-code     nil
          :rule             nil
          :sub-instructions [{:name             'my-sub-ins-1
                              :instruction-text [{:p     true
                                                  :texts [{:normal-text true
                                                           :text        "Really?"}]}]
                              :testing          {:is [{:form          "(= 1 0)"
                                                       :error-message "They are not equal"
                                                       :error-message-type :simple}]}}]})

(fact "sub-instructions have to be proper map format"
      (instruction nil nil nil nil {:some-key 1
                                    :text     "some"}) => (throws IllegalArgumentException)
      (instruction nil nil nil nil {:some-key 1
                                    :text     "some"
                                    :testing  {}}) => (throws IllegalArgumentException))

(fact "name can not be nil"
      (instruction nil
                   nil
                   nil
                   nil
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Really?"))
                                    (testing (is (= 1 0) "They are not equal")))) => (throws IllegalArgumentException))

(fact "name has to be symbol"
      (instruction "ins-without-rules"
                   nil
                   nil
                   nil
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Really?"))
                                    (testing (is (= 1 0) "They are not equal")))) => (throws IllegalArgumentException))

(fact "sub instructions' content can not be longer than 2000 chars long."

      (instruction 'ins-without-rules
                   false
                   nil
                   nil
                   (sub-instruction 'my-sub-ins-1
                                    (text (apply str (repeat 1000 "a")))
                                    (testing (is (= 1 0) "They are not equal")))
                   (sub-instruction 'my-sub-ins-1
                                    (text (apply str (repeat 1000 "a")))
                                    (testing (is (= 1 0) "They are not equal")))
                   (sub-instruction 'my-sub-ins-1
                                    (text "a1")
                                    (testing (is (= 1 0) "They are not equal")))) => (throws IllegalArgumentException))

(fact "instruction's sub-instructions can't have the same name"
      (instruction 'ins-my-first-instruction
                   (run-pre-tests? true)
                   (initial-code (= 1 1))
                   (rule :only-use-one-fn? true)
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Clojure is cool!"))
                                    (testing (is (= 1 0))))
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Clojure is cool!"))
                                    (testing (is (= 1 0))))
                   (sub-instruction
                     'my
                     (text (p "Clojure is cool!"))
                     (testing (is (= 1 0))))
                   (sub-instruction
                     'my
                     (text (p "Clojure is cool!"))
                     (testing (is (= 1 0))))) => (throws IllegalArgumentException)
      (fact "it is okay the have multiple non-named sub-instructions"
            (instruction 'ins-my-first-instruction
                         (run-pre-tests? false)
                         (initial-code (= 1 1))
                         (rule :only-use-one-fn? true)
                         (sub-instruction 'my-sub-ins-1
                                          (text (p "Clojure is cool!"))
                                          (testing (is (= 1 0))))
                         (sub-instruction 'my-sub-ins-2
                                          (text (p "Clojure is cool!"))
                                          (testing (is (= 1 0))))) => map?))

(fact "run-pre-test? can be true for named sub-ins"
      (instruction 'ins-my-first-instruction
                   (run-pre-tests? true)
                   (initial-code (= 1 1))
                   (rule :only-use-one-fn? true)
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Clojure is cool!"))
                                    (testing (is (= 1 0))))
                   (sub-instruction 'my-sub-ins-2
                                    (text (p "Clojure is cool!"))
                                    (testing (is (= 1 0))))
                   (sub-instruction 'my-sub-ins-3
                                    (text (p "Clojure is cool!"))
                                    (testing (is (= 1 0))))) => map?)

(fact "run-pre-test? can't be true for nameless sub-ins"
      (instruction 'ins-my-first-instruction
                   (run-pre-tests? true)
                   (initial-code (= 1 1))
                   (rule :only-use-one-fn? true)
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Clojure is cool!"))
                                    (testing (is (= 1 0))))
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Clojure is cool!"))
                                    (testing (is (= 1 0))))
                   (sub-instruction 'my-sub-ins-1
                                    (text (p "Clojure is cool!"))
                                    (testing (is (= 1 0))))) => (throws IllegalArgumentException))