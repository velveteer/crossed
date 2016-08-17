(ns app.views
    (:require [re-frame.core :refer [subscribe]]
              [app.components.nav :as nav]
              [app.pages.home :as home]
              [app.pages.game :as game]))

(defmulti pages identity)
(defmethod pages :game [] [game/main])
(defmethod pages :home [] [home/main])
(defmethod pages :default [] [:section])

(defn main []
  (let [current-page (subscribe [:current-page])
        loading? (subscribe [:loading?])]
    (fn []
      [:main.mb6
       [nav/main]
      (if @loading?
        [:div.spinner]
        (pages @current-page))
         ])))
