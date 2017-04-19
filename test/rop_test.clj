(ns rop-test
  (:require
   [clojure.test :refer [deftest is]]
   rop)
  (:import [rop Success Failure Result]))

(def msg-yes "yes is positive")
(def yes (Success. msg-yes))
(def succeeding-switch #(Success. %))

(def msg-no "no is negative")
(def no (Failure. msg-no))
(def msg-failure "Failure with previous Value of: ")
(def failing-switch #(Failure. (str msg-failure %)))

;; TODO is this verification needed?
;; (deftest success-is-result
;;   (is (instance? Result yes)))
;;
;; (deftest failure-is-result
;;   (is (instance? Result no)))

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
  ;; bind gets Failure and returns its value as a Failure
  (is (= (Failure. msg-no)
         ((rop/bind failing-switch) no)))

  ;; bind gets Success and evaluates failing-switch which returns an Failure
  (is (= (Failure. (str msg-failure msg-yes))
         ((rop/bind failing-switch) yes))))

(deftest bind-with-map-as-success-value)

(deftest bind-with-map-as-failure-value)

;; TODO resturcture these cases
(deftest bind-returns-success-on-success
  ;; bind gets Success and evalutes succeeding-switch which returns its value as a Success
  (is (= (Success. msg-yes)
         ((rop/bind succeeding-switch) yes)))

  ;; bind gets Failure and returns its value as a Failure
  (is (= (Failure. msg-no)
         ((rop/bind succeeding-switch) no))))

(deftest >>=-with-one-switch
  (is (= (Failure. (str msg-failure msg-yes))
         (rop/>>= yes
                  failing-switch)))
  (is (= (Success. msg-yes)
         (rop/>>= yes
                  succeeding-switch)))
  (is (= (Failure. msg-no)
         (rop/>>= no
                  failing-switch)))
  (is (= (Failure. msg-no)
         (rop/>>= no
                  succeeding-switch))))

(deftest >>=-with=multiple-switches)
