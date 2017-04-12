(ns rop
  "Railway Oriented Programming (ROP)")

(defrecord Success [success])
(defrecord Failure [failure])
(defrecord Result [success failure])

(defn bind
  "Takes a one-track function and transforms it to a two-track function.
  It does this by returning an closure which accepts a two-track-function and evaluates the provided switch-function if the provided Result contains a Success-type.
  Otherwise it returns a Failure."
  [switch-function]
  (fn [^Result result]
    (if (:success result)
      (Success. (switch-function (:success result)))
      (Failure. (:failure result)))))
