#!/usr/bin/perl -w

if( !scalar( @ARGV ) )
{
        warn("\n");
        warn("Usage: $0 [ ] Command [ Args ]\n");
        warn("\n");
        exit( 1 );
}
@Command = @ARGV;
while( 1 )
{
		print{ STDOUT } join(" ", "Command=", @Command, "\n");
        $EXIT_CODE = ( system( @Command ) >> 8 );
        printf "EXIT_CODE=%d\n", $EXIT_CODE;
        #restart for restartable code otherwise go down
        if( $EXIT_CODE != 95 )
        {
                print( "non-restartable exit code \n" );
                exit( $EXIT_CODE );
        }
        print( "Restartable exit code, sleep for 30 seconds and restart\n" );
        sleep( 30 );
}
