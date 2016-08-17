(ns app.components.nav
  (:require-macros [app.logging :refer [log]])
  (:require [app.routes :as routes]
            [re-frame.core :refer [dispatch subscribe]]))

(defn user-avatar [user]
  (fn []
    [:div.br-100.w3.h3 {:style {:background (str  "url(" (.-photoURL user) ")")}
                        :on-click (fn [] (dispatch [:toggle-login]))}
      ]))

(defn main []
  (let [user (subscribe [:user])]
    (fn []
      [:nav.pa3.pa4-ns.pb0-ns.tc
        [:a.mw6.center.header.link.dim.dark-gray.f-subheadline.db.mb3 {:href (routes/path-for :home) :title "Leave game"} "Crossed"]
        [:div.absolute.top-1.right-2.link.dim.black.db.mb3
         (if @user [user-avatar @user])]
        ])))
