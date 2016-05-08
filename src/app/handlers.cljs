(ns app.handlers
  (:require [re-frame.core :refer [register-handler]]
            [app.db :as db]))

(register-handler
  :initialize-db
  (fn  [_ _]
    db/default-db))

(register-handler
  :current-page
  (fn [db [_ page]]
    (merge db {:current-page page})))
