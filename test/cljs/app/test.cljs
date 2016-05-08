(ns app.test
  (:require-macros [cljs.test :refer [deftest testing is]])
  (:require [cljs.test :as t]
            [app.core :as app]))

(deftest test-arithmetic []
  (testing "Excluded middle"
    (is (= 1 0))))
