#!/bin/bash

mkdir -p backups
cd backups
source ../conf/db.properties


touch oneDayAgo.sql.bz2
mv oneDayAgo.sql.bz2 twoDaysAgo.sql.bz2 2>/dev/null

touch current.sql.bz2
mv current.sql.bz2 oneDayAgo.sql.bz2 2>/dev/null

mysqldump --opt --skip-extended-insert --max_allowed_packet=128M --single-transaction -u$dbuser -p$dbpasswd $dbname > current.sql

bzip2 current.sql

if [[ $(date +"%w") -eq 0 ]] ; then
  cp current.sql.bz2 backup$(date +"%m-%d").sql.bz2  
fi
 

