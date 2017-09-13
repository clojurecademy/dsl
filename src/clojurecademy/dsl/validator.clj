(ns clojurecademy.dsl.validator
  (:require [clojurecademy.dsl.assert :as assert]
            [clojurecademy.dsl.util :as util]
            [kezban.core :refer :all]))

(defn- get-map-route
  [child parent]
  (format "[%s]" (->> (conj parent child)
                      (map :name)
                      (interpose " -> ")
                      (apply str))))

(defn- validate-course
  [course]
  (try
    (assert/manifest (:manifest course))
    (assert/course (:chapters course))
    (catch Exception e
      (util/arg-error (str "Course: " (util/err-msg e))))))

(defn- validate-chapter
  [chapter]
  (try
    (assert/chapter (:name chapter) (:title chapter) (:sub-chapters chapter))
    (catch Exception e
      (util/arg-error (str "Chapter map route: " (get-map-route chapter []) " " (util/err-msg e))))))

(defn- validate-sub-chapter
  [sub-chapter map-route]
  (try
    (assert/sub-chapter (:name sub-chapter) (:title sub-chapter) (:subjects sub-chapter))
    (catch Exception e
      (util/arg-error (str "Sub-chapter map route: " (get-map-route sub-chapter map-route) " " (util/err-msg e))))))

(defn- validate-subject
  [subject map-route]
  (try
    (assert/subject (:name subject) (:title subject) {:learn (:learn subject)} (:instruction subject) (:ns subject))
    (assert/learn (:learn subject))
    (assert/check-text-styles (:text (:learn subject)))
    (catch Exception e
      (util/arg-error (str "Subject map route: " (get-map-route subject map-route) " " (util/err-msg e))))))

(defn- validate-instruction
  [instruction map-route]
  (try
    (assert/instruction (:name instruction)
                        (:run-pre-tests? instruction)
                        (:initial-code instruction)
                        (:rule instruction)
                        (:sub-instructions instruction))
    (assert/rule (:rule instruction))
    (catch Exception e
      (util/arg-error (str "Instruction map route: " (get-map-route instruction map-route) " " (util/err-msg e))))))

(defn- validate-sub-instruction
  [sub-instruction map-route]
  (try
    (assert/sub-instruction (:name sub-instruction) {:texts (:instruction-text sub-instruction)} (:testing sub-instruction))
    (assert/check-text-styles (:instruction-text sub-instruction))
    (assert/testing (-> sub-instruction :testing :is))
    (assert/check-is (-> sub-instruction :testing :is))
    (catch Exception e
      (util/arg-error (str "Sub-instruction map route: " (get-map-route sub-instruction map-route) " " (util/err-msg e))))))

(defn validate
  [course]
  (validate-course course)
  (doseq [chapter (:chapters course)]
    (validate-chapter chapter)
    (doseq [sub-chapter (:sub-chapters chapter)]
      (validate-sub-chapter sub-chapter [chapter])
      (doseq [subject (:subjects sub-chapter)]
        (validate-subject subject [chapter sub-chapter])
        (when-let [instruction (:instruction subject)]
          (validate-instruction instruction [chapter sub-chapter subject])
          (doseq [sub-instruction (:sub-instructions instruction)]
            (validate-sub-instruction sub-instruction [chapter sub-chapter subject instruction])))))))

(defn test?
  [test-var]
  (let [test-map (deref test-var)]
    (and (not (error?
                (eval (assert/defcoursetest (:route-vec test-map) (:body test-map)))))
         (true? (-> test-var
                    meta
                    :course-test-var)))))