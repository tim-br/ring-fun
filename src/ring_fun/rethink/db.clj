(ns ring-fun.rethink.db
  (:import [com.rethinkdb RethinkDB]
           [com.rethinkdb.gen.exc ReqlError]
           [com.rethinkdb.gen.exc ReqlQueryLogicError]
           [com.rethinkdb.gen.exc ReqlQueryLogicError]
           [com.rethinkdb.model MapObject]
           [com.rethinkdb.net Connection Cursor]))
;
(def ri RethinkDB/r)

;(def conn (.connection rethink-conn))
;
;(def host (.hostname conn "localhost"))
;
;(def port (.port host 28015))
;
;(def final-connect (.connect port))

(defn conn [rethink-instance]
  (-> (.connection rethink-instance)
      (.hostname "localhost")
      (.port 28015)
      (.connect)))

(defn create-db [rethink-instance name]
  (.db rethink-instance name))

(defn db [rethink-instance db]
  (.db rethink-instance db))

(defn create-table
  "(create-table (db rethink-conn \"test\")  final-connect \"foo\")"
  [db connection name]
  (.run (.tableCreate db name) connection))

(defn insert-query-into-table
  [ri table query conn]
  (-> (.table ri table)
      (.insert query)
      (.run conn)))

(defn basic-with [hm key value]
  (.with hm key value))

(defmacro fun-> [args]
  ())

(defn chain-withs [rm list]
  (if (empty? (rest (rest list)))
    (.with rm (first list) (second list))
    (.with (chain-withs rm (rest (rest list))) (first list) (second list))))

(defn chain-with2 [rm list]
  (let [pairs (partition 2 list)]))

#_(defmacro with-m [hm & args]
  (let [f (first args)
        s (second args)]
    (if (empty? (rest (rest args)))
      (.with hm f s)
      (do
        ))))

(defn rhash-map
  [key value]
  (-> (.hashMap ri key value)
      #_(.with key2 value2)
      #_(.with key3 value3)))

;(defn reduce)

(defn mhash-map
  [& args]
  (let [rm (rhash-map (first args) (second args))]
    (.with rm (first (rest (rest args))) (second (rest (rest args))))))


#_(if (empty? (rest (rest args)))
  (rhash-map (first args) (second args))
  (do
    (println (first (rest (rest args))))
    (println (second (rest (rest args))))
    (.with (mhash-map (rest (rest args))) (first args) (second args))))
(defn gre [hm & args]
  (let [f (first args)
        s (second args)]
    (if (empty? (rest (rest args)))
      (rhash-map hm f s)
      (let [later (gre hm (rest (rest args)))]
        `(.with ~later ~f ~s)))))

(def cursor (.run (.table ri "authors") (conn ri)))

(for [i cursor]
  (.get i "frank"))