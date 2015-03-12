(ns fourclojure.parentheses-test
  (:require [clojure.test :refer :all]
            [fourclojure.parentheses :refer :all]))

(deftest cases
  (is (= (solution 0) #{""}))
  (is (= (solution 1) #{"()"}))
  (is (= (solution 2) #{"()()" "(())"}))
  (is (= (solution 3) #{"((()))" "(())()" "()(())" "(()())" "()()()"}))
  (is (= #{"((()))" "()()()" "()(())" "(())()" "(()())"} (solution 3)))
  (is (= (solution 4) (set ["(((())))" "((()()))" "(()(()))" "((())())" "(()()())"
                            "()((()))" "()(()())" "()(())()" "()()()()" "()()(())"
                            "((()))()" "(()())()" "()()()()" "(())()()"
                            "(())(())"])))
  (is (= 16796 (count (solution 10))))
  (is (= (nth (sort (solution 12)) 5000) "(((((()()()()()))))(()))")))

;; (deftest support
;;   (is (= (solution-lengths 1) #{[1]}))
;;   (is (= (solution-lengths 2) #{[2] [1 1]}))
;;   (is (= (solution-lengths 3) #{[3] [2 1] [1 2] [1 1 1]}))
;;   (is (= (solution-lengths 4) #{[4] [3 1] [2 2] [1 3] [2 1 1] [1 2 1] [1 1 2] [1 1 1 1]})))
