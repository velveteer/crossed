(ns app.db)

(def default-db
  {:current-page nil
   :puzzle nil
   :cursor nil
   :game-state {}
   :user-list {}
   :user {
       :id nil
       :color-scheme "classic"
   }
   :cell-position nil})
