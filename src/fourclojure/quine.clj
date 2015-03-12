(ns fourclojure.quine)

(defmacro quine? [f]
  `(= (str '~f) (~f)))
