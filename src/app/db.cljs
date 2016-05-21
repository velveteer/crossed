(ns app.db)

(def default-db
  {:current-page nil
   :puzzle nil
   :current-game nil
   :current-games {}
   :loading? nil
   :game-state {}
   :user-list {}
   :user {
       :id nil
       :color-scheme "classic"
       :online? true
   }
   :user-games []})
