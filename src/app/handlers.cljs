(ns app.handlers
  (:require-macros [app.logging :refer [log]])
  (:require [re-frame.core :refer [register-handler dispatch dispatch-sync]]
            [clojure.string :as str]
            [firebase-cljs.core :as f]
            [firebase-cljs.database :as fd]
            [firebase-cljs.database.query :as fdq]
            [firebase-cljs.database.reference :as fdr]
            [firebase-cljs.database.datasnapshot :as s]
            [firebase-cljs.auth :as fa]
            [firebase-cljs.auth.provider :as fap]
            [firebase-cljs.user :as u]
            [ajax.core :refer [GET]]
            [app.colors :as c]
            [app.routes :refer [set-token!]]
            [app.util :refer [marshal-square convert-puzzle]]
            [app.db :as db]))

;; Connection to Firebase
(def opts { :apiKey "AIzaSyCIw_pe2ZnghxjTb4pHlTGvtUJSoCMhe_U",
            :authDomain "project-1130682223484791178.firebaseapp.com",
            :databaseURL "https://project-1130682223484791178.firebaseio.com",
            :storageBucket "project-1130682223484791178.appspot.com" })

(defonce app (f/init opts))
(defonce database (f/get-db app))
(defonce auth (f/get-auth app))
(defonce root (fd/get-ref database))
(defonce games (fdr/get-child root "/games"))
(defonce users (fdr/get-child root "/users"))

(register-handler
  :init
  (fn [db _]
    (fa/auth-changed auth (fn [user] (dispatch [:set-user user])))
    (merge db/default-db {:initializing? true})))

(register-handler
  :toggle-login
  (fn [db _]
    (let [user (:user db)
          provider (fap/google)]
      (if (nil? user)
          (do
            (fap/scope provider "https://www.googleapis.com/auth/plus.login")
            (fa/login-popup auth provider))
          (do
            (fa/logout auth)
            (dispatch [:set-user nil])
            (dispatch [:redirect-to-login])))
      db)))

(register-handler
  :current-page
  (fn [db [_ page]]
    (merge db {:current-page page})))

(register-handler
  :set-user
  (fn [db [_ user]]
    (let [color-scheme (.getItem js/localStorage "color-scheme")]
      (if user
        (do
          (fdr/update! (fdr/get-child users (u/uid user)) (clj->js {:name (u/name user)}))
          (dispatch [:get-user-score (u/uid user)])))
      (merge db {:user user :initializing? false :color-scheme (if color-scheme color-scheme "classic")}))))

(register-handler
  :get-user-score
  (fn [db [_ uid]]
    (let [user-ref (fdr/get-child users uid)
          score-ref (fdr/get-child user-ref "/score")]
      (fdq/on score-ref "value"
              (fn [score]
                #_(log (s/val score))
                (dispatch [:set-user-score uid (s/val score)]))))
    db))

(register-handler
  :set-user-score
  (fn [db [_ uid score]]
    (assoc-in db [:scores (keyword uid)] score)))


(register-handler
  :redirect-to-login
  (fn [db _]
    (set-token! "/")
    (merge db {:loading? false :initializing? false})))

(register-handler
  :set-color
  (fn [db [_ color]]
       (let [uid (u/uid (:user db))
             users-ref (:users-ref db)
             user-ref (fdr/get-child users-ref uid)]
         (fdr/update! user-ref (clj->js {:color-scheme color}))
         (.setItem js/localStorage "color-scheme" color)
         (merge db {:color-scheme color}))))

(register-handler
  :generate-game
  (fn [db [_ game-id game-ref]]
    (let [request (GET (str "/get-puzzle/" game-id)
                       {:response-format :json
                        :handler (fn [puzzle] (dispatch [:update-and-set-game [game-ref puzzle]]))})]
    ; track this request so we can abort it if user leaves the page while it's still generating
    (assoc db :pending-requests (conj (:pending-requests db) request)))))

(register-handler
  :update-and-set-game
  (fn [db [_ [game-ref puzzle]]]
    (let [user (:user db)
          users-ref (fdr/get-child game-ref "users")
          user-ref (fdr/get-child users-ref (u/uid user))
          puzzle-json (.stringify js/JSON (clj->js puzzle))]

      ; create game -- set puzzle (JSON string in Firebase, Clojure map in local state) and assign current user to this game
      (fdr/update! game-ref (clj->js {:puzzle puzzle-json}))
      (fdr/update! user-ref (clj->js {:name (u/name user)
                                      :color-scheme (:color-scheme db)
                                      :uid (u/uid user)
                                      :image (u/photo-url user)
                                      }))

      (merge db {:puzzle (convert-puzzle puzzle-json) :loading? false}))))

(register-handler
  :join-game
  (fn [db [_ game-id]]
    (let [game-ref (fdr/get-child games game-id)
          game-state-ref (fdr/get-child game-ref "game-state")
          users-ref (fdr/get-child game-ref "users")]

      ; check if game exists, otherwise generate one
      (fdq/once game-ref "value"
                (fn [game]
                  (if (s/val game)
                    (dispatch [:update-and-set-game [game-ref (convert-puzzle (.-puzzle (s/val game)))]])
                    (dispatch [:generate-game game-id game-ref]))))
      ; listen for updates to the game state
      (fdq/on game-state-ref "value"
              (fn [state]
                #_(log (s/val state))
                (dispatch [:game-state-update (f/->cljs (s/val state))])))
      ; listen for updates to this game's user list
      (fdq/on users-ref "value"
              (fn [users]
                #_(log (s/val users))
                (dispatch [:user-list-update (f/->cljs (s/val users))])))

    (merge db {:loading? true :game-state-ref game-state-ref :game-ref game-ref :users-ref users-ref}))))

(register-handler
  :leave-game
  (fn [db _]
    (let [game-state-ref (:game-state-ref db)
          users-ref (:users-ref db)
          requests (:pending-requests db)]
    ; clean up, go home
    (if game-state-ref (fdq/off game-state-ref))
    (if users-ref (fdq/off users-ref))
    (doseq [r requests] (.abort r))
    (merge db {:user-list nil :puzzle nil :loading? false :game-state-ref nil :users-ref nil :game-ref nil}))))

(register-handler
  :send-move
  (fn [db [_ [square letter]]]
    (let [user (:user db)
          square-user (if (nil? letter) nil (u/uid user))
          game-state-ref (:game-state-ref db)
          square-state {(keyword (marshal-square square)) {:letter letter :user square-user}}]
      (fdr/update! game-state-ref (clj->js square-state))
      ; update UI state optimistically
      (assoc-in db [:game-state (keyword (marshal-square square))] {:letter letter :user square-user}))))

(register-handler
  :user-list-update
  (fn [db [_ v]]
    (let [uid (u/uid (:user db))
          users (filter #(not= (str uid) (name %)) (keys v))]
    (merge db {:user-list v}))))

(register-handler
  :game-state-update
  (fn [db [_ v]]
    (assoc db :game-state v)))

(register-handler
  :solve-word
  (fn [db [_ word squares skeys]]
    (let [game-state (:game-state db)
          game-state-ref (:game-state-ref db)
          game-ref (:game-ref db)
          user-squares (group-by #(get % :user) (filter #(not (:solved %)) squares))
          scores (reduce-kv #(assoc %1 %2 (count %3)) {} user-squares)
          state (into {} (filter (fn [[k v]] (some #(= % k) skeys)) game-state))
          new-state (reduce-kv #(assoc %1 %2 (assoc %3 :solved true)) {} state)]
      (fdr/update! game-state-ref (clj->js new-state))
      (doseq [[uid s] scores]
        (fdr/transaction (fdr/get-child game-ref (str "/users/" uid "/score"))
                         (fn [score]
                           (+ score s)))
        (fdr/transaction (fdr/get-child users (str "/" uid "/score"))
                         (fn [score]
                           (+ score s))))
      db)))

(register-handler
  :get-all-games
  (fn [db _]
    (let [all-games (:all-games db)]
      (fdq/once (fdq/take-last games 10) "value"
                (fn [games]
                  (dispatch [:set-all-games (f/->cljs (s/val games))])))
      (merge db {:loading-games? (not (seq all-games))}))))

(register-handler
  :set-all-games
  (fn [db [_ games]]
    (merge db {:all-games games :loading-games? false})))
