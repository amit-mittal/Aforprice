package util;

import org.apache.log4j.Logger;

/**
 * Created by Anurag on 2/27/14.
 */
public class FastLogger {
    private enum LOGLEVEL{
        INFO, WARN, ERROR
    }
    private final Logger LOGGER;
    private FastLogger(Class clazz){
        LOGGER = Logger.getLogger(clazz);
    }

    public static FastLogger create(Class clazz){
        return new FastLogger(clazz);
    }

    public void info(String format, Object... objects){
    }

    public void warn(String format, Object... objects){
    }

    public void error(String format, Throwable t, Object... objects){
    }
}
