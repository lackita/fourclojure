(ns fourclojure.love-triangle-test
  (:require [clojure.test :refer :all]
            [fourclojure.love-triangle :refer :all]))

(deftest love-triangle-test
  (is (= (love-triangle [15 15 15 15 15]) 10))
  (is (= (love-triangle [1 3 7 15 31]) 15))
  (is (= (love-triangle [3 3]) 3))
  (is (= (love-triangle [7 3]) 4))
  (is (= (love-triangle [17 22 6 14 22]) 6))
  (is (= (love-triangle [18 7 14 14 6 3]) 9))
  (is (= (love-triangle [21 10 21 10]) nil))
  (is (= (love-triangle [0 31 0 31 0]) nil))
  )

(deftest build-cross-section-test
  (is (= (build-cross-section [15 15 15 15 15])
         [[1 1 1 1]
          [1 1 1 1]
          [1 1 1 1]
          [1 1 1 1]
          [1 1 1 1]])))

(deftest rotate-test
  (is (= (rotate [[1 2]
                  [3 4]])
         [[3 1]
          [4 2]])))
