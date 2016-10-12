#!/bin/bash
FILES=/Users/stevendeblieck/Dropbox/Informatica/Vakoverschrijdend/verslagen/mockups/*.png
echo "$FILES"
for f in $FILES
do
  echo "$f" | sed "s/\/Users\/stevendeblieck\/Dropbox\/Informatica\/Vakoverschrijdend\/verslagen\//\includegraphics\[scale=0.4\]{/"
done
