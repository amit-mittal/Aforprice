package db.types;

import java.util.Date;

import util.DateTimeUtils;

public class SQLTimestamp extends Qualitative {

	private final Date value;
	public SQLTimestamp(Date value) {
		this.value = value;
	}
	
	public String toString(){
		return DateTimeUtils.getTimeYYYYMMDDHHMMSS(value);
	}
}
