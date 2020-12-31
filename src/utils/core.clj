(ns utils.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn read-input [path]
  (-> path
      io/file
      slurp
      str/split-lines))



