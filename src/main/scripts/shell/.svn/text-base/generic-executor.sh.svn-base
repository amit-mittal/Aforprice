#!/bin/bash

#TODO: Use getopts to get all the optional inputs and check if the input is valid
#TODO: Usage function
LOG_FILE_NAME=$1
MAIN_CLASS=$2
ENV=$3
shift
shift
shift

if [ "$1" == "RETRY_WRAPPER_MODE" ]; then
        echo "retry wrapper mode"
        RETRY_WRAPPER_MODE=true
        shift
fi

CACHE_MODE=PLUGIN
if [[ "$1" =~ .*CACHE_MODE.* ]]; then
        #split using = and get 2nd string
        arr=`echo "$1" | cut -d \= -f 2`
        echo "cache mode is set to ${arr}"
        CACHE_MODE=${arr}
        shift
fi

MIN_HEAP_SIZE=256M
if [[ "$1" =~ .*MIN_HEAP_SIZE.* ]]; then
        arr=`echo "$1" | cut -d \= -f 2`
        echo "MIN_HEAP_SIZE is set to ${arr}"
        MIN_HEAP_SIZE=${arr}
        shift
fi

MAX_HEAP_SIZE=8G
if [[ "$1" =~ .*MAX_HEAP_SIZE.* ]]; then
        arr=`echo "$1" | cut -d \= -f 2`
        echo "MAX_HEAP_SIZE is set to ${arr}"
        MAX_HEAP_SIZE=${arr}
        shift
fi

PROGRAM_ARGS=$*
if [ -z "$ENV" ]; then
    echo "Need to specify ENVIRONMENT"
    exit 1
fi
if [ -z "$LOG_FILE_NAME" ]; then
    echo "Need to specify LOG FILE NAME"
    exit 1
fi
if [ -z "$MAIN_CLASS" ]; then
    echo "Need to specify MAIN CLASS"
    exit 1
fi

DEPLOYMENT_HOME=/home/batchprod/dist/cur
JAVA_HOME=/sw/jdk1.7.0_02
SCRIPTS_HOME=$DEPLOYMENT_HOME/scripts
CONF_HOME=$DEPLOYMENT_HOME/config
DATE=`date +%y%m%d`
LOG_FILE=/var/log/crawler/${LOG_FILE_NAME}-${ENV}-${DATE}.log

if [ ! -f $LOG_FILE ]; then
	echo Creating log file $LOG_FILE
	touch $LOG_FILE
fi

typeset MISSING_FILE=false
typeset CLASSPATH_OUT_FILE=$SCRIPTS_HOME/shell/classpath.out
if [ ! -f $CLASSPATH_OUT_FILE ]; then
	echo $CLASSPATH_OUT_FILE does not exist
	MISSING_FILE=true
fi

typeset LOG4J_PROP_FILE=$CONF_HOME/log4j/log4j.xml
if [ ! -f $LOG4J_PROP_FILE ]; then
	echo $LOG4J_PROP_FILE does not exist
	MISSING_FILE=true
fi

if [ "$MISSING_FILE" == "true" ]; then
	exit 1
fi

echo CLASSPATH_OUT_FILE=$CLASSPATH_OUT_FILE
typeset CLASSPATH=$DEPLOYMENT_HOME/lib/${project.artifactId}-${project.version}.jar
typeset TMP_PATH=`cat $CLASSPATH_OUT_FILE |tr -s '[:]' '[ ]'`
for TMP_PATH_ELEM in $TMP_PATH
do
	CLASSPATH=$CLASSPATH:${DEPLOYMENT_HOME}/$TMP_PATH_ELEM
done
if [ -z "$CLASSPATH" ]; then
	echo $CLASSPATH could not be set. Check $SCRIPTS_HOME for a valid classpath.out file
	exit 2
fi
OPTIONS="-DENVIRONMENT=${ENV} -DCACHE_MODE=${CACHE_MODE}"
OPTIONS="$OPTIONS -Dlog4j.configuration=file:$LOG4J_PROP_FILE"
COMMAND="$JAVA_HOME/bin/java -verbosegc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:HeapDumpPath=/var/log/crawler -XX:ParallelGCThreads=2 -XX:-HeapDumpOnOutOfMemoryError -XX:MaxGCPauseMillis=500 -Xms$MIN_HEAP_SIZE -Xmx$MAX_HEAP_SIZE $OPTIONS -cp $CLASSPATH $MAIN_CLASS $PROGRAM_ARGS"
echo "[COMMAND]" >> $LOG_FILE
echo $COMMAND >> $LOG_FILE
if [ "$RETRY_WRAPPER_MODE" == "true" ]; then
        $COMMAND >> $LOG_FILE 2>&1
else
        $COMMAND >> $LOG_FILE 2>&1 &
fi
