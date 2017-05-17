(ns rop
  "Railway Oriented Programming (ROP) in Clojure
  It provides a toolset of higher-order-functions to maintain a design-by-error philosophy.
  When used as a fundamental api concept it can help to build strong solid software-modules.
  
  Original Idea in F# by Scott Wlaschin.
  See corresponding test-file for more documentation."
  (:require [clojure.core.match :refer [match]])
  (:refer-clojure :exclude [map]))

(defrecord Success [^Object success])
(defrecord Failure [^Object failure])

(defn succeed
  "Success-Constructor"
  [value]
  (Success. value))

(defn fail
  "Failure-Constructor"
  [value]
  (Failure. value))

(defn switch
  "An adapter that takes a normal one-track function and turns it into a switch function.
  (Also known as a lift in some contexts.)"
  [fun]
  (fn [& params]
    (succeed (apply fun params))))

(defn bind
  "An adapter that takes a switch function and creates a new function that accepts two-track values as input."
  [switch-function]
  (fn [result]
    (match [result]
      [{:success s}] (switch-function s)
      [{:failure f}] (fail f)
      :else (fail "Input was no Result!"))))

(defn >>=
  "Takes a Result and pipes it through one or multiple switch-functions."
  [result & switch-functions]
  (reduce #((bind %2) %1)
          result
          switch-functions))

(defn >=>
  "Switch composition. A combiner that takes two switch functions and creates a new switch function by connecting them in series."
  [sw1 sw2]
  (comp (bind sw2) sw1))

(defn tee
  "An adapter that takes a dead-end function and turns it into a one-track function that can be used in a data flow.
  (Also known as tap.)"
  [fun result]
  (fun result)
  (result))

(defn try-catch
  "An adapter that takes a normal one-track function and turns it into a switch function, but also catches exceptions."
  [fun result]
  (try
    (succeed (fun result))
    (catch Exception e (fail (.getMessage e)))))

(defn map-parallel
  "An adapter that takes two one-track functions and turns them into a single two-track function. (Also known as bimap.)"
  [success-function failure-function]
  (fn [result]
    (match [result]
      [{:success s}] (success-function s)
      [{:failure f}] (failure-function f)
      :else (fail "Input was no Result!"))))

(defn map
  "An adapter that takes a normal one-track function and turns it into a two-track function. (Also known as a 'lift' in some contexts.)"
  [fun]
  (map-parallel fun identity))

(defn plus
  "A combiner that takes two switch functions and creates a new switch function by joining them in 'parallel' and 'adding' the results. 
  (Also known as ++ and <+> in other contexts.)"
  [add-success add-failure switch1 switch2 result]
  (match [(switch1 result) (switch2 result)]
    [{:success s1} {:success s2}] (succeed (add-success s1 s2))
    [{:failure f1} {:success _}] (fail f1)
    [{:success _} {:failure f2}] (fail f2)
    [{:failure f1} {:failure f2}] (fail (add-failure f1 f2))
    :else (fail "Input was no Result!")))
