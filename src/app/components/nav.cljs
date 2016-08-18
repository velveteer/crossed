(ns app.components.nav
  (:require-macros [app.logging :refer [log]]
                   [reagent.ratom :refer [reaction]])
  (:require [app.routes :as routes]
            [re-frame.core :refer [dispatch subscribe]]))

(defn user-avatar [user score]
  (fn []
    [:div.user-avatar.mw3.center
     [:div.br-100.w3.h3 {:style {:background (str "url(" (.-photoURL user) ")")}}] ;:on-click (fn [] (dispatch [:toggle-login]))}]
                                 [:div.user-list-scores.blue {:style {:position "relative"}} (str @score)]]
      ))

(defn main []
  (let [user (subscribe [:user])
        scores (subscribe [:scores])
        my-score (reaction (if @user (get @scores (keyword (.-uid @user)) nil)))]
    (fn []
      [:nav.pa3.tc
        [:a.mw6.center.header.link.dark-gray.f-subheadline.db.mb3 {:href (routes/path-for :home) :title "Leave game"} "Crossed"]
        #_(if @user [user-avatar @user my-score])
        ])))
