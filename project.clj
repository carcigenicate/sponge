(defproject sponge "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [helpers "1"]
                 [seesaw "1.4.5"]]
  :main ^:skip-aot sponge.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
