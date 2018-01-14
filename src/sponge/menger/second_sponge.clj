(ns sponge.menger.second-sponge)

(def subdivions 3)

(defrecord Tile [tl-x tl-y width])

(defn subdivide-tile-without-center
  "Divides a tile into subdivions many divisions per side, skipping the middle tile.
  A subdivions of 3 will split the tile into a grid of 8 tiles."
  [tile]
  #_{:post [(do (println tile (count %)) (= 8 (count %)))]}
  (let [{:keys [tl-x tl-y width]} tile
        sub-width (double (/ width subdivions))

        ; Calculates the limits for a dimension based on the given starting coordinate
        dim-range #(range % (+ % (- width sub-width)) (dec sub-width))

        ; The middle square index. Assumes subdivions is odd.
        remove-i (int (/ subdivions 2))]

    (for [[yi y] (map-indexed vector (dim-range tl-y))
          [xi x] (map-indexed vector (dim-range tl-x))
          :when (not= xi yi remove-i)]

      (->Tile x y sub-width))))

(defn fully-subdivide
  "Divides the collection of tiles into sub-tiles"
  [tiles]
  (mapcat subdivide-tile-without-center tiles))

(defn fully-subdivide-n-iterations
  "Iterates fully-subdivide n-iterations many times."
  [tiles n-iterations]
  (nth (iterate fully-subdivide tiles)
       n-iterations))