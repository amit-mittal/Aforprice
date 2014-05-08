package helper;

import global.exceptions.Bhagte2BandBajGaya;

public class Validate {
	public static void notNull(Object obj){
		if (obj == null)
			throw new Bhagte2BandBajGaya("Arg should not be null");
	}
	
	public static void notNull(Object obj, String msg){
		if (obj == null)
			throw new Bhagte2BandBajGaya(msg);
	}
}
