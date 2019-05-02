(ns eig.charts
  (:require [reagent.core :as r]
            [cljsjs.chartjs]))

(defn show-test-chart []
  (let [context    (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data {:type "bar"
                    :data {:labels   ["2017" "2018" "2019"]
                           :datasets [{:data            [5 10 1]
                                       :label           "This is something"
                                       :backgroundColor "#234567"}
                                      {:data            [3 6 9]
                                       :label           "And something else entirely"
                                       :backgroundColor "#245312"}]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-component
  []
  (r/create-class
   {:component-did-mount #(show-test-chart)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 


