#!/bin/bash

PLAY_PATH="/home/wouter/autodelen"

if [ -f "$PLAY_PATH/target/universal/stage/RUNNING_PID" ]; then
	sudo kill -9 $(cat "$PLAY_PATH/target/universal/stage/RUNNING_PID")
	sudo rm -f "$PLAY_PATH/target/universal/stage/RUNNING_PID"
fi
