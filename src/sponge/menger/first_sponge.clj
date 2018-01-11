(ns sponge.menger.first-sponge)

(defrecord Tile [tl-x tl-y width]
  Object
  (toString [self] (str (into {} self))))

(defrecord Sponge [tiles])

(defn subdivide-tile
  "Expects that side-divions is odd."
  [tile side-divions]
  (let [{:keys [tl-x tl-y width]} tile
        sub-width (double (/ width side-divions))]
    (for [y (range tl-y (+ tl-y width) (inc sub-width))
          x (range tl-x (+ tl-x width) (inc sub-width))]
      (->Tile x y sub-width))))

(defn subdivide-tile-remove-center
  "Expects that side-divions is odd."
  [tile side-divions]
  (let [{:keys [tl-x tl-y width]} tile
        sub-width (double (/ width side-divions))]
    (for [y (range tl-y (+ tl-y width) (inc sub-width))
          x (range tl-x (+ tl-x width) (inc sub-width))]
      (->Tile x y sub-width))))

(defn remove-center [subdivided-tiles side-divisions]
  (let [middle (int (/ side-divisions 2))
        remove-i (+ (* middle side-divisions) middle)]
    (map second
      (filter #(not= (first %) remove-i)
              (map vector (range) subdivided-tiles)))))

(defn full-menger-subdivide [tiles side-divisions]
  (mapcat #(remove-center (subdivide-tile % side-divisions) side-divisions)
          tiles))

(defn iterate-menger-subdivide [tiles side-divisions iterations]
  (nth (iterate #(full-menger-subdivide % side-divisions) tiles)
       (inc iterations)))


