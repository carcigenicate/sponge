(defproject sponge "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [helpers "1"]
                 [seesaw "1.4.5"]
                 [org.clojure/core.async "0.3.443"]]
  :main ^:skip-aot sponge.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
