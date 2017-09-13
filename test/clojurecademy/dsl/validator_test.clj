(ns clojurecademy.dsl.validator-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]
            [clojurecademy.dsl.validator :refer :all]))

(def course-map {:manifest {:title                    "Clojure Course"
                            :report-bug-email-or-link "ertu@gmail.com"
                            :name                     'my-clojure-course
                            :short-description        "Clojure is ...."
                            :long-description         "Long desc............."
                            :skip?                    false
                            :who-is-this-course-for   :clojure-experience}
                 :chapters [{:name         'as
                             :title        "1"
                             :sub-chapters [{:name     'sub-ch-clojure-intro
                                             :title    "Title of sub chapter"
                                             :subjects [{:name        'subject-scalars
                                                         :title       "Title"
                                                         :learn       {:texts [{:p     true
                                                                                :texts [{:normal-text true
                                                                                         :text        "Hi,there"}
                                                                                        {:code true
                                                                                         :form "(+ 1 2 3)"
                                                                                         :lang "clojure"}]}]}
                                                         :instruction {:name             'ins-type-scalar
                                                                       :run-pre-tests?   true
                                                                       :initial-code     {:form "(+ 1 2 3)"}
                                                                       :rule             {:only-use-one-fn? true
                                                                                          :required-fns     nil
                                                                                          :restricted-fns   nil}
                                                                       :sub-instructions [{:name             'sub-inss
                                                                                           :instruction-text [{:p     true
                                                                                                               :texts [{:normal-text true
                                                                                                                        :text        "Clojure is cool!"}
                                                                                                                       {:code true
                                                                                                                        :form "(+ 1 2 3)"
                                                                                                                        :lang "clojure"}]}]
                                                                                           :testing          {:is [{:form               "(+ 1 2 3)"
                                                                                                                    :error-message      nil
                                                                                                                    :error-message-type :simple}]}}]}
                                                         :ns          'my-ns}]}]}]})



(fact (validate course-map) => nil)
