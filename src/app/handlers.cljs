(ns app.handlers
  (:require-macros [app.logging :refer [log]])
  (:require [re-frame.core :refer [register-handler dispatch dispatch-sync]]
            [clojure.string :as str]
            [matchbox.core :as m]
            [matchbox.registry :as mr]
            [ajax.core :refer [GET]]
            [app.colors :as c]
            [app.routes :refer [set-token!]]
            [app.util :refer [marshal-square]]
            [app.db :as db]))

(defn convert-puzzle [puzzle] (-> (->> puzzle (.parse js/JSON)) (js->clj :keywordize-keys true)))

(def firebase-io-root "https://scorching-torch-2540.firebaseio.com/")

;; Connection to Firebase
(defonce fb-root (m/connect firebase-io-root))

(register-handler
  :initialize-db
  (fn  [_ _]
    (let [user (js->clj (js/JSON.parse (.getItem js/localStorage "user")) :keywordize-keys true)]
      (if user
        (assoc db/default-db :user user)
        db/default-db))))

(register-handler
  :current-page
  (fn [db [_ page]]
    (merge db {:current-page page})))

(register-handler
  :set-user
  (fn [db [_ user-id]]
    (let [user (:user db)]
      (.setItem js/localStorage "user" (js/JSON.stringify  (clj->js (assoc-in user [:id] user-id))))
      (assoc-in db [:user :id] user-id))))

(register-handler
  :set-colors
  (fn [db [_ color]]
      (m/merge-in! fb-root [:games (:current-game db) :users (:id (:user db))] {:color-scheme color})
      (.setItem js/localStorage "user" (js/JSON.stringify  (clj->js (assoc-in (:user db) [:color-scheme] color))))
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
  (fn [db [_ game-id user]]
    (let [request (GET (str "/get-puzzle/" (name game-id))
                       {:response-format :json
                        :handler (fn [puzzle] (dispatch [:update-and-set-game [puzzle game-id user]]))})]
    ; track this request so we can abort it if user leaves the page while it's still generating
    (assoc db :pending-requests (conj (:pending-requests db) request)))))

(register-handler
  :update-and-set-game
  (fn [db [_ [puzzle game-id user]]]
    (let [puzzle-json (.stringify js/JSON (clj->js puzzle))
          session (str (name game-id) "-" (:id user) "-" (.now js/Date))]

      ; create game -- set puzzle (JSON string in Firebase, Clojure map in local state), id, and assign current user to this game
      (m/merge-in! fb-root [:games game-id] {:id (name game-id) :puzzle puzzle-json})

      ; merge user into game's user map
      (m/merge-in! fb-root [:games game-id :users (:id user)] user)

      ; add session to global sessions
      (m/merge-in! fb-root [:sessions session] {:id session})

      ; remove session on disconnect
      (.remove (m/on-disconnect (.child fb-root (str "/sessions/" session))))

      (merge db {:puzzle (convert-puzzle puzzle-json) :loading? false :current-game game-id :session session}))))

(register-handler
  :join-game
  (fn [db [_ game-id]]
    (let [id  (keyword game-id)
          user (:user db)]
      (if (seq (:id user))
        ; if user has session then let them retrieve or generate a puzzle, see update-and-set-game and generate-game
        (do
          ; check if puzzle exists, otherwise generate one
          (m/deref-in fb-root [:games id :puzzle]
                      (fn [puzzle]
                        (if (seq puzzle)
                          (dispatch [:update-and-set-game [(convert-puzzle puzzle) id user]])
                          (dispatch [:generate-game id user]))))
          ; listen for updates to this game's user list
          (def users-listener
            (-> fb-root
              (m/get-in [:games id :users])
              (m/listen-to :value
                            (fn [[_ v]] #_(log v) (dispatch [:user-list-update v])))))
          ; listen for updates to the game state
          (def game-state-listener
            (-> fb-root
              (m/get-in [:games id :game-state])
              (m/listen-to :value
                            (fn [[_ v]] #_(log v) (dispatch [:game-state-update v]))))))

        ; anonymous login for anyone without a session -- sets user and then loops back to join the game
        ; TODO: Move this elsewhere
        (do
            (m/auth-anon fb-root (fn [err auth-data]
                                (dispatch [:set-user (:uid auth-data)])
                                (dispatch [:join-game game-id])))))

    (merge db {:loading? true}))))

(register-handler
  :leave-game
  ; Remove all matchbox listeners here
  (fn [db _]
    (let [user-id (:id (:user db))
          current-game (:current-game db)
          requests (:pending-requests db)
          session (:session db)]
    ; clean up, go home
    (if session (m/dissoc-in! fb-root [:sessions (keyword session)]))
    (mr/disable-listener! users-listener)
    (mr/disable-listener! game-state-listener)
    (doseq [r requests] (.abort r))
    (merge db {:current-game nil :session nil :puzzle nil}))))

(register-handler
  :send-move
  (fn [db [_ [square letter user]]]
    (let [square-state {(marshal-square square) {:letter letter :user user}}]
      (m/merge-in! fb-root [:games (:current-game db) :game-state] square-state)
      ; update UI state optimistically
      (assoc-in db [:game-state (keyword (marshal-square square))] {:letter letter :user user}))))

;; Always listening for online games
(register-handler
  :get-current-games
  (fn [db _]
    (m/listen-list fb-root :sessions (fn [sessions]
                                       (let [current-games (set (map #(first (str/split (:id %) "-")) sessions))]
                                         (dispatch [:set-current-games current-games]))))
    db))

(register-handler
  :set-current-games
  (fn [db [_ games]]
    (if (seq games)
      (merge db {:current-games games})
      (merge db {:current-games {}}))))
