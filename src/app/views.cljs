(ns app.views
    (:require [re-frame.core :refer [subscribe]]
              [app.components.nav :as nav]
              [app.components.footer :as footer]
              [app.pages.home :as home]
              [app.pages.game :as game]))

(defmulti pages identity)
(defmethod pages :game [] [game/main])
(defmethod pages :home [] [home/main])
(defmethod pages :default [] [:section])

(defn main []
  (let [current-page (subscribe [:current-page])
        initializing? (subscribe [:initializing?])]
    (fn []
      (if (not @initializing?)
        [:main
         [nav/main]
         (pages @current-page)
         [footer/main]
        ]
        ))))
