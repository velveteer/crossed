(ns app.colors)

(defn- mc [color bg]
    {:color                 color
     :background-color      bg})

(def colors
    {:classic (mc "#000000""#FFFFFF")
     :dune (mc "#AAFFFF" "#CCA741")
     :beach-day (mc "#0BFFF6" "#FF610B")
     :far-away (mc "#FFB184" "#B26639")
     :become-ocean (mc "#6AFFFF" "#27B2B2")
     :bubblegum (mc "#950FB2" "#FF6A90")
     :mig-28 (mc "#FFFFFF" "#000000")
     :jungle-juice (mc "#C5E31D" "#8C9942")
     :toxic (mc "#E2FF00""#6800B2")
     :you-did-what (mc "#5FFF47" "#FF0735")
     :unholiness (mc "#FF96E1" "#7A7F7B")
     :vaporwave (mc "#9AFFDE" "#73003E")
     })
