(ns user-service
  (:require rop)
  (:import [rop Success Failure]))

(defrecord Request [name email])

(defn validate-input [input]
  (cond
    (empty (:name input)) (Failure. "Name must not be blank")
    (empty (:email input)) (Failure. "Email must not be blank")
    :else (Success. input)))
