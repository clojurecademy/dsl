(ns clojurecademy.dsl.course-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(def chapter-map (chapter 'ch-1-intro-clojure
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
                                                'my-ns))))
(def chapter-map-with-dif-name (chapter 'ch-2-advanced-clojure
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
                                                              'my-ns))))
(fact "course is the main map"
      (course (manifest :title "Clojure Course"
                        :report-bug-email-or-link "ertu@gmail.com"
                        :name 'my-clojure-course
                        :skip? false
                        :short-description "Clojure is ...."
                        :long-description "Long desc............."
                        :who-is-this-course-for "This course for everyone!")
              chapter-map)
      => {:manifest {:title                    "Clojure Course"
                     :report-bug-email-or-link "ertu@gmail.com"
                     :name                     'my-clojure-course
                     :short-description        "Clojure is ...."
                     :long-description         "Long desc............."
                     :skip?                    false
                     :who-is-this-course-for   "This course for everyone!"}
          :chapters [{:name         'ch-1-intro-clojure
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
                                                                                    :testing          {:is [{:form               "(= 1 0)"
                                                                                                             :error-message      nil
                                                                                                             :error-message-type :simple}]}}]}
                                                  :ns          'my-ns}]}]}]})

(fact "course's chapters can't have the same name"
      (course (manifest :title "Clojure Course"
                        :report-bug-email-or-link "ertu@gmail.com"
                        :name 'my-clojure-course
                        :short-description "Clojure is ...."
                        :long-description "Long desc............."
                        :who-is-this-course-for :programming-experience)
              chapter-map
              chapter-map
              chapter-map) => (throws IllegalArgumentException))

(fact "course's chapters can have different names"
      (course (manifest :title "Clojure Course"
                        :report-bug-email-or-link "ertu@gmail.com"
                        :name 'my-clojure-course
                        :short-description "Clojure is ...."
                        :long-description "Long desc............."
                        :who-is-this-course-for :no-programming-experience)
              chapter-map
              chapter-map-with-dif-name) => map?)

(course (manifest :title "Clojure Course"
                  :report-bug-email-or-link "ertu@gmail.com"
                  :name 'my-clojure-course
                  :report-bug-email-or-link "ertu@gmail.com"
                  :short-description "Intro to Clojure Programming Language"
                  :long-description "You are going to learn Clojure Programming language in and out!"
                  :who-is-this-course-for :clojure-experience)
        (chapter 'ch-1
                 "Hello, World Clojure"
                 (sub-chapter 'sub-ch-1
                              "First Hello, World with Clojure"
                              (subject 'subject-1
                                       "Let's write first Hello, World App"
                                       (learn (text (p "Clojure is a functionnal programming language.")
                                                    (p "Also it's a Lisp dialect and runs on JVM")
                                                    (p "First we are going to write our first program.")))
                                       (instruction 'ins
                                                    (run-pre-tests? false)
                                                    nil
                                                    nil
                                                    (sub-instruction 'sub-ins-1
                                                                     (text (p "Please copy and pase following code to the editor and click run.")
                                                                           (code (println "Hello, World")))
                                                                     (testing (is (= (first (get-ds)) '(println "Hello, World"))))))
                                       'hello-world)
                              (subject 'subject-2
                                       "Let's do some Math"
                                       (learn (text (p "We are going to learn some math operators and use those functions inside our Clojure program")
                                                    (p "Those operators are: " (hi "+, -, /, *"))
                                                    (p "Clojure handles math operators so smoothly.")))
                                       (instruction 'ins
                                                    (run-pre-tests? false)
                                                    nil
                                                    nil
                                                    (sub-instruction 'sub-ins-1
                                                                     (text (p "Please copy and pase following " (bold "addition") " code to the editor and click run.")
                                                                           (code (+ 1 1)))
                                                                     (testing (is (= (first (get-ds)) '(+ 1 1)))))
                                                    (sub-instruction 'sub-ins-2
                                                                     (text (p "Please copy and pase following " (bold "substraction") " code after the addition code to the editor and click run.")
                                                                           (code (- 2 1)))
                                                                     (testing (is (= (second (get-ds)) '(- 2 1)))))
                                                    (sub-instruction 'sub-ins-3
                                                                     (text (p "Please copy and pase following " (bold "multiplication") " code after the substraction code to the editor and click run.")
                                                                           (code (* 3 2)))
                                                                     (testing (is (= (nth (get-ds) 2) '(* 3 2)))))
                                                    (sub-instruction 'sub-ins-4
                                                                     (text (p "Please copy and pase following " (bold "division") " code after the multiplication code to the editor and click run.")
                                                                           (code (/ 4 2)))
                                                                     (testing (is (= (nth (get-ds) 3) '(/ 4 2))))))
                                       'math-operations)
                              (subject 'subject-3

                                       "Let's write some functions!!!"
                                       (learn (text (p "Now this chapter going to teach you how to define a function, at the end of the section you will gain knowledge of function declaration.")
                                                    (p "Let's check function declaration in Clojure here is a simple function name called " (hi "add-2-vals") " which takes 2 parameters " (hi "x") " and " (hi "y") ".")
                                                    (code "(defn add-2-vals\n  [x y]\n  (+ x y))")
                                                    (p "Now you are going to declare a function, please follow instructions to create your cool function!")))
                                       (instruction 'ins
                                                    (run-pre-tests? false)
                                                    nil
                                                    nil
                                                    (sub-instruction 'sub-ins-1
                                                                     (text (p "Create function name " (hi "my-subs") " which does the substraction like " (hi "-") " does."))
                                                                     (testing
                                                                       (is (= 0 (my-subs 1 1)))
                                                                       (is (= 1 (my-subs 2 1)))
                                                                       (is (= -1 (my-subs 2 1 1 1)))
                                                                       (is (= 0 (my-subs 5 3 2))))))
                                       'writing-functions))))