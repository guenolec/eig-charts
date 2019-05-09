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

(defn a-propos []
  [:div {:style {:padding "2em"}}
   [:h1 "Présentation des données"]
   [:p "Ce site présente quelques données liées au programme "
    [:a {:href "https://entrepreneur-interet-general.etalab.gouv.fr/"} "Entrepreneurs d'intérêt général."]]
   [:p "Les données exposées ici sous forme de graphiques sont téléchargeables au format .ods."] ; FIXME
   [:h1 "Code source et licence"]
   [:p "Le code source de ce site est "
    [:a {:href "http://github.com/etalab/eig-charts"} "public"] "."]
   [:p "Si vous souhaitez corriger une erreur, merci de le signaler "
    [:a {:href "http://github.com/etalab/eig-charts/issues"} "ici"] " ou par email."]
   [:h1 "Contact"]
   [:p "Pour toute question sur le code source de ce site, merci d'écrire à "
    [:span.email "bastien.guerry AT data.gouv.fr"] "."]
   [:p "Pour toute question sur son contenu site, merci d'écrire à "
    [:span.email "entrepreneur-interet-general AT data.gouv.fr"] "."]])

(defn layout-content-view [view-name]
  [ant/layout-content {:class "content-area"}
   (case view-name
     "a-propos"       [a-propos]
     "carte"          [charts/chartjs-map]
     "promo"          [charts/chartjs-promo]
     "competences"    [charts/chartjs-competences]
     "genres"         [charts/chartjs-genres]
     "parcours"       [charts/chartjs-parcours]
     ;; "eig2017"        [charts/chartjs-eig2017]
     ;; "eig2018"        [charts/chartjs-eig2018]
     ;; "eig2019"        [charts/chartjs-eig2019]
     "financement"    [charts/chartjs-financement]
     "depenses"       [charts/chartjs-depenses]
     "accompagnement" [charts/chartjs-accompagnement]
     "communication"  [charts/chartjs-communication])])

(defn side-menu []
  [ant/menu {:mode     "inline" :theme "dark" :style {:height "100%"}
             :on-click (fn [e] (accountant/navigate! (.-key e)))}
   [ant/menu-item {:key "#promo"} "Promotions EIG"]
   ;; [ant/menu-sub-menu {:title "Dépenses"}
   ;;  [ant/menu-item {:key "#eig2017"} "EIG 2017"]
   ;;  [ant/menu-item {:key "#eig2018"} "EIG 2018"]
   ;;  [ant/menu-item {:key "#eig2019"} "EIG 2019"]]
   [ant/menu-sub-menu {:title "Profils EIG"}
    [ant/menu-item {:key "#competences"} "Compétences"]
    [ant/menu-item {:key "#genres"} "Genres"]
    [ant/menu-item {:key "#parcours"} "Parcours"]]
   [ant/menu-item {:key "#financement"} "Financement"]
   [ant/menu-item {:key "#depenses"} "Dépenses"]
   [ant/menu-item {:key "#accompagnement"} "Accompagnement"]
   [ant/menu-item {:key "#communication"} "Communication"]
   [ant/menu-item {:key "#carte"} "Carte des défis"]
   [ant/menu-item {:key "#a-propos"} "À propos"]])

(defn main-panel []
  [ant/locale-provider {:locale (ant/locales "fr_FR")}
   [ant/layout
    [ant/layout-sider [side-menu]]
    [ant/layout-content {:style {:padding "20px 20px" :margin "auto"}}
     [layout-content-view @(re-frame/subscribe [::subs/view])]
     [ant/layout-footer
      [:p
       [:a {:href "https://entrepreneur-interet-general.etalab.gouv.fr/"} "Entrepreneurs d'intérêt général"]
       " - "
       [:a {:href "https://www.etalab.gouv.fr"} "Etalab"]
       " / "
       [:a {:href "https://www.numerique.gouv.fr/"} "DINSIC"]]]]]])
