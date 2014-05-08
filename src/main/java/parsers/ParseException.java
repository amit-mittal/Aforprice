package parsers;

@SuppressWarnings("serial")
public class ParseException extends Exception {

	public ParseException(){		
	}

	public ParseException(String err) {
		super(err);
	}
}
