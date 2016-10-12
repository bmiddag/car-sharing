#!/bin/bash

PLAY_PATH="/home/wouter/autodelen"
SERVER_PATH="/home/wouter/server"

cd $SERVER_PATH
./play/stop.sh

cd $PLAY_PATH
git checkout -- .
git clean -f
git pull origin master
sed -i 's/test/prod/' conf/db.properties
sed -i 's/smtp\.conf/smtp-prod.conf/' conf/application.conf
sudo rm -rf target
sudo play modules
sudo play clean compile stage

cd $SERVER_PATH
./play/start.sh
