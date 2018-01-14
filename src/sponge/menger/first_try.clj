(ns sponge.menger.first-try
  (:require [seesaw.core :as sc]
            [seesaw.graphics :as sg]
            [seesaw.color :as s-col]
            [seesaw.font :as sf]

            [sponge.menger.second-sponge :as p]
            [sponge.menger.helpers :as ph]))

(def screen-width 1500)

(def default-sponge-width 1e9)

(def control-font (sf/font :size 30))

(defn sponge->canvas
  "Returns a triplet of [mapped-x, mapped-y, mapped-width]"
  [canvas sponge-x sponge-y sponge-width]
  (let [[width height] (ph/component-size canvas)
        small-dim (min width height)
        map-r #(ph/map-range %, 0 default-sponge-width, 0 small-dim)]

    [(map-r sponge-x) (map-r sponge-y) (map-r sponge-width)]))

(defn test-color-f [x y width]
  (let [w #(ph/wrap % 1 256)
        i-width (/ 1 width)]
    (s-col/color (w (* x 5 i-width))
                 (w (* y 0.01 i-width))
                 (w (* x y i-width 0.001)))))

(defn paint
  "Partially accepts
    - An atom containing a future list of tiles to be drawn
    - A function that accepts the top-left x/y coordinates of a tile, and the tile's width,
      and returns a Color to draw the tile as.

   To be used as a :paint function."
  [future-tiles-atom color-f cvs g]
  (let [fut-tiles? @future-tiles-atom]
    (when (and fut-tiles? (realized? fut-tiles?))
      (doseq [{:keys [tl-x tl-y width]} @fut-tiles?
              :let [[sx sy sw] (sponge->canvas cvs tl-x tl-y width)
                    color (color-f tl-x tl-y width)]]
        (sg/draw g
          (sg/rect sx sy sw)
          (sg/style :background color))))))

(defn future-tiles
  "Returns a future that will evaluate to a sequence of tiles to be drawn.
  Repaints the canvas once done."
  [canvas n-iterations]
  (future
    (let [tiles (p/fully-subdivide-n-iterations
                  [(p/->Tile 0 0 default-sponge-width)]
                  n-iterations)]

      (doall tiles)

      (sc/invoke-later
        (sc/repaint! canvas))

      tiles)))

(defn new-canvas [tiles-atom]
  (sc/canvas :paint (partial paint tiles-atom test-color-f)))

(defn iterations-selector-controls
  "Returns controls that reset the tiles-atom with a new future when activated."
  [canvas tiles-atom]
  (let [selector (sc/spinner :font control-font,
                             :model (sc/spinner-model 1 :from 0, :to 20, :by 1))
        sub-button (sc/button :text "Generate", :font control-font, :id :sub-btn)]

     (sc/listen sub-button
       :action (fn [_]
                 (swap! tiles-atom
                        (fn [old-fut]
                          (when old-fut
                            (future-cancel old-fut))

                          (future-tiles canvas (sc/value selector))))))

     (sc/horizontal-panel :items [selector sub-button])))

(defn new-main-panel []
  (let [future-tiles-atom (atom nil)
        canvas (new-canvas future-tiles-atom)
        controls (iterations-selector-controls canvas future-tiles-atom)]

    (sc/border-panel :center canvas, :south controls)))

(defn frame []
  (let [content (new-main-panel)
        f (sc/frame :size [screen-width :by screen-width], :content content)
        sub-btn (sc/select f [:#sub-btn])]

    (ph/set-enter-default f sub-btn)

    f))

(defn -main []
  (-> (frame)
      (sc/show!)))