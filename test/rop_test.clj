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

(deftest >>=-with-multiple-switches)

(deftest >=>-with-two-switches
  (def combined-failing (rop/>=> failing-switch
                                 failing-switch))
  (def combined-succeeding (rop/>=> succeeding-switch
                                    succeeding-switch))
  (def succeeding-failing (rop/>=> succeeding-switch
                                   failing-switch))
  (def failing-succeeding (rop/>=> failing-switch
                                   succeeding-switch))

  (is (= (Failure. (str msg-failure msg-yes))
         (rop/>>= yes
                  combined-failing)))
  (is (= (Failure. (str msg-failure msg-yes))
         (rop/>>= yes
                  succeeding-failing)))
  (is (= (Failure. (str msg-failure msg-yes))
         (rop/>>= yes
                  failing-succeeding)))
  (is (= (Success. msg-yes)
         (rop/>>= yes
                  combined-succeeding)))
  (is (= (Failure. msg-no)
         (rop/>>= no
                  combined-failing)))
  (is (= (Failure. msg-no)
         (rop/>>= no
                  succeeding-failing)))
  (is (= (Failure. msg-no)
         (rop/>>= no
                  failing-succeeding)))
  (is (= (Failure. msg-no)
         (rop/>>= no
                  combined-succeeding))))

(deftest >=>-with-multiple-switches
  ;; (def multiple-succeeding (rop/>=> succeeding-switch
  ;;                                   succeeding-switch
  ;;                                   succeeding-switch))
  ;; (def multiple-failing (rop/>=> failing-switch
  ;;                                failing-switch
  ;;                                failing-switch))
  ;; (def succeeding-failing-succeeding (rop/>=> succeeding-switch
  ;;                                             failing-switch
  ;;                                             succeeding-switch))
  ;; (def failing-succeeding-failing (rop/>=> failing-switch
  ;;                                          succeeding-switch
  ;;                                          failing-switch))
  )
