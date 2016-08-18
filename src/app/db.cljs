(ns app.db)

(def default-db
  {:current-page nil
   :puzzle nil
   :loading? nil
   :user nil
   :user-list {}
   :game-state {}
   :scores {}
   :all-games {}
   :color-scheme "classic"
   :pending-requests []})
