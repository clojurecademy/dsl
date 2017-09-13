(ns clojurecademy.dsl.assert
  (:require [clojurecademy.dsl.util :as util]
            [clojure.string :as str]
            [kezban.core :as kez]))

(def valid-keys-chapter #{:name :title :sub-chapters})
(def valid-keys-sub-chapter #{:name :title :subjects})
(def valid-keys-subject #{:name :title :learn :instruction :ns})
(def valid-keys-learn #{:learn})
(def valid-keys-instruction #{:name :run-pre-tests? :initial-code :rule :sub-instructions})
(def valid-keys-text #{:texts})
(def valid-keys-testing #{:is})
(def valid-keys-sub-instruction #{:name :instruction-text :testing})
(def valid-keys-initial-code #{:form})
(def valid-keys-rule #{:only-use-one-fn? :required-fns :restricted-fns})
(def valid-keys-p-text-style [#{:hi :text} #{:italic :text} #{:bold :text} #{:link :src :title} #{:normal-text :text}])
(def valid-keys-text-style [#{:p :texts} #{:code :form :lang}])
(def valid-keys-is #{:form :error-message :error-message-type})

(def who-is-this-course-for-keys #{:clojure-experience :programming-experience :no-programming-experience})

(def title-max-char-size 80)
(def short-description-max-char-size 125)
(def long-description-max-char-size 600)

(def supported-langs #{"clojure"
                       "ruby"
                       "clike"
                       "haskell"
                       "javascript"
                       "python"
                       "scheme"
                       "commonlisp"
                       "erlang"})

(declare check-text link is)


(defn lisp?
  [form]
  (if (string? form)
    (try
      (read-string (str "(\n" form "\n)"))
      true
      (catch Exception _
        false))
    false))

(defn unique-lisp?
  [form]
  (if (string? form)
    (try
      (list? (read-string form))
      (catch Exception _
        false))
    false))

(defn- check-name
  [name]
  (cond
    (nil? name)
    (util/arg-error "Name can not be nil.")

    (not (symbol? name))
    (util/arg-error "Name has to be symbol.")))

(defn- check-title
  [title]
  (cond
    (nil? title)
    (util/arg-error "Title can not be nil.")

    (not (string? title))
    (util/arg-error "Title has to be string.")

    (str/blank? title)
    (util/arg-error "Title can not be blank.")

    (> (count title) title-max-char-size)
    (util/arg-error (str "Title can not be longer than " title-max-char-size " characters long."))))

(defn disp-keys
  [valid-keys]
  (str "It has to have only this key(s): " valid-keys))

(defn- check-chapter-keys
  [chapters]
  (cond
    (nil? chapters)
    (util/arg-error "You have to define one chapter at least.")

    (not (util/valid-keys? valid-keys-chapter chapters))
    (util/arg-error (str "Chapters have to be proper map format." (disp-keys valid-keys-chapter)))))

(defn- check-sub-chapter-keys
  [sub-chapters]
  (cond
    (nil? sub-chapters)
    (util/arg-error "You have to define one sub-chapter at least.")

    (not (util/valid-keys? valid-keys-sub-chapter sub-chapters))
    (util/arg-error (str "Sub-chapters have to be proper map format." (disp-keys valid-keys-sub-chapter)))))

(defn- check-subject-keys
  [subjects]
  (cond
    (nil? subjects)
    (util/arg-error "You have to define one subject at least.")

    (not (util/valid-keys? valid-keys-subject subjects))
    (util/arg-error (str "Subjects have to be proper map format." (disp-keys valid-keys-sub-chapter)))))

(defn- check-learn-keys
  [learn]
  (cond
    (nil? learn)
    (util/arg-error "You have to provide learn text for subject.")

    (not (util/valid-keys? valid-keys-learn learn))
    (util/arg-error (str "Learn has to be proper map format." (disp-keys valid-keys-learn)))))

(defn- check-instruction-keys
  [instruction]
  (cond
    (and instruction (not (util/valid-keys? valid-keys-instruction instruction)))
    (util/arg-error (str "Instruction has to be proper map format." (disp-keys valid-keys-instruction)))))

(defn- check-ns
  [ns]
  (cond
    (and ns (not (util/ns? ns)))
    (util/arg-error "Ns has to be in proper ns format.")))

(defn check-ns-existence
  [ns instruction]
  (when (and (not ns) instruction)
    (let [form (some-> instruction :initial-code :form util/wrap-code read-string)]
      (if (= (ffirst form) 'ns)
        (when-not (util/ns? (second (first form)))
          (util/arg-error "Ns has to be in proper ns format."))
        (util/arg-error "You did not define ns.You need to define ns by using subject fn or :initial-code in instruction.")))))

(defn- check-text-keys
  [text]
  (cond
    (nil? text)
    (util/arg-error "You have to provide text")

    (not (util/valid-keys? valid-keys-text text))
    (util/arg-error (str "Learn's text has to be proper map format." (disp-keys valid-keys-text)))))

(defn- check-sub-instruction-keys
  [sub-instructions]
  (cond
    (nil? sub-instructions)
    (util/arg-error "You have to provide sub-instructions")

    (not (util/valid-keys? valid-keys-sub-instruction sub-instructions))
    (util/arg-error (str "Sub-instruction has to be proper map format." (disp-keys valid-keys-sub-instruction)))))

(defn- check-either-names-nil-or-not-nil
  [sub-instructions]
  (cond
    (not (or (every? :name sub-instructions) (every? #(nil? (:name %)) sub-instructions)))
    (util/arg-error "Sub instructions all names have to be either nil or non-nil.")))

(defn- check-initial-code-keys
  [initial-code]
  (cond
    (and initial-code (not (util/valid-keys? valid-keys-initial-code initial-code)))
    (util/arg-error (str "Initial code has to be proper map format." (disp-keys valid-keys-initial-code)))))

(defn- check-rule-keys
  [rule]
  (cond
    (and rule (not (util/valid-keys? valid-keys-rule rule)))
    (util/arg-error (str "Rule has to be proper map format." (disp-keys valid-keys-rule)))))

(defn- check-hi
  [hi]
  (when-not (true? (:hi hi))
    (util/arg-error (str "Text style hi has to be proper format.")))
  (try
    (check-text (:text hi))
    (catch Exception e
      (util/arg-error (str "Text style hi has to be proper format." (util/err-msg e))))))

(defn- check-italic
  [italic]
  (when-not (true? (:italic italic))
    (util/arg-error (str "Text style italic has to be proper format.")))
  (try
    (check-text (:text italic))
    (catch Exception e
      (util/arg-error (str "Text style italic has to be proper format." (util/err-msg e))))))

(defn- check-bold
  [bold]
  (when-not (true? (:bold bold))
    (util/arg-error (str "Text style bold has to be proper format.")))
  (try
    (check-text (:text bold))
    (catch Exception e
      (util/arg-error (str "Text style bold has to be proper format." (util/err-msg e))))))

(defn- check-link
  [link*]
  (when-not (true? (:link link*))
    (util/arg-error (str "Text style link has to be proper format.")))
  (try
    (link (:title link*) (:src link*))
    (catch Exception e
      (util/arg-error (str "Text style link has to be proper format." (util/err-msg e))))))

(defn- code-form?
  [form]
  (and (string? form)
       (not (str/blank? form))
       (lisp? form)))

(defn check-code
  [code]
  (when (or (not (true? (:code code)))
            (not (code-form? (:form code)))
            (not (string? (:lang code))))
    (util/arg-error (str "Text style code has to be proper format."))))

(defn check-normal-text
  [normal-text]
  (when (or (not (true? (:normal-text normal-text))) (not (string? (:text normal-text))))
    (util/arg-error (str "Text style normal-text has to be proper format."))))

;;TODO refactor here!!
(defn- check-style
  [text-style]
  (cond
    (:code text-style)
    (check-code text-style)

    (:p text-style)
    (loop [texts (:texts text-style)]
      (when-let [text-style* (some-> texts seq first)]
        (recur (do
                 (cond
                   (:hi text-style*)
                   (check-hi text-style*)

                   (:italic text-style*)
                   (check-italic text-style*)

                   (:bold text-style*)
                   (check-bold text-style*)

                   (:link text-style*)
                   (check-link text-style*)

                   (:normal-text text-style*)
                   (check-normal-text text-style*)

                   (:code text-style*)
                   (check-code text-style*)

                   :else
                   (util/arg-error (str "The type of text-styles couldn't be found!")))
                 (rest texts)))))
    :else
    (util/arg-error (str "The type of text-styles couldn't be found!"))))

(defn check-text-styles
  [text-styles]
  (loop [styles text-styles]
    (when-let [style (some-> styles seq first)]
      (recur (do
               (check-style style)
               (rest styles))))))

(defn- check-description*
  [description max-char-size]
  (cond
    (nil? description)
    (util/arg-error "Description can not be nil.")

    (not (string? description))
    (util/arg-error "Description has to be string.")

    (str/blank? description)
    (util/arg-error "Description can not be blank.")

    (> (count description) max-char-size)
    (util/arg-error (str "Description can not be longer than " max-char-size " characters long."))))

(defn- check-names-not-same
  [m dsl]
  (let [name-map (->> m (map :name) (filter some?))]
    (when-not (= (count (distinct name-map)) (count name-map))
      (util/arg-error (str "Names of " dsl " can not be the same.")))))

(defn check-is
  [forms]
  (try
    (doseq [form forms]
      (is (:form form) (:error-message form) (:error-message-type form)))
    (catch Exception e
      (util/arg-error "Is has to be proper map format" e))))

(defn- check-initial-code
  [initial-code]
  (when (and initial-code (not (code-form? (:form initial-code))))
    (util/arg-error (str "Initial code has to be proper format."))))

(defn check-short-description
  [short-desc]
  (check-description* short-desc short-description-max-char-size))

(defn check-long-description
  [long-desc]
  (check-description* long-desc long-description-max-char-size))

(defn check-skip?
  [skip?]
  (cond
    (and skip? (not (or (true? skip?) (false? skip?))))
    (util/arg-error "skip? should be either true or false.")))

(defn check-report-bug-email-or-link
  [report-bug-email-or-link]
  (when-not (or (util/url? report-bug-email-or-link)
                (util/email? report-bug-email-or-link))
    (util/arg-error "report-bug-email-or-link should be either url or email.")))

(defn check-who-is-this-course-for
  [who-is-this-course-for]
  (cond
    (and (string? who-is-this-course-for) (str/blank? who-is-this-course-for))
    (util/arg-error "Who is this course for? can not be blank")

    (and (not (string? who-is-this-course-for)) (not (who-is-this-course-for-keys who-is-this-course-for)))
    (util/arg-error (str "You need to select one of those keys: " who-is-this-course-for-keys))))

(defn manifest
  [m]
  (check-title (:title m))
  (check-name (:name m))
  (check-short-description (:short-description m))
  (check-long-description (:long-description m))
  (check-skip? (:skip? m))
  (check-report-bug-email-or-link (:report-bug-email-or-link m))
  (check-who-is-this-course-for (:who-is-this-course-for m)))

(defn course
  [chapters]
  (check-chapter-keys chapters)
  (check-names-not-same chapters "chapters"))

(defn chapter
  [name title sub-chapters]
  (check-name name)
  (check-title title)
  (check-sub-chapter-keys sub-chapters)
  (check-names-not-same sub-chapters "sub-chapters"))

(defn sub-chapter
  [name title subjects]
  (check-name name)
  (check-title title)
  (check-subject-keys subjects)
  (check-names-not-same subjects "subjects"))

(defn subject
  [name title learn instruction ns]
  (check-name name)
  (check-title title)
  (check-learn-keys learn)
  (check-instruction-keys instruction)
  (check-ns ns)
  (check-ns-existence ns instruction))

(defn learn
  [text]
  (check-text-keys text))

(defn run-pre-tests
  [x]
  (cond
    (not (or (true? x) (false? x)))
    (util/arg-error "It should be either true or false."))
  x)

(defn- check-run-pre-tests-for-named-sub-ins
  [run-pre-tests? sub-instructions]
  (when (and run-pre-tests? (not (every? #(some? (:name %)) sub-instructions)))
    (util/arg-error "You can use run-pre-test only for named sub instructions.")))

(defn instruction
  [name run-pre-tests? initial-code rule sub-instructions]
  (check-name name)
  (run-pre-tests run-pre-tests?)
  (check-run-pre-tests-for-named-sub-ins run-pre-tests? sub-instructions)
  (check-initial-code-keys initial-code)
  (check-initial-code initial-code)
  (check-rule-keys rule)
  (check-sub-instruction-keys sub-instructions)
  (check-either-names-nil-or-not-nil sub-instructions)
  (check-names-not-same sub-instructions "sub-instructions"))

(defn rule
  [m]
  (when m
    (cond
      (not ((kez/any-pred true? false?) (:only-use-one-fn? m)))
      (util/arg-error "Only-use-one-fn? should be either true or false.")

      (and (seq (:required-fns m)) (not (vector? (:required-fns m))))
      (util/arg-error "Required-fns has to be vector.")

      (and (seq (:required-fns m)) (not (every? symbol? (:required-fns m))))
      (util/arg-error "Every required-fn has to be symbol.")

      (and (seq (:restricted-fns m)) (not (vector? (:restricted-fns m))))
      (util/arg-error "Restricted-fns has to be vector.")

      (and (seq (:restricted-fns m)) (not (every? symbol? (:restricted-fns m))))
      (util/arg-error "Every restricted-fn has to be symbol.")

      (and (seq (:required-fns m)) (not (:only-use-one-fn? m)))
      (util/arg-error ":required-fns could be used when :only-use-one-fn? true."))))

(defn sub-instruction
  [name text testing]
  (check-name name)
  (cond
    (nil? text)
    (util/arg-error "Text can not be nil.")

    (nil? testing)
    (util/arg-error "Testing can not be nil.")

    (not (util/valid-keys? valid-keys-text text))
    (util/arg-error (str "Text has to be proper map format." (disp-keys valid-keys-text)))

    (not (util/valid-keys? valid-keys-testing testing))
    (util/arg-error (str "Testing has to be proper map format." (disp-keys valid-keys-testing)))))

(defn text
  [text-styles]
  (cond
    (or (nil? text-styles) (empty? text-styles))
    (util/arg-error "You have to define one text at least one.")

    (not (every? map? text-styles))
    (util/arg-error "Text can take maps as parameters")

    (not (util/valid-keys? valid-keys-text-style text-styles))
    (util/arg-error (str "Text styles have to be proper map format." (disp-keys valid-keys-text-style)))))

(defn p
  [text-styles]
  (cond
    (or (nil? text-styles) (empty? text-styles))
    (util/arg-error "You have to define one text at least one.")

    (not (every? #(or (string? %) (map? %)) text-styles))
    (util/arg-error "Text can take either strings or maps as parameters")

    (and (not (every? string? text-styles)) (not (util/valid-keys? valid-keys-p-text-style (filter map? text-styles))))
    (util/arg-error (str "Text styles have to be proper map format." (disp-keys valid-keys-p-text-style)))))

(defn check-text
  [text]
  (cond
    (nil? text)
    (util/arg-error "Text can not be nil.")

    (not (string? text))
    (util/arg-error "Text has to be string.")

    (str/blank? text)
    (util/arg-error "Text can not be blank.")))

(defn link
  [title src]
  (cond
    (not (string? src))
    (util/arg-error "Source has to be string")

    (not (util/url? src))
    (util/arg-error "Source has to be proper URL"))
  (when title
    (check-title title)))

(defn code
  [lang form]
  `(do
     (cond
       ~(nil? form)
       (util/arg-error "You have to define code form at least one.")

       ~(not (string? lang))
       (util/arg-error "Lang has to be string.")

       ~(not (contains? supported-langs lang))
       (util/arg-error (str "This language is currently not supported.Supported languages: " supported-langs))

       ~(and (= "clojure" lang) (not (or (string? form) (list? form))))
       (util/arg-error "Form has be string or list.")

       ~(and (= "clojure" lang) (not (if (string? form) (lisp? form) true)))
       (util/arg-error "Form has to be proper lisp form."))))

(defn testing
  [is]
  (cond
    (nil? is)
    (util/arg-error "You have to define is at least one.")

    (not (util/valid-keys? valid-keys-is is))
    (util/arg-error (str "Is forms have to be proper map format." (disp-keys valid-keys-is)))))

(defn is
  [form err-msg err-msg-type]
  `(cond
     ~(or (nil? form) (not (list? form)) (not (unique-lisp? (str form))))
     (util/arg-error "Form has to be list, in other words your test code.")

     (and ~err-msg (not (string? ~err-msg)))
     (util/arg-error "Message has to be string.")

     (and ~err-msg (str/blank? ~err-msg))
     (util/arg-error "Message can not be blank.")

     (and ~err-msg-type (nil? (#{:none :simple :advanced} ~err-msg-type)))
     (util/arg-error (str "Error message type can not be " ~err-msg-type))

     (and ~err-msg-type (str/blank? ~err-msg) (= :none ~err-msg-type))
     (util/arg-error (str "You have to provide an error message when you define error message type to :none"))))

(defn defcoursetest
  [route-vec body]
  `(cond
     (not (vector? '~route-vec))
     (util/arg-error "Route vector has to be vector.")

     (not (every? symbol? '~route-vec))
     (util/arg-error "Every route has to be symbol.")

     (not (= (count '~route-vec) 5))
     (util/arg-error "Route vector has to have 5 routes.")

     (not '~body)
     (util/arg-error "Test body can not be empty.")))