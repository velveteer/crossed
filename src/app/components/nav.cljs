(ns app.components.nav
  (:require [app.routes :as routes]))

(defn main []
  (fn []
    [:nav.pa3.pa4-ns.pb0-ns
     [:a.link.dim.black.b.f1.f-headline-ns.tc.db.mb3.mb4-ns {:href (routes/path-for :home)} "Crossed"]
     [:div.tc.pb3
        [:a.link.dim.gray.f6.f5-ns.dib.mr3.ttu
         {:style {:cursor "pointer"}
          :on-click (fn [] (routes/set-token! (str "/game/" (rand-int 1000000))))}
          [:i.fa.fa-meh-o.mr2] "Create New Game"]
    ]]))
