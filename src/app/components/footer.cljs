(ns app.components.footer)

(defn main []
  [:footer.footer
   [:span "Created by " [:a.dim.link {:href "https://github.com/velveteer" :target "_blank"} "Josh M."]]
   [:span.ph4 [:a.dim.link {:href "https://github.com/velveteer/crossed" :target "_blank"} [:i.fa.fa-3x.fa-github]]]
   [:span "With " [:a.link.dim {:href "https://github.com/ben-denham/clj-crosswords" :target "_blank"}  "code"] " from Ben D."]])


