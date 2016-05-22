(ns app.pages.game
  [:require [app.components.grid :as grid]])

(defn main []
  (fn []
    [:section.game-container
     [grid/main]]))
