(ns rop
  "Railway Oriented Programming (ROP)")

(defprotocol Result
  (success? [this])
  (failure? [this]))

(defrecord Success [success]
  Result
  (success? [this] true)
  (failure? [this] false))

(defrecord Failure [failure]
  Result
  (success? [this] false)
  (failure? [this] true))

(defn bind
  "Takes a one-track function and transforms it to a two-track function.
  It does this by returning a function evaluate the provided switch-function if the Result of its two-track-input contains a Success-type."
  [switch-function]
  (fn [result]
    (if (success? result)
      (Success. (switch-function (:success result)))
      (Failure. (:failure result)))))
