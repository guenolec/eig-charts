(ns eig.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [eig.db :as db]
            [accountant.core :as accountant]
            [antizer.reagent :as ant]
            [eig.subs :as subs]
            [eig.report :as report]
            [eig.events :as events]
            [cljsjs.moment]
            [cljsjs.moment.locale.fr]
            [goog.labs.format.csv :as csv]
            [cljsjs.chartjs]))

(defn show-revenue-chart []
  (let [context    (.getContext (.getElementById js/document "rev-chartjs") "2d")
        chart-data {:type "bar"
                    :data {:labels   ["2017" "2018" "2019"]
                           :datasets [{:data            [5 10 1]
                                       :label           "This is something"
                                       :backgroundColor "#234567"}
                                      {:data            [3 6 9]
                                       :label           "And something else entirely"
                                       :backgroundColor "#245312"}]}}]
    (js/Chart. context (clj->js chart-data))))

(defn rev-chartjs-component
  []
  (r/create-class
   {:component-did-mount #(show-revenue-chart)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "rev-chartjs"}])})) 

(defn display-chart []
  [ant/layout-content {:class "content-area"}
   [rev-chartjs-component]])

(defn layout-content-view [view-name]
  (case view-name
    :chart [display-chart]))

(defn side-menu []
  [ant/menu {:mode "inline" :theme "dark" :style {:height "100%"}}
   [ant/menu-item "Menu Item"]
   [ant/menu-sub-menu {:title "Sub Menu"}
    [ant/menu-item "Item 1"]
    [ant/menu-item "Item 2"]]
   [ant/menu-item {:disabled true} "Menu with Icons"]
   [ant/menu-item (r/as-element [:span [ant/icon {:type "home"}] "Menu Item"])]
   [ant/menu-sub-menu {:title (r/as-element [:span [ant/icon {:type "setting"}] "Sub Menu"])}
    [ant/menu-item (r/as-element [:span [ant/icon {:type "user"}] "Item 1"])]
    [ant/menu-item (r/as-element [:span [ant/icon {:type "notification"}] "Item 2"])]]])

(defn main-panel []
  [ant/locale-provider {:locale (ant/locales "fr_FR")}
   [ant/layout
    [ant/layout-sider [side-menu]]
    [ant/layout-content {:style {:padding "50px 50px" :width "85%" :margin "auto"}}
     [layout-content-view @(re-frame/subscribe [::subs/view])]
     [ant/layout-footer [:p "Pied de page."]]]]])
