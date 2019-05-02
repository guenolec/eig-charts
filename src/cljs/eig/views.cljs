(ns eig.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [eig.db :as db]
            [accountant.core :as accountant]
            [antizer.reagent :as ant]
            [eig.subs :as subs]
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

(defn main-panel []
  [ant/locale-provider {:locale (ant/locales "fr_FR")}
   [ant/layout
    [ant/layout-header
     [ant/menu {:mode "horizontal" :theme "dark"}
      [ant/menu-item "Menu without icons"]
      [ant/menu-item "Menu Item"]
      [ant/menu-sub-menu {:title "Sub Menu"}
       [ant/menu-item "Item 1"]
       [ant/menu-item "Item 2"]]]]
    [ant/layout-content {:style {:padding "50px 50px" :width "85%" :margin "auto"}}
     ;; [ant/breadcrumb
     ;;  [ant/breadcrumb-item "Bonjour"]
     ;;  [ant/breadcrumb-item "ReBonjour"]]
     [layout-content-view @(re-frame/subscribe [::subs/view])]]
    [ant/layout-footer
     [:p "Pied de page."]]]])
