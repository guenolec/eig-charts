(ns eig.charts
  (:require [reagent.core :as r]
            [eig.report :as report]
            [eig.colors :as color]
            [cljsjs.chartjs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn map-render []
  [:div#map {:style {:height "700px"}}])

(def admins_locations
  (flatten (vals report/administrations_map)))

(defn map-did-mount []
  (let [lmap  (.setView (.map js/L "map") #js [48.8503 2.30831] 7)
        items admins_locations]
    (.addTo (.tileLayer
             js/L "https://{s}.tile.openstreetmap.fr/osmfr/{z}/{x}/{y}.png"
             (clj->js {:attribution
                       "&copy; Openstreetmap France | &copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors"
                       :maxZoom 18}))
            lmap)
    (doseq [{:keys [lat lon admin eig2017 eig2018 eig2019]} items]
      (.addTo (.bindPopup (.marker js/L (clj->js
                                         (vector (js/parseFloat lon)
                                                 (js/parseFloat lat))))
                          (str "<h2>" admin "</h2>"
                               "<p>2017: " (or eig2017 0) " défi EIG </p>"
                               "2018: " (or eig2018 0) " défi EIG </p>"
                               "2019: " (or eig2019 0) " défi EIG </p>"))
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
         :options {:title      {:display "true" :text "Financement du programme EIG"}
                   :responsive "true"}
         :data    {:labels   financement-keys
                   :datasets [{:data            financement-2017
                               :label           "2017"
                               :backgroundColor color/blue}
                              {:data            financement-2018
                               :label           "2018"
                               :backgroundColor color/green}
                              {:data            financement-2019
                               :label           "2019"
                               :backgroundColor color/orange}
                              {:data            financement-total
                               :label           "Totaux"
                               :backgroundColor color/red}
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
         :options {:title      {:display "true" :text "3 promotions EIG"}
                   :elements   {:line {:tension 0}}
                   :responsive "true"}
         :data    {:labels   ["2017" "2018" "2019" "Totaux"]
                   :datasets [{:data             (conj (get report/programme "Nombre d'EIG")
                                                       (get promotion-totaux 0))
                               :label            "Nombre d'EIG"
                               :pointRadius      10
                               :pointHoverRadius 15
                               :backgroundColor  color/blue
                               :fill             "boundary"}
                              {:data             (conj (get report/programme "Nombre de mentors")
                                                       (get promotion-totaux 1))
                               :label            "Nombre de mentors"
                               :pointRadius      10
                               :pointHoverRadius 15
                               :backgroundColor  color/green
                               :fill             nil}
                              {:data             (conj (get report/programme "Nombre de défis")
                                                       (get promotion-totaux 2))
                               :label            "Nombre de défis"
                               :backgroundColor  color/orange
                               :pointRadius      10
                               :pointHoverRadius 15
                               :fill             nil}]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-promo
  []
  (r/create-class
   {:component-did-mount #(promotion)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def eig-keys ["Part des salaires pris en charge par le PIA"
               "Part des défis autofinancés dans le total des salaires"
               "Part prise en charge par les administrations en co-financement"])

(def eig-parts
  (vals (select-keys report/financement eig-keys)))

(defn eig2017 []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "pie"
         :options {:title      {:display "true" :text "EIG 2017"}
                   :responsive "true"}
         :data    {:labels   eig-keys
                   :datasets [{:data            (map first eig-parts)
                               :label           "EIG 1 - 2017"
                               :backgroundColor [color/blue color/green color/orange]}]}}]
    (js/Chart. context (clj->js chart-data))))

(defn eig2018 []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "pie"
         :options {:title      {:display "true" :text "EIG 2018"}
                   :responsive "true"}
         :data    {:labels   eig-keys
                   :datasets [{:data            (map second eig-parts)
                               :label           "EIG 2 - 2018"
                               :backgroundColor [color/blue color/green color/orange]}]}}]
    (js/Chart. context (clj->js chart-data))))

(defn eig2019 []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "pie"
         :options {:title      {:display "true" :text "EIG 2019"}
                   :responsive "true"}
         :data    {:labels   eig-keys
                   :datasets [{:data            (map #(nth % 2) eig-parts)
                               :label           "EIG 2 - 2019"
                               :backgroundColor [color/blue color/green color/orange]}]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-eig2019
  []
  (r/create-class
   {:component-did-mount #(eig2019)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 

(defn chartjs-eig2018
  []
  (r/create-class
   {:component-did-mount #(eig2018)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 

(defn chartjs-eig2017
  []
  (r/create-class
   {:component-did-mount #(eig2017)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def profils-keys
  ["Part des développeurs parmi les EIG"                
   "Part des data scientists parmi les EIG"             
   "Part des designers parmi les EIG"])

(def profils-data
  (into [] (vals (select-keys report/eig profils-keys))))

(defn profils []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "bar"
         :options {:title      {:display "true" :text "3 profils parmi les EIG"}
                   :responsive "true"
                   :scales     {:xAxes [{:stacked true}]
                                :yAxes [{:stacked true}]}}
         :data    {:labels   ["EIG 1 - 2017" "EIG 2 - 2018" "EIG 3 - 2019"]
                   :datasets [{:data            (get profils-data 0)
                               :label           "Développeurs"
                               :backgroundColor color/blue}
                              {:data            (get profils-data 1)
                               :label           "Datascientistes"
                               :backgroundColor color/green}
                              {:data            (get profils-data 2)
                               :label           "Designers"
                               :backgroundColor color/orange}
                              ]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-profils
  []
  (r/create-class
   {:component-did-mount #(profils)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def depenses-keys
  ["Part des salaires dans le coût total du programme"
   "Part de la recherche dans le coût total du programme"
   "Part du programme d'accompagnement dans le coût total du programme"])

(def depenses-data
  (into [] (vals (select-keys report/financement depenses-keys))))

(defn depenses []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "pie"
         :options {:title      {:display "true" :text "Les dépenses du programme EIG, de 2017 (au centre) à 2019"}
                   :responsive "true"}
         :data    {:labels   depenses-keys
                   :datasets [{:data            (map #(nth % 2) depenses-data)
                               :label           "2019"
                               :backgroundColor [color/blue color/green color/orange]}
                              {:data            (map second depenses-data)
                               :label           "2018"
                               :backgroundColor [color/blue color/green color/orange]}
                              {:data            (map first depenses-data)
                               :label           "2017"
                               :backgroundColor [color/blue color/green color/orange]}
                              ]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-depenses
  []
  (r/create-class
   {:component-did-mount #(depenses)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def accompagnement-2017
  (map #(get (val %) 0) report/accompagnement))
(def accompagnement-2018
  (map #(get (val %) 1) report/accompagnement))
(def accompagnement-2019
  (map #(get (val %) 2) report/accompagnement))
(def accompagnement-totaux
  (map (fn [a b c] (+ a b c))
       accompagnement-2017 accompagnement-2018 accompagnement-2019))

(defn accompagnement []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "bar"
         :options {:title      {:display "true" :text "Un programme d'accompagnement"}
                   :responsive "true"}
         :data    {:labels   (keys report/accompagnement)
                   :datasets [{:data            accompagnement-2017
                               :label           "EIG 1 - 2017"
                               :backgroundColor color/blue}
                              {:data            accompagnement-2018
                               :label           "EIG 2 - 2018"
                               :backgroundColor color/green}
                              {:data            accompagnement-2019
                               :label           "EIG 3 - 2019"
                               :backgroundColor color/orange}
                              {:data            accompagnement-totaux
                               :label           "Totaux"
                               :backgroundColor color/dark-grey}
                              ]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-accompagnement
  []
  (r/create-class
   {:component-did-mount #(accompagnement)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def communication-data
  (select-keys report/communication
               ["Articles dans la presse"
                "Relais administratifs (numerique.gouv, modernisation.gouv …)"
                "Blog Etalab"
                "Articles sur le blog EIG"]))

(def communication-2017
  (map #(get (val %) 0) communication-data))
(def communication-2018
  (map #(get (val %) 1) communication-data))
(def communication-2019
  (map #(get (val %) 2) communication-data))

(defn communication []
  (let [context (.getContext (.getElementById js/document "chartjs") "2d")
        chart-data
        {:type    "bar"
         :options {:title      {:display "true" :text "Communication autour du programme"}
                   :responsive "true"}
         :data    {:labels   (keys communication-data)
                   :datasets [{:data            communication-2017
                               :label           "EIG 1 - 2017"
                               :backgroundColor color/blue}
                              {:data            communication-2018
                               :label           "EIG 2 - 2018"
                               :backgroundColor color/green}
                              {:data            communication-2019
                               :label           "EIG 3 - 2019"
                               :backgroundColor color/orange}]}}]
    (js/Chart. context (clj->js chart-data))))

(defn chartjs-communication
  []
  (r/create-class
   {:component-did-mount #(communication)
    :display-name        "chartjs-component"
    :reagent-render      (fn [] [:canvas {:id "chartjs"}])})) 
