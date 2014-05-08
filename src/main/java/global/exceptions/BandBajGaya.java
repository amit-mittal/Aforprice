package global.exceptions;

@SuppressWarnings("serial")
public class BandBajGaya extends Exception {

	public BandBajGaya() {
	}

	public BandBajGaya(String message) {
		super(message);
	}

	public BandBajGaya(Throwable cause) {
		super(cause);
	}

	public BandBajGaya(String message, Throwable cause) {
		super(message, cause);
	}

	public BandBajGaya(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
