#!/bin/bash
export DISPLAY=:99
sudo nohup Xvfb :99 -ac -extension RANDR -screen 0 1024x768x24 &
nohup firefox &
