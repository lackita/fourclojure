(ns fourclojure.reversi
  (:refer clojure.pprint))

(defn full-function []
  (fn [board color]
    (let [size            (count board)
          opponent        (if (= color 'w) 'b 'w)
          empty-positions (for [y     (range size)
                                x     (range size)
                                :when (= ((board y) x) 'e)]
                            [y x])
          directions      (for [y [-1 0 1] x [-1 0 1] :when (not (= [y x] [0 0]))]
                            [y x])
          ray             (fn [position direction]
                            (rest (take-while #(and (>= (first %) 0)
                                                    (<  (first %) size)
                                                    (>= (last %) 0)
                                                    (<  (last %) size))
                                              (iterate #(map + % direction)
                                                       position))))
          piece           (fn [position]
                            ((board (first position)) (last position)))
          flippable-ray   (fn [ray]
                            (if (some #(= (piece %) color) ray)
                              (take-while #(= (piece %) opponent)
                                          (take-while #(not (= (piece %) color))
                                                      ray))
                              []))
          rays            (map (fn [position]
                                 [position (map (fn [direction]
                                                  (flippable-ray (ray position direction)))
                                                directions)])
                               empty-positions)
          pruned          (filter #(not (empty? (last %)))
                                  (map (fn [[position rays]]
                                         [position (filter (complement empty?) rays)])
                                       rays))]
      (into {} (map (fn [[position rays]]
                      [position (set (apply concat rays))])
                    pruned)))))
