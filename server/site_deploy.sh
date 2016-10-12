#!/bin/bash

SERVER_PATH="/home/wouter/server"

cd $SERVER_PATH
git pull origin master

sudo cp -r $SERVER_PATH/site/* /var/www/
