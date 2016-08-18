(ns app.subs
    (:require-macros [reagent.ratom :refer [reaction]]
                     [app.logging :refer [log]])
    (:require [re-frame.core :refer [register-sub]]))

(register-sub
  :current-page
  (fn [db] (reaction (:current-page @db))))

(register-sub
  :initializing?
  (fn [db] (reaction (:initializing? @db))))

(register-sub
  :loading?
  (fn [db] (reaction (:loading? @db))))

(register-sub
  :loading-games?
  (fn [db] (reaction (:loading-games? @db))))

(register-sub
  :puzzle
  (fn [db] (reaction (:puzzle @db))))

(register-sub
  :game-state
  (fn [db] (reaction (:game-state @db))))

(register-sub
  :user
  (fn [db] (reaction (:user @db))))

(register-sub
  :user-list
  (fn [db] (reaction (:user-list @db))))

(register-sub
  :scores
  (fn [db] (reaction (:scores @db))))

(register-sub
  :color-scheme
  (fn [db] (reaction (:color-scheme @db))))

(register-sub
  :all-games
  (fn [db] (reaction (:all-games @db))))
