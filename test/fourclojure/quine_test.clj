(ns fourclojure.quine-test
  (:require [clojure.test :refer :all]
            [fourclojure.quine :refer :all]))

(deftest only-test
  (is (quine? (fn []
                ((fn [x]
                   (str (list (quote fn) []
                              (list x (list (quote quote) x)))))
                 (quote (fn [x]
                          (str (list (quote fn) []
                                     (list x (list (quote quote) x)))))))))))
