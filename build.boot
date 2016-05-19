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
 '[environ.core                 :refer [env]]
 '[crisptrutski.boot-cljs-test  :refer [test-cljs]]
 '[deraen.boot-less             :refer [less]])

(task-options!
  pom {
    ;; needed to write the pom.xml file.
    :project (get-env :project)
    :version (get-env :version)

    ;; How to add in your project license
    :license {"Eclipse Public License"
              "http://www.eclipse.org/legal/epl-v10.html"}

    ;; And url.
    :url "https://juxt.pro/"}

 ;; beware the initial quote here too.
 ;; you could use :all true instead
 aot {:namespace '#{app.core}}
 jar {:main 'app.server.core}
 serve {:httpkit true
         :handler 'app.server.core/app
         :port (Integer/parseInt (:port env "8080"))})

(deftask dev []
  (comp (serve :reload true)
        (watch)
        (reload :on-jsload 'app.core/mount-root :ws-host "166.78.47.104" :port 34769)
        (cljs :optimizations :none :source-map true)
        (less)
        (target :dir #{"target"})))

(deftask build-cljs []
  (comp
    (less :compression true)
    (cljs :optimizations :advanced :compiler-options {:closure-defines {"goog.DEBUG" false}})
    (target :dir #{"dist"})))
  
(deftask build []
  (comp
   (build-cljs)
   (aot :namespace '#{app.server.core})
   (pom :project 'crossed
        :version "1.0.0")
   (uber)
   (jar :main 'app.server.core)))

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
