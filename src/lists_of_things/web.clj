(ns lists-of-things.web
  (:gen-class)
  (:require
    [lists-of-things
     [api :as api]
     [helpers :as helpers]
     [interface :as interface]
     [wrappers :as wrappers]]
    [compojure
     [handler :as handler]
     [core :as compojure]
     [route :as route]]
    [ring.adapter
     [jetty :as jetty]]
    [ring.middleware
     [gzip :as gzip]]))

(def api-controller
  (-> api/routes
      wrappers/json
      wrappers/jsonp))

(def interface-controller
  interface/routes)

(compojure/defroutes routes
  interface-controller
  (compojure/context "/api" [] api-controller)
  (route/resources "/")
  (route/not-found helpers/not-found))

(def app-controller (-> routes handler/site wrappers/connection gzip/wrap-gzip))

(defn -main [& args]
  (jetty/run-jetty app-controller {:port 3000}))

