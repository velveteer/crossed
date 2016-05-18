(ns app.server.util)

(defn generator->lazy-seq [gen]
  "Takes a function that returns a new value each time it is called,
  and returns a lazy-seq based on it."
  (cons (gen) (lazy-seq (generator->lazy-seq gen))))

(defn enumerate [seq]
  "Takes a seq, and returns a new seq of [index value] pairs."
  (let [indices (range 0 (count seq))]
    (zipmap indices seq)))

(defn first-index-of [item seq]
  "Return the index where item first occurs in seq."
  (first (keep-indexed (fn [idx val]
                         (when (= item val) idx))
                       seq)))
(defn last-index-of [item seq]
  "Return the index where item last occurs in seq."
  (first-index-of item (reverse seq)))