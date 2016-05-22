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
    db/default-db))

(register-handler
  :current-page
  (fn [db [_ page]]
    (merge db {:current-page page})))

(register-handler
  :set-user
  (fn [db [_ id]]
    (let [user (:user db)]
    (.setItem js/localStorage "user" (js/JSON.stringify  (clj->js (assoc-in user [:id] id))))
    (assoc-in db [:user :id] id))))

(register-handler
  :set-colors
  (fn [db [_ color]]
      (m/merge-in! fb-root [(:current-game db) :users (:id (:user db))] {:color-scheme color})
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
  (fn [db [_ game-id]]
    (let [request (GET (str "/get-puzzle/" (name game-id))
                       {:response-format :json
                        :keywords? false
                        :handler #(dispatch [:update-and-set-puzzle [% game-id]])})]
    ; return loading since new puzzle will take a sec
    (assoc db :loading? true :pending-requests (conj (:pending-requests db) request)))))

(register-handler
  :update-and-set-puzzle
  (fn [db [_ [puzzle game-id]]]
    (let [puz (m/get-in fb-root [game-id :puzzle])]
      ; new game is made with id key
      (m/merge-in! fb-root [game-id] {:id  (name game-id)})
      ; set puzzle to JSON string of puzzle data
      (m/reset! puz (.stringify js/JSON (clj->js puzzle)))
      ; once puzzle is updated, listen for changes and set local state with converted puzzle data
      (def puzzle-listener
        (m/listen-to puz :value
                     (fn [[_ v]] (dispatch [:set-puzzle (convert-puzzle v)])))))
    db))

(register-handler
  :set-puzzle
  (fn [db [_ puzzle]]
    ; set user in game's user-list here because puzzle is available
    ; otherwise we lose sync when users join games with puzzles still generating
    (let [user (:user db)]
      (if (seq puzzle)
        (do
          (m/merge-in! fb-root [(:id puzzle) :users] {(:id user) user})
          (merge db {:puzzle puzzle :loading? false :current-game (:id puzzle)}))
          (merge db {:puzzle nil :loading? false :current-game nil})))))

(register-handler
  :join-game
  (fn [db [_ game-id]]
    (let [id  (keyword game-id)
          user (:user db)]
      (if (seq (:id user))
        ; if user has session then let them set or generate a puzzle, see set-puzzle
        (do
          ; check if puzzle exists, otherwise generate one
          (m/deref-in fb-root [id :puzzle]
                      (fn [value]
                        (if (seq value)
                          (dispatch [:set-puzzle (convert-puzzle value)])
                          (dispatch [:generate-game id]))))
          ; remove user from game on disconnect
          (->> false (.set (m/on-disconnect (.child fb-root (str (name game-id) "/users/" (:id user) "/online?")))))
          ; listen for updates to this game's user list
          (def users-listener
            (-> fb-root
              (m/get-in [id :users])
              (m/listen-to :value
                            (fn [[_ v]] #_(log v) (dispatch [:user-list-update v])))))
          ; listen for updates to the game state
          (def game-state-listener
            (-> fb-root
              (m/get-in [id :game-state])
              (m/listen-to :value
                            (fn [[_ v]] #_(log v) (dispatch [:game-state-update v]))))))

        ;; anonymous login for anyone without a session -- sets user and then loops back to join the game
        (do
            (m/auth-anon fb-root (fn [err auth-data]
                                (dispatch [:set-user (:uid auth-data)])
                                (dispatch [:join-game game-id]))))))

    (merge db {:current-game nil :loading? (nil? (:puzzle db)) :user-games (conj (:user-games db) game-id)})))

(register-handler
  :leave-game
  ; TODO: Remove all matchbox listeners here
  (fn [db _]
    (let [user (:user db)
          current-game (:current-game db)
          requests (:pending-requests db)]
    (if current-game (doseq [game (:user-games db)] (m/merge-in! fb-root [game :users (:id user)] {:online? false})))
    (mr/disable-listener! users-listener)
    (mr/disable-listener! game-state-listener)
    (mr/disable-listener! puzzle-listener)
    (doseq [r requests] (.abort r))
    (merge db {:current-game nil :user-games [] :puzzle nil}))))

(register-handler
  :send-move
  (fn [db [_ [square letter user]]]
    (let [square-state {(marshal-square square) {:letter letter :user user}}]
      (m/merge-in! fb-root [(:current-game db) :game-state] square-state)
      ; update UI state optimistically
      (assoc-in db [:game-state (keyword (marshal-square square))] {:letter letter :user user}))))

(register-handler
  :get-current-games
  (fn [db _]
      (m/listen-list fb-root (fn [games]
                               (let [has-onlines? (fn [game] (->> game (map (fn [k v] (:online? k)))))
                                     current-games (->> games (filter (fn [k v] (some true? (has-onlines? (vals (:users k)))))))]
                                   (dispatch [:set-current-games current-games]))))

    db))

(register-handler
  :set-current-games
  (fn [db [_ games]]
    (if (seq games)
      (merge db {:current-games games})
      (merge db {:current-games {}}))))
