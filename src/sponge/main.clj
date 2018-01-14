(ns sponge.main
  (:require [sponge.menger.first-try :as sm]
            [seesaw.core :as sc])
  (:gen-class))

(defn -main [& args]
  (-> (sm/frame)
      (sc/show!)))
