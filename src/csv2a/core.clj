(ns csv2a.core)

(require '[clojure.data.csv :as csv]
         '[clojure.string :as str]
         '[clojure.java.io :as io])

(import (java.io File))

(defn split-line-to-vec [line sep]
  (let [new-line (str/replace line #";" "; ")]
    (into [] (.split new-line sep))))

(defn is-file [file-name]
  (. (io/file file-name) isFile))

(defn csv-file-to-vec [file-name]
  (with-open [rdr (io/reader file-name)]
    (reduce conj [] (line-seq rdr))))

(defn csv-file-to-vec-of-vec [file-name &
                        {:keys [separator quote]
                         :or {separator \; quote \"}}]
  {:pre [(is-file file-name)]}
	(with-open [in-file (io/reader file-name)]
	  (doall
	    (csv/read-csv in-file :separator separator :quote quote))))

(defn vec-to-csv-file [file-name vec]
	(with-open [out-file (io/writer file-name)]
	  (csv/write-csv out-file vec)))

(defn apply-struct [the-struct ls]
  (try
    (apply-struct-raw the-struct ls)
    (catch java.lang.IllegalArgumentException ex
      (if (= (.getMessage ex)
             "Too many arguments to struct constructor")
        (apply-struct-raw the-struct (pop ls))))))

(defn apply-struct-raw [the-struct ls]
  (apply struct the-struct ls))

(defn to-hash-arr [seq-vec]
  (let [header (first seq-vec)
        keyword-list (map #(keyword (str/replace % #" " "")) header)
        row-struct  (apply create-struct keyword-list)
        csv-list (rest seq-vec)
        row (first csv-list)]
    (map #(apply-struct row-struct % ) csv-list)))

(defn csv-file-to-hash [csv-file-name]
  (to-hash-arr
   (csv-file-to-vec-of-vec csv-file-name)))

(defn -main [& args]
  (let [csv-file (first args)]
    (if (is-file csv-file)
      (csv-file-to-hash csv-file)
      (println (str "Argument Error: " csv-file " Is not a file")))))
