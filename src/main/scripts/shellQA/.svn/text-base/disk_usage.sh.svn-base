#!/bin/bash

#admin email account
ADMIN="jayanta.hazra@gmail.com"
ADMIN2="jayanta.hazra@gmail.com"
if [ -n "$1" ];
then
        ADMIN=$1
fi

echo "Recipient email = $ADMIN"

# set usage alert threshold
THRESHOLD=15

if [ -n "$2" ];
then
        THRESHOLD=$2
fi
#hostname
HOSTNAME=$(hostname)

#mail client
MAIL=/usr/bin/msmtp

# store all disk info here
EMAIL=""

for line in $(df -hP | egrep '^/dev/' | awk '{ print $6 "_:_" $5 }')
do

        part=$(echo "$line" | awk -F"_:_" '{ print $1 }')
        part_usage=$(echo "$line" | awk -F"_:_" '{ print $2 }' | cut -d'%' -f1 )

        if [ $part_usage -ge $THRESHOLD -a -z "$EMAIL" ];
        then
                EMAIL="Disk space ALERT on $HOSTNAME\n"
                EMAIL="$EMAIL\n$part ($part_usage%) >= (Threshold = $THRESHOLD%)"

        elif [ $part_usage -ge $THRESHOLD ];
        then
                EMAIL="$EMAIL\n$part ($part_usage%) >= (Threshold = $THRESHOLD%)"
        fi
done

if [ -n "$EMAIL" ];
then
        echo -e "To:$ADMIN\nSubject: $EMAIL \n\nRun at $(date)" | msmtp --from=default -t $ADMIN2
        #echo -e "Subject: $EMAIL \n\nRun at $(date)" | msmtp --debug --from=default -t "$ADMIN"
        #$MAIL -s "Alert: Partition(s) almost out of diskspace on $HOSTNAME" "$ADMIN"
fi
