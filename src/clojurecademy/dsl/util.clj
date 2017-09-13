(ns clojurecademy.dsl.util
  (:require [clojure.string :as str]
            [kezban.core :refer :all]))


(defn url?
  [url]
  (or (str/blank? url)
      (re-matches #"^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" url)))

(defn email?
  [email]
  (and (not (str/blank? email))
       (re-matches #"(([^<>()\[\]\\.,;:\s@\"]+(\.[^<>()\[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))" email)))

(defn- set->vec-set
  [keys]
  (if (sequential? keys)
    keys
    (vector keys)))

(defn- map->vec-map
  [maps]
  (if (sequential? maps)
    maps
    (vector maps)))

(defn valid-keys?
  [keys-as-set maps]
  (let [vec-map (map->vec-map maps)
        vec-set (set->vec-set keys-as-set)]
    (if (empty? vec-map)
      false
      (every? #(any? (fn [keys-of-set]
                       (= (set (keys %)) keys-of-set)) vec-set) vec-map))))

(defn wrap-code
  [code]
  (str "(\n" code "\n)"))

(defn ns?
  [ns]
  (if (symbol? ns)
    (let [ns-as-str (str ns)
          regex     #"[a-zA-Z]+(?!.*--)[a-zA-Z0-9\-]+[a-zA-Z0-9]+"
          tokens    (str/split ns-as-str #"\.")]
      (every? #(re-matches regex %) tokens))
    false))

(defn update-val
  [m k val]
  (update m k (constantly val)))

(defn err-msg
  [e]
  (.getMessage e))

(defn arg-error
  ([^String message]
   (throw (IllegalArgumentException. message)))
  ([^String message ^Exception e]
   (throw (IllegalArgumentException. message e))))

(defmacro nested-update-val
  [coll key val]
  (when (seq coll)
    (if (= 1 (count coll))
      `(update-val ~(first coll) ~key ~val)
      `(update-val ~(first coll) ~(second coll) [(nested-update-val ~(drop 2 coll) ~key ~val)]))))