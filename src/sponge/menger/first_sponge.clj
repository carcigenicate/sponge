(ns sponge.menger.first-sponge)

(defrecord Tile [tl-x tl-y width])

(defrecord Sponge [tiles])


(defn subdivide-tile
  "Expects that side-divions is odd."
  [tile side-divions]
  (let [{:keys [tl-x tl-y width]} tile
        sub-width (double (/ width side-divions))]
    (for [y (range tl-y (+ tl-y width) sub-width)
          x (range tl-x (+ tl-x width) sub-width)]
      (->Tile x y sub-width))))

(defn remove-center [subdivided-tiles]
  ; Just change to vector and dissoc?
  (let [sqr (-> subdivided-tiles (count) (Math/sqrt) (int))
        middle (int (Math/floor (/ sqr 2)))
        remove-i (+ (* middle sqr) middle)]
    (map second
      (filter #(not= (first %) remove-i)
              (map vector (range) subdivided-tiles)))))

(defn full-menger-subdivide [tiles side-divisions]
  (mapcat #(-> %
               (subdivide-tile side-divisions)
               (remove-center))
          tiles))

(defn iterate-menger-subdivide [tiles side-divisions iterations]
  (nth (iterate #(full-menger-subdivide % side-divisions) tiles)
       iterations))


