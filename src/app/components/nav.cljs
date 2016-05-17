(ns app.components.nav
  (:require [app.routes :as routes]))

(defn main []
  (fn []
    [:div.nav
     [:ul
      [:li
       [:a {:href (routes/path-for :home)} [:i.fa.fa-home] "Home"]]
      [:li
       [:a {:href (routes/path-for :game :game-id :test)} [:i.fa.fa-home] "Test Game"]]
      ]]))
