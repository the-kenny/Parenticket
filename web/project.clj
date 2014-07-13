(defproject parenticket "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2268"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha"]
                 [com.stuartsierra/component "0.2.1"]

                 [om "0.6.4"]
                 [garden "1.1.8"]
                 [sablono "0.2.16"]
                 [secretary "1.2.0"]

                 [com.cemerick/piggieback "0.1.3"]
                 [weasel "0.2.1"]

                 ;; [com.cemerick/double-check "0.5.7-SNAPSHOT"]
                 [com.novemberain/validateur "2.1.0"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [cider/cider-nrepl "0.1.0-SNAPSHOT"]]
  :cljsbuild {:builds [{:id "client"
                        :source-paths ["src/"]
                        :compiler {:output-to "resources/public/main.js"
                                   :output-dir "resources/public/out/"
                                   :optimizations :advanced
                                   :libs [""]
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}]})
