(ns app.components.nav
  (:require [app.routes :as routes]))

(defn main []
  (fn []
    [:div.nav
     [:ul
      [:li
       [:a {:href (routes/url-for :home)} [:i.fa.fa-home] "Home"]]
      ]]))
