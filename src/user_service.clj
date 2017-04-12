(ns user-service
  (:require rop)
  (:import [rop Success Failure]))

(defrecord Request [name email])

(defn validate-input [input]
  (cond
    (clojure.string/blank? (:name input)) (Failure. "Name must not be blank")
    (clojure.string/blank? (:email input)) (Failure. "Email must not be blank")
    :else (Success. input)))
