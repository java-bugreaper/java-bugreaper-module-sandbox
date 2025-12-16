CREATE TABLE IF NOT EXISTS accounts (
	user_id serial PRIMARY KEY,
	username VARCHAR ( 50 ) UNIQUE NOT NULL,
	password VARCHAR ( 50 ) NOT NULL,
	email VARCHAR ( 255 ) UNIQUE NOT NULL,
	fee NUMERIC(5,2),
    is_active BOOLEAN,
	birthday DATE NULL,
	created_on TIMESTAMP NOT NULL,
    last_login TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
   role_id serial PRIMARY KEY,
   role_name VARCHAR (255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS account_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  grant_date TIMESTAMP,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (role_id)
      REFERENCES roles (role_id),
  FOREIGN KEY (user_id)
      REFERENCES accounts (user_id)
);

CREATE table IF NOT EXISTS public.cities_dict (
	city_id serial4 NOT NULL,
	name varchar(50) NOT NULL,
	description varchar(50) DEFAULT NULL,
	is_capital boolean,
	created_on timestamp NOT NULL,
	last_login timestamp NULL
);

INSERT INTO public.cities_dict
(city_id, name, description, is_capital, created_on, last_login)
values
(1, 'Kyiv', 'Ukraine', true, '2020-01-10', '2020-01-20'),
(2, 'Shmil', null, false, '2022-01-10', null);
