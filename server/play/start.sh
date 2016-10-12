#!/bin/bash

PLAY_PATH="/home/wouter/autodelen"
SERVER_PATH="/home/wouter/server"

mkdir -p "$SERVER_PATH/logs"

if [ ! -f "$PLAY_PATH/target/universal/stage/RUNNING_PID" ]; then
	sudo "$PLAY_PATH/target/universal/stage/bin/autodelen" -Dhttp.port=9000 -Dapplication.context=/prod/ >> "$SERVER_PATH/logs/playserver.log" 2>&1 &
fi
