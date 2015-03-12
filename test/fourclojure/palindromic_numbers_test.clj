(ns fourclojure.palindromic-numbers-test
  (:require [clojure.test :refer :all]
            [fourclojure.palindromic-numbers :refer :all]))

;; (deftest with-digits-test
;;   (testing "0"
;;     (is (= (palindromes-of-digit-length 1) [0 1 2 3 4 5 6 7 8 9]))
;;     (is (= (palindromes-of-digit-length 2) [11 22 33 44 55 66 77 88 99]))
;;     (is (= (palindromes-of-digit-length 3) [101 111 121 131 141 151 161 171 181 191
;;                                             202 212 222 232 242 252 262 272 282 292
;;                                             303 313 323 333 343 353 363 373 383 393
;;                                             404 414 424 434 444 454 464 474 484 494
;;                                             505 515 525 535 545 555 565 575 585 595
;;                                             606 616 626 636 646 656 666 676 686 696
;;                                             707 717 727 737 747 757 767 777 787 797
;;                                             808 818 828 838 848 858 868 878 888 898
;;                                             909 919 929 939 949 959 969 979 989 999]))))

(deftest full-function-test
  (is (= (take 26 ((full-function) 0))
         [0 1 2 3 4 5 6 7 8 9
          11 22 33 44 55 66 77 88 99
          101 111 121 131 141 151 161]))
  (is (= (take 16 ((full-function) 162))
         [171 181 191 202
          212 222 232 242
          252 262 272 282
          292 303 313 323]))
  (is (= (take 6 ((full-function) 1234550000))
         [1234554321 1234664321 1234774321
          1234884321 1234994321 1235005321]))
  (is (= (first ((full-function) (* 111111111 111111111)))
         (* 111111111 111111111)))
  (is (= (set (take 199 ((full-function) 0)))
         (set (map #(first ((full-function) %)) (range 0 10000)))))
  (is (= true
         (apply < (take 6666 ((full-function) 9999999)))))
  (is (= (nth ((full-function) 0) 10101)
         9102019))
  )
