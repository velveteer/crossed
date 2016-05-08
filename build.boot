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
                 [org.clojure/clojurescript     "1.7.228"]
                 [binaryage/devtools            "0.6.1"]
                 [reagent                       "0.6.0-alpha"]
                 [re-frame                      "0.7.0"]
                 [bidi                          "2.0.8"]
                 [kibu/pushy                    "0.3.6"]])
(require
 '[adzerk.boot-cljs             :refer [cljs]]
 '[adzerk.boot-cljs-repl        :refer [cljs-repl]]
 '[adzerk.boot-reload           :refer [reload]]
 '[pandeiro.boot-http           :refer [serve]]
 '[crisptrutski.boot-cljs-test  :refer [test-cljs]]
 '[deraen.boot-less             :refer [less]])

(deftask dev []
  (comp (serve :dir "target" :not-found 'dev.not-found/not-found-handler :port 8080)
        (watch)
        (reload :on-jsload 'app.core/mount-root)
        (speak)
        (cljs-repl)
        (cljs :optimizations :none :source-map true)
        (less)
        (target :dir #{"target"})))

(deftask build []
  (comp
    (less :compression true)
    (cljs :optimizations :advanced :compiler-options {:closure-defines {"goog.DEBUG" false}})
    (target :dir #{"dist"})))

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
