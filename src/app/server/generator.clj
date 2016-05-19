(ns app.server.generator
  (:require [app.server.clues :refer [random-clue-generator]]
            [app.server.util :refer [generator->lazy-seq enumerate
                                     first-index-of]]))

;; Dynamically bound generator function for fetching new clues from
;; functions in this file.
(def ^:dynamic *random-clue-generator* (fn [] nil))
(def grid-size 17)
(def word-count 100)
(def max-placement-checks 1000)
(def start-word-min-length 5)

(defrecord Placement [clue answer start-row start-col across?])

(defn- placement-start-cell [placement]
  "Returns a [row col] vector for the start-cell of a placement."
  [(:start-row placement) (:start-col placement)])

(defn- add-placement [placement grid]
  "Returns a new grid with the letters from placement added. Achieves
  this by placing the first letter, then recursing over the rest of
  the word."
  (let [{:keys [clue answer start-row start-col across?]} placement]
    (if (empty? answer)
      grid
      (let [[letter & rest-answer] answer
            new-grid (assoc-in grid [start-row start-col] letter)]
        (if across?
          (recur (Placement. clue rest-answer start-row (inc start-col) across?) new-grid)
          (recur (Placement. clue rest-answer (inc start-row) start-col across?) new-grid))))))

(defn- can-add-placement?
  "Returns true if placement can legally be added to the grid,
  otherwise false. Achieves this by checking the first letter, then
  recursing over the rest of the word."
  ([placement grid]
   (can-add-placement? placement grid true))
  ([placement grid first?]
   (let [{:keys [clue answer start-row start-col across?]} placement]
     (if (empty? answer)
       true
       (let [[letter & rest-answer] answer
             last? (empty? rest-answer)
             current-letter (get-in grid [start-row start-col])
             above (get-in grid [(dec start-row) start-col])
             below (get-in grid [(inc start-row) start-col])
             left (get-in grid [start-row (dec start-col)])
             right (get-in grid [start-row (inc start-col)])]
         (if (and (or (not first?) ;; The first letter must have an
                                   ;; empty space before it.
                      (if across?
                        (nil? left)
                        (nil? above)))
                  (or (not last?) ;; The last letter must have an
                                  ;; empty space after it.
                      (if across?
                        (nil? right)
                        (nil? below)))
                  ;; Each letter must be on the grid.
                  (>= start-row 0)
                  (< start-row grid-size)
                  (>= start-col 0)
                  (< start-col grid-size)
                  ;; It must either match an existing letter in the
                  ;; grid, or have no conflicting letters on either
                  ;; side.
                  (or (= letter current-letter)
                      (if across?
                        (and (nil? above)
                             (nil? below))
                        (and (nil? left)
                             (nil? right))))
                  ;; If there is already a letter, then it must be the
                  ;; same.
                  (or (= letter current-letter)
                      (nil? current-letter)))
           (if across?
             (recur (Placement. clue rest-answer start-row (inc start-col) across?) grid false)
             (recur (Placement. clue rest-answer (inc start-row) start-col across?) grid false))
           false))))))

(defn- letter-indices [letter word]
  (reduce (fn [indices [word-index word-letter]]
            (if (= letter word-letter)
              (conj indices word-index)
              indices))
          [] (enumerate word)))

(defn- word-intersections [a b]
  (reduce (fn [intersections [a-index a-letter]]
            (let [indices-of-a-letter-in-b (letter-indices a-letter b)
                  new-intersections (map #(vector a-index %) indices-of-a-letter-in-b)]
              (concat intersections new-intersections)))
          [] (enumerate a)))

(defn- build-placement [intersect existing-placement clue answer]
  (let [existing-across? (:across? existing-placement)
        existing-intersect-idx (first intersect)
        new-intersect-idx (second intersect)
        grid-intersect-col (if existing-across?
                             (+ (:start-col existing-placement) existing-intersect-idx)
                             (:start-col existing-placement))
        grid-intersect-row (if existing-across?
                             (:start-row existing-placement)
                             (+ (:start-row existing-placement) existing-intersect-idx))
        new-start-col (if existing-across?
                        grid-intersect-col
                        (- grid-intersect-col new-intersect-idx))
        new-start-row (if existing-across?
                        (- grid-intersect-row new-intersect-idx)
                        grid-intersect-row)]
    (Placement. clue answer new-start-row new-start-col (not existing-across?))))

(defn- possible-placements
  ([existing-placements clue answer]
   (possible-placements existing-placements clue answer []))
  ([existing-placements clue answer possible-placements]
   (if (empty? existing-placements)
     possible-placements
     (let [[existing-placement & rest-existing-placements] existing-placements
           existing-answer (:answer existing-placement)
           intersections (word-intersections existing-answer answer)
           new-possible-placements (map #(build-placement % existing-placement clue answer) intersections)
           updated-possible-placements (concat possible-placements new-possible-placements)]
       (recur rest-existing-placements clue answer updated-possible-placements)))))

(defn- placement-unique? [new-placement placements]
  (defn subword [word1 word2]
    (let [len1 (.length word1)
          len2 (.length word2)]
      (cond
        (< len1 len2) (not= (.indexOf word2 word1) -1)
        (> len1 len2) (not= (.indexOf word1 word2) -1)
        :else (= word1 word2))))
  (let [new-answer (:answer new-placement)
        answers (map :answer placements)]
    (every? #(not (subword new-answer %)) answers)))

(defn- build-crossword
  ([placements grid]
   (build-crossword placements grid 0))
  ([placements grid check-count]
   (if (or (>= (count placements) word-count)
           (>= check-count max-placement-checks))
     [placements grid]
     (let [[clue answer] (*random-clue-generator*)
           possible-placements-for-clue (possible-placements placements clue answer)
           allowed-placements (filter #(can-add-placement? % grid) possible-placements-for-clue)
           new-placement (first allowed-placements)]
       (if (and (not (nil? new-placement))
                ;; Ensure that the answer is not already in use on the grid.
                (placement-unique? new-placement placements))
         (recur (conj placements new-placement) (add-placement new-placement grid) (inc check-count))
         (recur placements grid (inc check-count)))))))

(defn- init-crossword []
  (let [starter-clues (filter (fn [[clue answer]]
                                (> (count answer) start-word-min-length))
                              (generator->lazy-seq *random-clue-generator*))
        [clue answer] (first starter-clues)
        first-placement (Placement. clue answer 0 0 true)]
    [[first-placement] (add-placement first-placement {})]))

(defn- get-start-cells [placements]
  (-> (map placement-start-cell placements)
      (distinct)
      (sort)))

(defn- get-clue-number [start-cell start-cells]
  "Returns the clue number for a start-cell given an ordered list of
  start-cells in the puzzle."
  (if-let [clue-index (first-index-of start-cell start-cells)]
    (inc clue-index)
    nil))

(defn- clues-with-numbers [placements start-cells]
  (map #(assoc %1 :number (get-clue-number (placement-start-cell %1) start-cells)) placements))

(defn- formatted-grid [grid start-cells]
  (->> (for [[row-idx row] grid
             [col-idx letter] row
             :let [cell-data {:letter (str letter)}]
             :let [clue-number (get-clue-number [row-idx col-idx] start-cells)]
             :let [cell-data-with-number (if (nil? clue-number)
                                           cell-data
                                           (assoc cell-data :number clue-number))]]
         {row-idx {col-idx cell-data-with-number}})
       (reduce #(merge-with merge %1 %2))))

(defn generate-crossword
  [id]
  (binding [*random-clue-generator* (random-clue-generator id)]
    (let [[start-placements start-grid] (init-crossword)
          [placements grid] (build-crossword start-placements start-grid)
          start-cells (get-start-cells placements)
          clues (clues-with-numbers placements start-cells)
          formatted-grid (formatted-grid grid start-cells)]
      {:id id
       :grid-size grid-size
       :words (count clues)
       :clues clues
       :grid formatted-grid})))