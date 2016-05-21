(ns app.pages.home
  (:require-macros [app.logging :refer [log]]
                   [reagent.ratom :refer [reaction]])
  (:require [app.routes :as routes]
            [re-frame.core :refer [dispatch subscribe]]))

(defn random-string [length]
  (let [ascii-codes (concat (range 66 91) (range 97 123))]
  (apply str (repeatedly length #(char (rand-nth ascii-codes))))))

(defn main []
  (let [games (subscribe [:current-games])
        ids (reaction (map #(:id %) @games))]
  (fn []
    [:section
        [:div.tc.pb3 (if (count @games) [:span.f6 (str "Current Live Games: " (count @games))])]
        [:div.tc.pb3
            [:button.btn.btn--green.f6.f5-ns.dib.mr3.ttu
            {:on-click (fn [] (routes/set-token! (str "/" (random-string 24))))}
              [:span "Create New Game"]]
            [:button.btn.f6.f5-ns.dib.mr3.ttu
              { :on-click (fn [] (routes/set-token! (str "/" (name (rand-nth (seq @ids))))))
                :disabled (= (count @games) 0)}
              [:span "Join Random Game"]]]])))
