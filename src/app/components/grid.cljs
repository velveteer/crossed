(ns app.components.grid
  (:require-macros [app.logging :refer [log]])
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [clojure.string :refer [join]]
            [app.util :as u]
            [app.colors :as c]
            [app.components.color-picker :as cp]))

(def puzzle-test
  "{\"id\":\"231121\",\"grid-size\":17,\"words\":33,\"clues\":[{\"clue\":\"a means of preserving from harm or unpleasantness\",\"answer\":\"salvation\",\"start-row\":0,\"start-col\":0,\"across?\":true,\"number\":1},{\"clue\":\"sawbuck\",\"answer\":\"tendollarll\",\"start-row\":0,\"start-col\":5,\"across?\":false,\"number\":3},{\"clue\":\"a mixture containing two or more metallic elements\",\"answer\":\"alloy\",\"start-row\":0,\"start-col\":1,\"across?\":false,\"number\":2},{\"clue\":\"the recipient of funds or other advantages\",\"answer\":\"beneficiary\",\"start-row\":2,\"start-col\":3,\"across?\":true,\"number\":7},{\"clue\":\"imagine; conceive of; see in one's mind\",\"answer\":\"fancy\",\"start-row\":7,\"start-col\":4,\"across?\":true,\"number\":16},{\"clue\":\"a dispute where there is strong disagreement\",\"answer\":\"controversy\",\"start-row\":4,\"start-col\":4,\"across?\":true,\"number\":10},{\"clue\":\"widely known and esteemed\",\"answer\":\"illustrious\",\"start-row\":9,\"start-col\":4,\"across?\":true,\"number\":18},{\"clue\":\"a well-substantiated explanation of some aspect of the world\",\"answer\":\"theory\",\"start-row\":9,\"start-col\":9,\"across?\":false,\"number\":19},{\"clue\":\"having or exerting a malignant influence\",\"answer\":\"malevolent\",\"start-row\":11,\"start-col\":6,\"across?\":true,\"number\":22},{\"clue\":\"having no definite form or distinct shape\",\"answer\":\"amorphous\",\"start-row\":13,\"start-col\":6,\"across?\":true,\"number\":26},{\"clue\":\"praise enthusiastically\",\"answer\":\"rave\",\"start-row\":1,\"start-col\":11,\"across?\":false,\"number\":5},{\"clue\":\"a safe place\",\"answer\":\"refuge\",\"start-row\":6,\"start-col\":13,\"across?\":false,\"number\":15},{\"clue\":\"take action in return for a perceived wrong\",\"answer\":\"avenge\",\"start-row\":7,\"start-col\":11,\"across?\":true,\"number\":17},{\"clue\":\"the act of gratifying a desire\",\"answer\":\"indulgence\",\"start-row\":1,\"start-col\":16,\"across?\":false,\"number\":6},{\"clue\":\"what may precede itself\",\"answer\":\"unto\",\"start-row\":13,\"start-col\":13,\"across?\":false,\"number\":27},{\"clue\":\"the proper or conventional behavior on some solemn occasion\",\"answer\":\"ceremony\",\"start-row\":16,\"start-col\":8,\"across?\":true,\"number\":31},{\"clue\":\"bran bit\",\"answer\":\"oat\",\"start-row\":4,\"start-col\":9,\"across?\":false,\"number\":11},{\"clue\":\"respectful address\",\"answer\":\"maam\",\"start-row\":11,\"start-col\":6,\"across?\":false,\"number\":22},{\"clue\":\"irobot vacuum\",\"answer\":\"roomba\",\"start-row\":12,\"start-col\":1,\"across?\":true,\"number\":24},{\"clue\":\"run easily\",\"answer\":\"lope\",\"start-row\":11,\"start-col\":2,\"across?\":false,\"number\":20},{\"clue\":\"genesis garden\",\"answer\":\"eden\",\"start-row\":14,\"start-col\":0,\"across?\":true,\"number\":28},{\"clue\":\"try to manage without help\",\"answer\":\"fend\",\"start-row\":13,\"start-col\":0,\"across?\":false,\"number\":25},{\"clue\":\"honolulu's isle\",\"answer\":\"oahu\",\"start-row\":11,\"start-col\":11,\"across?\":false,\"number\":23},{\"clue\":\"easily handled or managed\",\"answer\":\"docile\",\"start-row\":16,\"start-col\":0,\"across?\":true,\"number\":30},{\"clue\":\"opposing vote\",\"answer\":\"nay\",\"start-row\":0,\"start-col\":13,\"across?\":false,\"number\":4},{\"clue\":\"suffix with peace or neat\",\"answer\":\"nik\",\"start-row\":0,\"start-col\":13,\"across?\":true,\"number\":4},{\"clue\":\"sock section\",\"answer\":\"toe\",\"start-row\":3,\"start-col\":0,\"across?\":true,\"number\":8},{\"clue\":\"showing marked and often playful evasiveness or reluctance\",\"answer\":\"coy\",\"start-row\":14,\"start-col\":15,\"across?\":false,\"number\":29},{\"clue\":\"oparty up (up in here)o singer\",\"answer\":\"dmx\",\"start-row\":11,\"start-col\":4,\"across?\":false,\"number\":21},{\"clue\":\"freedom org.\",\"answer\":\"aclu\",\"start-row\":6,\"start-col\":7,\"across?\":false,\"number\":12},{\"clue\":\"soft watches painter\",\"answer\":\"dali\",\"start-row\":6,\"start-col\":11,\"across?\":false,\"number\":14},{\"clue\":\"answer from an ensign\",\"answer\":\"aye\",\"start-row\":3,\"start-col\":14,\"across?\":false,\"number\":9},{\"clue\":\"actor danson\",\"answer\":\"ted\",\"start-row\":6,\"start-col\":9,\"across?\":true,\"number\":13}],\"grid\":{\"0\":{\"0\":{\"letter\":\"s\",\"number\":1},\"1\":{\"letter\":\"a\",\"number\":2},\"2\":{\"letter\":\"l\"},\"3\":{\"letter\":\"v\"},\"4\":{\"letter\":\"a\"},\"5\":{\"letter\":\"t\",\"number\":3},\"6\":{\"letter\":\"i\"},\"7\":{\"letter\":\"o\"},\"8\":{\"letter\":\"n\"},\"13\":{\"letter\":\"n\",\"number\":4},\"14\":{\"letter\":\"i\"},\"15\":{\"letter\":\"k\"}},\"1\":{\"1\":{\"letter\":\"l\"},\"5\":{\"letter\":\"e\"},\"11\":{\"letter\":\"r\",\"number\":5},\"13\":{\"letter\":\"a\"},\"16\":{\"letter\":\"i\",\"number\":6}},\"2\":{\"1\":{\"letter\":\"l\"},\"3\":{\"letter\":\"b\",\"number\":7},\"4\":{\"letter\":\"e\"},\"5\":{\"letter\":\"n\"},\"6\":{\"letter\":\"e\"},\"7\":{\"letter\":\"f\"},\"8\":{\"letter\":\"i\"},\"9\":{\"letter\":\"c\"},\"10\":{\"letter\":\"i\"},\"11\":{\"letter\":\"a\"},\"12\":{\"letter\":\"r\"},\"13\":{\"letter\":\"y\"},\"16\":{\"letter\":\"n\"}},\"3\":{\"0\":{\"letter\":\"t\",\"number\":8},\"1\":{\"letter\":\"o\"},\"2\":{\"letter\":\"e\"},\"5\":{\"letter\":\"d\"},\"11\":{\"letter\":\"v\"},\"14\":{\"letter\":\"a\",\"number\":9},\"16\":{\"letter\":\"d\"}},\"4\":{\"1\":{\"letter\":\"y\"},\"4\":{\"letter\":\"c\",\"number\":10},\"5\":{\"letter\":\"o\"},\"6\":{\"letter\":\"n\"},\"7\":{\"letter\":\"t\"},\"8\":{\"letter\":\"r\"},\"9\":{\"letter\":\"o\",\"number\":11},\"10\":{\"letter\":\"v\"},\"11\":{\"letter\":\"e\"},\"12\":{\"letter\":\"r\"},\"13\":{\"letter\":\"s\"},\"14\":{\"letter\":\"y\"},\"16\":{\"letter\":\"u\"}},\"5\":{\"5\":{\"letter\":\"l\"},\"9\":{\"letter\":\"a\"},\"14\":{\"letter\":\"e\"},\"16\":{\"letter\":\"l\"}},\"6\":{\"5\":{\"letter\":\"l\"},\"7\":{\"letter\":\"a\",\"number\":12},\"9\":{\"letter\":\"t\",\"number\":13},\"10\":{\"letter\":\"e\"},\"11\":{\"letter\":\"d\",\"number\":14},\"13\":{\"letter\":\"r\",\"number\":15},\"16\":{\"letter\":\"g\"}},\"7\":{\"4\":{\"letter\":\"f\",\"number\":16},\"5\":{\"letter\":\"a\"},\"6\":{\"letter\":\"n\"},\"7\":{\"letter\":\"c\"},\"8\":{\"letter\":\"y\"},\"11\":{\"letter\":\"a\",\"number\":17},\"12\":{\"letter\":\"v\"},\"13\":{\"letter\":\"e\"},\"14\":{\"letter\":\"n\"},\"15\":{\"letter\":\"g\"},\"16\":{\"letter\":\"e\"}},\"8\":{\"5\":{\"letter\":\"r\"},\"7\":{\"letter\":\"l\"},\"11\":{\"letter\":\"l\"},\"13\":{\"letter\":\"f\"},\"16\":{\"letter\":\"n\"}},\"9\":{\"4\":{\"letter\":\"i\",\"number\":18},\"5\":{\"letter\":\"l\"},\"6\":{\"letter\":\"l\"},\"7\":{\"letter\":\"u\"},\"8\":{\"letter\":\"s\"},\"9\":{\"letter\":\"t\",\"number\":19},\"10\":{\"letter\":\"r\"},\"11\":{\"letter\":\"i\"},\"12\":{\"letter\":\"o\"},\"13\":{\"letter\":\"u\"},\"14\":{\"letter\":\"s\"},\"16\":{\"letter\":\"c\"}},\"10\":{\"5\":{\"letter\":\"l\"},\"9\":{\"letter\":\"h\"},\"13\":{\"letter\":\"g\"},\"16\":{\"letter\":\"e\"}},\"11\":{\"2\":{\"letter\":\"l\",\"number\":20},\"4\":{\"letter\":\"d\",\"number\":21},\"6\":{\"letter\":\"m\",\"number\":22},\"7\":{\"letter\":\"a\"},\"8\":{\"letter\":\"l\"},\"9\":{\"letter\":\"e\"},\"10\":{\"letter\":\"v\"},\"11\":{\"letter\":\"o\",\"number\":23},\"12\":{\"letter\":\"l\"},\"13\":{\"letter\":\"e\"},\"14\":{\"letter\":\"n\"},\"15\":{\"letter\":\"t\"}},\"12\":{\"1\":{\"letter\":\"r\",\"number\":24},\"2\":{\"letter\":\"o\"},\"3\":{\"letter\":\"o\"},\"4\":{\"letter\":\"m\"},\"5\":{\"letter\":\"b\"},\"6\":{\"letter\":\"a\"},\"9\":{\"letter\":\"o\"},\"11\":{\"letter\":\"a\"}},\"13\":{\"0\":{\"letter\":\"f\",\"number\":25},\"2\":{\"letter\":\"p\"},\"4\":{\"letter\":\"x\"},\"6\":{\"letter\":\"a\",\"number\":26},\"7\":{\"letter\":\"m\"},\"8\":{\"letter\":\"o\"},\"9\":{\"letter\":\"r\"},\"10\":{\"letter\":\"p\"},\"11\":{\"letter\":\"h\"},\"12\":{\"letter\":\"o\"},\"13\":{\"letter\":\"u\",\"number\":27},\"14\":{\"letter\":\"s\"}},\"14\":{\"0\":{\"letter\":\"e\",\"number\":28},\"1\":{\"letter\":\"d\"},\"2\":{\"letter\":\"e\"},\"3\":{\"letter\":\"n\"},\"6\":{\"letter\":\"m\"},\"9\":{\"letter\":\"y\"},\"11\":{\"letter\":\"u\"},\"13\":{\"letter\":\"n\"},\"15\":{\"letter\":\"c\",\"number\":29}},\"15\":{\"0\":{\"letter\":\"n\"},\"13\":{\"letter\":\"t\"},\"15\":{\"letter\":\"o\"}},\"16\":{\"0\":{\"letter\":\"d\",\"number\":30},\"1\":{\"letter\":\"o\"},\"2\":{\"letter\":\"c\"},\"3\":{\"letter\":\"i\"},\"4\":{\"letter\":\"l\"},\"5\":{\"letter\":\"e\"},\"8\":{\"letter\":\"c\",\"number\":31},\"9\":{\"letter\":\"e\"},\"10\":{\"letter\":\"r\"},\"11\":{\"letter\":\"e\"},\"12\":{\"letter\":\"m\"},\"13\":{\"letter\":\"o\"},\"14\":{\"letter\":\"n\"},\"15\":{\"letter\":\"y\"}}}}"
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
    (every? (fn [[square correct-letter]] (= (get game-state (keyword (u/marshal-square square))) correct-letter))
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
    (get-styles (get-theme (:id @current-user)))
    (if (> (count word) (count prev-word))
      (do 
        (swap! cursor-atom next-cursor puzzle)
        (dispatch [:send-move [cur-square (last word) @current-user]]))
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
                                   "active" (square-in-word? square active-word)})
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
        [:div.tc 
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
