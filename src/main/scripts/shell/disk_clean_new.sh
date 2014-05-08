#!/bin/bash

log() {
        echo `date`": $1"
}

HTML_CLEAN_UP_DIR=`date --date="2 days ago" +%Y%m%d`
LOG_CLEAN_POSTFIX=`date --date="7 days ago" +%y%m%d`.log

HTML_FILES_DIR=/home/batchprod/crawler/product_summary_html_files/prod
LOG_FILES_DIR=/var/log/crawler

TERA_DRIVE_HTML_BAK_DIR=/home/batchprod/teradrive/Backup/HTML

log "cleaning up old logs"
log "cd $LOG_FILES_DIR"
cd $LOG_FILES_DIR
log "rm -rf *$LOG_CLEAN_POSTFIX"
rm -rf *$LOG_CLEAN_POSTFIX
log "cleaned up old logs"

if [ -d $TERA_DRIVE_HTML_BAK_DIR ]; then
        log "tera drive is available. archive the html files"
        log "cd $TERA_DRIVE_HTML_BAK_DIR"
        cd $TERA_DRIVE_HTML_BAK_DIR

        if [ ! -d ${HTML_FILES_DIR}/$HTML_CLEAN_UP_DIR ]; then
                log "${HTML_FILES_DIR}/$HTML_CLEAN_UP_DIR not found. Nothing to archive."
                exit 1
        fi
        log "tar zcf ${HTML_CLEAN_UP_DIR}.tgz ${HTML_FILES_DIR}/$HTML_CLEAN_UP_DIR"
        tar zcf ${HTML_CLEAN_UP_DIR}.tgz ${HTML_FILES_DIR}/$HTML_CLEAN_UP_DIR
        log "rm -rf ${HTML_FILES_DIR}/$HTML_CLEAN_UP_DIR"
        rm -rf ${HTML_FILES_DIR}/$HTML_CLEAN_UP_DIR
        log "done archiving ${HTML_FILES_DIR}/$HTML_CLEAN_UP_DIR"
        exit 0
fi
log "WARN: tera drive is unavailable"

exit 1