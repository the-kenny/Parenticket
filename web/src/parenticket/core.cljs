(ns parenticket.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :refer-macros [html]]
            [cljs.reader :as reader]
            [cljs.core.async :as async]
            [clojure.string :as s]

            [parenticket.api :as api]
            [parenticket.navigation :as nav]

            [weasel.repl :as ws-repl])
  (:import [goog.net XhrIo]
           [goog.i18n DateTimeFormat])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)
(ws-repl/connect "ws://localhost:9001" :verbose true)

(defrecord AppState [tickets projects current-project])

(def app-state (atom
                (map->AppState {:tickets {} #_(into {}
                                                    (for [title (range 10)
                                                          status [:todo :doing :done]]
                                                      [(str title) {:id (str title)
                                                                    :status status
                                                                    :name (reduce str (repeat 10 title))
                                                                    :deadline (js/Date.)
                                                                    :tags ["foo" "bar" "baz" "asdfasdf"]
                                                                    :description "asdfasdasdf"}]))
                                :projects {} #_(into {}
                                                     (for [n (range 10)]
                                                       [(str n) {:id (str n)
                                                                 :name (reduce str (repeat 3 n))}]))
                                :current-project nil})))

(defn current-project [state]
  (get-in state [:projects (:current-project state)]))


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
         [:a {:href (nav/edit-ticket-route {:project (:project_id ticket)
                                            :ticket (:id ticket)})}
          [:img {:src "edit.png"}]]
         [:a {:href "#"
              :on-click (fn [_]
                          (let [ticket @ticket]
                            (when (js/confirm "Delete?")
                             (api/delete-ticket! adapter (:project_id ticket) ticket)))
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

(defn handle-new-project! [state owner]
  (when-let [name (js/prompt "Project Name?")]
    (go
      (when-let [project (<! (api/add-project! adapter name))]
        (nav/navigate! (nav/project-route {:project (:id project)}))))))

(defn project-column [state owner opts]
  (reify
    om/IRenderState
    (render-state [_ {:keys [search]}]
      (html
       [:.pane.project
        [:h1 "Parenticket"]
        [:h2 (get-in state [:projects (:current-project state) :name])]

        [:ul.actions
         [:li
          [:a.new-project {:href "#"
                           :on-click (fn [_]
                                       (handle-new-project! state owner)
                                       false)}
           "New Project"]]
         [:li
          [:a.new-ticket {:href (nav/new-ticket-route {:project (:current-project state)})}
           "New Ticket"]]]
        [:form.filter
         [:h4 "Filter"]
         [:input.tagfilter {:type "text"
                            :value search
                            :on-change (fn [e]
                                         (async/put! (:filter-chan opts)
                                                     (-> e .-target .-value))
                                         false)}]]
        [:h3 "Projects"]
        [:ul.projects
         (for [[id project] (:projects state)]
           [:li.project
            [:a {:href (str "#/project/" id)}
             (:name project)]])]
        ]))))

(defn ticket-view [ticket owner opts]
  (om/component
   (html
    [:.dialog.ticket
     [:.close {:on-click (fn [_]
                           (when (fn? (:close opts)) ((:close opts)))
                           false)}
      "X"]
     [:h3 "Ticket"]
     [:label.name "Name"
      [:span.name (:name ticket)]]
     [:label.description "Description"
      [:span.description (:description ticket)]]
     [:label.tags "Tags"
      [:span.tags (s/join ", " (:tags ticket))]]
     [:label.deadline "Deadline"
      [:span.deadline (:deadline ticket)]]
     [:label.priority "Priority"
      [:span.priority (:projects ticket)]]])))

(defn handle-edit! [project-id ticket-id owner]
  (let [ticket (om/get-state owner)
        {:keys [name description tags priority status]} ticket
        tags (map s/trim tags)]
    (prn (om/get-state owner))
    (if (string? name)
      (go
        (println "Saving...")
        (when (<! (if ticket-id
                    (api/update-ticket! adapter project-id  (assoc ticket
                                                              :id ticket-id))
                    (api/add-ticket! adapter project-id  (dissoc ticket :project_id))))
          (nav/navigate! (nav/project-route {:project project-id}))))
      (throw (ex-info "name empty" (:ticket ticket))))))

(defn change [owner key e]
  (om/set-state! owner key (-> e .-target .-value)))

(defn edit-ticket-view [ticket owner opts]
  (reify
    om/IInitState
    (init-state [_]
      (om/value ticket))
    om/IRenderState
    (render-state [_ {:keys [name description tags priority deadline status]}]
      (html
       [:.dialog.ticket
        [:.close {:on-click (fn [_]
                              (when (fn? (:close opts)) ((:close opts)))
                              false)}
         "X"]
        [:form {:on-submit (fn [_]
                             (handle-edit! (:project_id ticket) (:id ticket) owner)
                             false)}
         [:h3 "Ticket"]
         [:label.name "Name"
          [:input.name {:value name
                        :on-change (partial change owner :name)}]]
         [:label.description "Description"
          [:input.description {:value description
                               :on-change (partial change owner :description)}]]
         [:label.tags "Tags"
          [:input.tags {:value (s/join ", " tags)
                        :on-change (fn [e]
                                     (om/set-state! owner :tags
                                                    (-> e .-target .-value
                                                        (s/split #",\W*")))
                                     false)}]]
         [:label.deadline "Deadline"
          [:input.deadline {:value deadline
                            :disabled true}]]
         [:label.priority "Priority"
          [:input.priority {:value priority
                            :on-change (partial change owner :priority)}]]
         [:label.status "Status"
          [:select.status {:value status
                           :on-change (partial change owner :status)}
           [:option {:value 0} "Todo"]
           [:option {:value 1} "Doing"]
           [:option {:value 2} "Done"]]]

         [:button.cancel {:on-click (fn [_]
                                      (when (fn? (:close opts)) ((:close opts)))
                                      false)}
          "Cancel"]
         [:button.save
          "Save"]]]))))

(defn ticket-matches? [filter ticket]
  (not= -1 (.indexOf (s/lower-case (reduce str (vals ticket))) filter)))

(def render-start nil)
(defn parenticket [state owner]
  (reify
    om/IInitState
    (init-state [_]
      {:current-ticket nil
       :edit? false
       :ticket-filter ""
       :filter-chan (async/chan (async/dropping-buffer 1))})
    om/IWillMount
    (will-mount [_]
      (go-loop []
        (<! (api/reload-projects! adapter))
        (let [{:keys [project ticket edit]} (<! nav/navigation-channel)]
          (prn "asdfasdf" project ticket)
          ;; Store current project
          (if (contains? (:projects @state) project)
            (do
              (println "Switching to project " project)
              (when (not= project (:current-project @state))
                ;; Load tickets
                (<! (api/reload-tickets! adapter (get-in @state [:projects project]))))
              (om/update! state :current-project project))
            (println "Got unknown project :("))
          ;; Store current ticket
          (om/set-state! owner :current-ticket ticket)
          (om/set-state! owner :edit? edit))
        (recur))
      ;; Filter
      (go-loop []
        (when-let [v (<! (om/get-state owner :filter-chan))]
          (om/set-state! owner :ticket-filter v)
          (recur))))
    om/IRenderState
    (render-state [_ {:keys [current-ticket edit? ticket-filter]}]
      (let [current-project (:current-project state)
            tickets (->> (:tickets state)
                         vals
                         (filter #(= current-project (:project_id %)))
                         (filter (partial ticket-matches? ticket-filter))
                         (group-by :status))]
        (html
         [:.app
          (when edit?
            (list
             [:.overlay]
             (let [ticket (get-in state [:tickets current-ticket])]
               (om/build (if edit? edit-ticket-view ticket-view) (or (om/value ticket)
                                                                     {:project_id (:current-project state)})
                         {:opts {:close #(nav/navigate! (nav/project-route {:project (:current-project @state)} ))}}))))
          [:.pane.todo
           [:h2 "Todo"]
           (om/build ticket-column (get tickets 0))]
          [:.pane.doing
           [:h2 "Doing"]
           (om/build ticket-column (get tickets 1))]
          [:.pane.done
           [:h2 "Done"]
           (om/build ticket-column (get tickets 2))]
          (om/build project-column state
                    {:opts {:filter-chan (om/get-state owner :filter-chan)}
                     :state {:search ticket-filter}})])))))

(om/root parenticket app-state {:target (js/document.getElementById "main")})

