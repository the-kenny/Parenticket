(ns parenticket.api
  (:require [cljs.reader :as reader]
            [cljs.core.async :as async]
            [clojure.string :as s]

            [weasel.repl :as ws-repl])
  (:import [goog.net XhrIo])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(def ^:private xhr-timeout (* 3 1000))

(defrecord ApiAdapter [host port session])

(defn ^:private parse-xhr-response
  "Takes a finished googl.net.XhrIo, returns a map with :status, :response/text, etc."
  [xhr]
  (let [json (try (.getResponseJson xhr) (catch js/Error e nil))]
    {:status (.getStatus xhr)
     :error (.getLastError xhr)
     :successful? (.isSuccess xhr)
     :response/json json
     :response/edn (when json (js->clj json :keywordize-keys true))
     :response/text (.getResponseText xhr)}))

(defn xhr-request!
  "Performs an XmlHttpRequest to uri using method and payload data.

  Returns a channel containing the result of the request parsed by
  `parse-xhr-response'."
  ([uri method content-type data]
     (let [ch (async/chan)]
       (assert uri)
       #_(assert (contains? #{:post :get} method))
       (XhrIo/send uri
                   (fn [e]
                     (async/put! ch (-> e (.-target) parse-xhr-response)))
                   (s/upper-case (name method))
                   data
                   #js {"Content-Type" content-type
                        "Accept" "application/json"}
                   xhr-timeout)
       ch))
  ([uri method data]
     (xhr-request! uri method "application/json" data))
  ([uri method]
     (xhr-request! uri method nil)))

(defn ^:private base-url [adapter]
  (str "http://" (:host adapter) ":" (:port adapter)))

(defn fetch-projects [adapter]
  (go
    (-> (str (base-url adapter) "/projects")
        (xhr-request! :get)
        <!)))

(defn fetch-tickets [adapter project-id]
  (go
   (->  (str (base-url adapter) "/projects/" project-id)
        (xhr-request! :get)
        <!)))

(defn reload-projects! [adapter]
  (go
    (let [response (<! (fetch-projects adapter))]
      (if (:successful? response)
        (let [project-map (into {}
                                (for [p (:response/edn response)]
                                  [(:id p) p]))]
          (swap! (:session adapter) assoc :projects project-map))
        (throw "Broken shit" {:response response})))))

(defn reload-tickets! [adapter project]
  (go
    (let [session @(:session adapter)]
      (assert (contains? (:projects session) (:id project)))
      (let [response (<! (fetch-tickets adapter (:id project)))]
        (if (:successful? response)
          (let [ticket-map (into {}
                                 (for [t (:tickets (:response/edn response))]
                                   [(:id ticket) t]))]
            (println "got tickets:" (pr-str ticket-map))
            (swap! (:session adapter) update-in [:tickets] merge ticket-map))
          (throw (ex-info "Broken shit" {:response response})))))))

(defn add-ticket! [adapter project-id ticket]
  (assert project-id)
  (assert (:name ticket))
  (go
    (let [response
          (-> (str (base-url adapter) "/projects/" project-id "/tickets/" (:id ticket))
              (xhr-request! :post (JSON/stringify (clj->js ticket)))
              (<!))]
      (println "result:" (pr-str response))
      (if (:successful? response)
        (let [ticket (:response/edn response)]
          (swap! (:session adapter) assoc-in [:tickets (:id ticket)] ticket))
        (throw (ex-info "add fucked" {:response response}))))))

(defn update-ticket! [adapter project-id ticket]
  (assert project-id)
  (assert (:id ticket))
  (go
   (let [response
         (-> (str (base-url adapter) "/projects/" project-id "/tickets/" (:id ticket))
             (xhr-request! :put (JSON/stringify (clj->js ticket)))
             (<!))]
     (println "result:" (pr-str response))
     (if (:successful? response)
       (swap! (:session adapter) update-in [:tickets (:id ticket)] merge (:response/edn response))
       (throw (ex-info "update fucked" {:response response}))))))

(defn delete-ticket! [adapter project-id ticket]
  (assert project-id)
  (assert (:id ticket))
  (go
    (let [response
          (-> (str (base-url adapter) "/projects/" project-id "/tickets/" (:id ticket))
              (xhr-request! :delete)
              (<!))]
      (println "result:" (pr-str response))
      (if (:successful? response)
        (swap! (:session adapter) (fn [session] (update-in session [:tickets] dissoc (:id ticket))))
        (throw (ex-info "delete fucked" {:response response}))))))
