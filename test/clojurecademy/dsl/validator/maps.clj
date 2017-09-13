(ns clojurecademy.dsl.validator.maps
  (:require [clojurecademy.dsl.util :as util]))

(def course {:title       "Clojure Course"
             :description "Teaches clojure programming lang."
             :chapters    [{:name         'ch-1
                            :title        "My title"
                            :sub-chapters [{:name     'sub-ch-clojure-intro
                                            :title    "Title of sub chapter"
                                            :subjects [{:name        'subject-scalars
                                                        :title       "Title"
                                                        :learn       {:text [{:normal-text true
                                                                              :text        "Hi,there"}
                                                                             {:code true
                                                                              :form ["(+ 1 2 )" "{" "}"]
                                                                              :lang :clojure}]}
                                                        :instruction {:name             'ins-type-scalar
                                                                      :run-pre-tests?   true
                                                                      :initial-code     {:form ["(+ 1 2)" "[" "]"]}
                                                                      :rule             {:only-use-one-fn? true
                                                                                         :required-fns     nil
                                                                                         :restricted-fns   nil}
                                                                      :sub-instructions [{:name             'sub-inss
                                                                                          :instruction-text [{:normal-text true
                                                                                                              :text        "Clojure is cool!"}
                                                                                                             {:code true
                                                                                                              :form ["1" "2" "" "(+ 1 2)"]
                                                                                                              :lang :clojure}]
                                                                                          :testing          {:is [{:form          "(+ 1 2 3)"
                                                                                                                   :error-message nil
                                                                                                                   :error-message-type :simple}]}}]}
                                                        :ns          'my-ns}]}]}]})

(def chapter (-> course :chapters first))

(def sub-chapter (-> chapter :sub-chapters first))

(def subject (-> sub-chapter :subjects first))

(def instruction (-> subject :instruction))

(def sub-instruction (-> instruction :sub-instructions first))

(def testing (-> sub-instruction :testing))