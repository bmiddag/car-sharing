#!/bin/bash

if [ $# -ne 1 ]; then 
  echo "Please give a name for the DB to fill"
  exit
fi

if [ "$1" != "test" -a "$1" != "devel" -a "$1" != "prod" ] ; then
  echo "Name must be either test, devel or prod."
  exit 2
fi

  
./mysqlr "USE $1; source fillDB.sql ;"

