(ns app.core
  (:require [reagent.dom :refer [render]]
            [re-frame.core :refer [dispatch-sync]]
            [devtools.core :as devtools]
            [app.routes :as routes]
            [app.views :as views]
            [app.handlers]
            [app.subs]))

;; -------------------------
;; Initialize app

(defn mount-root []
  (render [views/main] (.getElementById js/document "app")))

(defn ^:export init []
  (if ^boolean goog.DEBUG (devtools/install! [:custom-formatters :sanity-hints]))
  (routes/start!)
  (dispatch-sync [:initialize-db])
  (dispatch-sync [:get-current-games])
  (mount-root))
