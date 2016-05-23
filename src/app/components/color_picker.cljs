(ns app.components.color-picker
    (:require-macros [app.logging :refer [log]]
                     [reagent.ratom :refer [reaction]])
    (:require [app.colors :as c]
              [re-frame.core :refer [subscribe dispatch]]))

(defn color-square [label scheme selected]
  (fn [label scheme selected]
    [:button.color-square {:on-click (fn [] (dispatch [:set-colors label]))
                           :title label
                           :class (if (= label selected) "color-selected")
                           :style {:color (:color scheme) :background-color (:background-color scheme) :border (str "3px solid" (:color scheme))}}
     [:span.ttu.tracked.f6 label]]))

(defn color-select []
  (let [user (subscribe [:user])
        selected (reaction (:color-scheme @user))]
    (fn []
      [:div.pt3.color-select
       (doall (for [[k v] c/colors]  ^{:key k} [color-square (name k) v @selected]))
       ])))

(defn main []
  (fn [] [color-select]))
