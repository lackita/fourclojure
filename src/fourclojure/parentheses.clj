(ns fourclojure.parentheses)

(defn every-position-increased [v]
  (map #(update-in v [%] inc) (range (count v))))

(defn unnested-lengths [n]
  (vec (repeat n 1)))

(defn solution-lengths [n]
  (if (= n 1)
    #{[1]}
    (set (concat (mapcat every-position-increased (solution-lengths (dec n)))
                 [(unnested-lengths n)]))))

;; (defn all-combinations [& colls]
;;   (if (= (count colls) 1)
;;     (first colls)
;;     (for [current (first colls)
;;           next (all-combinations (rest colls))]
;;       )))

;; (defn new-solution [n]
;;   (case n
;;     0 #{""}
;;     1 #{"()"}
;;     2 #{"(())" "()()"}
;;     (mapcat #(map new-solution %)
;;             (disj (solution-lengths n) [n]))))





;; (defn solution [n]
;;   (case n
;;     0 #{""}
;;     1 #{"()"}





;;     (let [previous-solution (solution (dec n))]
;;       (set (concat (mapcat #(clojure.string/join "" (map solution %)) (solution-lengths (dec n)))
;;                    (map #(str "(" % ")") previous-solution))))))



(def recursive-solution
  (memoize (fn [n]
             (if (= n 0)
               #{""}
               (set (concat (map #(str "(" % ")") (recursive-solution (dec n)))
                            (mapcat (fn [l] (mapcat (fn [s] (map #(str s %)
                                                                 (recursive-solution (- n l))))
                                                    (recursive-solution l)))
                                    (range 1 n))))))))

(def solution
  (fn [n]
    (loop [lesser-solutions {0 #{""}}
           c 1]
      (if (lesser-solutions n)
        (lesser-solutions n)
        (recur (assoc lesser-solutions
                 c (set (concat (map #(str "(" % ")") (lesser-solutions (dec c)))
                                (mapcat (fn [l] (mapcat (fn [s] (map #(str s %)
                                                                     (lesser-solutions (- c l))))
                                                        (lesser-solutions l)))
                                        (range 1 c)))))
               (inc c))))))
