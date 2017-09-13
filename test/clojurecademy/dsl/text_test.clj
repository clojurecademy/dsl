(ns clojurecademy.dsl.text-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "Text has to take at least one parameter"
      (text) => (throws IllegalArgumentException))

(fact "Text can not take unkown map"
      (text (p "Hey you! -> " {:unkown-key 1})) => (throws IllegalArgumentException)
      (text {:unkown-key 1}) => (throws IllegalArgumentException))

(fact "Text can take either strings or maps otherwise fails"
      (text 1 true 1/2 "Cool" {:a 1}) => (throws IllegalArgumentException))

(fact "Text is map"
      (text (p "Clojure" "is" "so" "cool")) => map?)

(fact "Text has maps nothing more"
      (text (p "Hi,")) => {:texts [{:p     true
                                    :texts [{:normal-text true
                                             :text        "Hi,"}]}]})

(fact "Text is formatter for (code), (link), (bold), (italic), (hi)"
      (fact "With code"
            (text (p "This is the Clojure code: ") (code (+ 1 2 3))) => {:texts [{:p     true
                                                                                  :texts [{:normal-text true
                                                                                           :text        "This is the Clojure code: "}]}
                                                                                 {:code true
                                                                                  :form "(+ 1 2 3)"
                                                                                  :lang "clojure"}]})
      (fact "With link"
            (text (p "Here is my link: " (link "https://google.com"))) => {:texts [{:p     true
                                                                                    :texts [{:normal-text true
                                                                                             :text        "Here is my link: "}
                                                                                            {:link  true
                                                                                             :src   "https://google.com"
                                                                                             :title nil}]}]}

            (text (p "Here is my link with title: " (link "Home Page" "https://google.com"))) => {:texts [{:p     true
                                                                                                           :texts [{:normal-text true
                                                                                                                    :text        "Here is my link with title: "}
                                                                                                                   {:link  true
                                                                                                                    :src   "https://google.com"
                                                                                                                    :title "Home Page"}]}]})
      (fact "With bold"
            (text (p "Here is the bold one: " (bold "Yeah That's me!"))) => {:texts [{:p     true
                                                                                      :texts [{:normal-text true
                                                                                               :text        "Here is the bold one: "}
                                                                                              {:bold true
                                                                                               :text "Yeah That's me!"}]}]})
      (fact "With italic"
            (text (p "Het italic!" (italic "Hey normal text!"))) => {:texts [{:p     true
                                                                              :texts [{:normal-text true
                                                                                       :text        "Het italic!"}
                                                                                      {:italic true
                                                                                       :text   "Hey normal text!"}]}]})
      (fact "With hi"
            (text (p "Hi five?" (hi "Nope :/"))) => {:texts [{:p     true
                                                              :texts [{:normal-text true
                                                                       :text        "Hi five?"}
                                                                      {:hi   true
                                                                       :text "Nope :/"}]}]})
      (fact "With p"
            (text (p "Please go down!") (p "And please do it again!")) => {:texts [{:p     true
                                                                                    :texts [{:normal-text true
                                                                                             :text        "Please go down!"}]}
                                                                                   {:p     true
                                                                                    :texts [{:normal-text true
                                                                                             :text        "And please do it again!"}]}]})
      (fact "And all together"
            (text (p "Clojure is " (italic "functional programming language") "and it is dialect of" (bold "Lisp Language"))
                  (p "Here is the official website: " (link "Clojure Site" "http://clojure.org") ".")
                  (p "Here is the simple Clojure Code that sums the values and prints 6:")
                  (code (reduce + [1 2 3]))
                  (p "Clojure is higly scalable and very " (hi "productive ") "language"))
            => {:texts [{:p     true
                         :texts [{:normal-text true
                                  :text        "Clojure is "}
                                 {:italic true
                                  :text   "functional programming language"}
                                 {:normal-text true
                                  :text        "and it is dialect of"}
                                 {:bold true
                                  :text "Lisp Language"}]}
                        {:p     true
                         :texts [{:normal-text true
                                  :text        "Here is the official website: "}
                                 {:link  true
                                  :src   "http://clojure.org"
                                  :title "Clojure Site"}
                                 {:normal-text true
                                  :text        "."}]}
                        {:p     true
                         :texts [{:normal-text true
                                  :text        "Here is the simple Clojure Code that sums the values and prints 6:"}]}
                        {:code true
                         :form "(reduce + [1 2 3])"
                         :lang "clojure"}
                        {:p     true
                         :texts [{:normal-text true
                                  :text        "Clojure is higly scalable and very "}
                                 {:hi   true
                                  :text "productive "}
                                 {:normal-text true
                                  :text        "language"}]}]}))