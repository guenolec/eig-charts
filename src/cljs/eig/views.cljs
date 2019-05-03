(ns eig.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [eig.db :as db]
            [antizer.reagent :as ant]
            [accountant.core :as accountant]
            [eig.subs :as subs]
            [eig.charts :as charts]
            [eig.report :as report]
            [eig.events :as events]))

(defn layout-content-view [view-name]
  [ant/layout-content {:class "content-area"}
   (case view-name
     "carte"          [charts/chartjs-map]
     "promo"          [charts/chartjs-promo]
     "profils"        [charts/chartjs-profils]
     "eig2017"        [charts/chartjs-eig2017]
     "eig2018"        [charts/chartjs-eig2018]
     "eig2019"        [charts/chartjs-eig2019]
     "financement"    [charts/chartjs-financement]
     "depenses"       [charts/chartjs-depenses]
     "accompagnement" [charts/chartjs-accompagnement]
     "communication"  [charts/chartjs-communication])])

(defn side-menu []
  [ant/menu {:mode     "inline" :theme "dark" :style {:height "100%"}
             :on-click (fn [e] (accountant/navigate! (.-key e)))}
   [ant/menu-item {:key "#promo"} "Promotions EIG"]
   [ant/menu-sub-menu {:title "Salaires"}
    [ant/menu-item {:key "#eig2017"} "EIG 2017"]
    [ant/menu-item {:key "#eig2018"} "EIG 2018"]
    [ant/menu-item {:key "#eig2019"} "EIG 2019"]]
   [ant/menu-item {:key "#profils"} "Profils EIG"]
   [ant/menu-item {:key "#financement"} "Financement"]
   [ant/menu-item {:key "#depenses"} "Dépenses"]
   [ant/menu-item {:key "#accompagnement"} "Accompagnement"]
   [ant/menu-item {:key "#communication"} "Communication"]
   [ant/menu-item {:key "#carte"} "Carte des défis"]])

(defn main-panel []
  [ant/locale-provider {:locale (ant/locales "fr_FR")}
   [ant/layout
    [ant/layout-sider [side-menu]]
    [ant/layout-content {:style {:padding "20px 20px" :margin "auto"}}
     [layout-content-view @(re-frame/subscribe [::subs/view])]
     [ant/layout-footer
      [:p
       "Auteur: "
       [:a {:href "https://entrepreneur-interet-general.etalab.gouv.fr/"} "Entrepreneur d'intérêt général"]
       " - "
       [:a {:href "https://www.etalab.gouv.fr"} "Etalab"]
       " / "
       [:a {:href "https://www.numerique.gouv.fr/"} "DINSIC"]]]]]])
