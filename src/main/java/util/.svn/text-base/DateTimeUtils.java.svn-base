package util;

import global.errorhandling.ErrorCodes;
import global.exceptions.Bhagte2BandBajGaya;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {
	
	public enum TIME_OF_DAY{
		DAY, NIGHT; 
	}
	private static Map<String, SimpleDateFormat> dbDateFormats = new HashMap<String, SimpleDateFormat>();
	//todo: wrap this in thread local 
	private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat yyyymmddHHMMSSsss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	static{
		//MYSQL YYYY-MM-DD
		dbDateFormats.put(Constants.MYSQL, new SimpleDateFormat("yyyy-MM-dd"));
	}
	public static String getDateInDbFormat(String db){
		SimpleDateFormat format = dbDateFormats.get(db);
		if(format == null)
			throw new Bhagte2BandBajGaya(ErrorCodes.INVALID_INPUT);
		return format.format(new Date());
	}
	
	public static String currentDateYYYYMMDD(){
		return YYYYMMDD.format(new Date());
	}
	
	public static String currentDateYYYYMMDD(Date date){
		return YYYYMMDD.format(date);
	}
	
	public static Date dateFromyyyyMMdd(String yyyyMMdd){
		try {
			return YYYYMMDD.parse(yyyyMMdd);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new Bhagte2BandBajGaya(e);
		}
	}
	
	public static Timestamp getSQLTS(Date date){
		return new Timestamp(date.getTime());
	}
	
	public static Calendar getMidNight(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar midNight = Calendar.getInstance();
		
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);		
		midNight.set(year, month, dayOfMonth, 0, 0, 0);
		return midNight;
	}
	
	public static Calendar getNextMidNight(Date date){
		Calendar midNight = getMidNight(date);
		Calendar nextMidNight = Calendar.getInstance();		
		nextMidNight.setTimeInMillis(midNight.getTimeInMillis() + TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS));
		return nextMidNight;
	}
	
	public static Calendar getPrevMidNight(Date date){
		Calendar midNight = getMidNight(date);
		Calendar prevMidNight = Calendar.getInstance();
		prevMidNight.setTimeInMillis(midNight.getTimeInMillis() - TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS));
		return prevMidNight;
	}

	public static Calendar getPrevWeekMidNight(Date date){
		Calendar midNight = getMidNight(date);
		Calendar prevWeekMidNight = Calendar.getInstance();
		prevWeekMidNight.setTimeInMillis(midNight.getTimeInMillis() - TimeUnit.MILLISECONDS.convert(24*7, TimeUnit.HOURS));
		return prevWeekMidNight;
	}
	
	public static Calendar getPrevNDaysMidNight(Date date, int n){
		Calendar midNight = getMidNight(date);
		Calendar prevWeekMidNight = Calendar.getInstance();
		prevWeekMidNight.setTimeInMillis(midNight.getTimeInMillis() - TimeUnit.MILLISECONDS.convert(24*n, TimeUnit.HOURS));
		return prevWeekMidNight;
	}

	
	public static String getTime(long time){
		Date d = new Date(time);		
		return d.toString();
	}
	
	public static Date getTime(String timeStr){
		try {
			return yyyyMMddHHmmss.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getTimeYYYYMMDDHHMMSS(Date dateTime){
		return yyyyMMddHHmmss.format(dateTime);
	}
	
	public static Date dateFromyyyymmddHHMMSSsss(String timeStr){
		try {
			return yyyymmddHHMMSSsss.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static String convertToStringyyyymmddHHMMSSsss(Timestamp time){
		return yyyymmddHHMMSSsss.format(time);
	}
	
	public static String convertToStringyyyymmddHHMMSSsss(Date date){
		return yyyymmddHHMMSSsss.format(date);
	}
	
	public static Date add(Date date, long amount, TimeUnit unit){
		return add(date, amount, unit, 0);
	}
	public static Date add(Date date, long amount, TimeUnit unit, int epsilonMs){
		long time = date.getTime();
		long result = time + TimeUnit.MILLISECONDS.convert(amount, unit) - epsilonMs;
		return new Date(result);
	}
	
	public static long diff(Date date1, Date date2, TimeUnit unit){
		long diff = date1.getTime() - date2.getTime();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	public static TIME_OF_DAY getTimeOfDay(){
		Calendar midNight = getMidNight(new Date());
		long midNightTime = midNight.getTimeInMillis();
		long sevenAmTime = midNightTime + 60*60*1000*7;
		long currentTime = System.currentTimeMillis();
		if(currentTime >= midNightTime && currentTime <= sevenAmTime){
			return TIME_OF_DAY.NIGHT;
		}
		return TIME_OF_DAY.DAY;
	}
	
	public static Date getReconAlertTime(){
		return getReconAlertTime(new Date());
	}
	
	public static Date getReconAlertTime(Date date){
		Calendar midNight = getMidNight(date);
		long eightPM = midNight.getTimeInMillis() + TimeUnit.MILLISECONDS.convert(20, TimeUnit.HOURS);
		Calendar eightPMCal = Calendar.getInstance();
		eightPMCal.setTimeInMillis(eightPM);
		return eightPMCal.getTime();
	}
	
	public static Date getTodaysMidNight(){
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
}