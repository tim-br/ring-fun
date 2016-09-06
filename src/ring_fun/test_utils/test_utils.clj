(ns ring-fun.test-utils.test-utils
  (:require [clojure.java.jdbc :as j]
            [ring-fun.db :as db]))

(defn test-firm-connection
  []
  {:classname "org.postgresql.Driver",
   :subprotocol "postgresql",
   :subname "//localhost:5432/company-app",
   :user "timothy",
   :password ""})

(defmacro with-rollback
  "Binds a firm database connection to db. This works for both
  a firm database and view config."
  [db & tests]
  `(j/with-db-transaction [~db (test-firm-connection)]
    (j/db-set-rollback-only! ~db)
    (with-redefs [ring-fun.db/db-spec (constantly ~db)]
      ~@tests)))