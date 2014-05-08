#!/bin/bash

DIST_DIR=`echo /home/batchprod/dist`
echo "DIST_DIR=$DIST_DIR"

echo "point new to new build crawler-$BUILD_ID"
rm -Rf "$DIST_DIR/new"
ln -s "$DIST_DIR/crawler-$BUILD_ID" "$DIST_DIR/new"

#if ! ls -1 "$DIST_DIR/new" &> /dev/null; then
 #echo "ERROR: new version is not available to flip"
 #exit 1
#fi

echo "old -> del"
if [[ -r "$DIST_DIR/old" ]]; then
 rm -Rf "$DIST_DIR/del"
 mv -f -- "$DIST_DIR/old" "$DIST_DIR/del"
else
 echo "old link is not present"
fi

echo "cur -> old"
if [[ -r "$DIST_DIR/cur" ]]; then
 mv -f -- "$DIST_DIR/cur" "$DIST_DIR/old"
else
 echo "cur link is not present"
fi

echo "new -> cur"
if [[ -r "$DIST_DIR/new" ]]; then
 NEW_REALLINK=$(readlink -f "$DIST_DIR/new")
 #remove the link
 rm -Rf "$DIST_DIR/cur"
 ln -s $NEW_REALLINK "$DIST_DIR/cur"
else
 echo "new link is not present"
fi

echo "make script executable"
chmod +x /home/batchprod/dist/cur/scripts/shell/*
chmod +x /home/batchprod/dist/cur/scripts/shell/buildjobs/*

echo "link flip done"
