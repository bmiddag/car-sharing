#!/bin/bash

PLAY_PATH="/home/wouter/autodelen-test"
SERVER_PATH="/home/wouter/server"

cd $SERVER_PATH
./play-test/stop.sh

cd $PLAY_PATH
sudo chown -R wouter:wouter .
sudo chmod -R 777 .
git checkout -- .
git clean -f
git pull origin development

sudo chown -R wouter:wouter .
sudo chmod -R 777 .
sed -i 's/localhost:9000/edran.ugent.be\/test/' $PLAY_PATH/test/ApplicationTest.java
sudo rm -rf target
sudo play modules
sudo play clean compile stage

cd $SERVER_PATH
./play-test/start.sh
