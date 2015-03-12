(ns fourclojure.squares-squared)

(defn full-function []
  (fn squares-squared [start end]
    (letfn [(replace-position [square [y x] value]
              (let [[before-y after-y] (split-at y square)
                    [before-x after-x] (split-at x (first after-y))]
                (concat before-y
                        [(apply str (concat before-x [value] (rest after-x)))]
                        (rest after-y))))
            (expand-square [square]
              (cond (= (count square) 1) [(str " " (first square) " ")
                                          "* *"
                                          " * "]
                    (= (count square) 3) (map #(str %2 %1 %2)
                                              `(" * " "* *" ~@square)
                                              [" " " " "*" " " " "])
                    :else                (map #(str %2 %1 %2)
                                              `(~@(butlast square)
                                                ~(str "*" (subs (last square) 1 4) "*")
                                                " * * " "  *  ")
                                              [" " " " " " "*" " " " " " "])))
            (get-update-positions []
              (map :value (iterate (fn [{pair      :value
                                         direction :direction}]
                                     {:value     (map + pair direction)
                                      :direction (cond (#{[0 0] [2 4]} pair) [1 -1]
                                                       (#{[1 2] [5 4]} pair) [-1 -1]
                                                       (= [2 1] pair)        [1 0]
                                                       (= [1 0] pair)        [-1 1]
                                                       (= [1 1] pair)        [1 1]
                                                       (= [1 3] pair)        [1 2]
                                                       :else                 direction)})
                                   {:value     [0 0]
                                    :direction [1 2]})))]
      (loop [square ["*"]
             content (apply str (take-while #(<= % end) (iterate #(* % %) start)))
             update-positions (get-update-positions)]
        (cond (empty? content)
              square
              (not-any? #(some (partial = \*)
                               %)
                        square)
              (recur (expand-square square)
                     content
                     update-positions)
              :else
              (recur (replace-position square
                                       (first update-positions)
                                       (first content))
                     (rest content)
                     (rest update-positions)))))))
