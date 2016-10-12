#!/bin/bash

PLAY_PATH="/home/wouter/autodelen-dev"
SERVER_PATH="/home/wouter/server"

mkdir -p "$SERVER_PATH/logs"

if [ ! -f "$PLAY_PATH/target/universal/stage/RUNNING_PID" ]; then
	sudo "$PLAY_PATH/target/universal/stage/bin/autodelen" -Dhttp.port=9001 -Dapplication.context=/dev/ >> "$SERVER_PATH/logs/playserver-dev.log" 2>&1 &
fi
