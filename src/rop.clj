(ns rop
  "Railway Oriented Programming (ROP) in Clojure
  It provides a toolset of higher-order-functions to maintain a design-by-error philosophy.
  When used as a fundamental api concept it can help to build strong solid software-modules.
  
  Original Idea in F# by Scott Wlaschin.
  See corresponding test-file for more documentation."
  (:require [clojure.core.match :refer [match]]))

(defrecord Success [^Object success])
(defrecord Failure [^Object failure])
;; (defrecord Result [^Success success
;;                    ^Failure failure])

(defn bind
  "Takes a one-track switch-function and connects a Failure-Path to its Result.
  So it Fits in a Two-track-chain."
  [switch-function]
  (fn [result]
    (match [result]
      [{:success s}] (switch-function s)
      [{:failure f}] (Failure. f)
      :else (Failure. "Input was no Result!"))))

(defn >>=
  "Takes a Result and pipes it through one or multiple switch-functions.
  It will fit in a Two-track-chain.
  "
  [result switch-function]
  ;; TODO use apply, use rest-argument to catch all terms
  ((bind switch-function) result))

(defn >=>
  "Takes one or multiple switch-functions and composes them together.
  It wont fit in a two-track-chain yet."
  [switch1 switch2]
  ;; TODO use apply, use rest-argument to catch all terms
  ;; TODO define in terms of bind and comp
  (fn [result]
    (match [(switch1 result)]
      [{:success s}] (switch2 s)
      [{:failure f}] (Failure. f)
      :else (Failure. "Input was no Result!"))))
