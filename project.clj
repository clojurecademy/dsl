(defproject clojurecademy "0.1.0"

  :description "Clojurecademy DSL"

  :url "https://github.com/clojurecademy/dsl"

  :author "Ertuğrul Çetin"

  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [kezban "0.1.7"]
                 [midje "1.8.3"]]

  :plugins [[lein-midje "3.2.1"]]

  :uberjar-name "clojurecademy-dsl.jar")