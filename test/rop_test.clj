(ns rop-test
  (:require
   [clojure.test :refer [deftest is]]
   [rop :refer [fail succeed]]))

(def msg-yes "yes is positive")
(def yes (succeed msg-yes))
(def succeeding-switch #(succeed %))

(def msg-no "no is negative")
(def no (fail msg-no))
(def msg-failure "failwith previous Value of: ")
(def failing-switch #(fail (str msg-failure %)))

(deftest success-is-success
  (is (= msg-yes
         (:success yes)))
  (is (nil? (:failure yes))))

(deftest failure-is-failure
  (is (= msg-no
         (:failure no)))
  (is (nil? (:success no))))

;; TODO resturcture these cases
(deftest bind-returns-failure-on-failure
  ;; bind gets failand returns its value as a Failure
  (is (= (fail msg-no)
         ((rop/bind failing-switch) no)))

  ;; bind gets succeedand evaluates failing-switch which returns an Failure
  (is (= (fail (str msg-failure msg-yes))
         ((rop/bind failing-switch) yes))))

(deftest bind-with-map-as-success-value)

(deftest bind-with-map-as-failure-value)

;; TODO resturcture these cases
(deftest bind-returns-success-on-success
  ;; bind gets succeedand evalutes succeeding-switch which returns its value as a Success
  (is (= (succeed msg-yes)
         ((rop/bind succeeding-switch) yes)))

  ;; bind gets failand returns its value as a Failure
  (is (= (fail msg-no)
         ((rop/bind succeeding-switch) no))))

(deftest >>=-with-one-switch
  (is (= (fail (str msg-failure msg-yes))
         (rop/>>= yes
                  failing-switch)))
  (is (= (succeed msg-yes)
         (rop/>>= yes
                  succeeding-switch)))
  (is (= (fail msg-no)
         (rop/>>= no
                  failing-switch)))
  (is (= (fail msg-no)
         (rop/>>= no
                  succeeding-switch))))

(deftest >>=-with-multiple-switches
  (is (= (fail (str msg-failure msg-yes))
         (rop/>>= yes
                  succeeding-switch
                  succeeding-switch
                  succeeding-switch
                  failing-switch)))
  (is (= (succeed msg-yes)
         (rop/>>= yes
                  succeeding-switch
                  succeeding-switch
                  succeeding-switch)))
  (is (= (fail msg-no)
         (rop/>>= no
                  failing-switch
                  failing-switch
                  failing-switch)))
  (is (= (fail msg-no)
         (rop/>>= no
                  succeeding-switch
                  succeeding-switch))))

(deftest >=>-with-two-switches
  (def combined-failing (rop/>=> failing-switch
                                 failing-switch))
  (def combined-succeeding (rop/>=> succeeding-switch
                                    succeeding-switch))
  (def succeeding-failing (rop/>=> succeeding-switch
                                   failing-switch))
  (def failing-succeeding (rop/>=> failing-switch
                                   succeeding-switch))

  (is (= (fail (str msg-failure msg-yes))
         (rop/>>= yes
                  combined-failing)))
  (is (= (fail (str msg-failure msg-yes))
         (rop/>>= yes
                  succeeding-failing)))
  (is (= (fail (str msg-failure msg-yes))
         (rop/>>= yes
                  failing-succeeding)))
  (is (= (succeed msg-yes)
         (rop/>>= yes
                  combined-succeeding)))
  (is (= (fail msg-no)
         (rop/>>= no
                  combined-failing)))
  (is (= (fail msg-no)
         (rop/>>= no
                  succeeding-failing)))
  (is (= (fail msg-no)
         (rop/>>= no
                  failing-succeeding)))
  (is (= (fail msg-no)
         (rop/>>= no
                  combined-succeeding))))

(deftest switch
  (def plus-as-switch (rop/switch +))
  (is (= (succeed 4)
         (plus-as-switch 2 2))))

;; (deftest map
;;   (is (= ()
;;        (rop/map )
;;        ))
  ;; )
