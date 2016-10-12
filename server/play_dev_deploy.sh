#!/bin/bash

PLAY_PATH="/home/wouter/autodelen-dev"
SERVER_PATH="/home/wouter/server"

cd $SERVER_PATH
./play-dev/stop.sh

cd $PLAY_PATH
sudo chown -R wouter:wouter .
sudo chmod -R 777 .
git checkout -- .
git clean -f
git pull origin development
sed -i 's/test/devel/' conf/db.properties
sudo rm -rf target
sudo play modules
sudo play clean compile stage

function packages {
    cd "${PLAY_PATH}$1"
    ls -dm */ | sed "s/, /:/g;s/\///g"
}

sed -i "s/\(.*javaPackagesApp = \).*/\1\"$(packages '/app')\"/;\
    s/\(.*javaPackagesDBModule = \).*/\1\"$(packages '/modules/DBModule/src')\"/;\
    s/\(.*javaPackagesAuthModule = \).*/\1\"$(packages '/modules/AuthModule/src')\"/" project/ApiDocSettings.scala

sudo play api-doc

cd $SERVER_PATH
./play-dev/start.sh
