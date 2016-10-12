#!/bin/bash

toSave=15
toDelete=$(echo "$(ls /screenshots | wc -l)-$toSave" | bc)
if [[ toDelete > 0 ]]; then
    ls /screenshots | head -n $toDelete | while read file; do
        sudo rm -f "/screenshots/$file"
    done
fi
