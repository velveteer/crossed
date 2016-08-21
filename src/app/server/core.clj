(ns app.server.core
  (:use [org.httpkit.server :only [run-server]])
  (:require [compojure.core :refer [GET defroutes]]
            [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-js include-css]]
            [compojure.route :refer [not-found]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.util.response :refer [response content-type]]
            [ring.util.http-response :refer [ok]]
            [environ.core :refer [env]]
            [clojure.data.json :as json]
            [app.server.generator :refer [generate-crossword]])
  (:gen-class))

(def index-page
  (html
   (html5
    [:head
     [:title "Crossed"]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     [:link {:rel "shortcut icon" :href "/images/favicon.ico" :type "image/x-icon"}]
     [:link {:rel "icon" :href "/images/favicon.ico" :type "image/x-icon"}]
     (include-css "https://npmcdn.com/tachyons@4.1.0/css/tachyons.min.css")
     (include-css "https://maxcdn.bootstrapcdn.com/font-awesome/4.6.1/css/font-awesome.min.css")
     (include-css "/styles/app.css")]
    [:body
     [:div#app]
     (include-js "/scripts/app.js")
     (include-js "/scripts/backspace.js")
     ])))

(defn json-response [data]
  {:body (json/write-str data)
   :status 200
   :headers {"Content-Type" "application/json"} })

(defmacro r
  "Save precious characters by automatically json-responsing"
  [method route params & body]
  `(~method ~route ~params (json-response ~@body)))

(defroutes app-routes
  (r GET "/get-puzzle/:id" [id]
    (-> (generate-crossword id (str (rand-int 1000000)))))
  (GET "/" []
    (-> (ok index-page) (content-type "text/html")))
  (not-found (-> (ok index-page) (content-type "text/html"))))

(def app (-> app-routes
             (wrap-resource "public")
             (wrap-content-type)
             (wrap-keyword-params)
             (wrap-params)))

(defn -main [& args]
  (let [port (Integer/parseInt (or (env :port) 8080))]
    (run-server app {:port port})))
