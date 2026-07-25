# NO SQL Databases Examples

- [mongo] MongoDb tests
  - interact with database
    - Insert records
    - Clean
  - Asserts
    - Collection empty/notEmpty
    - Record exist by STRICT or CONTAINS JSON

- [redis] Redis tests
  - interact with database
    - Insert records (with TTL too)
    - Clean all, specific keys, keys by pattern
  - Asserts
    - Keys existence, count
    - Records data in keys
    - TTL check