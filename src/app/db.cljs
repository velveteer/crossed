(ns app.db)

(def default-db
  {:current-page nil
   :puzzle nil
   :loading? nil
   :game-state {}
   :user-list {}
   :user nil
   :color-scheme "classic"
   :pending-requests []
   :session nil})
