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


echo "Dropping old database..."
./mysqlr "drop database $1"
echo "Creating new database..."
./createNamedDB.sh $1
echo "Creating user 'edran' and granting privileges..."
./mysqlr "GRANT ALL PRIVILEGES ON $1.* TO 'edran'@'localhost' identified by 'edran';"
echo "Creating user 'auth' and granting privileges..."
./mysqlr "GRANT ALL PRIVILEGES ON $1.* TO 'auth'@'localhost' identified by 'auth';"
echo "Filling database..."
./fillDB.sh $1

