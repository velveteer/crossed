(ns app.components.color-picker
    (:require-macros [app.logging :refer [log]])
    (:require [app.colors :as c]
              [re-frame.core :refer [subscribe dispatch]]
              [clojure.string :as str]))

(defn color-select []
    (let [user (subscribe [:user])]
        (fn [] 
            [:div.tc.pt1
             [:select.pa1 {:value (:color-scheme @user) :on-change (fn [ev] (dispatch [:set-colors (.. ev -target -value)]))}
              (for [[k v] c/colors] ^{:key k} [:option {:value k} (name k)])
              ]])))
    
(defn main []
    (fn []
        [:div
            [color-select]]))