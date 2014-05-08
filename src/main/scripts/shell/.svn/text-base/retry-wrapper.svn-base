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
        $EXIT_CODE = system( @Command ) >> 8;

        #if its a good exit then do not restart, otherwise restart
        if( $EXIT_CODE == 0 )
        {
                #exit( $EXIT_CODE );
        }

        print{ STDOUT } "Sleeping for 10 seconds\n";
        sleep( 10 );
}
