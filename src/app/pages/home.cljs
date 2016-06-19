(ns app.pages.home
  (:require-macros [app.logging :refer [log]]
                   [reagent.ratom :refer [reaction]])
  (:require [app.routes :as routes]
            [app.components.game-list :as game-list]
            [re-frame.core :refer [dispatch subscribe]]))

(defn random-string [length]
  (let [ascii-codes (concat (range 66 91) (range 97 123))]
  (apply str (repeatedly length #(char (rand-nth ascii-codes))))))

(defn main []
  (let [current-games (subscribe [:current-games])]
        ;all-games (subscribe [:all-games])
        ;games (reaction (filter #(contains? @current-games (:id %)) @all-games))]
    (fn []
      [:section
       [:div.tc.pb3
        [:button.btn.btn--green.f6.f5-ns.dib.mr3.ttu
         {:on-click (fn [] (routes/set-token! (str "/" (random-string 24))))}
         [:span.f6 "Create New Game"]]
       [game-list/main (seq  @current-games)]
        ]])))
