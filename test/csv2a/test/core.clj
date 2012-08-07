(ns csv2a.test.core
  (:use [csv2a.core])
  (:use [clojure.test]))

(deftest test-split-line
   (testing "split-line-to-vec"
      (is (=  ["h" "k"] (split-line-to-vec "h;k" ";")) "hmm")
      (is (=  ["h"] (split-line-to-vec "h;" ";")) "hmm")
      (is (=  ["h" "k" "Hej hvad saa"] (split-line-to-vec "h;k;Hej hvad saa" ";")) "hmm")
))

(deftest test-is-file
	(testing "is-file"
		(is (= true  (is-file "scpi.csv")))
		(is (= false (is-file "hej")))
	))

(deftest test-csv-file-to-vec
	(testing "csv-file-to-vec"
	  (is (= true (vector? (csv-file-to-vec "scpi.csv" ))))
	  (is (= true (string? (first (csv-file-to-vec "scpi.csv" )))))
	  (is (= ["a;b;c" "1;2;3"] (csv-file-to-vec "tmp/test-csv.csv" )))
	)
)

(deftest test-apply-struct
	(testing "apply-struct"
	   (is (= {:hej 1 :igen 2}  (apply-struct (create-struct :hej :igen ) '(1 2))))
	   (is (= true  (map? (apply-struct (create-struct :hej :igen ) '(1 2)))))
	))

(deftest test-to-hash-arr
	(testing "to-hash-arr"
	  (is (= '({:a "1", :b "2", :c "3"})
	         (to-hash-arr
		        (map #(split-line-to-vec % ";")
		        (csv-file-to-vec "tmp/test-csv.csv")))))
	))

(defn test-ns-hook []
  (println "Running test hook")
  (test-is-file)
  (test-csv-file-to-vec)
  (test-apply-struct)
  (test-to-hash-arr)
  (test-split-line)
)
