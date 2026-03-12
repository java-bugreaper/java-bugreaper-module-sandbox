-- # create databases
CREATE DATABASE IF NOT EXISTS myschema;


CREATE TABLE myschema.users
(
    id UInt32,
    name String,
    created_at DateTime
)
ENGINE = Log;
