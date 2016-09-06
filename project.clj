(defproject ring-fun "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha7"]
                 [ring "1.5.0"]
                 [garden "1.3.2"]
                 [hiccup "1.0.5"]
                 [honeysql "0.7.0"]
                 [org.clojure/java.jdbc "0.6.2-alpha1"]
                 [org.clojure/data.json "0.2.6"]
                 [com.rethinkdb/rethinkdb-driver "2.3.0"]
                 [ring/ring-mock "0.3.0"]
                 [ring/ring-json "0.4.0"]
                 [migratus "0.8.26"]
                 [postgresql "9.3-1102.jdbc41"]]
  :main ring-fun.core
  :plugins [[migratus-lein "0.1.0"]]
  :migratus {:store :database
             :migration-dir "migrations"
             :db {:classname   "org.postgresql.Driver" ; must be in classpath
                  :subprotocol "postgresql"
                  :subname     "//localhost:5432/company-app"
                  ; Any additional keys are passed to the driver
                  ; as driver-specific properties.
                  :user        "timothy"
                  :password    ""}})
