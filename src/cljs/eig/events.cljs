(ns eig.events
  (:require [re-frame.core :as re-frame]
            [eig.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-view!
 (fn [db [_ view]]
   (assoc db :view view)))
