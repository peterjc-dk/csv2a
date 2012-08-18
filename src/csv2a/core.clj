(ns csv2a.core)

(require '[clojure.data.csv :as csv]
         '[clojure.string :as str]
         '[clojure.java.io :as io])

(import (java.io File))

;;;;;;;;;;;;
;;; Pre and Post checker functions

(defn file? [file-name]
  (. (io/file file-name) isFile))

(defn sq-of-sq? [s]
  (and (sequential? s) (= #{true} (set (map #(sequential? %) s)))))

(defn vec-of-vec? [s]
  (and (vector? s) (= #{true} (set (map #(vector? %) s)))))

(defn sq-of-map? [s]
  (and (sequential? s) (= #{true} (set (map #(map? %) s)))))

;;;;;;;;;;;;
(defn csv-file-to-vec-vec
  [file-name & {:keys [separator quote]
                :or {separator \; quote \"}}]
  
  {:pre [(file? file-name)]
   :post [(vec-of-vec? %)]}
  
  (into [] (with-open [in-file (io/reader file-name)]
     (doall
      (csv/read-csv in-file :separator separator :quote quote)))))

(defn sq-sq-to-csv-file 
  [file-name sq &
   {:keys [separator quote] :or {separator \; quote \"}}]
  
  {:pre [(sq-of-sq? sq)]
   :post [(file? file-name)]}
    
  (with-open [out-file (io/writer file-name)]
    (csv/write-csv out-file sq :separator separator :quote quote)))

(defn sq-sq-to-sq-map [sq]
  {:pre [(sequential? sq)]
   :post [(sq-of-map? %)]}
  
  (let [header (first sq)
        keyword-list (map #(keyword (str/replace % #" " "")) header)
        csv-list (filter #(not= % [""]) (rest sq))
        row (first csv-list)]
    (mapv #(zipmap keyword-list %) csv-list)))

(defn sq-map-to-sq-sq [sm]
  {:pre [(sq-of-map? sm)]
   :post [(sq-of-sq? %)]}
  
  (let [header (map name (keys (first sm)))
        values (seq (map #(vals %) sm))]
    (conj values header)))

;;;;;;;;;;;;;;;
;; top functions

(defn csv2vec [file-name]
  (csv-file-to-vec-vec file-name))

(defn csv2map [file-name]
  (sq-sq-to-sq-map
   (csv-file-to-vec-vec file-name)))

(defn sq2csv [file-name sq]
  (sq-sq-to-csv-file file-name sq))

(defn map2csv [file-name sm]
  (vec-to-csv-file file-name
                   (sq-map-to-sq-sq sm)))

(comment
  ;; TODO
  ;;;;;;;;;;
  ;; map2xml
  ;; map2html
  ;; csv2xml
  ;; csv2html
  ;; map2xls
  ;; get-row
  ;; 
  ;; Tests!!!
  )

(defn -main [& args]
  (let [csv-file (first args)]
    (if (is-file csv-file)
      (csv2map csv-file)
      (println (str "Argument Error: " csv-file " Is not a file")))))

