(ns app.components.grid
  (:require-macros [app.logging :refer [log]])
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [clojure.string :refer [join]]
            [app.util :as u]
            [app.colors :as c]
            [app.components.color-picker :as cp]))

(def puzzle-test
  "{\"id\":\"23145745\",\"grid-size\":17,\"words\":36,\"clues\":[{\"clue\":\"a person who pleads for a person, cause, or idea\",\"answer\":\"advocate\",\"start-row\":0,\"start-col\":0,\"across?\":true,\"number\":1},{\"clue\":\"an inoffensive expression substituted for an offensive one\",\"answer\":\"euphemism\",\"start-row\":0,\"start-col\":7,\"across?\":false,\"number\":4},{\"clue\":\"without exaggeration\",\"answer\":\"literally\",\"start-row\":4,\"start-col\":4,\"across?\":true,\"number\":11},{\"clue\":\"a small boat propelled by oars or by sails or by a motor\",\"answer\":\"skiff\",\"start-row\":6,\"start-col\":5,\"across?\":true,\"number\":16},{\"clue\":\"completely acceptable; not open to reproach\",\"answer\":\"unexceptionable\",\"start-row\":2,\"start-col\":1,\"across?\":true,\"number\":8},{\"clue\":\"the state when one person or group has power over another\",\"answer\":\"dominance\",\"start-row\":8,\"start-col\":5,\"across?\":true,\"number\":20},{\"clue\":\"salad ingredient\",\"answer\":\"endive\",\"start-row\":2,\"start-col\":15,\"across?\":false,\"number\":9},{\"clue\":\"family car\",\"answer\":\"wagon\",\"start-row\":7,\"start-col\":10,\"across?\":false,\"number\":18},{\"clue\":\"convoy trucks\",\"answer\":\"semis\",\"start-row\":7,\"start-col\":13,\"across?\":false,\"number\":19},{\"clue\":\"make reference to\",\"answer\":\"cite\",\"start-row\":10,\"start-col\":12,\"across?\":true,\"number\":24},{\"clue\":\"give or supply\",\"answer\":\"render\",\"start-row\":9,\"start-col\":15,\"across?\":false,\"number\":22},{\"clue\":\"examine minutely\",\"answer\":\"scour\",\"start-row\":14,\"start-col\":11,\"across?\":true,\"number\":32},{\"clue\":\"something that serves to suggest\",\"answer\":\"indication\",\"start-row\":11,\"start-col\":1,\"across?\":true,\"number\":25},{\"clue\":\"an ornament consisting of a horizontal sculptured band\",\"answer\":\"frieze\",\"start-row\":9,\"start-col\":1,\"across?\":false,\"number\":21},{\"clue\":\"2005 nicole kidman film\",\"answer\":\"bewitched\",\"start-row\":14,\"start-col\":0,\"across?\":true,\"number\":31},{\"clue\":\"top level in the draft\",\"answer\":\"onea\",\"start-row\":0,\"start-col\":3,\"across?\":false,\"number\":2},{\"clue\":\"how foods are often fried\",\"answer\":\"inoil\",\"start-row\":0,\"start-col\":10,\"across?\":false,\"number\":6},{\"clue\":\"make an effort or attempt\",\"answer\":\"assay\",\"start-row\":12,\"start-col\":11,\"across?\":false,\"number\":27},{\"clue\":\"a jaunty rhythm in music\",\"answer\":\"lilt\",\"start-row\":13,\"start-col\":3,\"across?\":false,\"number\":29},{\"clue\":\"a trite or obvious remark\",\"answer\":\"platitude\",\"start-row\":16,\"start-col\":0,\"across?\":true,\"number\":33},{\"clue\":\"looks at closely\",\"answer\":\"eyes\",\"start-row\":16,\"start-col\":10,\"across?\":true,\"number\":34},{\"clue\":\"artificially formal\",\"answer\":\"stilted\",\"start-row\":5,\"start-col\":3,\"across?\":false,\"number\":14},{\"clue\":\"decrees\",\"answer\":\"acts\",\"start-row\":5,\"start-col\":0,\"across?\":true,\"number\":13},{\"clue\":\"longing\",\"answer\":\"yen\",\"start-row\":4,\"start-col\":12,\"across?\":false,\"number\":12},{\"clue\":\"marked by prudence or modesty and wise self-restraint\",\"answer\":\"discreet\",\"start-row\":0,\"start-col\":9,\"across?\":true,\"number\":5},{\"clue\":\"opposition position\",\"answer\":\"anti\",\"start-row\":7,\"start-col\":0,\"across?\":true,\"number\":17},{\"clue\":\"answer angrily\",\"answer\":\"snapat\",\"start-row\":3,\"start-col\":0,\"across?\":false,\"number\":10},{\"clue\":\"buffy's weapon\",\"answer\":\"stake\",\"start-row\":10,\"start-col\":7,\"across?\":false,\"number\":23},{\"clue\":\"suffix with peace or neat\",\"answer\":\"nik\",\"start-row\":5,\"start-col\":14,\"across?\":true,\"number\":15},{\"clue\":\"wails\",\"answer\":\"sobs\",\"start-row\":13,\"start-col\":13,\"across?\":false,\"number\":30},{\"clue\":\"mork's birthplace, on tv\",\"answer\":\"ork\",\"start-row\":11,\"start-col\":9,\"across?\":false,\"number\":26},{\"clue\":\"irs employee\",\"answer\":\"cpa\",\"start-row\":0,\"start-col\":12,\"across?\":false,\"number\":7},{\"clue\":\"scotch and __ (bar drink)\",\"answer\":\"soda\",\"start-row\":6,\"start-col\":5,\"across?\":false,\"number\":16},{\"clue\":\"on ___ (without a contract)\",\"answer\":\"spec\",\"start-row\":7,\"start-col\":13,\"across?\":true,\"number\":19},{\"clue\":\"a continuous portion of a circle\",\"answer\":\"arc\",\"start-row\":0,\"start-col\":5,\"across?\":false,\"number\":3},{\"clue\":\"bouncers read them\",\"answer\":\"ids\",\"start-row\":12,\"start-col\":14,\"across?\":true,\"number\":28}],\"grid\":{\"0\":{\"0\":{\"letter\":\"a\",\"number\":1},\"1\":{\"letter\":\"d\"},\"2\":{\"letter\":\"v\"},\"3\":{\"letter\":\"o\",\"number\":2},\"4\":{\"letter\":\"c\"},\"5\":{\"letter\":\"a\",\"number\":3},\"6\":{\"letter\":\"t\"},\"7\":{\"letter\":\"e\",\"number\":4},\"9\":{\"letter\":\"d\",\"number\":5},\"10\":{\"letter\":\"i\",\"number\":6},\"11\":{\"letter\":\"s\"},\"12\":{\"letter\":\"c\",\"number\":7},\"13\":{\"letter\":\"r\"},\"14\":{\"letter\":\"e\"},\"15\":{\"letter\":\"e\"},\"16\":{\"letter\":\"t\"}},\"1\":{\"3\":{\"letter\":\"n\"},\"5\":{\"letter\":\"r\"},\"7\":{\"letter\":\"u\"},\"10\":{\"letter\":\"n\"},\"12\":{\"letter\":\"p\"}},\"2\":{\"1\":{\"letter\":\"u\",\"number\":8},\"2\":{\"letter\":\"n\"},\"3\":{\"letter\":\"e\"},\"4\":{\"letter\":\"x\"},\"5\":{\"letter\":\"c\"},\"6\":{\"letter\":\"e\"},\"7\":{\"letter\":\"p\"},\"8\":{\"letter\":\"t\"},\"9\":{\"letter\":\"i\"},\"10\":{\"letter\":\"o\"},\"11\":{\"letter\":\"n\"},\"12\":{\"letter\":\"a\"},\"13\":{\"letter\":\"b\"},\"14\":{\"letter\":\"l\"},\"15\":{\"letter\":\"e\",\"number\":9}},\"3\":{\"0\":{\"letter\":\"s\",\"number\":10},\"3\":{\"letter\":\"a\"},\"7\":{\"letter\":\"h\"},\"10\":{\"letter\":\"i\"},\"15\":{\"letter\":\"n\"}},\"4\":{\"0\":{\"letter\":\"n\"},\"4\":{\"letter\":\"l\",\"number\":11},\"5\":{\"letter\":\"i\"},\"6\":{\"letter\":\"t\"},\"7\":{\"letter\":\"e\"},\"8\":{\"letter\":\"r\"},\"9\":{\"letter\":\"a\"},\"10\":{\"letter\":\"l\"},\"11\":{\"letter\":\"l\"},\"12\":{\"letter\":\"y\",\"number\":12},\"15\":{\"letter\":\"d\"}},\"5\":{\"0\":{\"letter\":\"a\",\"number\":13},\"1\":{\"letter\":\"c\"},\"2\":{\"letter\":\"t\"},\"3\":{\"letter\":\"s\",\"number\":14},\"7\":{\"letter\":\"m\"},\"12\":{\"letter\":\"e\"},\"14\":{\"letter\":\"n\",\"number\":15},\"15\":{\"letter\":\"i\"},\"16\":{\"letter\":\"k\"}},\"6\":{\"0\":{\"letter\":\"p\"},\"3\":{\"letter\":\"t\"},\"5\":{\"letter\":\"s\",\"number\":16},\"6\":{\"letter\":\"k\"},\"7\":{\"letter\":\"i\"},\"8\":{\"letter\":\"f\"},\"9\":{\"letter\":\"f\"},\"12\":{\"letter\":\"n\"},\"15\":{\"letter\":\"v\"}},\"7\":{\"0\":{\"letter\":\"a\",\"number\":17},\"1\":{\"letter\":\"n\"},\"2\":{\"letter\":\"t\"},\"3\":{\"letter\":\"i\"},\"5\":{\"letter\":\"o\"},\"7\":{\"letter\":\"s\"},\"10\":{\"letter\":\"w\",\"number\":18},\"13\":{\"letter\":\"s\",\"number\":19},\"14\":{\"letter\":\"p\"},\"15\":{\"letter\":\"e\"},\"16\":{\"letter\":\"c\"}},\"8\":{\"0\":{\"letter\":\"t\"},\"3\":{\"letter\":\"l\"},\"5\":{\"letter\":\"d\",\"number\":20},\"6\":{\"letter\":\"o\"},\"7\":{\"letter\":\"m\"},\"8\":{\"letter\":\"i\"},\"9\":{\"letter\":\"n\"},\"10\":{\"letter\":\"a\"},\"11\":{\"letter\":\"n\"},\"12\":{\"letter\":\"c\"},\"13\":{\"letter\":\"e\"}},\"9\":{\"1\":{\"letter\":\"f\",\"number\":21},\"3\":{\"letter\":\"t\"},\"5\":{\"letter\":\"a\"},\"10\":{\"letter\":\"g\"},\"13\":{\"letter\":\"m\"},\"15\":{\"letter\":\"r\",\"number\":22}},\"10\":{\"1\":{\"letter\":\"r\"},\"3\":{\"letter\":\"e\"},\"7\":{\"letter\":\"s\",\"number\":23},\"10\":{\"letter\":\"o\"},\"12\":{\"letter\":\"c\",\"number\":24},\"13\":{\"letter\":\"i\"},\"14\":{\"letter\":\"t\"},\"15\":{\"letter\":\"e\"}},\"11\":{\"1\":{\"letter\":\"i\",\"number\":25},\"2\":{\"letter\":\"n\"},\"3\":{\"letter\":\"d\"},\"4\":{\"letter\":\"i\"},\"5\":{\"letter\":\"c\"},\"6\":{\"letter\":\"a\"},\"7\":{\"letter\":\"t\"},\"8\":{\"letter\":\"i\"},\"9\":{\"letter\":\"o\",\"number\":26},\"10\":{\"letter\":\"n\"},\"13\":{\"letter\":\"s\"},\"15\":{\"letter\":\"n\"}},\"12\":{\"1\":{\"letter\":\"e\"},\"7\":{\"letter\":\"a\"},\"9\":{\"letter\":\"r\"},\"11\":{\"letter\":\"a\",\"number\":27},\"14\":{\"letter\":\"i\",\"number\":28},\"15\":{\"letter\":\"d\"},\"16\":{\"letter\":\"s\"}},\"13\":{\"1\":{\"letter\":\"z\"},\"3\":{\"letter\":\"l\",\"number\":29},\"7\":{\"letter\":\"k\"},\"9\":{\"letter\":\"k\"},\"11\":{\"letter\":\"s\"},\"13\":{\"letter\":\"s\",\"number\":30},\"15\":{\"letter\":\"e\"}},\"14\":{\"0\":{\"letter\":\"b\",\"number\":31},\"1\":{\"letter\":\"e\"},\"2\":{\"letter\":\"w\"},\"3\":{\"letter\":\"i\"},\"4\":{\"letter\":\"t\"},\"5\":{\"letter\":\"c\"},\"6\":{\"letter\":\"h\"},\"7\":{\"letter\":\"e\"},\"8\":{\"letter\":\"d\"},\"11\":{\"letter\":\"s\",\"number\":32},\"12\":{\"letter\":\"c\"},\"13\":{\"letter\":\"o\"},\"14\":{\"letter\":\"u\"},\"15\":{\"letter\":\"r\"}},\"15\":{\"3\":{\"letter\":\"l\"},\"11\":{\"letter\":\"a\"},\"13\":{\"letter\":\"b\"}},\"16\":{\"0\":{\"letter\":\"p\",\"number\":33},\"1\":{\"letter\":\"l\"},\"2\":{\"letter\":\"a\"},\"3\":{\"letter\":\"t\"},\"4\":{\"letter\":\"i\"},\"5\":{\"letter\":\"t\"},\"6\":{\"letter\":\"u\"},\"7\":{\"letter\":\"d\"},\"8\":{\"letter\":\"e\"},\"10\":{\"letter\":\"e\",\"number\":34},\"11\":{\"letter\":\"y\"},\"12\":{\"letter\":\"e\"},\"13\":{\"letter\":\"s\"}}}}"
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
