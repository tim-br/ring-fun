(ns ring-fun.html.core
  (:require [hiccup.core :as hiccup]
            [hiccup.page :as page]))

(def add-company
  [:form {:action "/companies/add-post"
          :method "post"}
   [:input {:name  "new_company"
            :value "Baskerville"}]
   [:button "Create New Company"]])

(def signin
  [:form {:action "/companies/signin"
          :method "post"}
   [:input {:name  "user_name"
            :value "enter user name here"}]
   [:input {:name  "password"
            :value "password"}]
   [:button "Sign In!"]])

(defn body
  [html]
  [:body html])

(defn main-html
  [body]
  [:html {:lang "en"}
   add-company])

(def add-company-page
  (page/html5 (body add-company)))

(def signin-page
  (page/html5 (body signin)))