(ns app.components.nav
  (:require-macros [app.logging :refer [log]]
                   [reagent.ratom :refer [reaction]])
  (:require [app.routes :as routes]
            [re-frame.core :refer [dispatch subscribe]]))

(defn user-avatar [user score]
  (fn [user score]
    [:div.user-avatar.mw3.absolute.top-1.right-1
     [:div.br-100.w3.h3.dim {:style {:cursor "pointer" :background (str "url(" (.-photoURL user) ")")}
                             :title "Logout"
                             :on-click (fn [] (dispatch [:toggle-login]))
                             }]]))

(defn main []
  (let [user (subscribe [:user])
        scores (subscribe [:scores])
        my-score (reaction (if @user (get @scores (keyword (.-uid @user)) nil)))]
    (fn []
      [:nav.pa3.tc
        [:a.mw6.center.header.link.dark-gray.f-subheadline.db.mb3 {:href (routes/path-for :home) :title "Leave game"} "Crossed"]
        (if @user [user-avatar @user @my-score])
        ])))
