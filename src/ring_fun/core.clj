(ns ring-fun.core
  (:require [ring.adapter.jetty :as j]
            [clojure.java.io :as io]))

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
  (println "of course")
  (str "hello computer"))

(defn website-responses
  [uri]
  (println (str "the uri is " uri))
  (case uri
    "/" (slurp (file "login.html"))
    "/example" (slurp (file "example.html"))
    "/verify_login" (sign-in 3290)
    "/signin.css" (slurp (file "signin.css"))
    "/CampFlyer[final].pdf" (pdf-file "CampFlyer[final].pdf")
    (slurp (file "example.html"))))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn handler [request]
  (println "yo")
  {:status  200
   :headers (if (= "/signin.css" (:uri request))
              {"Content-Type" "text/css"}
              (if (= "/CampFlyer[final].pdf" (:uri request))
                {"Content-Type" "application/pdf"}
                {"Content-Type" "text/html"}))
   :body    (website-responses (:uri request))

   #_(if (= "/example" (:uri request))
       (slurp data-file)
       "hellomoon")
   ;:body    (str (:request-method request))
   ;:body (slurp data-file)
   })

(defn what-is-my-ip [request]
  (println "yolo")
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (:remote-addr request)})

#_(j/run-jetty #'handler {:port 4000})

;(do
;  (j/run-jetty what-is-my-ip {:port 4444})
;  (j/run-jetty handler {:port 6666}))

(j/run-jetty #'handler {:port 12223 :join? false})

