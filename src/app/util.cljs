(ns app.util
    (:require [clojure.string :refer [split]]))

(defn convert-puzzle [puzzle] (-> (->> puzzle (.parse js/JSON)) (js->clj :keywordize-keys true)))

(defn marshal-square [square]
    (str "c" (:col square) "r" (:row square)))

(defn unmarshal-square [square]
    (let [col (-> (last (split square "c"))
                        (split "r")
                        (first)
                        (int))
          row (->
                  (last (split square "r"))
                  (int))]
      {:col col :row row}))

(defn get-squares
  [squares state]
  (reduce #(assoc %1 %2 (get state %2)) {}
          (map #(keyword (marshal-square %)) squares)))

