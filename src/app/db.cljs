(ns app.db)

(def default-db
  {:current-page nil
   :puzzle nil
   :loading? false
   :loading-games? false
   :initializing? false
   :user nil
   :user-list {}
   :game-state {}
   :scores {}
   :all-games {}
   :color-scheme "classic"
   :pending-requests []})
