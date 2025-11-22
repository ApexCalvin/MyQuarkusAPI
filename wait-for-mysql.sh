#!/bin/sh
echo "Waiting for MySQL..."
while ! nc -z mysql 3306; do
  sleep 1
done
echo "MySQL is up - starting Quarkus"
exec java -jar quarkus-run.jar
