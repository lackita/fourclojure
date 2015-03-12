(ns fourclojure.latin-square-slicing-test
  (:require [clojure.test :refer :all]
            [fourclojure.latin-square-slicing :refer :all]))

(deftest latin-square?-test
  (is (latin-square? [[1 2]
                      [2 1]]))
  (is (not (latin-square? [[1 1]
                           [2 1]])))
  (is (not (latin-square? [[2 1]
                           [2 1]])))
  (is (not (latin-square? [[1 2 1]
                           [2 1 2]
                           [1 2 1]])))
  (is (not (latin-square? [[1 nil]
                           [nil 1]]))))

(deftest subsquare-test
  (is (= (subsquare [[1 2]
                     [2 1]]
                    [0 0]
                    2)
         [[1 2]
          [2 1]]))
  (is (= (subsquare [[1 2 3]
                     [1 2 3]]
                    [0 0]
                    2)
         [[1 2]
          [1 2]]))
  (is (= (subsquare [[1 2 3]
                     [1 2 3]
                     [1 2 3]]
                    [0 0]
                    2)
         [[1 2]
          [1 2]]))
  (is (= (subsquare [[1 2 3]
                     [1 2 3]
                     [3 1 2]]
                    [1 0]
                    2)
         [[1 2]
          [3 1]]))
  (is (= (subsquare [[1 2 3]
                     [1 2 3]
                     [1 2 3]]
                    [0 1]
                    2)
         [[2 3]
          [2 3]]))
  (is (= (subsquare [[1 2 3]
                     [1 2 3]
                     [1 2 3]]
                    [0 0]
                    3)
         [[1 2 3]
          [1 2 3]
          [1 2 3]])))

(deftest alignments-test
  (is (= (alignments [[1 2]] 3)
         [[[1 2 nil]]
          [[nil 1 2]]]))
  (is (= (alignments [[1 2 3]
                      [4 5 6]])
         [[[1 2 3]
           [4 5 6]]]))
  (is (= (alignments [[1 2]
                      [4 5 6]])
         [[[1 2 nil]
           [4 5 6]]
          [[nil 1 2]
           [4 5 6]]]))
  (is (= (alignments [[1 2 3]
                      [4 5]])
         [[[1 2 3]
           [4 5 nil]]
          [[1 2 3]
           [nil 4 5]]]))
  (is (= (alignments [[1 2 3]] 5)
         [[[1 2 3 nil nil]] [[nil 1 2 3 nil]] [[nil nil 1 2 3]]])))

(deftest subsquares-test
  (is (= (subsquares [[1 1]
                      [1 1]])
         [[[1 1]
           [1 1]]]))
  (is (= (subsquares [[1 4]
                      [2 5]
                      [3 6]])
         [[[1 4]
           [2 5]]
          [[2 5]
           [3 6]]]))
  (is (= (subsquares [[1 2 3]
                      [4 5 6]])
         [[[1 2]
           [4 5]]
          [[2 3]
           [5 6]]]))
  (is (= (subsquares [[1 2 3]
                      [4 5 6]
                      [7 8 9]])
         [[[1 2]
           [4 5]]
          [[2 3]
           [5 6]]
          [[4 5]
           [7 8]]
          [[5 6]
           [8 9]]
          [[1 2 3]
           [4 5 6]
           [7 8 9]]])))

(deftest problem-test
  (is (= (problem '[[A B C D]
                    [A C D B]
                    [B A D C]
                    [D C A B]])
         {}))
  (is (= (problem '[[A B C D E F]
                    [B C D E F A]
                    [C D E F A B]
                    [D E F A B C]
                    [E F A B C D]
                    [F A B C D E]])
         {6 1}))
  (is (= (problem '[[A B C D]
                    [B A D C]
                    [D C B A]
                    [C D A B]])
         {4 1, 2 4}))
  (is (= (problem '[[B D A C B]
                    [D A B C A]
                    [A B C A B]
                    [B C A B C]
                    [A D B C A]])
         {3 3}))
  (is (= (problem [[2 4 6 3]
                   [3 4 6 2]
                   [6 2 4]])
         {}))
  (is (= (problem [[1]
                   [1 2 1 2]
                   [2 1 2 1]
                   [1 2 1 2]
                   []       ])
         {2 2}))
  (is (= (problem [[3 1 2]
                   [1 2 3 1 3 4]
                   [2 3 1 3]    ])
         {3 1, 2 2}))
  (is (= (problem [[8 6 7 3 2 5 1 4]
                   [6 8 3 7]
                   [7 3 8 6]
                   [3 7 6 8 1 4 5 2]
                   [1 8 5 2 4]
                   [8 1 2 4 5]])
         {4 1, 3 1, 2 7})))
