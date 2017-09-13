(ns clojurecademy.dsl.core
  (:require [clojure.string :as str]
            [clojurecademy.dsl.util :as util]
            [clojurecademy.dsl.assert :as assert]))

(declare code)

(defn- get-who-is-this-course-for-str
  [who-is-this-course-for]
  (case who-is-this-course-for
    :clojure-experience "This course requires some Clojure experience."
    :programming-experience "This course requires some programming experience, Clojure experience is NOT required."
    :no-programming-experience "This course does not require any programming experience, it's for total beginners to programming."
    who-is-this-course-for))

(defn manifest
  [& {:as m}]
  (assert/manifest m)
  (assoc m :who-is-this-course-for
           (get-who-is-this-course-for-str (:who-is-this-course-for m))))

(defn course
  [manifest & chapters]
  (assert/course chapters)
  {:manifest manifest
   :chapters (vec chapters)})

(defn chapter
  [name title & sub-chapters]
  (assert/chapter name title sub-chapters)
  {:name         name
   :title        title
   :sub-chapters (vec sub-chapters)})

(defn sub-chapter
  [name title & subjects]
  (assert/sub-chapter name title subjects)
  {:name     name
   :title    title
   :subjects (vec subjects)})

(defn subject
  ([name title learn]
   (subject name title learn nil nil))
  ([name title learn instruction]
   (subject name title learn instruction nil))
  ([name title learn instruction ns]
   (assert/subject name title learn instruction ns)
   {:name        name
    :title       title
    :learn       (:learn learn)
    :instruction instruction
    :ns          ns}))

(defn learn
  [text]
  (assert/learn text)
  {:learn text})

(defn instruction
  [name run-pre-tests initial-code rule & sub-instructions]
  (assert/instruction name run-pre-tests initial-code rule sub-instructions)
  {:name             name
   :run-pre-tests?   run-pre-tests
   :initial-code     initial-code
   :rule             rule
   :sub-instructions (vec sub-instructions)})

(defn run-pre-tests?
  [x]
  (assert/run-pre-tests x)
  x)

(defmacro initial-code
  [form]
  (if (= form :none)
    nil
    `(dissoc (code ~form) :code :lang)))

(defn rule
  [& {:keys [only-use-one-fn? required-fns restricted-fns no-rule?]
      :or   {only-use-one-fn? false}
      :as   m}]
  (if no-rule?
    nil
    (let [rule-map (when m {:only-use-one-fn? only-use-one-fn?
                            :required-fns     required-fns
                            :restricted-fns   restricted-fns})]
      (assert/rule rule-map)
      rule-map)))

(defn sub-instruction
  [name text testing]
  (assert/sub-instruction name text testing)
  {:name             name
   :instruction-text (:texts text)
   :testing          testing})

(defn text
  [& args]
  (assert/text args)
  {:texts (vec args)})

(defn p
  [& args]
  (assert/p args)
  (let [text-maps (reduce #(if (map? %2)
                             (conj %1 %2)
                             (conj %1 {:normal-text true
                                       :text        %2})) [] args)]
    {:p     true
     :texts text-maps}))

(defn hi
  [text]
  (assert/check-text text)
  {:hi   true
   :text text})

(defn italic
  [text]
  (assert/check-text text)
  {:italic true
   :text   text})

(defn bold
  [text]
  (assert/check-text text)
  {:bold true
   :text text})

(defn link
  ([src]
   (link nil src))
  ([title src]
   (assert/link title src)
   {:link  true
    :src   src
    :title title}))

(defmacro code
  ([form]
   `(code "clojure" ~form))
  ([lang form]
   `(do
      ~(assert/code lang form)
      {:code true
       :form ~(if (= (first form) (symbol "str"))
                form
                (str form))
       :lang ~lang})))

(defn testing
  [& is]
  (assert/testing is)
  {:is (vec is)})

(defmacro is
  ([form]
   `(is ~form nil :simple))
  ([form err-msg]
   `(is ~form ~err-msg :simple))
  ([form err-msg err-msg-type]
   (let [err-msg* (if (= :default err-msg) nil err-msg)]
     `(do
        ~(assert/is form err-msg* err-msg-type)
        {:form               ~(str form)
         :error-message      ~err-msg*
         :error-message-type ~err-msg-type}))))