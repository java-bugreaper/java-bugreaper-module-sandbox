
# How to start

### To run local env (in docker) for all tests


```shell script
./up_local_env.sh
```

# PART 0 (core)

- run tests on the local environment:

```shell script
./gradlew clean :java-test-core:test 
```

- local report (without server)
```shell script
./gradlew :java-test-core:downloadAllure &&
./gradlew :java-test-core:allureServe
```

- push allure report to the local allure web server

```shell script
./gradlew :java-test-core:allureGenReport
```

- see report on
http://localhost:8080

***

# PART 1 (base)

- run tests on the local environment:
```shell script
./gradlew clean :java-test-part1:test 
```

- run only positive tests
```shell script
./gradlew clean :java-test-part1:all 
```

- run only expected failed tests
```shell script
./gradlew clean :java-test-part1:failed 
```

- local report (without server)
```shell script
./gradlew :java-test-part1:downloadAllure &&
./gradlew :java-test-part1:allureServe
```

- see mock-server UI http://localhost:1080/mockserver/dashboard
- see minio UI http://localhost:9001 (admin:password)
- connect to mariaDb localhost:3306 (MARIA_SCHEMA:maria_pass)

***

# Brokers (artemis, rabbit, kafka)

- run tests on the local environment:

```shell script
./gradlew clean :java-test-brokers:test 
```

- local report (without server)
```shell script
./gradlew :java-test-part1:downloadAllure &&
./gradlew :java-test-part1:allureServe
```

- see artemis UI on http://localhost:8161 (artemis:artemis-password)
- see rabbit UI on http://localhost:15672 (guest_user:guest_pass)

***

# Databases (Mssql, Postgresql, OracleDb)

- run tests on the local environment:

```shell script
./gradlew clean :java-test-db:test 
```

- local report (without server)
```shell script
./gradlew :java-test-db:downloadAllure &&
./gradlew :java-test-db:allureServe
```

- connect to PostgreSql localhost:5432 (alex:12345)
- connect to MsSql localhost:1433 (SA:mssql_pass1234567)
- connect to OracleDb localhost:1521 (ADMIN:ADMIN)
