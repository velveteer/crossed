(ns app.util
    (:require [clojure.string :as str]))

(defn marshal-square [square]
    (str "c" (:col square) "r" (:row square)))

(defn unmarshal-square [square]
    (let [col (last (str/split square "c"))
          row (last (str/split square "r"))]
      {:col col :row row}))