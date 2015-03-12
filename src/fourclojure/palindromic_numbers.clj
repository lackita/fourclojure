(ns fourclojure.palindromic-numbers
  (:require [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

(defn full-function []
  (fn [start]
    (let [first-palindrome (first (filter #(= (str %) (clojure.string/reverse (str %)))
                                          (iterate inc start)))]
      (cons first-palindrome ((fn palindromes [previous]
                                (lazy-seq
                                 (let [digits                 (loop [n previous
                                                                     a []]
                                                                (if (< n 10)
                                                                  (cons n a)
                                                                  (recur (quot n 10)
                                                                         (cons (rem n 10)
                                                                               a))))
                                       digits#                (count digits)
                                       increment-last         (fn increment-last [s]
                                                                (cond (empty? s)     [1]
                                                                      (= 9 (last s)) (conj (vec (increment-last (butlast s)))
                                                                                           0)
                                                                      :else          (conj (vec (butlast s))
                                                                                           (inc (last s)))))
                                       first-half             (take (/ digits# 2) digits)
                                       incremented-first-half (increment-last first-half)
                                       next-value             (loop [s (cond (every? #(= 9 %) first-half) (increment-last (cons 1 (repeat digits# 0)))
                                                                             (even? digits#)              (concat incremented-first-half
                                                                                                                  (reverse incremented-first-half))
                                                                             :else                        (concat incremented-first-half
                                                                                                                  (rest (reverse incremented-first-half))))
                                                                     a 0]
                                                                (if (empty? s)
                                                                  a
                                                                  (recur (rest s) (+ (* a 10) (first s)))))]
                                   (cons next-value (palindromes next-value)))))
                              first-palindrome)))))

;; (defnp build-up-from-half-function []
;;   "This is correct, but doesn't have the performance I want"
;;   (fn [start]
;;     (letfn [(extract-digits [x]
;;               (if (< x 10)
;;                 [x]
;;                 (conj (extract-digits (quot x))
;;                       _)))
;;             (digits-in [number]
;;               (loop [n number
;;                      acc 1]
;;                 (if (< n 10)
;;                   acc
;;                   (recur (quot n 10) (inc acc)))))
;;             (pow [b n]
;;               (nth (iterate #(* b %) 1) n))
;;             (numbers-of-length [length]
;;               (range (max (quot start (pow 10 (quot (digits-in start) 2)))
;;                           (pow 10 (dec length)))
;;                      (pow 10 length)))
;;             (front [number offset]
;;               (* number (pow 10 offset)))
;;             (end [number]
;;               (Integer/parseInt (clojure.string/reverse (str number))))
;;             (palindromes-at [digits]
;;               (cond (= 1 digits) (range 10)
;;                     (even? digits) (map #(+ (front % (digits-in %))
;;                                             (end %))
;;                                         (numbers-of-length (/ digits 2)))
;;                     :else          (map #(+ (front % (dec (digits-in %)))
;;                                             (end (quot % 10)))
;;                                         (numbers-of-length (inc (quot digits 2))))))
;;             (palindromes-by [digits]
;;               (lazy-seq (cons (palindromes-at digits)
;;                               (palindromes-by (inc digits)))))]
;;       (drop-while #(< % start)
;;                   (apply concat (palindromes-by (digits-in start)))))))

(defn build-up-from-previous-function
  "main problem is that we have to consider every single palindrome, padded by 0s"
  []
  (fn [start]
    (drop-while
     #(< % start)
     (mapcat
      :current
      (cons {:current (range 10)}
            ((fn calculate-digits [previous-iteration]
               (lazy-seq (let [next-iteration {:current (:next previous-iteration)
                                               :next    (apply
                                                         interleave
                                                         (map (fn [base]
                                                                (map #(+ (* % (first (:offsets previous-iteration)))
                                                                         (* 10 base)
                                                                         %)
                                                                     (range 1 10)))
                                                              (cons 0 (:current previous-iteration))))
                                               :offsets (rest (:offsets previous-iteration))}]
                           (cons next-iteration (calculate-digits next-iteration)))))
             {:current (range 1 10)
              :next    (map #(+ (* 10 %) %)
                            (range 1 10))
              :offsets (iterate #(* 10 %) 100)}))))))
