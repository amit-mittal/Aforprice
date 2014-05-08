package util;

import java.util.Date;

//ToDo: redirect this to log4j or replace it as it makes sense

public class UtilityLogger {
	//todo: change this to enum with member variable of int loglevel
	public static int LOG_LEVEL_ERROR=0;
	public static int LOG_LEVEL_WARNING=1;
	public static int LOG_LEVEL_INFO=5;
	public static int LOG_LEVEL_DEBUG=6;
	private static int LOG_LEVEL=LOG_LEVEL_INFO;
	
	public static void  setLogLevel(int logLevel){
		LOG_LEVEL = logLevel;
	}

	public static void logInfo(String log){
		if(LOG_LEVEL>=LOG_LEVEL_INFO)
			System.out.println(new Date() + " INFO:" + log);
	}

	public static void logDebug(String log){
		if(LOG_LEVEL>=LOG_LEVEL_DEBUG)
			System.out.println(new Date() + " DEBUG:" + log);
	}

	public static void logError(String log){
		if(LOG_LEVEL>=LOG_LEVEL_ERROR)
			System.out.println(new Date() + " ERROR:" + log);
	}

	public static void logError(Throwable e){
		//if you change below then test in eclipse to make sure clicking on the stacktrace takes you to correct class/line 
		String str=" " + e.toString() + "\n";
		for(StackTraceElement s : e.getStackTrace())
			str += s.toString() + "\n";
		logError(str);
	}

	public static void logWarning(String log){
		if(LOG_LEVEL>=LOG_LEVEL_WARNING)		
			System.out.println(new Date() + " WARN:" + log);
	}

}
