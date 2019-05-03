(ns eig.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [eig.events :as events]
            [eig.views :as views]))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
