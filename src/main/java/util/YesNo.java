package util;

public enum YesNo {
	Y("Y"), N("N");
	
	private String value;
	private YesNo(String v){
		this.value = v;
	}
	public String getDbValue(){
		return value;
	}
}
