(ns app.components.grid
  (:require-macros [app.logging :refer [log]])
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [clojure.string :refer [join lower-case]]
            [app.util :as u]
            [app.colors :as c]
            [app.components.color-picker :as cp]))

(def puzzle-test
  "{\"id\":\"618492\",\"grid-size\":17,\"words\":42,\"clues\":[{\"clue\":\"Purse item\",\"answer\":\"COMB\",\"start-row\":0,\"start-col\":0,\"across?\":true,\"number\":1},{\"clue\":\"Ms. Farrow\",\"answer\":\"MIA\",\"start-row\":0,\"start-col\":2,\"across?\":false,\"number\":2},{\"clue\":\"Not very much\",\"answer\":\"ATRIFLE\",\"start-row\":2,\"start-col\":2,\"across?\":true,\"number\":5},{\"clue\":\"Desert-dry\",\"answer\":\"ARID\",\"start-row\":1,\"start-col\":4,\"across?\":false,\"number\":3},{\"clue\":\"Cutlass or 88, informally\",\"answer\":\"OLDS\",\"start-row\":1,\"start-col\":7,\"across?\":false,\"number\":4},{\"clue\":\"No longer standing\",\"answer\":\"SEATED\",\"start-row\":4,\"start-col\":7,\"across?\":true,\"number\":8},{\"clue\":\"Messy spot\",\"answer\":\"STY\",\"start-row\":3,\"start-col\":10,\"across?\":false,\"number\":6},{\"clue\":\"Gift to a nonprofit\",\"answer\":\"DONATION\",\"start-row\":4,\"start-col\":12,\"across?\":false,\"number\":9},{\"clue\":\"1987 Cher film\",\"answer\":\"MOONSTRUCK\",\"start-row\":8,\"start-col\":7,\"across?\":true,\"number\":17},{\"clue\":\"Nursery bed\",\"answer\":\"CRIB\",\"start-row\":0,\"start-col\":0,\"across?\":false,\"number\":1},{\"clue\":\"Letter routing abbr.\",\"answer\":\"ATTN\",\"start-row\":11,\"start-col\":9,\"across?\":true,\"number\":25},{\"clue\":\"Give off fumes\",\"answer\":\"REEK\",\"start-row\":5,\"start-col\":16,\"across?\":false,\"number\":11},{\"clue\":\"Fire, or fire-fighting tool\",\"answer\":\"AXE\",\"start-row\":11,\"start-col\":9,\"across?\":false,\"number\":25},{\"clue\":\"Lent a hand\",\"answer\":\"AIDED\",\"start-row\":13,\"start-col\":6,\"across?\":true,\"number\":28},{\"clue\":\"Pie ___ mode\",\"answer\":\"ALA\",\"start-row\":13,\"start-col\":6,\"across?\":false,\"number\":28},{\"clue\":\"Mexican ranch\",\"answer\":\"HACIENDA\",\"start-row\":15,\"start-col\":5,\"across?\":true,\"number\":35},{\"clue\":\"Short haircut\",\"answer\":\"BOB\",\"start-row\":7,\"start-col\":8,\"across?\":false,\"number\":15},{\"clue\":\"Flow out\",\"answer\":\"EFFUSE\",\"start-row\":5,\"start-col\":14,\"across?\":false,\"number\":10},{\"clue\":\"Top level in the draft\",\"answer\":\"ONEA\",\"start-row\":10,\"start-col\":12,\"across?\":true,\"number\":21},{\"clue\":\"Respectful address\",\"answer\":\"MAAM\",\"start-row\":13,\"start-col\":12,\"across?\":false,\"number\":30},{\"clue\":\"Safe document\",\"answer\":\"DEED\",\"start-row\":4,\"start-col\":1,\"across?\":true,\"number\":7},{\"clue\":\"Secret motive\",\"answer\":\"ANGLE\",\"start-row\":10,\"start-col\":15,\"across?\":false,\"number\":22},{\"clue\":\"Intense apprehension\",\"answer\":\"DREAD\",\"start-row\":4,\"start-col\":1,\"across?\":false,\"number\":7},{\"clue\":\"Adjusts, as tires\",\"answer\":\"ALIGNS\",\"start-row\":7,\"start-col\":1,\"across?\":true,\"number\":14},{\"clue\":\"Knotted attire\",\"answer\":\"TIES\",\"start-row\":6,\"start-col\":3,\"across?\":false,\"number\":12},{\"clue\":\"A fan of\",\"answer\":\"INTO\",\"start-row\":6,\"start-col\":5,\"across?\":false,\"number\":13},{\"clue\":\"Latest craze\",\"answer\":\"RAGE\",\"start-row\":12,\"start-col\":13,\"across?\":true,\"number\":27},{\"clue\":\"Wrinkled fruit\",\"answer\":\"UGLI\",\"start-row\":10,\"start-col\":7,\"across?\":false,\"number\":20},{\"clue\":\"Opera part\",\"answer\":\"ACTE\",\"start-row\":14,\"start-col\":12,\"across?\":true,\"number\":33},{\"clue\":\"Leopard's marking\",\"answer\":\"SPOT\",\"start-row\":9,\"start-col\":3,\"across?\":true,\"number\":18},{\"clue\":\"Early 007 foe\",\"answer\":\"DRNO\",\"start-row\":13,\"start-col\":10,\"across?\":false,\"number\":29},{\"clue\":\"Sphagnum moss\",\"answer\":\"PEAT\",\"start-row\":9,\"start-col\":4,\"across?\":false,\"number\":19},{\"clue\":\"Salty bodies of water\",\"answer\":\"SEAS\",\"start-row\":11,\"start-col\":2,\"across?\":true,\"number\":24},{\"clue\":\"Fr. holy women\",\"answer\":\"STES\",\"start-row\":11,\"start-col\":2,\"across?\":false,\"number\":24},{\"clue\":\"Recent USNA grad\",\"answer\":\"ENS\",\"start-row\":7,\"start-col\":10,\"across?\":false,\"number\":16},{\"clue\":\"Fla. clock setting\",\"answer\":\"EST\",\"start-row\":12,\"start-col\":0,\"across?\":true,\"number\":26},{\"clue\":\"Duplicate again\",\"answer\":\"RECOPY\",\"start-row\":11,\"start-col\":0,\"across?\":false,\"number\":23},{\"clue\":\"Impressionist Claude\",\"answer\":\"MONET\",\"start-row\":16,\"start-col\":12,\"across?\":true,\"number\":37},{\"clue\":\"Nagano honorific\",\"answer\":\"SAN\",\"start-row\":14,\"start-col\":2,\"across?\":true,\"number\":31},{\"clue\":\"Droid download\",\"answer\":\"APP\",\"start-row\":14,\"start-col\":3,\"across?\":false,\"number\":32},{\"clue\":\"Typing meas.\",\"answer\":\"WPM\",\"start-row\":16,\"start-col\":2,\"across?\":true,\"number\":36},{\"clue\":\"Cookie container\",\"answer\":\"TIN\",\"start-row\":14,\"start-col\":14,\"across?\":false,\"number\":34}],\"grid\":{\"0\":{\"0\":{\"letter\":\"C\",\"number\":1},\"1\":{\"letter\":\"O\"},\"2\":{\"letter\":\"M\",\"number\":2},\"3\":{\"letter\":\"B\"}},\"1\":{\"0\":{\"letter\":\"R\"},\"2\":{\"letter\":\"I\"},\"4\":{\"letter\":\"A\",\"number\":3},\"7\":{\"letter\":\"O\",\"number\":4}},\"2\":{\"0\":{\"letter\":\"I\"},\"2\":{\"letter\":\"A\",\"number\":5},\"3\":{\"letter\":\"T\"},\"4\":{\"letter\":\"R\"},\"5\":{\"letter\":\"I\"},\"6\":{\"letter\":\"F\"},\"7\":{\"letter\":\"L\"},\"8\":{\"letter\":\"E\"}},\"3\":{\"0\":{\"letter\":\"B\"},\"4\":{\"letter\":\"I\"},\"7\":{\"letter\":\"D\"},\"10\":{\"letter\":\"S\",\"number\":6}},\"4\":{\"1\":{\"letter\":\"D\",\"number\":7},\"2\":{\"letter\":\"E\"},\"3\":{\"letter\":\"E\"},\"4\":{\"letter\":\"D\"},\"7\":{\"letter\":\"S\",\"number\":8},\"8\":{\"letter\":\"E\"},\"9\":{\"letter\":\"A\"},\"10\":{\"letter\":\"T\"},\"11\":{\"letter\":\"E\"},\"12\":{\"letter\":\"D\",\"number\":9}},\"5\":{\"1\":{\"letter\":\"R\"},\"10\":{\"letter\":\"Y\"},\"12\":{\"letter\":\"O\"},\"14\":{\"letter\":\"E\",\"number\":10},\"16\":{\"letter\":\"R\",\"number\":11}},\"6\":{\"1\":{\"letter\":\"E\"},\"3\":{\"letter\":\"T\",\"number\":12},\"5\":{\"letter\":\"I\",\"number\":13},\"12\":{\"letter\":\"N\"},\"14\":{\"letter\":\"F\"},\"16\":{\"letter\":\"E\"}},\"7\":{\"1\":{\"letter\":\"A\",\"number\":14},\"2\":{\"letter\":\"L\"},\"3\":{\"letter\":\"I\"},\"4\":{\"letter\":\"G\"},\"5\":{\"letter\":\"N\"},\"6\":{\"letter\":\"S\"},\"8\":{\"letter\":\"B\",\"number\":15},\"10\":{\"letter\":\"E\",\"number\":16},\"12\":{\"letter\":\"A\"},\"14\":{\"letter\":\"F\"},\"16\":{\"letter\":\"E\"}},\"8\":{\"1\":{\"letter\":\"D\"},\"3\":{\"letter\":\"E\"},\"5\":{\"letter\":\"T\"},\"7\":{\"letter\":\"M\",\"number\":17},\"8\":{\"letter\":\"O\"},\"9\":{\"letter\":\"O\"},\"10\":{\"letter\":\"N\"},\"11\":{\"letter\":\"S\"},\"12\":{\"letter\":\"T\"},\"13\":{\"letter\":\"R\"},\"14\":{\"letter\":\"U\"},\"15\":{\"letter\":\"C\"},\"16\":{\"letter\":\"K\"}},\"9\":{\"3\":{\"letter\":\"S\",\"number\":18},\"4\":{\"letter\":\"P\",\"number\":19},\"5\":{\"letter\":\"O\"},\"6\":{\"letter\":\"T\"},\"8\":{\"letter\":\"B\"},\"10\":{\"letter\":\"S\"},\"12\":{\"letter\":\"I\"},\"14\":{\"letter\":\"S\"}},\"10\":{\"4\":{\"letter\":\"E\"},\"7\":{\"letter\":\"U\",\"number\":20},\"12\":{\"letter\":\"O\",\"number\":21},\"13\":{\"letter\":\"N\"},\"14\":{\"letter\":\"E\"},\"15\":{\"letter\":\"A\",\"number\":22}},\"11\":{\"0\":{\"letter\":\"R\",\"number\":23},\"2\":{\"letter\":\"S\",\"number\":24},\"3\":{\"letter\":\"E\"},\"4\":{\"letter\":\"A\"},\"5\":{\"letter\":\"S\"},\"7\":{\"letter\":\"G\"},\"9\":{\"letter\":\"A\",\"number\":25},\"10\":{\"letter\":\"T\"},\"11\":{\"letter\":\"T\"},\"12\":{\"letter\":\"N\"},\"15\":{\"letter\":\"N\"}},\"12\":{\"0\":{\"letter\":\"E\",\"number\":26},\"1\":{\"letter\":\"S\"},\"2\":{\"letter\":\"T\"},\"4\":{\"letter\":\"T\"},\"7\":{\"letter\":\"L\"},\"9\":{\"letter\":\"X\"},\"13\":{\"letter\":\"R\",\"number\":27},\"14\":{\"letter\":\"A\"},\"15\":{\"letter\":\"G\"},\"16\":{\"letter\":\"E\"}},\"13\":{\"0\":{\"letter\":\"C\"},\"2\":{\"letter\":\"E\"},\"6\":{\"letter\":\"A\",\"number\":28},\"7\":{\"letter\":\"I\"},\"8\":{\"letter\":\"D\"},\"9\":{\"letter\":\"E\"},\"10\":{\"letter\":\"D\",\"number\":29},\"12\":{\"letter\":\"M\",\"number\":30},\"15\":{\"letter\":\"L\"}},\"14\":{\"0\":{\"letter\":\"O\"},\"2\":{\"letter\":\"S\",\"number\":31},\"3\":{\"letter\":\"A\",\"number\":32},\"4\":{\"letter\":\"N\"},\"6\":{\"letter\":\"L\"},\"10\":{\"letter\":\"R\"},\"12\":{\"letter\":\"A\",\"number\":33},\"13\":{\"letter\":\"C\"},\"14\":{\"letter\":\"T\",\"number\":34},\"15\":{\"letter\":\"E\"}},\"15\":{\"0\":{\"letter\":\"P\"},\"3\":{\"letter\":\"P\"},\"5\":{\"letter\":\"H\",\"number\":35},\"6\":{\"letter\":\"A\"},\"7\":{\"letter\":\"C\"},\"8\":{\"letter\":\"I\"},\"9\":{\"letter\":\"E\"},\"10\":{\"letter\":\"N\"},\"11\":{\"letter\":\"D\"},\"12\":{\"letter\":\"A\"},\"14\":{\"letter\":\"I\"}},\"16\":{\"0\":{\"letter\":\"Y\"},\"2\":{\"letter\":\"W\",\"number\":36},\"3\":{\"letter\":\"P\"},\"4\":{\"letter\":\"M\"},\"10\":{\"letter\":\"O\"},\"12\":{\"letter\":\"M\",\"number\":37},\"13\":{\"letter\":\"O\"},\"14\":{\"letter\":\"N\"},\"15\":{\"letter\":\"E\"},\"16\":{\"letter\":\"T\"}}}}"
)

(def puz (-> (->> puzzle-test (.parse js/JSON)) (js->clj :keywordize-keys true)))

(defonce puzzle-test-atom (r/atom puz))

(defn class-list [classes]
  (join " "
        (for [[class include?] classes
              :when include?]
          class)))

(defn build-square [col row]
  {:col col :row row})

(defn build-cursor [square across?]
  {:square square :across? across?})

(defonce puzzle-atom (r/atom {}))
(defonce cursor-atom (r/atom (build-cursor nil true)))
(defonce cell-position-atom (r/atom {}))

(defn get-clue-id [clue]
  (str (:number clue) "-" (if (:across? clue) "across" "down")))

(defn squares-in-word [clue]
  (let [across? (:across? clue)
        length (count (:answer clue))
        start-row (:start-row clue)
        start-col (:start-col clue)
        row-count (if across? 1 length)
        col-count (if across? length 1)]
    (for [row (range start-row (+ start-row row-count))
          col (range start-col (+ start-col col-count))]
      (build-square col row))))

(defn square-in-word? [square clue]
  (contains? (set (squares-in-word clue)) square))

(defn words-containing-square-inner [square clues]
  (filter #(square-in-word? square %) clues))

(def words-containing-square
  (memoize words-containing-square-inner))

(defn selected-word [cursor clues]
  (->> (words-containing-square (:square cursor) clues)
       (filter #(= (:across? %) (:across? cursor)))
       (first)))
     
(defn user-input [cursor clues game-state]
  (map #(get game-state %) (squares-in-word (selected-word cursor clues))))

(defn direction-allowed? [cursor clues]
  (some #(= (:across? cursor) (:across? %))
        (words-containing-square (:square cursor) clues)))

(defn word-correct? [clue game-state]
  (let [squares (squares-in-word clue)
        squares-with-correct-letters (map vector squares (:answer clue))]
    #_(doseq [[square letter] squares-with-correct-letters] (log (= (lower-case letter) (get-in game-state [(keyword (u/marshal-square square)) :letter]))))
    (every? (fn [[square correct-letter]] (= (get-in game-state [(keyword (u/marshal-square square)) :letter]) (lower-case correct-letter)))
            squares-with-correct-letters)))

(defn square-correct? [square clues game-state]
  (some #(word-correct? % game-state) (words-containing-square square clues)))

(defn puzzle-complete? [puzzle game-state]
  (every? #(word-correct? % game-state) (:clues puzzle)))

(defn focus-input []
    (->> 100 (js/setTimeout (fn [] (.focus (.getElementById js/document "word-input"))))))

(defn blur-input []
  (let [input (.getElementById js/document "word-input")]
       (.blur input)))

(defn update-cursor [square clues]
  (let [old-cursor @cursor-atom
        same-location (= square (:square old-cursor))
        old-across? (:across? old-cursor)
        flipped-across? (if same-location (not old-across?) old-across?)
        new-across? (if (direction-allowed? (build-cursor square flipped-across?) clues)
                      flipped-across? (not flipped-across?))
        cell (.getBoundingClientRect (.getElementById js/document (str (:col square) (:row square))))]
    (reset! cell-position-atom {:top (aget cell "top") 
                                :right (aget cell "right")
                                :left (aget cell "left")
                                :bottom (aget cell "bottom")})
    (focus-input)
    (reset! cursor-atom (build-cursor square new-across?))))

(defn valid-cursor-position? [square grid]
  (let [row (:row square)
        col (:col square)
        grid-keys (map #(keyword (str %)) [row col])]
    (not (nil? (get-in grid grid-keys)))))

(defn transform-cursor [col-transform row-transform cursor puzzle]
  (let [grid (:grid puzzle)
        clues (:clues puzzle)
        row (:row (:square cursor))
        col (:col (:square cursor))
        across? (:across? cursor)
        new-square (build-square (col-transform col)
                                 (row-transform row))
        new-across? (if (direction-allowed? (build-cursor new-square across?) clues)
                      across? (not across?))]
    (if (valid-cursor-position? new-square grid)
      (build-cursor new-square new-across?)
      (do
        (blur-input)
        (build-cursor {:col -1 :row -1} false)))))

(defn next-cursor [cursor puzzle]
  (let [across? (:across? cursor)
        row-transform (if across? identity inc)
        col-transform (if across? inc identity)]
    (transform-cursor col-transform row-transform cursor puzzle)))

(defn prev-cursor [cursor puzzle]
  (let [across? (:across? cursor)
        row-transform (if across? identity dec)
        col-transform (if across? dec identity)]
    (transform-cursor col-transform row-transform cursor puzzle)))

(defn get-theme [user-id]
  (let [ulist (subscribe [:user-list])]
       (get-in @ulist [(keyword user-id) :color-scheme])))
     
(defn get-styles [theme]
    (get c/colors (keyword theme)))

(defn handle-change [e]
  (let [puzzle @puzzle-atom
        game-state (subscribe [:game-state])
        current-user (subscribe [:user])
        cursor @cursor-atom
        clues (:clues puzzle)
        prev-word (join " " (user-input cursor clues @game-state))
        word (.. e -target -value)
        cur-square (:square cursor)]
    (.preventDefault e)
    (if (> (count word) (count prev-word))
      (if (re-matches #"^[A-z]+$" (join "" (last word)))
        (do 
          (swap! cursor-atom next-cursor puzzle)
          (dispatch [:send-move [cur-square (last word) @current-user]])))
      (do 
        (swap! cursor-atom prev-cursor puzzle)
        (dispatch [:send-move [cur-square nil nil]])))))
      
(defn crossword-table [puzzle cursor game-state]
  (let [grid (:grid puzzle)
        clues (:clues puzzle)
        grid-size (:grid-size puzzle)
        active-word (selected-word cursor clues)]
    (defn crossword-table-cell [col-idx row-idx cell]
      (if (nil? cell)
        [:div.cell.cell--empty]
        (let [square (build-square col-idx row-idx)
              classes (class-list {"selected" (= square (:square cursor))
                                   "active" (square-in-word? square active-word)
                                   "correct" (square-correct? square clues game-state)})
              click-handler #(update-cursor square clues)
              styles (get-styles (get-theme (get-in game-state [(keyword (u/marshal-square square)) :user :id])))]
          [:div.cell {:id (str col-idx row-idx) :on-click click-handler :class classes :style styles}
           (when-let [letter (get-in game-state [(keyword (u/marshal-square square)) :letter])] [:span letter])
           (if-let [number (:number cell)]
             [:span.clue-number number])])))
    (defn crossword-table-row [row-idx row]
      (let [cells (->> (range 0 grid-size)
                       (map #(get row (keyword (str %)))))]
        [:div.row
         (for [[idx cell] (map-indexed vector cells)]
           ^{:key idx} [crossword-table-cell idx row-idx cell])]))
    (let [rows (->> (range 0 grid-size)
                    (map #(get grid (keyword (str %)))))
          cell-position @cell-position-atom]
      [:div
       [:input {:id "word-input"
                :style {:z-index "-9999" 
                        :position "absolute" 
                        :top (+ (:top cell-position) js/window.scrollY) :left (+ (:left cell-position) js/window.scrollX)
                        :border "none" :outline "none" 
                        :color "transparent"
                        :width 1 :height 1}
                :value (user-input cursor clues game-state)
                :on-change handle-change}]
       [:div#crossword-table
         (if (puzzle-complete? puzzle game-state)
           [:h3.f2.tc.mt0 "Congratulations, you win!"])
        (for [[idx row] (map-indexed vector rows)]
          ^{:key idx} [crossword-table-row idx row])]])))

(defn crossword-clue [puzzle cursor]
  (let [clue (selected-word cursor (:clues puzzle))
        clue-number (:number clue)
        clue-text (:clue clue)
        clue-length (count (:answer clue))
        id (get-clue-id clue)]
    (if clue
        [:div.clue [:p {:id id} (str clue-number ". " clue-text)]]
        [:div.clue [:p " "]])))

(defn crossword-player [puzzle-atom-input]
  (let [puzzle @puzzle-atom-input
        cursor @cursor-atom
        game-state (subscribe [:game-state])
        user-list (subscribe [:user-list])]
    (reset! puzzle-atom puzzle)
    (if (nil? puzzle)
      [:p "Loading..."]
      [:div.crossword-player
        #_[:div.tc 
         [:h3 "Players: "]
         (for [user (vals @user-list)]
            ^{:key user} [:p.f5 (str (:id user) " " (:color-scheme user))])]
        [crossword-clue puzzle cursor]
        [crossword-table puzzle cursor @game-state]
        [cp/main]
       ])))

(defn main []
  (fn []
    (crossword-player puzzle-test-atom)))
