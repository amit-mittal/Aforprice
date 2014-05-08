#!/bin/bash

DIR_NAME=`date --date="3 days ago" +%Y%m%d`
PATH_LOC=/home/batchprod/crawler/product_summary_html_files/prod
TERA_DRIVE=/home/batchprod/teradrive/Backup/HTML
echo `date`":cd ${PATH_LOC}"
cd $PATH_LOC
if [ ! -d $DIR_NAME ]; then
        echo "$DIR_NAME not found. Nothing to zip and backup. Quitting now"
        exit 1
fi
echo `date`": tar zcf ${DIR_NAME}.tgz $DIR_NAME"
tar zcf ${DIR_NAME}.tgz $DIR_NAME
if [  $? != 0 ]; then
	echo `date`":tar zcf ${DIR_NAME}.tgz $DIR_NAME failed"
	exit 1
fi
echo "rm -rf ${DIR_NAME}" 
rm -rf ${DIR_NAME}

if [ ! -d $TERA_DRIVE ]; then
        echo `date`":Not moving file to TB drive as it is unavailable"
        exit 1
fi

echo `date`":mv ${DIR_NAME}.tgz /home/batchprod/teradrive/Backup/HTML"
mv ${DIR_NAME}.tgz /home/batchprod/teradrive/Backup/HTML
echo `date`":all done"