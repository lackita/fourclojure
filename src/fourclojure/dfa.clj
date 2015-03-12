(ns fourclojure.dfa)

(defn full-function []
  (fn [dfa]
    (letfn [(accepts-ordering [{start :start
                                accepts :accepts}]
              (if (accepts start) 0 1))
            (accepts-first [x y]
              (compare (accepts-ordering x)
                       (accepts-ordering y)))
            (next-states-of [{base :base
                              transitions :transitions
                              start :start
                              :as dfa}]
              (map #(merge dfa {:base (str base (key %))
                                :start (val %)})
                   (transitions start)))
            (inputs-for [{accepts :accepts
                          base :base
                          start :start
                          :as dfa}]
              (let [next-inputs (lazy-seq (mapcat inputs-for
                                                  (sort accepts-first
                                                        (next-states-of dfa))))]
                (if (accepts start)
                  (cons base next-inputs)
                  next-inputs)))]
      (inputs-for (assoc dfa :base "")))))
