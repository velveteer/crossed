(ns app.colors)

(defn- mc [color bg]
    {:color                 color
     :background-color      bg})
     
(def classic (mc "#000000""#FFFFFF"))
(def toxic (mc "#E2FF00""#6800B2"))
(def beach-day (mc "#0BFFF6" "#FF610B"))
(def far-away (mc "#FFB184" "#B26639"))
(def become-ocean (mc "#6AFFFF" "#27B2B2"))
(def bubblegum (mc "#950FB2" "#FF6A90"))
(def dune (mc "#AAFFFF" "#CCA741"))

(def colors
    {:classic classic
     :dune dune
     :beach-day beach-day
     :far-away far-away
     :become-ocean become-ocean
     :bubblegum bubblegum
     :toxic toxic})