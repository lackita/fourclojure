(ns fourclojure.squares-squared-test
  (:require [clojure.test :refer :all]
            [fourclojure.squares-squared :refer :all]))

(deftest fourclojure-test
  (is (= ((full-function) 2 2) ["2"]))
  (is (= ((full-function) 2 4) [" 2 "
                                "* 4"
                                " * "]))
  (is (= ((full-function) 3 81) [" 3 "
                                 "1 9"
                                 " 8 "]))
  )
