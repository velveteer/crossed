(ns app.components.grid
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [clojure.string :refer [join]]))

(def puzzle-test
  "{\"id\":\"24141\",\"grid-size\":23,\"words\":72,\"clues\":[{\"clue\":\"using language effectively to please or persuade\",\"answer\":\"rhetoric\",\"start-row\":0,\"start-col\":0,\"across?\":true,\"number\":1},{\"clue\":\"make reference to\",\"answer\":\"cite\",\"start-row\":0,\"start-col\":7,\"across?\":false,\"number\":4},{\"clue\":\"the conscious exclusion of unacceptable thoughts or desires\",\"answer\":\"inhibition\",\"start-row\":1,\"start-col\":7,\"across?\":true,\"number\":9},{\"clue\":\"a drastic and far-reaching change in ways of thinking\",\"answer\":\"revolution\",\"start-row\":0,\"start-col\":0,\"across?\":false,\"number\":1},{\"clue\":\"the quality of disagreeing\",\"answer\":\"incongruity\",\"start-row\":1,\"start-col\":14,\"across?\":false,\"number\":12},{\"clue\":\"a person who makes a will\",\"answer\":\"testator\",\"start-row\":0,\"start-col\":3,\"across?\":false,\"number\":2},{\"clue\":\"especially fine or decorative clothing\",\"answer\":\"raiment\",\"start-row\":0,\"start-col\":5,\"across?\":false,\"number\":3},{\"clue\":\"an elaborate and systematic plan of action\",\"answer\":\"scheme\",\"start-row\":3,\"start-col\":13,\"across?\":true,\"number\":15},{\"clue\":\"confined by bonds\",\"answer\":\"bound\",\"start-row\":1,\"start-col\":11,\"across?\":false,\"number\":11},{\"clue\":\"move or draw apart\",\"answer\":\"diverge\",\"start-row\":7,\"start-col\":10,\"across?\":true,\"number\":26},{\"clue\":\"liberality in bestowing gifts\",\"answer\":\"magnanimity\",\"start-row\":10,\"start-col\":5,\"across?\":true,\"number\":34},{\"clue\":\"someone who carries out a learned profession\",\"answer\":\"practitioner\",\"start-row\":8,\"start-col\":6,\"across?\":false,\"number\":29},{\"clue\":\"travel around, either by plane or ship\",\"answer\":\"circumnavigate\",\"start-row\":9,\"start-col\":11,\"across?\":false,\"number\":32},{\"clue\":\"deeply rooted; firmly fixed or held\",\"answer\":\"ingrained\",\"start-row\":5,\"start-col\":13,\"across?\":true,\"number\":20},{\"clue\":\"emotionally hardened\",\"answer\":\"callous\",\"start-row\":9,\"start-col\":9,\"across?\":false,\"number\":31},{\"clue\":\"having or showing arrogant superiority to\",\"answer\":\"supercilious\",\"start-row\":18,\"start-col\":3,\"across?\":true,\"number\":59},{\"clue\":\"force to leave an office\",\"answer\":\"depose\",\"start-row\":16,\"start-col\":3,\"across?\":true,\"number\":53},{\"clue\":\"take away possessions from someone\",\"answer\":\"divest\",\"start-row\":12,\"start-col\":1,\"across?\":true,\"number\":39},{\"clue\":\"a puzzle consisting of pictures representing words\",\"answer\":\"rebus\",\"start-row\":22,\"start-col\":10,\"across?\":true,\"number\":69},{\"clue\":\"something many people believe that is false\",\"answer\":\"illusion\",\"start-row\":5,\"start-col\":18,\"across?\":false,\"number\":21},{\"clue\":\"deviating from the general or common order or type\",\"answer\":\"anomalous\",\"start-row\":16,\"start-col\":11,\"across?\":true,\"number\":54},{\"clue\":\"moral weakness\",\"answer\":\"vice\",\"start-row\":0,\"start-col\":18,\"across?\":false,\"number\":6},{\"clue\":\"express criticism towards\",\"answer\":\"upbraid\",\"start-row\":13,\"start-col\":11,\"across?\":true,\"number\":43},{\"clue\":\"understatement for rhetorical effect\",\"answer\":\"litotes\",\"start-row\":0,\"start-col\":20,\"across?\":false,\"number\":7},{\"clue\":\"shrewdness as demonstrated by being skilled in deception\",\"answer\":\"guile\",\"start-row\":10,\"start-col\":2,\"across?\":false,\"number\":33},{\"clue\":\"stir up so as to form small waves\",\"answer\":\"ripple\",\"start-row\":10,\"start-col\":17,\"across?\":true,\"number\":35},{\"clue\":\"answer from an ensign\",\"answer\":\"aye\",\"start-row\":4,\"start-col\":3,\"across?\":true,\"number\":17},{\"clue\":\"in a hasty and foolhardy manner\",\"answer\":\"headlong\",\"start-row\":20,\"start-col\":9,\"across?\":true,\"number\":63},{\"clue\":\"enclosed firmly in a surrounding mass\",\"answer\":\"embedded\",\"start-row\":10,\"start-col\":22,\"across?\":false,\"number\":36},{\"clue\":\"disturb, especially by minor irritations\",\"answer\":\"rile\",\"start-row\":13,\"start-col\":19,\"across?\":true,\"number\":45},{\"clue\":\"opera set in ancient egypt\",\"answer\":\"aida\",\"start-row\":13,\"start-col\":15,\"across?\":false,\"number\":44},{\"clue\":\"a single time\",\"answer\":\"once\",\"start-row\":0,\"start-col\":16,\"across?\":false,\"number\":5},{\"clue\":\"marked by care and persistent effort\",\"answer\":\"sedulous\",\"start-row\":15,\"start-col\":4,\"across?\":false,\"number\":49},{\"clue\":\"easter lead-in\",\"answer\":\"lent\",\"start-row\":4,\"start-col\":9,\"across?\":true,\"number\":18},{\"clue\":\"the periodic rise and fall of the sea level\",\"answer\":\"tide\",\"start-row\":6,\"start-col\":5,\"across?\":true,\"number\":23},{\"clue\":\"an indication that something has been present\",\"answer\":\"vestige\",\"start-row\":22,\"start-col\":2,\"across?\":true,\"number\":68},{\"clue\":\"tear or be torn violently\",\"answer\":\"rend\",\"start-row\":5,\"start-col\":8,\"across?\":false,\"number\":19},{\"clue\":\"the act of ejecting someone or forcing them out\",\"answer\":\"ouster\",\"start-row\":8,\"start-col\":17,\"across?\":true,\"number\":30},{\"clue\":\"an ability that has been acquired by training\",\"answer\":\"skill\",\"start-row\":16,\"start-col\":19,\"across?\":false,\"number\":56},{\"clue\":\"remote in manner\",\"answer\":\"aloof\",\"start-row\":20,\"start-col\":1,\"across?\":true,\"number\":61},{\"clue\":\"the environmental condition\",\"answer\":\"milieu\",\"start-row\":18,\"start-col\":16,\"across?\":true,\"number\":60},{\"clue\":\"without or seeming to be without plan or method; offhand\",\"answer\":\"casual\",\"start-row\":16,\"start-col\":1,\"across?\":false,\"number\":52},{\"clue\":\"kill intentionally and with premeditation\",\"answer\":\"slay\",\"start-row\":20,\"start-col\":18,\"across?\":true,\"number\":66},{\"clue\":\"ocean east of nc\",\"answer\":\"atl\",\"start-row\":14,\"start-col\":5,\"across?\":true,\"number\":47},{\"clue\":\"weather system\",\"answer\":\"low\",\"start-row\":0,\"start-col\":20,\"across?\":true,\"number\":7},{\"clue\":\"hockey great bobby\",\"answer\":\"hull\",\"start-row\":1,\"start-col\":9,\"across?\":false,\"number\":10},{\"clue\":\"it may be hooked\",\"answer\":\"worm\",\"start-row\":0,\"start-col\":22,\"across?\":false,\"number\":8},{\"clue\":\"varnish ingredient\",\"answer\":\"lac\",\"start-row\":12,\"start-col\":9,\"across?\":true,\"number\":40},{\"clue\":\"an interest followed with exaggerated zeal\",\"answer\":\"fad\",\"start-row\":15,\"start-col\":20,\"across?\":true,\"number\":51},{\"clue\":\"on ___ (without a contract)\",\"answer\":\"spec\",\"start-row\":14,\"start-col\":0,\"across?\":true,\"number\":46},{\"clue\":\"unwavering\",\"answer\":\"set\",\"start-row\":11,\"start-col\":4,\"across?\":false,\"number\":37},{\"clue\":\"ick opposite\",\"answer\":\"yum\",\"start-row\":20,\"start-col\":21,\"across?\":false,\"number\":67},{\"clue\":\"a large open vessel for holding or storing liquids\",\"answer\":\"vat\",\"start-row\":17,\"start-col\":0,\"across?\":true,\"number\":57},{\"clue\":\"one of the jointed appendages of an animal\",\"answer\":\"limb\",\"start-row\":22,\"start-col\":19,\"across?\":true,\"number\":70},{\"clue\":\"nagano honorific\",\"answer\":\"san\",\"start-row\":15,\"start-col\":9,\"across?\":true,\"number\":50},{\"clue\":\"cause to slow down or get stuck\",\"answer\":\"bog\",\"start-row\":20,\"start-col\":7,\"across?\":false,\"number\":62},{\"clue\":\"an unbroken period of time during which you do something\",\"answer\":\"stint\",\"start-row\":2,\"start-col\":3,\"across?\":true,\"number\":13},{\"clue\":\"a/c measure, for short\",\"answer\":\"btu\",\"start-row\":14,\"start-col\":18,\"across?\":false,\"number\":48},{\"clue\":\"its setting may be a setting\",\"answer\":\"gem\",\"start-row\":20,\"start-col\":16,\"across?\":false,\"number\":65},{\"clue\":\"mork's birthplace, on tv\",\"answer\":\"ork\",\"start-row\":8,\"start-col\":0,\"across?\":true,\"number\":27},{\"clue\":\"ms. farrow\",\"answer\":\"mia\",\"start-row\":3,\"start-col\":17,\"across?\":false,\"number\":16},{\"clue\":\"fall away or decline\",\"answer\":\"ebb\",\"start-row\":12,\"start-col\":13,\"across?\":false,\"number\":41},{\"clue\":\"femur's upper end\",\"answer\":\"hip\",\"start-row\":8,\"start-col\":4,\"across?\":true,\"number\":28},{\"clue\":\"hilo garland\",\"answer\":\"lei\",\"start-row\":11,\"start-col\":16,\"across?\":false,\"number\":38},{\"clue\":\"not neath\",\"answer\":\"oer\",\"start-row\":6,\"start-col\":22,\"across?\":false,\"number\":25},{\"clue\":\"concise and full of meaning\",\"answer\":\"pithy\",\"start-row\":17,\"start-col\":9,\"across?\":false,\"number\":58},{\"clue\":\"catalina, for one: abbr.\",\"answer\":\"isl\",\"start-row\":13,\"start-col\":0,\"across?\":false,\"number\":42},{\"clue\":\"treaty org. since 1948\",\"answer\":\"oas\",\"start-row\":20,\"start-col\":14,\"across?\":false,\"number\":64},{\"clue\":\"eliminate\",\"answer\":\"omit\",\"start-row\":3,\"start-col\":0,\"across?\":true,\"number\":14},{\"clue\":\"fat cat's victim\",\"answer\":\"odie\",\"start-row\":16,\"start-col\":17,\"across?\":false,\"number\":55},{\"clue\":\"someone new to a field or activity\",\"answer\":\"tyro\",\"start-row\":6,\"start-col\":0,\"across?\":true,\"number\":22},{\"clue\":\"bouncers read them\",\"answer\":\"ids\",\"start-row\":6,\"start-col\":10,\"across?\":false,\"number\":24}],\"grid\":{\"0\":{\"0\":{\"letter\":\"r\",\"number\":1},\"1\":{\"letter\":\"h\"},\"2\":{\"letter\":\"e\"},\"3\":{\"letter\":\"t\",\"number\":2},\"4\":{\"letter\":\"o\"},\"5\":{\"letter\":\"r\",\"number\":3},\"6\":{\"letter\":\"i\"},\"7\":{\"letter\":\"c\",\"number\":4},\"16\":{\"letter\":\"o\",\"number\":5},\"18\":{\"letter\":\"v\",\"number\":6},\"20\":{\"letter\":\"l\",\"number\":7},\"21\":{\"letter\":\"o\"},\"22\":{\"letter\":\"w\",\"number\":8}},\"1\":{\"0\":{\"letter\":\"e\"},\"3\":{\"letter\":\"e\"},\"5\":{\"letter\":\"a\"},\"7\":{\"letter\":\"i\",\"number\":9},\"8\":{\"letter\":\"n\"},\"9\":{\"letter\":\"h\",\"number\":10},\"10\":{\"letter\":\"i\"},\"11\":{\"letter\":\"b\",\"number\":11},\"12\":{\"letter\":\"i\"},\"13\":{\"letter\":\"t\"},\"14\":{\"letter\":\"i\",\"number\":12},\"15\":{\"letter\":\"o\"},\"16\":{\"letter\":\"n\"},\"18\":{\"letter\":\"i\"},\"20\":{\"letter\":\"i\"},\"22\":{\"letter\":\"o\"}},\"2\":{\"0\":{\"letter\":\"v\"},\"3\":{\"letter\":\"s\",\"number\":13},\"4\":{\"letter\":\"t\"},\"5\":{\"letter\":\"i\"},\"6\":{\"letter\":\"n\"},\"7\":{\"letter\":\"t\"},\"9\":{\"letter\":\"u\"},\"11\":{\"letter\":\"o\"},\"14\":{\"letter\":\"n\"},\"16\":{\"letter\":\"c\"},\"18\":{\"letter\":\"c\"},\"20\":{\"letter\":\"t\"},\"22\":{\"letter\":\"r\"}},\"3\":{\"0\":{\"letter\":\"o\",\"number\":14},\"1\":{\"letter\":\"m\"},\"2\":{\"letter\":\"i\"},\"3\":{\"letter\":\"t\"},\"5\":{\"letter\":\"m\"},\"7\":{\"letter\":\"e\"},\"9\":{\"letter\":\"l\"},\"11\":{\"letter\":\"u\"},\"13\":{\"letter\":\"s\",\"number\":15},\"14\":{\"letter\":\"c\"},\"15\":{\"letter\":\"h\"},\"16\":{\"letter\":\"e\"},\"17\":{\"letter\":\"m\",\"number\":16},\"18\":{\"letter\":\"e\"},\"20\":{\"letter\":\"o\"},\"22\":{\"letter\":\"m\"}},\"4\":{\"0\":{\"letter\":\"l\"},\"3\":{\"letter\":\"a\",\"number\":17},\"4\":{\"letter\":\"y\"},\"5\":{\"letter\":\"e\"},\"9\":{\"letter\":\"l\",\"number\":18},\"10\":{\"letter\":\"e\"},\"11\":{\"letter\":\"n\"},\"12\":{\"letter\":\"t\"},\"14\":{\"letter\":\"o\"},\"17\":{\"letter\":\"i\"},\"20\":{\"letter\":\"t\"}},\"5\":{\"0\":{\"letter\":\"u\"},\"3\":{\"letter\":\"t\"},\"5\":{\"letter\":\"n\"},\"8\":{\"letter\":\"r\",\"number\":19},\"11\":{\"letter\":\"d\"},\"13\":{\"letter\":\"i\",\"number\":20},\"14\":{\"letter\":\"n\"},\"15\":{\"letter\":\"g\"},\"16\":{\"letter\":\"r\"},\"17\":{\"letter\":\"a\"},\"18\":{\"letter\":\"i\",\"number\":21},\"19\":{\"letter\":\"n\"},\"20\":{\"letter\":\"e\"},\"21\":{\"letter\":\"d\"}},\"6\":{\"0\":{\"letter\":\"t\",\"number\":22},\"1\":{\"letter\":\"y\"},\"2\":{\"letter\":\"r\"},\"3\":{\"letter\":\"o\"},\"5\":{\"letter\":\"t\",\"number\":23},\"6\":{\"letter\":\"i\"},\"7\":{\"letter\":\"d\"},\"8\":{\"letter\":\"e\"},\"10\":{\"letter\":\"i\",\"number\":24},\"14\":{\"letter\":\"g\"},\"18\":{\"letter\":\"l\"},\"20\":{\"letter\":\"s\"},\"22\":{\"letter\":\"o\",\"number\":25}},\"7\":{\"0\":{\"letter\":\"i\"},\"3\":{\"letter\":\"r\"},\"8\":{\"letter\":\"n\"},\"10\":{\"letter\":\"d\",\"number\":26},\"11\":{\"letter\":\"i\"},\"12\":{\"letter\":\"v\"},\"13\":{\"letter\":\"e\"},\"14\":{\"letter\":\"r\"},\"15\":{\"letter\":\"g\"},\"16\":{\"letter\":\"e\"},\"18\":{\"letter\":\"l\"},\"22\":{\"letter\":\"e\"}},\"8\":{\"0\":{\"letter\":\"o\",\"number\":27},\"1\":{\"letter\":\"r\"},\"2\":{\"letter\":\"k\"},\"4\":{\"letter\":\"h\",\"number\":28},\"5\":{\"letter\":\"i\"},\"6\":{\"letter\":\"p\",\"number\":29},\"8\":{\"letter\":\"d\"},\"10\":{\"letter\":\"s\"},\"14\":{\"letter\":\"u\"},\"17\":{\"letter\":\"o\",\"number\":30},\"18\":{\"letter\":\"u\"},\"19\":{\"letter\":\"s\"},\"20\":{\"letter\":\"t\"},\"21\":{\"letter\":\"e\"},\"22\":{\"letter\":\"r\"}},\"9\":{\"0\":{\"letter\":\"n\"},\"6\":{\"letter\":\"r\"},\"9\":{\"letter\":\"c\",\"number\":31},\"11\":{\"letter\":\"c\",\"number\":32},\"14\":{\"letter\":\"i\"},\"18\":{\"letter\":\"s\"}},\"10\":{\"2\":{\"letter\":\"g\",\"number\":33},\"5\":{\"letter\":\"m\",\"number\":34},\"6\":{\"letter\":\"a\"},\"7\":{\"letter\":\"g\"},\"8\":{\"letter\":\"n\"},\"9\":{\"letter\":\"a\"},\"10\":{\"letter\":\"n\"},\"11\":{\"letter\":\"i\"},\"12\":{\"letter\":\"m\"},\"13\":{\"letter\":\"i\"},\"14\":{\"letter\":\"t\"},\"15\":{\"letter\":\"y\"},\"17\":{\"letter\":\"r\",\"number\":35},\"18\":{\"letter\":\"i\"},\"19\":{\"letter\":\"p\"},\"20\":{\"letter\":\"p\"},\"21\":{\"letter\":\"l\"},\"22\":{\"letter\":\"e\",\"number\":36}},\"11\":{\"2\":{\"letter\":\"u\"},\"4\":{\"letter\":\"s\",\"number\":37},\"6\":{\"letter\":\"c\"},\"9\":{\"letter\":\"l\"},\"11\":{\"letter\":\"r\"},\"14\":{\"letter\":\"y\"},\"16\":{\"letter\":\"l\",\"number\":38},\"18\":{\"letter\":\"o\"},\"22\":{\"letter\":\"m\"}},\"12\":{\"1\":{\"letter\":\"d\",\"number\":39},\"2\":{\"letter\":\"i\"},\"3\":{\"letter\":\"v\"},\"4\":{\"letter\":\"e\"},\"5\":{\"letter\":\"s\"},\"6\":{\"letter\":\"t\"},\"9\":{\"letter\":\"l\",\"number\":40},\"10\":{\"letter\":\"a\"},\"11\":{\"letter\":\"c\"},\"13\":{\"letter\":\"e\",\"number\":41},\"16\":{\"letter\":\"e\"},\"18\":{\"letter\":\"n\"},\"22\":{\"letter\":\"b\"}},\"13\":{\"0\":{\"letter\":\"i\",\"number\":42},\"2\":{\"letter\":\"l\"},\"4\":{\"letter\":\"t\"},\"6\":{\"letter\":\"i\"},\"9\":{\"letter\":\"o\"},\"11\":{\"letter\":\"u\",\"number\":43},\"12\":{\"letter\":\"p\"},\"13\":{\"letter\":\"b\"},\"14\":{\"letter\":\"r\"},\"15\":{\"letter\":\"a\",\"number\":44},\"16\":{\"letter\":\"i\"},\"17\":{\"letter\":\"d\"},\"19\":{\"letter\":\"r\",\"number\":45},\"20\":{\"letter\":\"i\"},\"21\":{\"letter\":\"l\"},\"22\":{\"letter\":\"e\"}},\"14\":{\"0\":{\"letter\":\"s\",\"number\":46},\"1\":{\"letter\":\"p\"},\"2\":{\"letter\":\"e\"},\"3\":{\"letter\":\"c\"},\"5\":{\"letter\":\"a\",\"number\":47},\"6\":{\"letter\":\"t\"},\"7\":{\"letter\":\"l\"},\"9\":{\"letter\":\"u\"},\"11\":{\"letter\":\"m\"},\"13\":{\"letter\":\"b\"},\"15\":{\"letter\":\"i\"},\"18\":{\"letter\":\"b\",\"number\":48},\"22\":{\"letter\":\"d\"}},\"15\":{\"0\":{\"letter\":\"l\"},\"4\":{\"letter\":\"s\",\"number\":49},\"6\":{\"letter\":\"i\"},\"9\":{\"letter\":\"s\",\"number\":50},\"10\":{\"letter\":\"a\"},\"11\":{\"letter\":\"n\"},\"15\":{\"letter\":\"d\"},\"18\":{\"letter\":\"t\"},\"20\":{\"letter\":\"f\",\"number\":51},\"21\":{\"letter\":\"a\"},\"22\":{\"letter\":\"d\"}},\"16\":{\"1\":{\"letter\":\"c\",\"number\":52},\"3\":{\"letter\":\"d\",\"number\":53},\"4\":{\"letter\":\"e\"},\"5\":{\"letter\":\"p\"},\"6\":{\"letter\":\"o\"},\"7\":{\"letter\":\"s\"},\"8\":{\"letter\":\"e\"},\"11\":{\"letter\":\"a\",\"number\":54},\"12\":{\"letter\":\"n\"},\"13\":{\"letter\":\"o\"},\"14\":{\"letter\":\"m\"},\"15\":{\"letter\":\"a\"},\"16\":{\"letter\":\"l\"},\"17\":{\"letter\":\"o\",\"number\":55},\"18\":{\"letter\":\"u\"},\"19\":{\"letter\":\"s\",\"number\":56},\"22\":{\"letter\":\"e\"}},\"17\":{\"0\":{\"letter\":\"v\",\"number\":57},\"1\":{\"letter\":\"a\"},\"2\":{\"letter\":\"t\"},\"4\":{\"letter\":\"d\"},\"6\":{\"letter\":\"n\"},\"9\":{\"letter\":\"p\",\"number\":58},\"11\":{\"letter\":\"v\"},\"17\":{\"letter\":\"d\"},\"19\":{\"letter\":\"k\"},\"22\":{\"letter\":\"d\"}},\"18\":{\"1\":{\"letter\":\"s\"},\"3\":{\"letter\":\"s\",\"number\":59},\"4\":{\"letter\":\"u\"},\"5\":{\"letter\":\"p\"},\"6\":{\"letter\":\"e\"},\"7\":{\"letter\":\"r\"},\"8\":{\"letter\":\"c\"},\"9\":{\"letter\":\"i\"},\"10\":{\"letter\":\"l\"},\"11\":{\"letter\":\"i\"},\"12\":{\"letter\":\"o\"},\"13\":{\"letter\":\"u\"},\"14\":{\"letter\":\"s\"},\"16\":{\"letter\":\"m\",\"number\":60},\"17\":{\"letter\":\"i\"},\"18\":{\"letter\":\"l\"},\"19\":{\"letter\":\"i\"},\"20\":{\"letter\":\"e\"},\"21\":{\"letter\":\"u\"}},\"19\":{\"1\":{\"letter\":\"u\"},\"4\":{\"letter\":\"l\"},\"6\":{\"letter\":\"r\"},\"9\":{\"letter\":\"t\"},\"11\":{\"letter\":\"g\"},\"17\":{\"letter\":\"e\"},\"19\":{\"letter\":\"l\"}},\"20\":{\"1\":{\"letter\":\"a\",\"number\":61},\"2\":{\"letter\":\"l\"},\"3\":{\"letter\":\"o\"},\"4\":{\"letter\":\"o\"},\"5\":{\"letter\":\"f\"},\"7\":{\"letter\":\"b\",\"number\":62},\"9\":{\"letter\":\"h\",\"number\":63},\"10\":{\"letter\":\"e\"},\"11\":{\"letter\":\"a\"},\"12\":{\"letter\":\"d\"},\"13\":{\"letter\":\"l\"},\"14\":{\"letter\":\"o\",\"number\":64},\"15\":{\"letter\":\"n\"},\"16\":{\"letter\":\"g\",\"number\":65},\"18\":{\"letter\":\"s\",\"number\":66},\"19\":{\"letter\":\"l\"},\"20\":{\"letter\":\"a\"},\"21\":{\"letter\":\"y\",\"number\":67}},\"21\":{\"1\":{\"letter\":\"l\"},\"4\":{\"letter\":\"u\"},\"7\":{\"letter\":\"o\"},\"9\":{\"letter\":\"y\"},\"11\":{\"letter\":\"t\"},\"14\":{\"letter\":\"a\"},\"16\":{\"letter\":\"e\"},\"21\":{\"letter\":\"u\"}},\"22\":{\"2\":{\"letter\":\"v\",\"number\":68},\"3\":{\"letter\":\"e\"},\"4\":{\"letter\":\"s\"},\"5\":{\"letter\":\"t\"},\"6\":{\"letter\":\"i\"},\"7\":{\"letter\":\"g\"},\"8\":{\"letter\":\"e\"},\"10\":{\"letter\":\"r\",\"number\":69},\"11\":{\"letter\":\"e\"},\"12\":{\"letter\":\"b\"},\"13\":{\"letter\":\"u\"},\"14\":{\"letter\":\"s\"},\"16\":{\"letter\":\"m\"},\"19\":{\"letter\":\"l\",\"number\":70},\"20\":{\"letter\":\"i\"},\"21\":{\"letter\":\"m\"},\"22\":{\"letter\":\"b\"}}}}"
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
(defonce game-state-atom (r/atom {}))
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
    (every? (fn [[square correct-letter]] (= (game-state square) correct-letter))
            squares-with-correct-letters)))

(defn square-correct? [square clues game-state]
  (some #(word-correct? % game-state) (words-containing-square square clues)))

(defn puzzle-complete? [puzzle game-state]
  (every? #(word-correct? % game-state) (:clues puzzle)))

(defn focus-input []
    (.focus (.getElementById js/document "word-input")))

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

(defn handle-change [e]
  (let [puzzle @puzzle-atom
        game-state (subscribe [:game-state])
        cursor @cursor-atom
        clues (:clues puzzle)
        prev-word (join " " (user-input cursor clues @game-state))
        word (.. e -target -value)
        cur-square (:square cursor)]
    (.preventDefault e)
    (if (> (count word) (count prev-word))
      (do 
        (swap! cursor-atom next-cursor puzzle)
        (dispatch [:send-move [cur-square (last word)]]))
      (do 
        (swap! cursor-atom prev-cursor puzzle)
        (dispatch [:send-move [cur-square nil]])))))

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
           (when-let [letter (get game-state (keyword (str col-idx row-idx)))] [:span letter])
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
                        :top (:top cell-position) :left (:left cell-position) :right (:left cell-position) :bottom (:bottom cell-position)
                        :border "none" :outline "none" 
                        :width 1 :height 1}
                :value (user-input cursor clues game-state)
                :on-change handle-change}]
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
        game-state (subscribe [:game-state])]
    (reset! puzzle-atom puzzle)
    (if (nil? puzzle)
      [:p "Loading..."]
      [:div.crossword-player
        [:div {:style {:height 100}} [crossword-clue puzzle cursor]]
        [crossword-table puzzle cursor @game-state]
       ])))

(defn main []
  (fn []
    (crossword-player puzzle-test-atom)))
