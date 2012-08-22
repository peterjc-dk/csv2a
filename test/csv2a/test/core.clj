(ns csv2a.test.core
  (:use [csv2a.core])
  (:use [clojure.test]))

(deftest test-is-file
  (testing "file?"
    (is (= true  (file? "test/resources/scpi.csv")))
    (is (= false (file? "hej")))))

(deftest test-is-sq-sq
  (testing "sq-of-sq?"
    (is (= true  (sq-of-sq? [[]])))
    (is (= false (sq-of-sq? "hej")))))

(deftest test-is-vec-vec
  (testing "vec-of-vec?"
    (is (= true  (vec-of-vec? [[]])))
    (is (= false (vec-of-vec? [])))))

(deftest test-is-sq-map
  (testing "sq-of-map?"
    (is (= true  (sq-of-map? [{}])))
    (is (= false (sq-of-map? [])))))

(deftest test-csv-file-to-vec-vec
  (testing "csv-file-to-vec-vec"
    (is (= true
           (vector?
            (csv-file-to-vec-vec "test/resources/scpi.csv" ))))
    (is (= true
           (vector?
            (first (csv-file-to-vec-vec "test/resources/scpi.csv" )))))
    (is (= true
           (vec-of-vec?
            (csv-file-to-vec-vec "test/resources/scpi.csv" ))))
    (is (= [["a" "b" "c"] ["1" "2" "3"]]
           (csv-file-to-vec-vec "test/resources/test-csv.csv" )))
    (is (thrown? java.lang.AssertionError
                 (csv-file-to-vec-vec "not-there.csv")))))

(deftest test-sq-sq-to-csv-file
  (testing "sq-sq-to-csv-file"
    (let [file-name "test/resources/test.csv"
          data [["1" "2"]]]
      (is (= false (file? file-name)))
      (is (= nil (sq-sq-to-csv-file file-name data)))
      (is (= true (file? file-name)))
      (is (= data (csv-file-to-vec-vec file-name)))
      (is (= true (clojure.java.io/delete-file file-name)))
      (is (= false (file? file-name))))))

(deftest test-sq-sq-to-sq-map
  (testing "sq-sq-to-sq-map"
    (is (= [{:1 "a" :2 "b"}]
           (sq-sq-to-sq-map [["1" "2"] ["a" "b"]])))
    (is (= [{:1 "a" :2 "b"}]
           (sq-sq-to-sq-map [["1" "2"] ["a" "b" "c"]])))
    (is (= [{:1 "a" :2 "b"}]
           (sq-sq-to-sq-map [["1" "2"] ["a" "b"] [""]])))
    (is (= [{:1 "a" :2 "b"}]
           (sq-sq-to-sq-map [["1" "2"] ["a" "b"] []])))
    (is (= [{:1 "a" :2 "b"} {:1 "c" :2 "d"} {:1 "f"}]
           (sq-sq-to-sq-map [["1" "2"] ["a" "b"] ["c" "d" "e"] ["f"]])))
    (is (thrown? java.lang.AssertionError
                 (sq-sq-to-sq-map [])))
    (is (thrown? java.lang.AssertionError
                 (sq-sq-to-sq-map [[]])))
    (is (thrown? java.lang.AssertionError
                 (sq-sq-to-sq-map [["a" "b"]])))))

(deftest test-sq-map-to-sq-sq
  (testing "sq-map-to-sq-sq"
    (is (= '(("1" "2") ("a" "b"))
           (sq-map-to-sq-sq [{:1 "a" :2 "b"}])))
    (is (= '(("1" "2") ("a" "b") ("c" "d") ("f"))
           (sq-map-to-sq-sq [{:1 "a" :2 "b"} {:1 "c" :2 "d"} {:1 "f"}])))
    (is (thrown? java.lang.AssertionError
                 (sq-map-to-sq-sq [])))
    (is (thrown? java.lang.AssertionError
                 (sq-map-to-sq-sq [[]])))
    (is (thrown? java.lang.AssertionError
                 (sq-map-to-sq-sq [["a" "b"]])))))

(deftest test-csv2vec-and-sq2csv
  (testing "test-csv2vec-and-sq2csv"
    (let [data [["a" "b" "v"] ["s" "t" "g"] ["k" "j" "l"]]
          file-name "test/resources/tmp-out.csv" ]
      (is (= (sq2csv file-name data)))
      (is (= data (csv2vec file-name))))))

(deftest test-csv2map-and-map2csv
  (testing "test-csv2map-and-map2csv"
    (let [data [{:a "1"  :b  "23" :c "78"}
                {:a "3" :b "7" :c "8"}
                {:a "9" :b "er" :c "kl"} ]
          file-name "test/resources/tmp-out.csv" ]
      (is (= (map2csv file-name data)))
      (is (= data (csv2map file-name))))))

;; (defn test-ns-hook [] (println "Running test hook"))
