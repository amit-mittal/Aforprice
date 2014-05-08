#!/usr/bin/perl -w

#keep this in sync with svn copy at src/main/scripts/dbbackup.pl
use strict;
use Getopt::Long;

if( !scalar( @ARGV ) )
{
        warn("\n");
        warn("Usage: $0 [ ] [-S <socket file>] -u <user> -p <password> DBNAME path_to_destination\n");
        warn("\n");
        exit( 1 );
}

my %opt = (
    noindices   => 0,
    allowold    => 0,   # for safety
    keepold     => 0,
    method      => "cp",
    flushlog    => 0,
);

GetOptions( \%opt,
    "host|h=s",
    "database|db=s",
    "destination|dir=s",
    "user|u=s",
    "password|p=s",
    "socket|S=s",
    "source|src=s",
) or usage("Invalid option");

print "$opt{user} \n";

sub mylog{
        print scalar(localtime(time))." ";
        print @_;
        print "\n";
};

sub logErrorAndAlert{
        warn "Executing command $_[0] failed ($_[1]).\n";
        #send mail
        exit( 1 );
};

sub backup_database {
        mylog("starting database backup");
        #### mysqlhotcopy -S /var/run/mysqld/mysqldqa.sock -u root -p iluviya PRODUCTS /home/batchprod/teradrive/Backup/DB/mysqlqa/20130202
        # mysqlhotcopy -S /var/run/mysqld/mysqldqa.sock -u root -p iluviya PRODUCTS /home/batchprod/dbbackuparea/mysqlqa/20130202
        my $backup_command;
        $backup_command = "mysqlhotcopy ";
        if($opt{socket})
        {
         $backup_command .= "-S $opt{socket} ";
        }
        my($day, $mon, $year) = (localtime)[3,4,5];
        my $yyyymmdd = sprintf "%.4d%.2d%.2d", $year+1900, $mon+1, $day;
        my $workarea = sprintf "/home/batchprod/dbbackuparea/%s/%s", $opt{user}, $yyyymmdd;
        #mkdir /home/batchprod/dbbackuparea/mysqlqa/20130202
        if (-d $workarea) {
        	mylog("Directory $workarea exists, no need to create");
        }
        else{
        	my $create_dir = "mkdir $workarea";
       	 	mylog("Executing command $create_dir");
        	my $cp_status = system "$create_dir";
        	if ($cp_status != 0 ){
				logErrorAndAlert($create_dir, $cp_status);
       		}
       	}	

        $backup_command .= "-u $opt{user} -p $opt{password} $opt{database} $workarea";
        mylog("Executing command $backup_command");
        my $cp_status = system "$backup_command";
        if ($cp_status != 0) {
            logErrorAndAlert($backup_command, $cp_status);
        }
        # tar zcf /home/batchprod/teradrive/Backup/DB/mysqlqa/PRODUCTS_20130202.tarz /home/batchprod/dbbackuparea/mysqlqa/20130202/PRODUCTS
        my $zip_command = "tar zcf $opt{destination}/$opt{database}_$yyyymmdd.tarz $workarea/$opt{database}";
        mylog("Zipping backup by running command $zip_command");
        $cp_status = system "$zip_command";
        if ($cp_status != 0){
			logErrorAndAlert($zip_command, $cp_status);
        }
        #remove unzipped directory
        my $rm_command = "rm -r  $workarea";
        mylog("Removing unzipped folder by running command $rm_command");
        $cp_status = system "$rm_command";
        if ($cp_status != 0) {
                logErrorAndAlert($rm_command, $cp_status);
        }
        mylog("Database backup is complete");
}

backup_database;
        