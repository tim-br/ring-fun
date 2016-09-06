CREATE TABLE companies(
  company_id SERIAL PRIMARY KEY,
  name text NOT NULL,
  date_created DATE DEFAULT now()
)