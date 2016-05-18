(ns app.server.clues
  (:require [clojure.java.io :refer [reader resource]]
            [clojure.string :as string]))

(def clues-path (resource "clues.tsv"))

(defn parse-int [string]
  "Integer parser that falls back to 0 if string cannot be parsed"
  (try
    (. Integer parseInt string)
    (catch NumberFormatException ex 0)))

(defn deterministic-shuffle
  [^java.util.Collection coll seed]
  (let [al (java.util.ArrayList. coll)
        rng (java.util.Random. seed)]
    (java.util.Collections/shuffle al rng)
    (clojure.lang.RT/vector (.toArray al))))

(defn- get-file-lines [file-path]
  "Return a sequence of all of the lines in the given file."
  (with-open [rdr (reader file-path)]
    (doall (line-seq rdr))))

(defn- random-line! [rng lines]
  "Retrieves a random line from the given lines based on the rng."
  (let [random-line-index (.nextInt rng (count lines))]
    (->> lines
         (drop random-line-index) ;; Drop lines before
                                  ;; random-line-index.
         (first))))

(defn- random-clue! [rng lines]
  "Retrieves a random clue (a vector of [clue answer]) from the list
  of clues lines based on the rng. Each clue line is expected to
  contain the clue text followed by a tab followed by the answer."
  (let [clue-line (random-line! rng lines)]
    (string/split clue-line #"\t")))

(defn- line->clue [line]
  (string/split line #"\t"))

(defn random-clue-generator [seed]
  "Returns a function that returns a random clue each time it is
  called based on the provided rng seed."
  (let [seed-int (parse-int seed)
        lines (get-file-lines clues-path)
        random-order-lines (deterministic-shuffle lines seed-int)
        next-lines (atom (cycle random-order-lines))]
    (fn []
      (let [line (first @next-lines)]
        (swap! next-lines rest)
        (line->clue line)))))