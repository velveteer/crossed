(ns app.colors)

(defn- mc [color bg]
    {:color                 color
     :background-color      bg})
     
(def classic (mc "#000000""#FFFFFF"))
(def toxic (mc "#E2FF00""#6800B2"))

(def colors
    {:classic classic
     :toxic toxic})