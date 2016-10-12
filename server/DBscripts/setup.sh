#!/bin/bash

set -o errexit
trap 'echo "Previous command did not complete successfully. Exiting." >&2' ERR


if [ $# -ne 1 ]; then
  echo "Please supply the name of the DB to reset. [test,devel,prod]"
  exit 2
fi

if [ "$1" != "test" -a "$1" != "devel" -a "$1" != "prod" ] ; then
  echo "Name must be either test, devel or prod."
  exit 2
fi

source ../configurations/database.conf

echo "Creating user 'edran' and granting privileges..."
$DBsft --local-infile -u$DBusr -p$DBpass -e "GRANT ALL PRIVILEGES ON $1.* TO 'edran'@'localhost' identified by 'edran';"
echo "Creating user 'auth' and granting privileges..."
$DBsft --local-infile -u$DBusr -p$DBpass -e "GRANT ALL PRIVILEGES ON $1.* TO 'auth'@'localhost' identified by 'auth';"

echo "Running database setup ..."
$DBsft --local-infile -u$DBusr -p$DBpass $1 < tables.sql
$DBsft --local-infile -u$DBusr -p$DBpass $1 < setup.sql
$DBsft --local-infile -u$DBusr -p$DBpass $1 < procedures.sql
