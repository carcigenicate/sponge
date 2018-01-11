(ns sponge.menger.first-try
  (:require [seesaw.core :as sc]
            [seesaw.graphics :as sg]
            [seesaw.dev :as sd]
            [seesaw.color :as s-col]
            [seesaw.font :as sf]

            [sponge.menger.first-sponge :as sp]))

; TODO: Base size on canvas size, not window size
; TODO: map coordinates from sponge coord to window coord

(def screen-width 1500)

(def default-subdivisions 3)

(def control-font (sf/font :size 30))

(defn paint [future-tiles-atom cvs g]
  (let [fut? @future-tiles-atom]
    (when (and fut? (realized? fut?))
      (doseq [{:keys [tl-x tl-y width] :as tile} @fut?]
        (sg/draw g
          (sg/rect tl-x tl-y (+ width 2))
          (sg/style :background :black))))))

(defn future-tiles [iterations]
  (future
    (sp/iterate-menger-subdivide
       [(sp/->Tile 0 0 screen-width)]
       default-subdivisions
       iterations)))

(defn new-canvas [tiles-atom]
  (let [canvas (sc/canvas :paint (partial paint tiles-atom))]
    canvas))

(defn iterations-selector-controls [canvas tiles-atom]
  (let [selector (sc/spinner :font control-font,
                             :model (sc/spinner-model 1 :from 0, :to 7, :by 1))
        sub-button (sc/button :text "Generate", :font control-font)]

     (sc/listen sub-button
       :action (fn [_]
                 (swap! tiles-atom
                        (fn [old-fut]
                          (when old-fut
                            (future-cancel old-fut))

                          (let [new-fut (future-tiles (sc/value selector))]
                            (sc/repaint! canvas)
                            new-fut)))))

     (sc/horizontal-panel :items [sub-button selector])))

(defn new-main-panel []
  (let [future-tiles-atom (atom nil)
        canvas (new-canvas future-tiles-atom)
        controls (iterations-selector-controls canvas future-tiles-atom)]

    (sc/border-panel :center canvas, :south controls)))

(defn frame []
  (let [content (new-main-panel)

        f (sc/frame :size [screen-width :by screen-width], :content content)]
    f))

