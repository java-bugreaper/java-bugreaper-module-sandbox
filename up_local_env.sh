#!/bin/bash

# make .env
echo "make local env"
echo CI_PROJECT_DIR=. > .env
echo TAG=all >> .env

# kill all old containers
echo "delete active containers"
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

# dummy log on startup
echo 'startup log success' > java-test/src/test/resources/logs/test.log

docker-compose -f docker-compose-core.yml up -d allure-server
docker-compose -f docker-compose-part1.yml up -d minio

