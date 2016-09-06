CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  name text NOT NULL,
  company_id integer NOT NULL references companies(company_id),
  date_created DATE DEFAULT now(),
  last_login TIMESTAMP,
  email VARCHAR (355) UNIQUE,
  password VARCHAR (50) NOT NULL
)