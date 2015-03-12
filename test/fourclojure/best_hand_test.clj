(ns fourclojure.best-hand-test
  (:require [clojure.test :refer :all]
            [fourclojure.best-hand :refer :all]))

(deftest hand-test
  (testing "high card"
    (is (= ((full-function) ["HA" "D2" "H3" "C9" "DJ"])
           :high-card))
    (is (= ((full-function) ["HA" "HQ" "SJ" "DA" "HT"])
           :pair))
    (is (= ((full-function) ["HA" "DA" "HQ" "SQ" "HT"])
           :two-pair))
    (is (= ((full-function) ["HA" "DA" "CA" "HJ" "HT"])
           :three-of-a-kind))
    (is (= ((full-function) ["HA" "DK" "HQ" "HJ" "HT"])
           :straight))
    (is (= ((full-function) ["HA" "H2" "S3" "D4" "C5"])
           :straight))
    (is (= ((full-function) ["HA" "HK" "H2" "H4" "HT"])
           :flush))
    (is (= ((full-function) ["HA" "DA" "CA" "HJ" "DJ"])
           :full-house))
    (is (= ((full-function) ["HA" "DA" "CA" "SA" "DJ"])
           :four-of-a-kind))
    (is (= ((full-function) ["HA" "HK" "HQ" "HJ" "HT"])
           :straight-flush))
    )
  )
