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
  (let [lmap  (.setView (.map js/L "map") #js [48.8503 2.30831] 7)
        items admins_locations]
    (.addTo (.tileLayer
             js/L "http://{s}.tiles.mapbox.com/v3/bzg.i8bb9pdk/{z}/{x}/{y}.png"
             (clj->js {:attribution "Map data &copy; [...]" :maxZoom 18}))
            lmap)
    (doseq [{:keys [lat lon admin eig2017 eig2018 eig2019]} items]
      (.addTo (.bindPopup (.marker js/L (clj->js
                                         (vector (js/parseFloat lon)
                                                 (js/parseFloat lat))))
                          (str "<h2>" admin "</h2>"
                               "<p>2017: " (or eig2017 0) " EIG </p>"
                               "2018: " (or eig2018 0) " EIG </p>"
                               "2019: " (or eig2019 0) " EIG </p>"))
              lmap))))

(defn chartjs-map []
  (r/create-class {:reagent-render      map-render
                   :component-did-mount map-did-mount}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def financement-keys
  ["Enveloppe maximale allouée par le PIA"
   "Salaires pris en charge par PIA"
   "Coût total salaires EIG"])

(def financement-cout
  (select-keys report/financement financement-keys))

(def financement-2017
  (map #(get (val %) 0) financement-cout))
(def financement-2018
  (map #(get (val %) 1) financement-cout))
(def financement-2019
  (map #(get (val %) 2) financement-cout))

(def financement-total
  (map (fn [a b c] (+ a b c))
       financement-2017 financement-2018 financement-2019))

(defn financement []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "bar"
         :options {:title {:display "true" :text "Financement du programme EIG"}}
         :data    {:labels   financement-keys
                   :datasets [{:data            financement-2017
                               :label           "2017"
                               :backgroundColor "#234567"}
                              {:data            financement-2018
                               :label           "2018"
                               :backgroundColor "#245312"}
                              {:data            financement-2019
                               :label           "2019"
                               :backgroundColor "red"}
                              {:data            financement-total
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

(def promotion-totaux
  (into
   []
   (map (fn [[a b c]] (+ a b c))
        (vals (select-keys report/programme
                           ["Nombre d'EIG" "Nombre de mentors" "Nombre de défis"])))))

(defn promotion []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "line"
         :options {:title {:display "true" :text "3 promotions EIG"}}
         :data    {:labels   ["2017" "2018" "2019" "Totaux"]
                   :datasets [{:data            (conj (get report/programme "Nombre d'EIG")
                                                      (get promotion-totaux 0))
                               :label           "Nombre d'EIG"
                               :backgroundColor "#234567"
                               :fill            nil}
                              {:data            (conj (get report/programme "Nombre de mentors")
                                                      (get promotion-totaux 1))
                               :label           "Nombre de mentors"
                               :backgroundColor "#245312"
                               :fill            nil}
                              {:data            (conj (get report/programme "Nombre de défis")
                                                      (get promotion-totaux 2))
                               :label           "Nombre de défis"
                               :backgroundColor "red"
                               :fill            nil}]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-promo
  []
  (r/create-class
   {:component-did-mount #(promotion)
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
