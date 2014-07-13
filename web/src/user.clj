(ns user
  (:require cemerick.piggieback
            weasel.repl.websocket))

(defn repl []
  (cemerick.piggieback/cljs-repl))

(defn ws-repl []
  (cemerick.piggieback/cljs-repl
   :repl-env (weasel.repl.websocket/repl-env)))

