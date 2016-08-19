(ns app.core
  (:require [reagent.dom :refer [render]]
            [re-frame.core :refer [dispatch-sync]]
            [app.routes :as routes]
            [app.views :as views]
            [app.handlers]
            [app.subs]))

;; -------------------------
;; Initialize app

(defn mount-root []
  (render [views/main] (.getElementById js/document "app")))

(defn ^:export init []
  (routes/start!)
  (dispatch-sync [:init])
  (mount-root))
