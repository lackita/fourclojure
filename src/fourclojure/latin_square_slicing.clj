(ns fourclojure.latin-square-slicing)

(defn latin-square? [square]
  (and (apply = (concat (map set square)
                        (map set (apply map vector square))))
       (= (count (first square)) (count (set (first square))))
       (not (contains? (set (first square)) nil))))

(defn subsquare [v [y x] order]
  (mapv #(take order (drop x %))
        (take order (drop y v))))

(defn variations [width row]
  (mapv #(concat (repeat % nil)
                 row
                 (repeat (- width (count row) %)
                         nil))
        (range (inc (- width (count row))))))

(defn alignments
  ([v] (alignments v (apply max (map count v))))
  ([v width]
     (if (empty? v)
       [[]]
       (let [vary (partial variations width)
             subsequent-alignments (alignments (rest v) width)]
         (mapcat (fn [subsequent] (map #(cons % subsequent)
                                       (vary (first v))))
                 subsequent-alignments)))))

(defn subsquares [v]
  (let [width (count (first v))
        height (count v)]
    (mapcat (fn [order]
              (mapcat #(mapv (fn [x] (subsquare v [% x] order))
                             (range (inc (- width order))))
                      (range (inc (- height order)))))
            (range 2 (inc (min width height))))))

(defn problem [v]
  (into {} (map (fn [[order squares]]
                  [order (count squares)])
                (group-by #(count %)
                          (distinct (filter latin-square?
                                            (mapcat subsquares
                                                    (alignments v))))))))
