(ns ring-fun.migrations
  (:require [migratus.core :as migratus]
            [ring-fun.db :as db]))

(def config {:store                :database
             :migration-dir        "migrations/"
             :db db/db-spec})
