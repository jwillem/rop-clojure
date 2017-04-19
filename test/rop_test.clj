(ns rop-test
  (:require
   [clojure.test :refer [deftest is]]
   rop)
  (:import [rop Success Failure Result]))

;; (deftest success-is-result
;;   (is (instance? Result yes)))
;;
;; (deftest failure-is-result
;;   (is (instance? Result no)))

(def msg-yes "yes is positive")
(def yes (Success. msg-yes))

(def msg-no "no is negative")
(def no (Failure. msg-no))

(deftest success-is-success
  (is (true? (instance? Success yes)))
  (is (= msg-yes
         (:success yes)))
  (is (nil? (:failure yes))))

(deftest failure-is-failure
  (is (true? (instance? Failure no)))
  (is (= msg-no
         (:failure no)))
  (is (nil? (:success no))))

;; TODO resturcture these cases

(deftest bind-returns-failure-on-failure
  (def msg-failure "Failure with previous Value of: ")
  (def failing-switch (rop/bind #(Failure. (str msg-failure %))))

  ;; bind gets Failure and returns its value as a Failure
  (is (= (Failure. msg-no)
         (failing-switch no)))

  ;; bind gets Success and evaluates failing-switch which returns an Failure
  (is (= (Failure. (str msg-failure msg-yes))
         (failing-switch yes))))

(deftest bind-returns-success-on-success
  (def succeeding-switch (rop/bind #(Success. %)))

  ;; bind gets Success and evalutes succeeding-switch which returns its value as a Success
  (is (= (Success. msg-yes)
         (succeeding-switch yes)))

  ;; bind gets Failure and returns its value as a Failure
  (is (= (Failure. msg-no)
         (succeeding-switch no))))
