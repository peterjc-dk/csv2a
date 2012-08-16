(require '[clojure.core.reducers :as r])
(reduce + (r/filter even? (r/map inc [1 1 1 2])))

(iterate inc 1)
(time 1)