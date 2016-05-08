(ns app.components.grid
  (:require [reagent.core :as r]
            [goog.events :as events]
            [clojure.string :refer [join]])
  (:import [goog.events KeyHandler]
           [goog.events.KeyHandler EventType]))

(def puzzle-test
"{\"id\":\"620876\",\"grid-size\":15,\"words\":29,\"clues\":[{\"clue\":\"And Abijah and his people slew them with a great slaughter: so there ____ down slain of Israel five hundred thousand chosen men.\",\"answer\":\"FELL\",\"start-row\":0,\"start-col\":0,\"across?\":true,\"number\":1},{\"clue\":\"As the living Father hath sent me, and I ____ by the Father: so he that eateth me, even he shall ____ by me.\",\"answer\":\"LIVE\",\"start-row\":0,\"start-col\":2,\"across?\":false,\"number\":2},{\"clue\":\"And he will ____ you a large upper room furnished and prepared: there make ready for us.\",\"answer\":\"SHEW\",\"start-row\":3,\"start-col\":0,\"across?\":true,\"number\":7},{\"clue\":\"Yea, he magnified himself even to the prince of the host, and by him the daily _________ was taken away, and the place of the sanctuary was cast down.\",\"answer\":\"SACRIFICE\",\"start-row\":3,\"start-col\":0,\"across?\":false,\"number\":7},{\"clue\":\"For whosoever shall give you a cup of _____ to drink in my name, because ye belong to Christ, verily I say unto you, he shall not lose his reward.\",\"answer\":\"WATER\",\"start-row\":3,\"start-col\":3,\"across?\":false,\"number\":8},{\"clue\":\"All those that were numbered of the Levites, whom Moses and _____ and the chief of Israel numbered, after their families, and after the house of their fathers,\",\"answer\":\"AARON\",\"start-row\":4,\"start-col\":3,\"across?\":true,\"number\":10},{\"clue\":\"And in the feasts and in the solemnities the meat offering shall be an ephah to a bullock, and an ephah to a ram, and to the lambs as he is able to give, and an hin of ___ to an ephah.\",\"answer\":\"OIL\",\"start-row\":4,\"start-col\":6,\"across?\":false,\"number\":11},{\"clue\":\"When thou shalt besiege a city a long time, in making war against it to take it, thou shalt not destroy the trees thereof by forcing an axe against them: for thou mayest eat of them, and thou shalt not ___ them down (for the tree of the field is man's life) to employ them in the siege:\",\"answer\":\"CUT\",\"start-row\":10,\"start-col\":0,\"across?\":true,\"number\":22},{\"clue\":\"And the king of Israel said, Take Micaiah, and carry him back unto Amon the governor of the ____, and to Joash the king's son;\",\"answer\":\"CITY\",\"start-row\":8,\"start-col\":2,\"across?\":false,\"number\":17},{\"clue\":\"But overthrew Pharaoh and his host in the Red ___: for his mercy endureth for ever.\",\"answer\":\"SEA\",\"start-row\":6,\"start-col\":2,\"across?\":true,\"number\":12},{\"clue\":\"For he shall stand at the right hand of the ____, to save him from those that condemn his soul.\",\"answer\":\"POOR\",\"start-row\":1,\"start-col\":5,\"across?\":false,\"number\":6},{\"clue\":\"Yet let no man strive, nor reprove another: for thy people are as they that strive with the ______.\",\"answer\":\"PRIEST\",\"start-row\":1,\"start-col\":5,\"across?\":true,\"number\":6},{\"clue\":\"All these which were chosen to be porters in the gates were two hundred and twelve. These were reckoned by their genealogy in their villages, whom David and Samuel the seer did ordain in their ___ office.\",\"answer\":\"SET\",\"start-row\":0,\"start-col\":8,\"across?\":false,\"number\":3},{\"clue\":\"The name of the LORD is a ______ tower: the righteous runneth into it, and is safe.\",\"answer\":\"STRONG\",\"start-row\":0,\"start-col\":10,\"across?\":false,\"number\":4},{\"clue\":\"I shall not die, but live, and declare the _____ of the LORD.\",\"answer\":\"WORKS\",\"start-row\":3,\"start-col\":9,\"across?\":true,\"number\":9},{\"clue\":\"Thou shalt fall upon the open field: for I have ______ it, saith the Lord GOD.\",\"answer\":\"SPOKEN\",\"start-row\":0,\"start-col\":12,\"across?\":false,\"number\":5},{\"clue\":\"In the multitude of words there wanteth not ___: but he that refraineth his lips is wise.\",\"answer\":\"SIN\",\"start-row\":0,\"start-col\":12,\"across?\":true,\"number\":5},{\"clue\":\"Then said the LORD unto me; This gate shall be shut, it shall not be opened, and no man shall enter in by it; because the LORD, the God of ______, hath entered in by it, therefore it shall be shut.\",\"answer\":\"ISRAEL\",\"start-row\":9,\"start-col\":2,\"across?\":true,\"number\":19},{\"clue\":\"I thought on my ways, and turned my ____ unto thy testimonies.\",\"answer\":\"FEET\",\"start-row\":8,\"start-col\":6,\"across?\":false,\"number\":18},{\"clue\":\"And the south side southward, from Tamar even to the waters of strife in Kadesh, the _____ to the great sea. And this is the south side southward.\",\"answer\":\"RIVER\",\"start-row\":9,\"start-col\":4,\"across?\":false,\"number\":20},{\"clue\":\"I have hated the ____________ of evil doers; and will not sit with the wicked.\",\"answer\":\"CONGREGATION\",\"start-row\":13,\"start-col\":0,\"across?\":true,\"number\":26},{\"clue\":\"Also the ________ of Noph and Tahapanes have broken the crown of thy head.\",\"answer\":\"CHILDREN\",\"start-row\":6,\"start-col\":11,\"across?\":false,\"number\":14},{\"clue\":\"______, I will bring a fear upon thee, saith the Lord GOD of hosts, from all those that be about thee; and ye shall be driven out every man right forth; and none shall gather up him that wandereth.\",\"answer\":\"BEHOLD\",\"start-row\":7,\"start-col\":9,\"across?\":true,\"number\":15},{\"clue\":\"What shall I say? he hath both spoken unto me, and himself hath done it: I shall go softly all my _____ in the bitterness of my soul.\",\"answer\":\"YEARS\",\"start-row\":11,\"start-col\":8,\"across?\":true,\"number\":23},{\"clue\":\"In his ____ Pharaohnechoh king of Egypt went up against the king of Assyria to the river Euphrates: and king Josiah went against him; and he slew him at Megiddo, when he had seen him.\",\"answer\":\"DAYS\",\"start-row\":7,\"start-col\":14,\"across?\":false,\"number\":16},{\"clue\":\"Let the sighing of the prisoner come before thee; according to the greatness of thy power preserve thou those that are appointed to ___;\",\"answer\":\"DIE\",\"start-row\":9,\"start-col\":9,\"across?\":false,\"number\":21},{\"clue\":\"Wherefore also it is contained in the scripture, Behold, I ___ in Sion a chief corner stone, elect, precious: and he that believeth on him shall not be confounded.\",\"answer\":\"LAY\",\"start-row\":6,\"start-col\":6,\"across?\":true,\"number\":13},{\"clue\":\"And it came to pass, when the king ___ in his house, and the LORD had given him rest round about from all his enemies;\",\"answer\":\"SAT\",\"start-row\":12,\"start-col\":7,\"across?\":false,\"number\":25},{\"clue\":\"Therefore let all the house of Israel know assuredly, that ___ hath made the same Jesus, whom ye have crucified, both Lord and Christ.\",\"answer\":\"GOD\",\"start-row\":12,\"start-col\":1,\"across?\":false,\"number\":24}],\"grid\":{\"0\":{\"0\":{\"letter\":\"F\",\"number\":1},\"1\":{\"letter\":\"E\"},\"2\":{\"letter\":\"L\",\"number\":2},\"3\":{\"letter\":\"L\"},\"8\":{\"letter\":\"S\",\"number\":3},\"10\":{\"letter\":\"S\",\"number\":4},\"12\":{\"letter\":\"S\",\"number\":5},\"13\":{\"letter\":\"I\"},\"14\":{\"letter\":\"N\"}},\"1\":{\"2\":{\"letter\":\"I\"},\"5\":{\"letter\":\"P\",\"number\":6},\"6\":{\"letter\":\"R\"},\"7\":{\"letter\":\"I\"},\"8\":{\"letter\":\"E\"},\"9\":{\"letter\":\"S\"},\"10\":{\"letter\":\"T\"},\"12\":{\"letter\":\"P\"}},\"2\":{\"2\":{\"letter\":\"V\"},\"5\":{\"letter\":\"O\"},\"8\":{\"letter\":\"T\"},\"10\":{\"letter\":\"R\"},\"12\":{\"letter\":\"O\"}},\"3\":{\"0\":{\"letter\":\"S\",\"number\":7},\"1\":{\"letter\":\"H\"},\"2\":{\"letter\":\"E\"},\"3\":{\"letter\":\"W\",\"number\":8},\"5\":{\"letter\":\"O\"},\"9\":{\"letter\":\"W\",\"number\":9},\"10\":{\"letter\":\"O\"},\"11\":{\"letter\":\"R\"},\"12\":{\"letter\":\"K\"},\"13\":{\"letter\":\"S\"}},\"4\":{\"0\":{\"letter\":\"A\"},\"3\":{\"letter\":\"A\",\"number\":10},\"4\":{\"letter\":\"A\"},\"5\":{\"letter\":\"R\"},\"6\":{\"letter\":\"O\",\"number\":11},\"7\":{\"letter\":\"N\"},\"10\":{\"letter\":\"N\"},\"12\":{\"letter\":\"E\"}},\"5\":{\"0\":{\"letter\":\"C\"},\"3\":{\"letter\":\"T\"},\"6\":{\"letter\":\"I\"},\"10\":{\"letter\":\"G\"},\"12\":{\"letter\":\"N\"}},\"6\":{\"0\":{\"letter\":\"R\"},\"2\":{\"letter\":\"S\",\"number\":12},\"3\":{\"letter\":\"E\"},\"4\":{\"letter\":\"A\"},\"6\":{\"letter\":\"L\",\"number\":13},\"7\":{\"letter\":\"A\"},\"8\":{\"letter\":\"Y\"},\"11\":{\"letter\":\"C\",\"number\":14}},\"7\":{\"0\":{\"letter\":\"I\"},\"3\":{\"letter\":\"R\"},\"9\":{\"letter\":\"B\",\"number\":15},\"10\":{\"letter\":\"E\"},\"11\":{\"letter\":\"H\"},\"12\":{\"letter\":\"O\"},\"13\":{\"letter\":\"L\"},\"14\":{\"letter\":\"D\",\"number\":16}},\"8\":{\"0\":{\"letter\":\"F\"},\"2\":{\"letter\":\"C\",\"number\":17},\"6\":{\"letter\":\"F\",\"number\":18},\"11\":{\"letter\":\"I\"},\"14\":{\"letter\":\"A\"}},\"9\":{\"0\":{\"letter\":\"I\"},\"2\":{\"letter\":\"I\",\"number\":19},\"3\":{\"letter\":\"S\"},\"4\":{\"letter\":\"R\",\"number\":20},\"5\":{\"letter\":\"A\"},\"6\":{\"letter\":\"E\"},\"7\":{\"letter\":\"L\"},\"9\":{\"letter\":\"D\",\"number\":21},\"11\":{\"letter\":\"L\"},\"14\":{\"letter\":\"Y\"}},\"10\":{\"0\":{\"letter\":\"C\",\"number\":22},\"1\":{\"letter\":\"U\"},\"2\":{\"letter\":\"T\"},\"4\":{\"letter\":\"I\"},\"6\":{\"letter\":\"E\"},\"9\":{\"letter\":\"I\"},\"11\":{\"letter\":\"D\"},\"14\":{\"letter\":\"S\"}},\"11\":{\"0\":{\"letter\":\"E\"},\"2\":{\"letter\":\"Y\"},\"4\":{\"letter\":\"V\"},\"6\":{\"letter\":\"T\"},\"8\":{\"letter\":\"Y\",\"number\":23},\"9\":{\"letter\":\"E\"},\"10\":{\"letter\":\"A\"},\"11\":{\"letter\":\"R\"},\"12\":{\"letter\":\"S\"}},\"12\":{\"1\":{\"letter\":\"G\",\"number\":24},\"4\":{\"letter\":\"E\"},\"7\":{\"letter\":\"S\",\"number\":25},\"11\":{\"letter\":\"E\"}},\"13\":{\"0\":{\"letter\":\"C\",\"number\":26},\"1\":{\"letter\":\"O\"},\"2\":{\"letter\":\"N\"},\"3\":{\"letter\":\"G\"},\"4\":{\"letter\":\"R\"},\"5\":{\"letter\":\"E\"},\"6\":{\"letter\":\"G\"},\"7\":{\"letter\":\"A\"},\"8\":{\"letter\":\"T\"},\"9\":{\"letter\":\"I\"},\"10\":{\"letter\":\"O\"},\"11\":{\"letter\":\"N\"}},\"14\":{\"1\":{\"letter\":\"D\"},\"7\":{\"letter\":\"T\"}}}}")

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
(defonce game-state-atom (r/atom {}))

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

(defn direction-allowed? [cursor clues]
  (some #(= (:across? cursor) (:across? %))
        (words-containing-square (:square cursor) clues)))

(defn word-correct? [clue game-state]
  (let [squares (squares-in-word clue)
        squares-with-correct-letters (map vector squares (:answer clue))]
    (every? (fn [[square correct-letter]] (= (game-state square) correct-letter))
            squares-with-correct-letters)))

(defn square-correct? [square clues game-state]
  (some #(word-correct? % game-state) (words-containing-square square clues)))

(defn puzzle-complete? [puzzle game-state]
  (every? #(word-correct? % game-state) (:clues puzzle)))

(defn update-cursor [square clues]
  (let [old-cursor @cursor-atom
        same-location (= square (:square old-cursor))
        old-across? (:across? old-cursor)
        flipped-across? (if same-location (not old-across?) old-across?)
        new-across? (if (direction-allowed? (build-cursor square flipped-across?) clues)
                      flipped-across? (not flipped-across?))]
    (.focus (.getElementById js/document (str (:col square) (:row square))))
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
      cursor)))

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

(defn up-cursor [cursor puzzle]
  (transform-cursor inc identity cursor puzzle))
(defn down-cursor [cursor puzzle]
  (transform-cursor dec identity cursor puzzle))
(defn left-cursor [cursor puzzle]
  (transform-cursor identity dec cursor puzzle))
(defn right-cursor [cursor puzzle]
  (transform-cursor identity inc cursor puzzle))

(defn sort-clues [clues]
  (let [clue-direction-groups (group-by :across? clues)
        across-clues (get clue-direction-groups true)
        down-clues (get clue-direction-groups false)]
    (concat (sort-by :number across-clues)
            (sort-by :number down-clues))))

(defn handle-keypress [e]
  (let [puzzle @puzzle-atom
        cursor @cursor-atom
        cur-square (:square cursor)]
    (defn letter-press [keycode]
      (let [letter (.fromCharCode js/String keycode)]
        (swap! cursor-atom next-cursor puzzle)
        (swap! game-state-atom assoc cur-square letter)))
    (defn letter-keycode? [keycode]
      (and (>= keycode 65) (<= keycode 90)))
    (defn backspace-press []
      (swap! cursor-atom prev-cursor puzzle)
      (swap! game-state-atom assoc cur-square nil))
    (defn move-cursor [transform]
      (swap! cursor-atom transform puzzle))
    (let [keycode (.-keyCode e)
          shift? (.-shiftKey e)]
      (.preventDefault e)
      (cond (letter-keycode? keycode) (letter-press keycode)
            (= 37 keycode) (move-cursor left-cursor)
            (= 38 keycode) (move-cursor down-cursor)
            (= 39 keycode) (move-cursor right-cursor)
            (= 40 keycode) (move-cursor up-cursor)
            (= 8 keycode) (backspace-press)))))

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
              click-handler #(update-cursor square clues)]
          [:div.cell {:id (str col-idx row-idx) :on-click click-handler :class classes}
           (when-let [letter (get game-state square)] [:span letter])
           (if-let [number (:number cell)]
             [:span.clue-number number])])))
    (defn crossword-table-row [row-idx row]
      (let [cells (->> (range 0 grid-size)
                       (map #(get row (keyword (str %)))))]
        [:div.row
         (for [[idx cell] (map-indexed vector cells)]
           ^{:key idx} [crossword-table-cell idx row-idx cell])]))
    (let [rows (->> (range 0 grid-size)
                    (map #(get grid (keyword (str %)))))]
      [:div
        [:div#crossword-table
        (for [[idx row] (map-indexed vector rows)]
          ^{:key idx} [crossword-table-row idx row])]
        (if (puzzle-complete? puzzle game-state)
          [:h3 {:class "win"} "Congratulations, you win!"])])))

(defn crossword-clue [puzzle cursor]
  (let [clue (selected-word cursor (:clues puzzle))
        clue-number (:number clue)
        clue-text (:clue clue)
        clue-length (count (:answer clue))
        id (get-clue-id clue)]
    (if clue
        [:div.clue [:p {:id id} (str clue-number ". " clue-text)]]
        [:div.clue])))

(defn crossword-player [puzzle-atom-input]
  (let [puzzle @puzzle-atom-input
        cursor @cursor-atom
        game-state @game-state-atom]
    (reset! puzzle-atom puzzle)
    (if (nil? puzzle)
      [:p "Loading..."]
      [:div.crossword-player
       [crossword-table puzzle cursor game-state]
       [crossword-clue puzzle cursor]])))

(defn init-handlers []
  (events/listen (KeyHandler. js/document) EventType.KEY handle-keypress))

(defonce init
  (init-handlers))

(defn main []
  (fn []
    (crossword-player puzzle-test-atom)))
