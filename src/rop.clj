(ns rop
  "Railway Oriented Programming (ROP) in Clojure
  It provides a toolset of higher-order-functions to maintain a design-by-error philosophy.
  When used as a fundamental api concept it can help to build strong solid software-modules."
  (:require [clojure.core.match :refer [match]]))

(defrecord Success [^Object success])
(defrecord Failure [^Object failure])
(defrecord Result [^Success success
                   ^Failure failure])

(defn bind
  "Takes a one-track function and transforms it to a two-track function."
  [switch-function]
  (fn [^Result result]
    (match [result]
      [{:success s}] (switch-function s)
      [{:failure f}] (Failure. f))))

(defn =>=
  "Takes a two track-function and bind-pipes it to a further two-track-function."
  [result switch-function]
  ;; TODO use apply, use rest-argument to catch all terms
  (bind switch-function result))

(defn >=>
  "Takes two switch functions and composes them together."
  [switch1 switch2]
  (-> switch1
      (bind switch2)))

  ;; (def result1 (switch1 x))
;;   (cond
 ;; TODO match should create a let bind -- we would like to unwrap the value in the success-type 
;;     (:success result) (switch2 s)
;;     (:failure result) (Failure. f)))
