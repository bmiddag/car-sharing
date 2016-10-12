#!/bin/bash

cd modules/DBModule/src
javac -cp "$(ls ../lib/*.jar | sed ':a;N;s/\n/:/;ta')" */*.java
jar cf ../../../lib/DBModule.jar */*.class
rm */*.class
cd -
