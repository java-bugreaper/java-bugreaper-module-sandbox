
# How to start

### To run local env (in docker) for all tests


```shell script
./up_local_env.sh
```

=================================================================================
# CORE (core features)

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

=================================================================================
# PART 1 (API, mocks, Db(MariaDb), Minio)

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
- see Mailpit(email) UI http://localhost:8025
- connect to mariaDb localhost:3306 (MARIA_SCHEMA:maria_pass MARIA_SCHEMA)

***

=================================================================================
# Brokers (artemis, rabbit, kafka)

- run tests on the local environment:

```shell script
./gradlew clean :java-test-brokers:test 
```

- local report (without server)
```shell script
./gradlew :java-test-brokers:downloadAllure &&
./gradlew :java-test-brokers:allureServe
```

- see artemis UI on http://localhost:8161 (artemis:artemis-password)
- see rabbit UI on http://localhost:15672 (guest_user:guest_pass)
- see kafka UI on http://localhost:8082

***

=================================================================================
# Databases (MySql, Mssql, Postgresql, OracleDb, Clickhouse)

- run tests on the local environment:

```shell script
./gradlew clean :java-test-db:test 
```

- local report (without server)
```shell script
./gradlew :java-test-db:downloadAllure &&
./gradlew :java-test-db:allureServe
```

- connect to PostgreSql localhost:5432 (alex:12345 public)
- connect to MsSql localhost:1433 (SA:mssql_pass1234567 test_schema)
- connect to OracleDb localhost:1521 (ADMIN:ADMIN ORACLE_SCHEMA)
- connect to MySql localhost:3308 (mysql_user:mysql_pass MYSQL_SCHEMA)
- connect to Clickhouse localhost:3308 (default:password myschema)


=================================================================================
# No sql Databases (MongoDb, Redis)

- run tests on the local environment:

```shell script
./gradlew clean :java-test-nosql:test 
```

- local report (without server)
```shell script
./gradlew :java-test-nosql:downloadAllure &&
./gradlew :java-test-nosql:allureServe
```

- connect to MongoDb UI localhost:8081 (root:example_password)
