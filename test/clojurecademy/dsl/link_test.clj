(ns clojurecademy.dsl.link-test
  (:require [midje.sweet :refer :all]
            [clojurecademy.dsl.core :refer :all]))

(fact "Link's src has to be string"
      (link true) => (throws IllegalArgumentException)
      (link false) => (throws IllegalArgumentException)
      (link 1.2) => (throws IllegalArgumentException)
      (link \a) => (throws IllegalArgumentException)
      (link 1) => (throws IllegalArgumentException))

(fact "Link's src has to be proper URL"
      (link "google.com") => (throws IllegalArgumentException))

(fact "Link is map"
      (link "http://www.google.com") => map?)

(fact "Link's src is URL"
      (link "https://www.google.com") => {:link  true
                                          :src   "https://www.google.com"
                                          :title nil}

      (link "https://google.com") => {:link  true
                                      :src   "https://google.com"
                                      :title nil}

      (link "https://google.com/#/search?q=try-me") => {:link  true
                                                        :src   "https://google.com/#/search?q=try-me"
                                                        :title nil})

(fact "Link also takes title for src"
      (link "Google Home Page" "https://google.com") => {:link  true
                                                         :src   "https://google.com"
                                                         :title "Google Home Page"})

(fact "Title can not be any type but string"
      (link 1 "https://google.com") => (throws IllegalArgumentException)
      (link true "https://google.com") => (throws IllegalArgumentException)
      (link 1.2 "https://google.com") => (throws IllegalArgumentException)
      (link \a "https://google.com") => (throws IllegalArgumentException))

(fact "Title can be nil"
      (link nil "https://google.com") => {:link  true
                                          :src   "https://google.com"
                                          :title nil})
