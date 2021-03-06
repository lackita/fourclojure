(ns fourclojure.infinite-matrix-test
  (:require [clojure.test :refer :all]
            [fourclojure.infinite-matrix :refer :all]))

(deftest full-function-test
  (is (= (take 5 (map #(take 6 %) ((full-function) str)))
         [["00" "01" "02" "03" "04" "05"]
          ["10" "11" "12" "13" "14" "15"]
          ["20" "21" "22" "23" "24" "25"]
          ["30" "31" "32" "33" "34" "35"]
          ["40" "41" "42" "43" "44" "45"]]))
  (is (= (take 6 (map #(take 5 %) ((full-function) str 3 2)))
         [["32" "33" "34" "35" "36"]
          ["42" "43" "44" "45" "46"]
          ["52" "53" "54" "55" "56"]
          ["62" "63" "64" "65" "66"]
          ["72" "73" "74" "75" "76"]
          ["82" "83" "84" "85" "86"]]))
  (is (= ((full-function) * 3 5 5 7)
         [[15 18 21 24 27 30 33]
          [20 24 28 32 36 40 44]
          [25 30 35 40 45 50 55]
          [30 36 42 48 54 60 66]
          [35 42 49 56 63 70 77]]))
  (is (= ((full-function) #(/ % (inc %2)) 1 0 6 4)
         [[1/1 1/2 1/3 1/4]
          [2/1 2/2 2/3 1/2]
          [3/1 3/2 3/3 3/4]
          [4/1 4/2 4/3 4/4]
          [5/1 5/2 5/3 5/4]
          [6/1 6/2 6/3 6/4]])))
