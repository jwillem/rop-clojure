(ns rop-test
  (:require
   [clojure.test :refer [deftest is]]
   rop)
  (:import [rop Success Failure Result]))

(def yes (Success. "yes is positve"))
(def no (Failure. "no is negative"))

(deftest create-success
  (is (instance? Success yes)))

(deftest create-failure
  (is (instance? Failure no)))

;; (deftest success-is-result
;;   (is (instance? Result yes)))
;;
;; (deftest failure-is-result
;;   (is (instance? Result no)))

(deftest success-is-success
  (is (-> yes
          (:success)))
  (is (-> yes
          (:failure)
          (complement))))

(deftest failure-is-failure
  (is (-> no
          (:failure)))
  (is (-> no
          (:success)
          (complement))))

;; (deftest )

;; (deftest bind-returns-failure-on-failure
;;   (is (-> no
;;           (bind #(no))
;;           )))
