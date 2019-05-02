(ns eig.charts
  (:require [reagent.core :as r]
            [eig.report :as report]
            [cljsjs.chartjs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn map-render []
  [:div#map {:style {:height "800px"}}])

(def admins_locations
  (flatten (vals report/administrations_map)))

(defn map-did-mount []
  (let [lmap  (.setView (.map js/L "map") #js [48.29716 4.074626] 7)
        items admins_locations]
    (.addTo (.tileLayer
             js/L "http://{s}.tiles.mapbox.com/v3/bzg.i8bb9pdk/{z}/{x}/{y}.png"
             (clj->js {:attribution "Map data &copy; [...]" :maxZoom 18}))
            lmap)
    (doseq [{:keys [lat lon admin eig2017 eig2018 eig2019]} items]
      (.addTo (.bindPopup (.marker js/L (clj->js
                                         (vector (js/parseFloat lon)
                                                 (js/parseFloat lat))))
                          (str "<h2>" admin "</h2><p>EIG: " (+ eig2017 eig2018 eig2019) "</p>"))
              lmap))))

(defn chartjs-map []
  (r/create-class {:reagent-render      map-render
                   :component-did-mount map-did-mount}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn financement []
  (let [context    (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data {:type "bar"
                    :data {:labels   ["2017" "2018" "2018 (réel)" "2019"]
                           :datasets [{:data            [800000.00 2500000.00 0 1500000.00]
                                       :label           "Enveloppe maximale allouée par le PIA"
                                       :backgroundColor "#234567"}
                                      {:data            [688702.68 1735000.00 1600984.00 1167560.00]
                                       :label           "Salaires pris en charge par PIA"
                                       :backgroundColor "#245312"}
                                      {:data            [756553.38 1935342.00 1801326.00 2100364.09]
                                       :label           "Coût total salaires EIG"
                                       :backgroundColor "red"}
                                      {:data            [4800000.0 3591262.68 456624.00 744372.79]
                                       :label           "Totaux"
                                       :backgroundColor "green"}
                                      ]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-financement
  []
  (r/create-class
   {:component-did-mount #(financement)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn test-chart []
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

(defn chartjs-test
  []
  (r/create-class
   {:component-did-mount #(test-chart)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 
