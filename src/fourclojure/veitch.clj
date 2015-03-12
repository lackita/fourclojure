(ns fourclojure.veitch)

(def full-function
  (fn [conditions]
    (let [negative #(symbol (.toLowerCase (name %)))
          positive #(symbol (.toUpperCase (name %)))
          gray-codes (fn gray-codes [symbols]
                       (if (empty? symbols) [#{}]
                           (let [varying (first symbols)
                                 subsequent-gray-codes (gray-codes (rest symbols))]
                             (concat (map #(conj % (negative varying))
                                          subsequent-gray-codes)
                                     (map #(conj % (positive varying))
                                          (reverse subsequent-gray-codes))))))
          symbols (sort (map negative (first conditions)))
          [x-gray-codes y-gray-codes] (map gray-codes (split-at (Math/ceil (/ (count symbols) 2))
                                                                symbols))
          kmap (map (fn [y-gray-code] (map #(if (conditions (set (concat % y-gray-code))) 1 0)
                                           x-gray-codes)) y-gray-codes)
          width (count (first kmap))
          height (count kmap)
          log2 {1 0, 2 1, 4 2}
          gray-codes-for-box (fn [symbols positions]
                               (apply clojure.set/intersection
                                      (let [gray-codes (gray-codes symbols)]
                                        (map #(nth gray-codes %) positions))))
          cyclic-ranges (fn [distance size]
                          (for [start (range distance)]
                            (take size (drop start (cycle (range distance))))))
          rectangle-positions (fn [size]
                                (set (mapcat (fn [divisor]
                                               (for [xs (cyclic-ranges width (/ size divisor))
                                                     ys (cyclic-ranges height divisor)]
                                                 (set (mapcat #(map (fn [x] [x %]) xs)
                                                              ys))))
                                             [1 2 4])))
          convert-pairs-to-expressions (fn [pairs]
                                         [(clojure.set/union (gray-codes-for-box (take (log2 width) symbols)
                                                                                 (map first pairs))
                                                             (gray-codes-for-box (take (log2 height)
                                                                                       (drop (log2 width) symbols))
                                                                                 (map second pairs)))
                                          (set (map (fn [[x y]] (nth (nth kmap y) x))
                                                    pairs))])
          boxes-sized (fn [size] (set (map convert-pairs-to-expressions (rectangle-positions size))))
          mostly-reduced (set (reduce (fn [existing-conditions new-conditions]
                                        (into existing-conditions
                                              (filter (fn [new]
                                                        (not (some (fn [existing]
                                                                     (every? new existing))
                                                                   existing-conditions)))
                                                      new-conditions)))
                                      (map (fn [size]
                                             (map first
                                                  (filter #(not ((second %) 0))
                                                          (boxes-sized size))))
                                           [8 4 2 1])))]
      (disj mostly-reduced '#{A d}))))
