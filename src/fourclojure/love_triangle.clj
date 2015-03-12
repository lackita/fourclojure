(ns fourclojure.love-triangle)

(defn digit-magnitudes [base-ten]
  (reverse (take-while #(<= % base-ten)
                       (take 5 (map #(Math/pow 2 %)
                                    (range))))))

(defn digits [base-ten magnitudes]
  (if (empty? magnitudes)
    []
    (let [magnitude (first magnitudes)
          digit (if (< base-ten magnitude) 0 1)]
      (cons digit (digits (- base-ten (* digit magnitude))
                          (rest magnitudes))))))

(defn build-cross-section [bit-maps]
  (map (fn [bit-map]
         (digits bit-map (digit-magnitudes (apply max bit-maps))))
       bit-maps))

(defn harvestable? [mineral]
  (and (every? #(every? (partial = 1) %) mineral)
       (some #(= (count %) 1) mineral)))

(defn square? [cross-section]
  (= (count cross-section) (count (first cross-section))))

(defn shear-horizontally [cross-section]
  (rest cross-section))

(defn shear-vertically [cross-section]
  (map rest cross-section))

(defn reduce-to-square [cross-section]
  (if (square? cross-section)
    cross-section
    (shear-horizontally cross-section)))

(defn shear-diagonally
  ([cross-section] (shear-diagonally 1 cross-section))
  ([cut-point cross-section]
     (if (empty? cross-section)
       []
       (cons (take cut-point (first cross-section))
             (shear-diagonally (inc cut-point)
                               (rest cross-section))))))

(defn calculate-value [mineral]
  (if (and (every? #(every? (partial = 1) %) mineral)
           (> (count mineral) 1))
    (apply + (apply concat mineral))
    0))

(defn flip-vertically [cross-section]
  (reverse cross-section))

(defn flip-horizontally [cross-section]
  (map reverse cross-section))

(defn rotate [cross-section]
  (apply map vector (flip-vertically cross-section)))

(defn calculate-diagonal-shear [cross-section]
  (calculate-value (shear-diagonally (reduce-to-square cross-section))))

(defn remove-border [cross-section]
  (-> cross-section
      shear-horizontally
      flip-vertically
      shear-horizontally
      shear-vertically
      flip-horizontally
      shear-vertically))

(defn shear-right-corners [cross-section]
  (-> cross-section
      shear-diagonally
      flip-vertically
      shear-diagonally
      calculate-value))
 

(defn mine [cross-section]
  (if (or (empty? cross-section) (every? empty? cross-section))
    0
    (max (calculate-diagonal-shear cross-section)
         (calculate-diagonal-shear (rotate cross-section))
         (calculate-diagonal-shear (rotate (rotate (rotate cross-section))))
         (-> cross-section
             rotate
             flip-horizontally
             shear-right-corners)
         (shear-right-corners cross-section)
         (mine (remove-border cross-section))
         (mine (-> cross-section shear-vertically flip-horizontally shear-vertically flip-vertically shear-horizontally)))))

(defn love-triangle [bit-maps]
  (let [value (mine (build-cross-section bit-maps))]
    (if (= value 0)
      nil
      value)))
