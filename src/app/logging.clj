(ns app.logging)

(defmacro log [& args]
  `(.log js/console ~@args))