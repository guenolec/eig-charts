(ns eig.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [eig.db :as db]
            [accountant.core :as accountant]
            [antizer.reagent :as ant]
            [eig.subs :as subs]
            [eig.charts :as charts]
            [eig.report :as report]
            [eig.events :as events]
            [cljsjs.moment]
            [cljsjs.moment.locale.fr]))

(defn layout-content-view [view-name]
  [ant/layout-content {:class "content-area"}
   (case view-name
     "carte"       [charts/chartjs-map]
     "promo"       [charts/chartjs-promo]
     "eig2017"     [charts/chartjs-eig2017]
     "financement" [charts/chartjs-financement])])

(defn side-menu []
  [ant/menu {:mode     "inline" :theme "dark" :style {:height "100%"}
             :on-click (fn [e] (re-frame/dispatch [::events/set-view! (.-key e)]))}
   [ant/menu-item {:key "carte"} "Carte"]
   [ant/menu-item {:key "promo"} "Promotions EIG"]
   [ant/menu-item {:key "eig2017"} "EIG 2017"]
   [ant/menu-item {:key "financement"} "Financement"]])

(defn main-panel []
  [ant/locale-provider {:locale (ant/locales "fr_FR")}
   [ant/layout
    [ant/layout-sider [side-menu]]
    [ant/layout-content {:style {:padding "50px 50px" :width "85%" :margin "auto"}}
     [layout-content-view @(re-frame/subscribe [::subs/view])]
     [ant/layout-footer [:p "Pied de page."]]]]])
