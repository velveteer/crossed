(ns app.components.nav
  (:require-macros [app.logging :refer [log]]
                   [reagent.ratom :refer [reaction]])
  (:require [app.routes :as routes]
            [re-frame.core :refer [dispatch subscribe]]))

(defn user-avatar [user score]
  (fn [user score]
    [:div.mw3.absolute.top-1.right-1
     [:div.avatar.br-100.relative.grow {:style {:cursor "pointer" :background (str "url(" (aget user "photoURL") ")")}
                             :title "Logout"
                             :on-click (fn [] (dispatch [:toggle-login]))}
                             [:i.fa.fa-2x.fa-sign-out.absolute.top-1.left-1.white]
                             ]]))

(defn main []
  (let [user (subscribe [:user])
        scores (subscribe [:scores])
        my-score (reaction (if @user (get @scores (keyword (aget @user "uid")) nil)))]
    (fn []
      [:nav.pa3.tc
        [:a.mw6.mt3.center.header.link.dark-gray.f-subheadline.db.mb3 {:href (routes/path-for :home) :title "Leave game"} "Crossed"]
        (if @user [user-avatar @user @my-score])
        ])))
