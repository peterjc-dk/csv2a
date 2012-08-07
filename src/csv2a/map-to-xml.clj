
(require '[clojure.xml :as xml])

(defn map2tag-map [k v] {:tag k :content [v]})

(xml/emit-element (map2tag-map :a "hej"))