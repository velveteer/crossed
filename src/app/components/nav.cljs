(ns app.components.nav
  (:require [app.routes :as routes]))

(defn main []
    (fn []
      [:nav.pa3.pa4-ns.pb0-ns
        [:a.header.link.dim.black.f1.f-subheadline-ns.tc.db.mb3 {:href (routes/path-for :home)} "Crossed"]]))
