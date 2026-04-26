# Examples

- REST (API & Mocks) tests
  - interact with API & mock-server

- Minio tests
  - interact with brokers/objects
  - upload/download/read/share objects

- [db] MariaDb tests
  - interact with database
    - Insert from templates & merged test data (json/Objects dataProviders)
  - Asserts (record exist/notExist, table empty/notEmpty)
    - Specific data check
      - JSON_CONTAINS, JSON_EQUALS, REGEX

- [email] SMTP & Mailpit tests
  - interact with SMTP
    - send simple message
    - send message with builder (Cc, Bcc, attaches)
  - Asserts with Mailpit wrapper
    - Email data check
    - Email counts check
  - negative tests with Mailpit CHAOS
    - return SMTP errors

- Expected FAILED/BROKEN
  - MockEnchantedReport on FAIL example (difference)
  - Database tests fails (awaiting, show data from table)
  - tests (api, minio, db,  mocks) for example
- MockEnchantedReport on FAIL example

