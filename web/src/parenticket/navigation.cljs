(ns parenticket.navigation
  (:require [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [cljs.core.async :as async])
  (:import goog.History
           goog.history.EventType))

(declare chat-route)

(defonce navigation-channel (async/chan (async/sliding-buffer 1)))

(defn navigate! [route & [ignore-history]]
  (if-not ignore-history
    (set! (-> js/window .-location .-hash) route)
    (do (js/window.history.replaceState nil "" route)
        (secretary/dispatch! (.substring route 1)))))

(def +open-routes+ #{:login :page :registration})

(defn needs-authentication? [route]
  (not
   (contains? +open-routes+
              (if (vector? route) (first route) route))))

(secretary/defroute project-route "/project/:project" [project]
  (let [project (if (string? project) (js/parseInt project) project)]
   (async/put! navigation-channel {:project project})))

(secretary/defroute ticket-route "/project/:project/ticket/:ticket" [project ticket]
  (let [project (if (string? project) (js/parseInt project) project)
        ticket (if (string? ticket) (js/parseInt ticket) ticket)]
   (async/put! navigation-channel {:project project
                                   :ticket ticket})))

(secretary/defroute edit-ticket-route "/project/:project/ticket/:ticket/edit" [project ticket]
  (let [project (if (string? project) (js/parseInt project) project)
        ticket (if (string? ticket) (js/parseInt ticket) ticket)]
    (async/put! navigation-channel {:project project
                                    :ticket ticket
                                    :edit true})))

(secretary/defroute ^:private default-route "*" []
  (async/put! navigation-channel {}))

(secretary/set-config! :prefix "#")

(defonce ^:private history
  (let [h (History.)]
    (goog.events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
    (doto h
      (.setEnabled true))))
