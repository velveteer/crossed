(ns dev.not-found
  (:require [ring.util.response :refer [file-response]]))

(defn not-found-handler [request]
  (assoc-in
    (file-response "target/index.html")
    [:headers "Content-Type"]
    "text/html;charset=utf8"))
