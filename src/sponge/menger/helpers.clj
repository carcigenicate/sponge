(ns sponge.menger.helpers
  (:import (java.awt Component)
           (javax.swing RootPaneContainer JRootPane JButton)))

(defn component-size
  "Returns a pair of [width height] of the component"
  [^Component c]
  [(.getWidth c) (.getHeight c)])

(defn wrap
  "Wraps n so it's between min-n (inclusive) and max-n (exclusive)."
  [n min-n max-n]
  (let [limit (- max-n min-n)]
    (+ (mod n limit) min-n)))

(defn map-range
  "Maps a value in the inclusive range [start1 stop1] to a value in the range [start2 stop2].
  Stolen and translated from Processing."
  [value start1 stop1 start2 stop2]
  (+ start2
     (* (- stop2 start2)
        (/ (- value start1)
           (- stop1 start1)))))

(defn set-enter-default
  "Automatically presses the given button when the Enter key is pressed."
  [^RootPaneContainer root, ^JButton target]
  (.setDefaultButton (.getRootPane root) target))
