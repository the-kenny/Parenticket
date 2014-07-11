(ns parenticket.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :refer-macros [html]]
            [cljs.reader :as reader]
            [cljs.core.async :as async]
            [clojure.string :as s]

            [parenticket.api :as api]

            [weasel.repl :as ws-repl])
  (:import [goog.net XhrIo]
           [goog.i18n DateTimeFormat])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)
(ws-repl/connect "ws://localhost:9001" :verbose true)

(defrecord AppState [tickets projects current-project])

(def app-state (atom
                (map->AppState {:tickets {} #_(vec
                                               (for [title (range 10)
                                                     status [:todo :doing :done]]
                                                 {:id title
                                                  :status status
                                                  :name (reduce str (repeat 10 title))
                                                  :deadline (js/Date.)
                                                  :tags ["foo" "bar" "baz" "asdfasdf"]
                                                  :description "asdfasdasdf"}))
                                :projects {} #_(into {}
                                                     (for [n (range 10)]
                                                       [n {:id n
                                                           :name (reduce str (repeat 3 n))}]))
                                :current-project nil})))


(def adapter (api/->ApiAdapter "10.10.10.46" 3000 app-state))

(let [formatter (DateTimeFormat. "dd.MM.yyyy HH:mm")]
  (defn ^:private format-date [date]
    (when (instance? js/Date date)
     (.format formatter date))))

(defn ticket [ticket owner opts]
  (reify
    om/IRender
    (render [_]
      (html
       [:li.ticket
        [:h3.name (:name ticket)]
        [:.actions
         [:a {:href "#"}
          [:img {:src "edit.png"}]]
         [:a {:href "#"
              :on-click (fn [_]
                          (let [ticket @ticket]
                           (api/delete-ticket! adapter (:project_id ticket) ticket))
                          false)}
          [:img {:src "delete.png"}]]]
        [:span.description (:description ticket)]
        [:span.deadline (format-date (:deadline ticket))]
        [:span.tags (s/join ", " (:tags ticket))]]))))

(defn ticket-column [tickets owner opts]
  (reify
    om/IRender
    (render [_]
      (html
       [:ul.tickets
        (om/build-all ticket tickets)]))))

(defn project-column [state owner opts]
  (reify
    om/IInitState
    (init-state [_]
      {:search ""})
    om/IRenderState
    (render-state [_ {:keys [search]}]
      (html
       [:div.pane.project
        [:h1 "Parenticket"]
        [:h2 (get-in state [:projects (:current-project state) :name])]
        [:h3 "Projects"]
        [:ul.projects
         (for [[id project] (:projects state)]
           [:li.project
            [:a {:href (str "#/project/" id)}
             (:name project)]])]
        [:form.filter
         [:h4 "Filter"]
         [:input.tagfilter {:type "text"
                            :value search
                            :on-change (fn [e]
                                         (om/set-state! owner :search (-> e .-target .-value))
                                         false)}]]]))))

(def render-start nil)
(defn parenticket [state owner]
  (om/component
   (let [tickets #_(group-by :status (:tickets state)) {:todo (vals (:tickets state))}]
     (html
      [:.app
       [:.pane.todo
        [:h2 "Todo"]
        (om/build ticket-column (:todo tickets))]
       [:.pane.doing
        [:h2 "Doing"]
        (om/build ticket-column (:doing tickets))]
       [:.pane.done
        [:h2 "Done"]
        (om/build ticket-column (:done tickets))]
       (om/build project-column state)]))))

(go
  (<! (api/reload-projects! adapter))
  (<! (api/reload-projects! adapter))
  (<! (api/reload-tickets! adapter (-> app-state deref :projects first val))))
(om/root parenticket app-state {:target (js/document.getElementById "main")})

