(ns app.components.nav
  (:require [app.routes :as routes]))

(defn main []
  (fn []
    [:nav.pa3.pa4-ns.pb0-ns
     [:a.link.dim.black.b.f1.f-headline-ns.tc.db.mb3.mb4-ns {:href (routes/path-for :home)} "Crossed"]
     [:div.tc.pb3
        [:a.link.dim.gray.f6.f5-ns.dib.mr3 {:href (routes/path-for :home)} [:i.fa.fa-home.mr1] "Home"]
        [:a.link.dim.gray.f6.f5-ns.dib.mr3 {:href (routes/path-for :new-game)} [:i.fa.fa-at.mr1] "Create New Game"]
    ]]))