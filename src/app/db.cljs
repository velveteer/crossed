(ns app.db)

(def default-db
  {:current-page nil
   :puzzle nil
   :current-game nil
   :current-games {}
   :all-games {}
   :loading? nil
   :game-state {}
   :user-list {}
   :user {:id nil :color-scheme "classic"}
   :pending-requests []
   :session nil})
