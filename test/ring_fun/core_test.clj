(ns ring-fun.core-test
  (:require [clojure.test :refer :all]
            [ring-fun.core :refer :all]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [ring-fun.db :as db :refer [db-spec]]
            [ring-fun.test-utils.test-utils :as util]
            [ring.mock.request :as mock]))

(def mock-company-data
  (list {:company_id 1, :name "yolo", :date_created "07/07/2016"}
        {:company_id 5, :name "iron smith", :date_created "07/08/2016"}
        {:company_id 6, :name "howdy", :date_created "07/08/2016"}))

(deftest companies-end-point
  (with-redefs [ring-fun.db/companies-to-map (constantly mock-company-data)]
    (is (= (:body (app (mock/request :get "/companies")))
           (json/write-str {:companies mock-company-data})))))

(deftest add-company-endpoint
  (with-redefs [ring-fun.db/create-new-company! (constantly (println "creating company"))]
    (let [resp (app (mock/request :post
                                  "/companies/add-post"
                                  {"new_company" "bill"}))]
      (is (= (:body resp) "company created"))
      (is (= (:status resp) 302)))))

(defn post
  ([path params] (mock/request :post path params))
  ([user path params] (mock/request :post user path params)))

(deftest login-page-test
  (is (= (handler (mock/request :get "/"))
         {:status  200
          :headers {"content-type" "text/html"}
          :body    (slurp (file "login.html"))})))

(deftest camp-flyer
  (is (= (:status (handler (mock/request :get "/CampFlyer.pdf")))
         200))
  (is (= (print-bytes-of-pdf2 (:body (handler (mock/request :get "/CampFlyer.pdf"))) [])
         (print-bytes-of-pdf2 (pdf-file "CampFlyer.pdf") [])))
  (is (= (:headers (handler (mock/request :get "/CampFlyer.pdf")))
         {"Content-Type" "application/pdf"})))

(defn test-firm-connection
  []
  {:classname "org.postgresql.Driver",
   :subprotocol "postgresql",
   :subname "//localhost:5432/company-app",
   :user "timothy",
   :password ""})

(deftest running
  (util/with-rollback test-firm-connection
    (db/create-new-company! db-spec "ban")
    (is (= 1 (count (db/company "ban"))))))