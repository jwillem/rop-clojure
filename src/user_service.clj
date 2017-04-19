(ns user-service
  (:require [rop :refer [>>= bind]])
  (:import [rop Success Failure]))

(defrecord Request [^String name
                    ^String email])

(defn validate-name-not-blank [input]
  (cond
    (clojure.string/blank? (:name input)) (Failure. "Name must not be blank")
    :else (Success. input)))

(defn validate-name-length [input]
  (cond
    (> 50 (count (:name input)))  (Failure. "Name must not be longer than 50 chars")
    :else (Success. input)))

(defn validate-email-not-blank [input]
  (cond
    (clojure.string/blank? (:email input)) (Failure. "Email must not be blank")
    :else (Success. input)))

(defn validate-input
  "This version is function-oriented"
  [input]
  (-> validate-email-not-blank
      (bind validate-name-not-blank)
      (bind validate-name-length)))

(defn validate-input'
  "This version is data-oriented"
  [input]
  (-> input
      validate-email-not-blank
      (>>= validate-name-not-blank
           validate-name-length)))
