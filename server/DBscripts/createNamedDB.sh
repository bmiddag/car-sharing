#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Please give a name for the DB to be created"
fi
name=$1
./mysqlr "CREATE DATABASE $name CHARACTER SET utf8"

./mysqlr "USE $1 ; source tables.sql ;"

