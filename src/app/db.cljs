(ns app.db)

(defn get-user []
  (let [user (js->clj (js/JSON.parse (.getItem js/localStorage "user")) :keywordize-keys true)]
    (if user
      user
       {:id nil
       :color-scheme "classic"
       :online? true})))

(def default-db
  {:current-page nil
   :puzzle nil
   :current-game nil
   :current-games {}
   :loading? nil
   :game-state {}
   :user-list {}
   :user (get-user)
   :pending-requests []
   :user-games []})
