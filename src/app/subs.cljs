(ns app.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :refer [register-sub]]))

(register-sub
  :current-page
  (fn [db] (reaction (:current-page @db))))

