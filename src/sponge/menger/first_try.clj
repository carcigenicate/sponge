(ns sponge.menger.first-try
  (:require [seesaw.core :as sc]
            [seesaw.graphics :as sg]
            [seesaw.dev :as sd]
            [seesaw.color :as s-col]

            [sponge.menger.first-sponge :as sp]))

(def screen-width 1800)

(def tiles (sp/iterate-menger-subdivide
             [(sp/->Tile 0 0 screen-width)]
             3 6))

(defn paint [cvs g]
  (let []
    (doseq [{:keys [tl-x tl-y width]} tiles]
      (sg/draw g
        (sg/rect tl-x tl-y width)
        (sg/style :background :red)))))


(defn new-canvas []
  (let [canvas (sc/canvas :paint paint)]
    canvas))

(defn new-main-panel []
  (let [canvas (new-canvas)]
    (sc/border-panel :center canvas)))

(defn frame []
  (let [content (new-main-panel)

        f (sc/frame :size [screen-width :by screen-width], :content content)]
    f))

