#!/bin/bash

DIST_DIR=`echo /home/batchprod/dist`
echo "DIST_DIR=$DIST_DIR"

if ! ls -1 "$DIST_DIR/old" &> /dev/null; then
 echo "ERROR: old version is not available to flip"
 exit 1
fi

echo "old -> cur"
if [[ -r "$DIST_DIR/old" ]]; then
 rm -Rf "$DIST_DIR/cur"
 mv -f -- "$DIST_DIR/old" "$DIST_DIR/cur"
else
 echo "old link is not present"
fi

echo "del -> old"
if [[ -r "$DIST_DIR/del" ]]; then
 mv -f -- "$DIST_DIR/del" "$DIST_DIR/old"
else
 echo "del link is not present"
fi

echo "link flip done"

