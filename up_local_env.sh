#!/bin/bash

# fill .env
echo "update local env"
echo CI_PROJECT_DIR=. > .env
echo TAG=all >> .env

run_core() {
  docker-compose -f docker-compose-core.yml up -d allure-server
  # dummy log on startup
  echo 'startup log success' > java-test-core/src/test/resources/logs/test.log
}

run_part1() {
  docker-compose -f docker-compose-part1.yml up -d minio mock-server mariadb
}

run_db() {
  docker-compose -f docker-compose-db.yml up -d postgresql mssql oracle mysql clickhouse
}

run_brokers() {
  docker-compose -f docker-compose-brokers.yml up -d artemis rabbit test_ui_kafka
}

run_all() {
  run_core
  run_part1
  run_db
  run_brokers
}

PS3="Choose environment to start: "

options=("core" "part1" "db" "brokers" "all" "exit")


select opt in "${options[@]}"; do
  echo "In progress..."

  # kill all old containers
  echo "delete active containers..."
  docker stop $(docker ps -a -q)
  docker rm $(docker ps -a -q)

  case $opt in
    "core")
      run_core
      break
      ;;
    "part1")
      run_part1
      break
      ;;
    "db")
      run_db
      break
      ;;
    "brokers")
      run_brokers
      break
      ;;
    "all")
      run_all
      break
      ;;
    "exit")
      echo "Bye"
      break
      ;;
    *)
      echo "Invalid option"
      ;;
  esac
done
