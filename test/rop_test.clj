(ns rop-test
  (:require
   [clojure.test :refer [deftest is]]
   rop)
  (:import [rop Success Failure]))

(def yes (Success. "yes"))
(def no (Failure. "no"))

(deftest create-success
  (is (instance? Success yes)))

(deftest create-failure
  (is (instance? Failure no)))

(deftest success-is-success
  (is (rop/success? yes))
  (is (not (rop/failure? yes))))

(deftest failure-is-no-success
  (is (rop/failure? no))
  (is (not (rop/success? no))))
