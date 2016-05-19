(ns app.views
    (:require [re-frame.core :refer [subscribe]]
              [app.components.nav :as nav]
              [app.pages.game :as game]))

(defmulti pages identity)
(defmethod pages :game [] [game/main])
(defmethod pages :home [] [:section])
(defmethod pages :default [] [:section])

(defn main []
  (let [current-page (subscribe [:current-page])]
    (fn []
      [:main.mb6
        [nav/main]
        (pages @current-page)
        ])))
