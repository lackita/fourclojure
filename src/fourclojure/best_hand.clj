(ns fourclojure.best-hand)

(defn full-function []
  (fn [hand]
    (letfn [(occurrences-count [n]
              (count (filter #(= (count (val %)) n)
                             (group-by second hand))))
            (value [card]
              ({\2 2 \3 3 \4 4 \5 5
                \6 6 \7 7 \8 8 \9 9
                \T 10 \J 11 \Q 12 \K 13}
               (second card)))
            (adjacent? [x y]
              (cond (= (second x) \A) (#{\K \2} (second y))
                    (= (second y) \A) (#{\K \2} (second x))
                    :else             (= (Math/abs (- (value x) (value y)))
                                         1)))
            (straight-from? [current-card cards]
              (if (empty? cards)
                true
                (let [next-card (first (filter #(adjacent? current-card %)
                                               cards))]
                  (and next-card (straight-from? next-card (disj cards next-card))))))
            (straight? []
              (some #(straight-from? % (disj (set hand) %)) hand))
            (flush? []
              (= (count (group-by first hand)) 1))
            (full-house? []
              (and (= (occurrences-count 3) 1)
                   (= (occurrences-count 2) 1)))]
      (cond (and (straight?) (flush?))  :straight-flush
            (= (occurrences-count 4) 1) :four-of-a-kind
            (full-house?)               :full-house
            (flush?)                    :flush
            (straight?)                 :straight
            (= (occurrences-count 3) 1) :three-of-a-kind
            (= (occurrences-count 2) 2) :two-pair
            (= (occurrences-count 2) 1) :pair
            :else                       :high-card))))
