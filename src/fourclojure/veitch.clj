(ns fourclojure.veitch)

(defn negative [s]
  (symbol (.toLowerCase (name s))))

(defn positive [s]
  (symbol (.toUpperCase (name s))))

(defn gray-codes [symbols]
  (if (empty? symbols) [#{}]
      (let [varying (first symbols)
            subsequent-gray-codes (gray-codes (rest symbols))]
        (concat (map #(conj % (negative varying))
                     subsequent-gray-codes)
                (map #(conj % (positive varying))
                     (reverse subsequent-gray-codes))))))

(defn divide-symbols [symbols]
  (split-at (Math/ceil (/ (count symbols) 2)) symbols))

(defn build-grid [conditions x-gray-codes y-gray-codes]
  (map (fn [y-gray-code]
         (map #(if (conditions (set (concat % y-gray-code))) 1 0)
              x-gray-codes)) y-gray-codes))

(defn build-kmap [conditions]
  (let [[x-gray-codes y-gray-codes] (->> (first conditions)
                                         (map negative)
                                         sort
                                         divide-symbols
                                         (map gray-codes))
        grid (build-grid conditions x-gray-codes y-gray-codes)]
    {:grid grid
     :x-gray-codes x-gray-codes
     :y-gray-codes y-gray-codes
     :width (count (first grid))
     :height (count grid)}))


(defn gray-codes-for-box [gray-codes positions]
  (apply clojure.set/intersection
         (map #(nth gray-codes %) positions)))

(defn cyclic-ranges [distance size]
  (for [start (range distance)]
    (take size (drop start (cycle (range distance))))))

(defn rectangle-coordinates [kmap size]
  (set (for [divisor [1 2 4]
             xs (cyclic-ranges (kmap :width)
                               (/ size divisor))
             ys (cyclic-ranges (kmap :height)
                               divisor)]
         (set (for [x xs y ys] [x y])))))

(defn convert-pairs-to-expressions [kmap pairs]
  [(clojure.set/union
    (gray-codes-for-box (kmap :x-gray-codes)
                        (map first pairs))
    (gray-codes-for-box (kmap :y-gray-codes)
                        (map second pairs)))
   (set (map (fn [[x y]] (nth (nth (kmap :grid)
                                   y) x))
             pairs))])

(defn boxes-sized [kmap size]
  (set (map #(convert-pairs-to-expressions kmap %)
            (rectangle-coordinates kmap size))))

(defn identify-simplifications [kmap]
  (map (fn [size] (->> (boxes-sized kmap size)
                       (filter #(not (get-in % [1 0])))
                       (map first)))
       [8 4 2 1]))


(defn already-fulfilled? [existing-conditions new-condition]
  (not (some (fn [existing]
               (every? new-condition existing))
             existing-conditions)))

(defn prune-fulfilled [simplified-conditions]
  (set (reduce (fn [existing-conditions new-conditions]
                 (into existing-conditions
                       (filter #(already-fulfilled? existing-conditions %) new-conditions)))
               simplified-conditions)))

(defn identify-condition-coverage [full-conditions simplified-conditions]
  (into {} (map (fn [condition]
                  [condition (set (filter #(every? condition %)
                                          simplified-conditions))])
                full-conditions)))

(defn necessary-condition? [coverage candidate]
  (some #(and (= 1 (count %)) (% candidate)) (vals coverage)))

(defn remove-from-coverage [coverage candidate]
  (into {} (map (fn [[original-rule covering-rules]]
                  [original-rule (disj covering-rules candidate)])
                coverage)))

(defn prune-duplicates [full-conditions simplified-conditions]
  (let [pruned-conditions (prune-fulfilled simplified-conditions)
        condition-coverage (identify-condition-coverage full-conditions pruned-conditions)]
    (second (reduce (fn [[coverage necessary] candidate]
                      (if (necessary-condition? coverage candidate)
                        [coverage (conj necessary candidate)]
                        [(remove-from-coverage coverage candidate) necessary]))
                    [condition-coverage #{}]
                    pruned-conditions))))


(defn simplify-rules [conditions]
  (->> (build-kmap conditions)
       identify-simplifications
       (prune-duplicates conditions)))
