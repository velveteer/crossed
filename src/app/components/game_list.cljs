(ns app.components.game-list
  (:require-macros [app.logging :refer [log]]
                   [reagent.ratom :refer [reaction]])
  (:require [app.routes :as routes]
            [app.util :refer [convert-puzzle]]
            [reagent.core :refer [create-class]]
            [re-frame.core :refer [dispatch subscribe]]))

(defn get-progress [game]
  (let [puzzle (convert-puzzle (get (val game) :puzzle))
        solved-count (count (map (fn [[k v]] (get v :solved)) (:game-state (val game))))
        cells (for [row (:grid puzzle)]
                (for [cell row]
                  (if (not (keyword? cell))
                    (count cell))))
        cell-count (reduce #(+ %1 (second %2)) 0 cells)]

    (* 100 (/ solved-count cell-count))))

(defn game-row [game]
  (fn [game]
    (let [game-id (name (key game))]
      [:div.ph3.dib
       [:p.mb0.mt4.center {:style {:width "150px" :overflow "hidden" :text-overflow "ellipsis"}} game-id]
       [:p.mb0 (str "Players: " (count (get (val game) :users)))]
       [:p.mb0 (str "Progress: " (str (.round js/Math (get-progress game)) "%"))]
       [:div
        [:button.btn.f6.f5-ns.ttu
         {:on-click (fn [] (routes/set-token! (str "/" game-id)))}
         [:span.f6 "Join Game"]]]])))

(defn main []
  (let [all-games (subscribe [:all-games])
        loading? (subscribe [:loading-games?])]
    (create-class
      {:component-did-mount
       #(dispatch [:get-all-games])

       :display-name "games-list"

       :reagent-render
       (fn []
         (if @loading? [:div.spinner]
           [:section.mw7.center
            [:h2.f4.ttu.tracked.mt5.pb1.bb.bw1.b--light-gray.mw6.center.dark-gray "Open Games:"]
            (if (seq @all-games)
              [:div.pb3 (for [game @all-games] ^{:key (name (key game))} [game-row game])]
              [:h2.f4.fw3.i "How odd, there are no games yet."])
            ]))}
         )))
