(ns app.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [clojure.string :as str]
            [matchbox.core :as m]
            [app.db :as db]))
  
  ;; -- REPLACE with your own DB location ---------
(def firebase-io-root "https://scorching-torch-2540.firebaseio.com/")
;; ----------------------------------------------

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
  :set-username-input
  (fn [db [_ value]]
    (assoc db :username value)))

(register-handler
  :game-state-update
  (fn [db [_ v]]
    (assoc db :game-state v)))

(register-handler
  :join-game
  (fn [db [_ game-id]]
    ;; listen to game state on firebase
    (let [id  (-> (name game-id) (str/lower-case) (keyword))]
      (-> fb-root
          (m/get-in [id :game-state])
          (m/listen-to :value
                       (fn [[_ v]] (.log js/console v) (dispatch [:game-state-update v])))))

    ;; update current game id
    (assoc db :current-game game-id)))

(register-handler
  :send-move
  (fn [db [_ [square letter]]]
    (m/merge-in! fb-root [(:current-game db) :game-state] {(str (:col square) (:row square)) letter})
    db))
