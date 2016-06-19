(ns app.components.game-list
  (:require-macros [app.logging :refer [log]]
                   [reagent.ratom :refer [reaction]])
  (:require [app.routes :as routes]
            [re-frame.core :refer [dispatch subscribe]]))

(defn game-row [game-id]
  (fn [game-id]
    [:div.center.mw6.ph3 {:style {:display "flex" :flex-direction "column" :align-items "center" :justify-content "space-between"}}
     [:p.mb0.mt4.dib {:style {:width "150px" :overflow "hidden" :text-overflow "ellipsis"}}  game-id]
     [:div
      [:button.btn.f6.f5-ns.ttu
        {:on-click (fn [] (routes/set-token! (str "/" game-id)))}
      [:span.f6 "Join Game"]]]]))

(defn main [games]
  (fn [games]
    [:section
     [:h2.f4.ttu.tracked.mt5.pb1.bb.bw1.b--light-gray.mw6.center.dark-gray "Current Games:"]
     (if (seq games)
      [:div.tc.pb3 (for [game-id games] ^{:key game-id} [game-row game-id])]
      [:h2.f4.fw3.i "Nobody is playing right now."])
     ]
    ))
