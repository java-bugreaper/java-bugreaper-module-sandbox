# Brokers examples

- API tests 
  - API requests/asserts (body, header, JSON, xml)

- Mocks tests
  - mocks expectations/verify/sequences/logs
  - verify with enchanted report flag on fail will generate report with diffs & summary table(in allure & logs info)

- DB tests
  - Insert fixtures (with fixture builder)
  - Assert data in DB (with/without conditions)
  - Assert JSON equal/contain

- Logs(files) tests
  - Check data in logs
  - Get log after each test

- FAILED tests (debug info)
  - Show data from DB on failed assert
  - FULL JSON assert on fail (not only current field)
