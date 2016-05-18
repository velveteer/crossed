(ns app.handlers
  (:require-macros [app.logging :refer [log]])
  (:require [re-frame.core :refer [register-handler dispatch dispatch-sync]]
            [clojure.string :as str]
            [matchbox.core :as m]
            [ajax.core :refer [GET]]
            [app.colors :as c]
            [app.util :refer [marshal-square]]
            [app.db :as db]))
  
(def firebase-io-root "https://scorching-torch-2540.firebaseio.com/")

;; Connection to Firebase
(defonce fb-root (m/connect firebase-io-root))

(register-handler
  :initialize-db
  (fn  [_ _]
    db/default-db))
  
(register-handler
  :current-page
  (fn [db [_ page]]
    (merge db {:current-page page})))
  
(register-handler
  :set-user
  (fn [db [_ id]]
    (assoc-in db [:user :id] id)))
  
(register-handler
  :set-colors
  (fn [db [_ color]]
      (m/merge-in! fb-root [(:current-game db) :users (:id (:user db))] {:color-scheme color})
      (assoc-in db [:user :color-scheme] color)))

(register-handler
  :game-state-update
  (fn [db [_ v]]
    (if (seq v)
      (assoc db :game-state v)
      (assoc db :game-state {}))))
    
(register-handler
  :user-list-update
  (fn [db [_ v]]
    (if (seq v)
      (merge db {:user-list v})
      (assoc db :user-list {}))))

(register-handler
  :generate-game
  (fn [db [_ _]]
    (GET "get-puzzle"
      {:response-format :json
        :keywords? true
        :handler #(log %)})))
      
(register-handler
  :join-game
  (fn [db [_ game-id]]
    ;; listen to game state on firebase
    (let [id  (-> (name game-id) (str/lower-case) (keyword))
          user (:user db)]
      (if (seq (:id user))
        ;; if user has session then put them into user-list for game
        (do 
            (m/merge-in! fb-root [id :users] {(:id user) user})
            (-> fb-root
                (m/get-in [id :users])
                (m/listen-to :value
                             (fn [[_ v]] #_(log v) (dispatch [:user-list-update v]))))
            (-> fb-root
                (m/get-in [id :game-state])
                (m/listen-to :value
                             (fn [[_ v]] #_(log v) (dispatch [:game-state-update v])))))
                           
        ;; anonymous login for anyone without a session -- sets user and then loops back to join the game
        (do 
            (m/auth-anon fb-root (fn [err auth-data]
                                (dispatch [:set-user (:uid auth-data)])
                                (dispatch [:join-game game-id])
                                ;; remove user from this game's user-list on disconnect
                                (.remove (m/on-disconnect (.child fb-root (str (name game-id) "/users/" (:uid auth-data)))))))))

    ;; update current game id
    (assoc db :current-game game-id))))

(register-handler
  :send-move
  (fn [db [_ [square letter user]]]
    (m/merge-in! fb-root [(:current-game db) :game-state] {(marshal-square square) {:letter letter :user user}})
    db))
