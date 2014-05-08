package uploader.util;

public enum CategoryType {
	PARENT,
	TERMINAL,
	UNKNOWN;

	public static final CategoryType valueOfDbString(String value){
		if (value.equals("Y"))
			return PARENT;
		else if (value.equals("N"))
			return TERMINAL;
		else
			return UNKNOWN;
	}

	public String toDbString(){
		if(this == CategoryType.TERMINAL)
			return "N";
		else if(this == CategoryType.PARENT)
			return "Y";
		else //CategoryType.UNKNOWN
			return "X";
	}

}

