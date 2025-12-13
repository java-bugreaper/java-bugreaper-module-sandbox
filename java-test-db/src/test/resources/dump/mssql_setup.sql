DROP TABLE IF EXISTS test_schema.drivers;
DROP SCHEMA IF EXISTS test_schema;

IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'Test')
  BEGIN
    EXEC ('CREATE SCHEMA test_schema;');
  END;

CREATE TABLE test_schema.drivers (
    id INT,
    full_name VARCHAR(50),
    fee DECIMAL(5,2),
    birth DATE,
    last_call DATETIME DEFAULT NULL
);

INSERT into test_schema.drivers
VALUES
(1, 'vasiliy', 15.99, '1991-10-14', null);
