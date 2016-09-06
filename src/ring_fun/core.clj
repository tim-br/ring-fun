(ns ring-fun.core
  (:require [ring.adapter.jetty :as j]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [migratus.core :as migratus]
            [ring-fun.db :as db]
            [ring-fun.html.core :as page]
            [ring-fun.migrations :refer [config]]
            [ring.util.response :as resp]
            [ring.middleware.json :as middleware]
            [ring.middleware.params :as params]
            [ring.middleware.session :as session])
            (:gen-class))

(defn session-handler [{session :session}]
  (let [                                                    ;count   (:count session 0)
        session (assoc session :count (inc count))]
    (-> (resp/response (str "You accessed this page " count " times."))
        (assoc :session session))))

(defn wrap-content-type [handler content-type]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers "Content-Type"] content-type))))

(defn file
  [name]
  (io/file
    (io/resource
      name)))

(defn pdf-file
  [name]
  (io/input-stream
    (io/resource
      name)))

(defn sign-in
  [request]
  (println "of course"))

(defn website-responses
  [uri]
  (println (str "the uri is " uri))
  (case uri
    "/" (slurp (io/resource "login.html"))
    "/example" (slurp (io/resource "example.html"))
    "/verify_login" (sign-in 3290)
    "/signin" page/signin-page
    "/signin.css" (slurp (io/resource "signin.css"))
    "/CampFlyer.pdf" (pdf-file "CampFlyer.pdf")
    "/companies/add" page/add-company-page
    "/companies" (json/write-str {:companies (db/companies-to-map (db/companies db/db-spec))})
    "/companies/add-post" "yolo world"
    (slurp (io/resource "example.html"))))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn test-handler [request]
  (def bar "cubicle")
  (println "hello ftwt")
  (when (= :post (:request-method request))
    (println "here?")
    (let [new-company-name (-> (:form-params request)
                               (get-in ["new_company"]))]
      (db/create-new-company! db/db-spec new-company-name)
      (println new-company-name)
      (println "yup"))
    {:status 200
     :headers {"content-type" "text/html"}
     :body (slurp (file "example.html"))})
  {:status 200
   :headers {"content-type" "text/html"}
   :body (slurp (file "add_company.html"))})

(def test-app
  (params/wrap-params test-handler))

(defn handle-add-company-post
  [request]
  (let [new-company-name (-> (:form-params request)
                             (get-in ["new_company"]))]
    (db/create-new-company! db/db-spec new-company-name)
    {:body "company created"
     :status 302}))

(defn handler [{session :session :as request}]
  (println "yo")
  (def bar request)
  (cond (and (= :post (:request-method request)) (= "/companies/add-post" (:uri request)))
        (handle-add-company-post request)

        (= (:uri request) "/fun")
        (let [count   (:count session 0)
              session (assoc session :count (inc count))]
          (-> (resp/response (str "You accessed this page " count " times."))
              (assoc :session session)))
        #_{:status  200
         :headers {"content-type" "text/html"}
         :body    "ok ok "}
        ;session-handler

        :else
        {:status  200
         :headers (if (= "/signin.css" (:uri request))
                    {"Content-Type" "text/css"}
                    (if (= "/CampFlyer.pdf" (:uri request))
                      {"Content-Type" "application/pdf"}
                      (if (= "/companies" (:uri request))
                        {"content-type" "application/json"}
                        {"content-type" "text/html"})))
         :body    (website-responses (:uri request))}))

(def app
  (session/wrap-session
    (params/wrap-params handler)))

#_(if (= "/example" (:uri request))
    (slurp data-file)
    "hellomoon")
;:body    (str (:request-method request))
;:body (slurp data-file)

(defn what-is-my-ip [request]
  (println "yolo")
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (:remote-addr request)})

#_(j/run-jetty #'handler {:port 4000})

;(do
;  (j/run-jetty what-is-my-ip {:port 4444})
;  (j/run-jetty handler {:port 6666}))

(defn start-server
  []
  (j/run-jetty #'app {:port 9999 :join? false}))

(defn start-side-server
  []
  (j/run-jetty #'test-app {:port 9195 :join? false}))

(defn print-bytes-of-pdf [file]
  (let [byte (.read file)]
    (when (> byte 0)
      (println byte)
      (recur file))))

(defn print-bytes-of-pdf2 [file acc]
  (let [byte (.read file)]
    (if (> byte 0)
      (recur file (conj acc byte))
      acc)))

;(with-open [o (io/output-stream "fun.pdf")]
;  (print-bytes-of-pdf (pdf-file "CampFlyer.pdf") o))
;
;(defn print-bytes-of-pdf [file ta]
;  (let [byte (.read file)]
;    (when (> byte 0)
;      (println byte)
;      (.write ta byte)
;      (recur file ta))))

(defn -main [& args]
  (if (= "migrate" (first args))
    (migratus/migrate config)
    (start-server)))
