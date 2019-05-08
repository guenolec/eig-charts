(ns eig.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [eig.events :as events]
            [eig.views :as views]
            [accountant.core :as accountant]
            [secretary.core :as secretary :include-macros true]))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(secretary/defroute "/#a-propos" []
  (re-frame/dispatch [::events/set-view! "a-propos"]))
(secretary/defroute "/#carte" []
  (re-frame/dispatch [::events/set-view! "carte"]))
(secretary/defroute "/#promo" []
  (re-frame/dispatch [::events/set-view! "promo"]))
(secretary/defroute "/#profils" []
  (re-frame/dispatch [::events/set-view! "profils"]))
(secretary/defroute "/#eig2017" []
  (re-frame/dispatch [::events/set-view! "eig2017"]))
(secretary/defroute "/#eig2018" []
  (re-frame/dispatch [::events/set-view! "eig2018"]))
(secretary/defroute "/#eig2019" []
  (re-frame/dispatch [::events/set-view! "eig2019"]))
(secretary/defroute "/#financement" []
  (re-frame/dispatch [::events/set-view! "financement"]))
(secretary/defroute "/#depenses" []
  (re-frame/dispatch [::events/set-view! "depenses"]))
(secretary/defroute "/#accompagnement" []
  (re-frame/dispatch [::events/set-view! "accompagnement"]))
(secretary/defroute "/#communication" []
  (re-frame/dispatch [::events/set-view! "communication"]))

(defn ^:export init []
  (accountant/configure-navigation!
   {:nav-handler  (fn [path] (secretary/dispatch! path))
    :path-exists? (fn [path] (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
