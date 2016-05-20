;; vi:syntax=clojure

(set-env!
 :source-paths    #{"src"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs              "1.7.228-1"       :scope "test"]
                 [adzerk/boot-cljs-repl         "0.3.0"           :scope "test"]
                 [adzerk/boot-reload            "0.4.7"           :scope "test"]
                 [pandeiro/boot-http            "0.7.3"           :scope "test"]
                 [com.cemerick/piggieback       "0.2.1"           :scope "test"]
                 [crisptrutski/boot-cljs-test   "0.2.2-SNAPSHOT"  :scope "test"]
                 [weasel                        "0.7.0"           :scope "test"]
                 [org.clojure/tools.nrepl       "0.2.12"          :scope "test"]
                 [deraen/boot-less              "0.2.1"           :scope "test"]

                  ; Clojure
                 [org.clojure/clojure           "1.7.0"]
                 [hiccup                        "1.0.5"]
                 [environ                       "1.0.0"]
                 [compojure                     "1.1.5"]
                 [ring/ring-core                "1.4.0"]
                 [metosin/ring-http-response    "0.6.5"]
                 [org.clojure/data.json         "0.2.6"]
                 [http-kit                      "2.1.18"]

                 ; Clojurescript
                 [org.clojure/clojurescript     "1.7.228"]
                 [cljs-ajax                     "0.5.4"]
                 [binaryage/devtools            "0.6.1"]
                 [reagent                       "0.6.0-alpha"]
                 [re-frame                      "0.7.0"]
                 [bidi                          "2.0.8"]
                 [kibu/pushy                    "0.3.6"]
                 [matchbox                      "0.0.8-SNAPSHOT"]])
(require
 '[adzerk.boot-cljs             :refer [cljs]]
 '[adzerk.boot-cljs-repl        :refer [cljs-repl]]
 '[adzerk.boot-reload           :refer [reload]]
 '[pandeiro.boot-http           :refer [serve]]
 '[crisptrutski.boot-cljs-test  :refer [test-cljs]]
 '[deraen.boot-less             :refer [less]])

(deftask dev []
  (comp
        (watch)
        (serve :reload true :port 8080 :handler 'app.server.core/app :httpkit true)
        (reload :on-jsload 'app.core/mount-root)
        (less)
        (cljs :optimizations :none :source-map true)))

(deftask build-frontend []
  (comp
    (less :compression true)
    (cljs :optimizations :advanced :compiler-options {:closure-defines {"goog.DEBUG" false}})))

(deftask build []
  (comp
   (build-frontend)
   (aot :namespace '#{app.server.core})
   (pom :project 'crossed
        :version "1.0.0")
   (uber)
   (jar :file "crossed.jar" :main 'app.server.core)
   (sift :include #{#"\.jar$"})
   (target)))

(deftask testing []
  (merge-env! :resource-paths #{"test/cljs"})
  (task-options! test-cljs {:namespaces '#{app.test} :js-env :phantom})
  identity)

(deftask test-once []
  (comp (testing)
        (test-cljs :exit? true)))

(deftask auto-test []
  (comp (testing)
        (watch)
        (test-cljs)))
