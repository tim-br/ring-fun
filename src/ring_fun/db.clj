 (ns ring-fun.db
   (:require [clojure.java.jdbc :as j]
             [honeysql.core :as hsql]))

 (def db-spec
   {:classname   "org.postgresql.Driver" ; must be in classpath
    :subprotocol "postgresql"
    :subname     "//localhost:5432/company-app"
    ; Any additional keys are passed to the driver
    ; as driver-specific properties.
    :user        "postgres"
    :password    "pass"})

(def firm-connection 'nothing)

(defn create-new-company!
  [db name]
  (j/insert! db :companies
    {:name name}))

(defn parse-date [inst]
  (.format (java.text.SimpleDateFormat. "MM/dd/yyyy")
           inst))

(defn company-map
  [name]
  {:select [:*]
   :from   [[:companies :c]]
   :where  [:= :c.name name]})

(defn company
  [name]
  (j/query db-spec (hsql/format (company-map name))))

(defn companies
  [db]
  (j/query db ["select * from companies"]))

(defn companies-to-map
  [company-list]
  (map #(assoc % :date_created (parse-date (:date_created %))) company-list))