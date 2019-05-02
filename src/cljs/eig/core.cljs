(ns eig.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [eig.events :as events]
            [eig.views :as views]
            [eig.config :as config]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (accountant/configure-navigation!
   {:nav-handler  (fn [path] (secretary/dispatch! path))
    :path-exists? (fn [path] (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
