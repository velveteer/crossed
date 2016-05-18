(ns app.views
    (:require [re-frame.core :refer [subscribe]]
              [app.components.nav :as nav]
              [app.pages.home :as home]))

(defmulti pages identity)
(defmethod pages :home [] [:section])
(defmethod pages :game [] [home/main])
(defmethod pages :default [] [:section])

(defn main []
  (let [current-page (subscribe [:current-page])]
    (fn []
      [:main.mb6
        [nav/main]
        (pages @current-page)
        ])))
