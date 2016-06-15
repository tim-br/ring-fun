(ns ring-fun.css-style
  (:require [garden.core :refer [css]]))

(defn body []
  (css [:body {:padding-top "40px"
               :padding-bottom "40px"
               :background-color "#FFFFFF"}]))

