(ns app.routes
  (:require-macros [app.logging :refer [log]])
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]))

(def routes ["/" { "" :home
                  ["game/" [keyword :game-id]] :game}])

(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [match]
  (case (:handler match)
    :home (re-frame/dispatch [:current-page :home])
    :game (do 
              (re-frame/dispatch [:current-page :game])
              (re-frame/dispatch [:join-game (-> match :route-params :game-id)]))))

(def history (pushy/pushy dispatch-route (partial bidi/match-route routes)))

(defn start! []
  (pushy/start! history))

(defn set-token! [token]
  (pushy/set-token! history token))

(defn path-for [tag & args]
  (apply bidi/path-for routes tag args))