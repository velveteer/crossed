(ns app.components.nav
  (:require [app.routes :as routes]))

(defn main []
    (fn []
      [:nav.pa3.pa4-ns.pb0-ns.tc
        [:a.header.link.dim.dark-gray.f-subheadline.db.mb3 {:href (routes/path-for :home) :title "Leave game"} "Crossed"]
        [:a.mw1.absolute.top-1.right-2.link.dim.black.db.mb3 {:href "https://github.com/velveteer/crossed" :title "Source"} [:i.fa.fa-2x.fa-github] ""]
        ]))
