(ns parentwicket.api
  (:require [cljs.reader :as reader]
            [cljs.core.async :as async]
            [clojure.string :as s]

            [weasel.repl :as ws-repl])
  (:import [goog.net XhrIo])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(def ^:private xhr-timeout (* 30 1000))

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
       (assert (contains? #{:post :get} method))
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

(defn fetch-projects [adapter]
  (xhr-request! (str "://")))
