(ns fourclojure.infinite-matrix)

(defn full-function []
  (fn infinite-matrix
    ([f] (infinite-matrix f 0 0))
    ([f m n] (letfn [(integers [start]
                       (cons start (lazy-seq (integers (inc start)))))]
               (map (fn [i] (map (fn [j] (f i j)) (integers n))) (integers m))))
    ([f m n s t] (map (partial take t)
                      (take s (infinite-matrix f m n))))))
