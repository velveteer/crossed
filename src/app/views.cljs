(ns app.views
    (:require [re-frame.core :as re-frame]
              [app.components.nav :as nav]
              [app.pages.home :as home]
              [app.pages.about :as about]))

(defmulti pages identity)
(defmethod pages :home [] [:div "Welcome Home"])
(defmethod pages :game [] [home/main])
(defmethod pages :default [] [home/main])

(defn main []
  (let [current-page (re-frame/subscribe [:current-page])]
    (fn []
      [:main
        [nav/main]
        (pages @current-page)
        ])))
