package receipts.ocr.abby;

/**
 * Authentication settings for Cloud OCR SDK client.
 * 
 * Please provide your application id and password here.
 */
public class ClientSettings {
	// Name of application you created
	public static final String APPLICATION_ID = "afoprice-receiptsscanner";
	// Password should be sent to your e-mail after application was created
	public static final String PASSWORD = "4SRbcH3FVW33CG6sDwdvrfR6";
	
	public static void setupProxy()
	{
		// Uncomment this if you are behind a proxy
		/*
		String host = "";
		String port = "";
		String user = "";
		String password = "";
		System.getProperties().put("http.proxyHost", host );
		System.getProperties().put("https.proxyHost", host );
		System.getProperties().put("http.proxyPort", port);
		System.getProperties().put("https.proxyPort", port);
		System.getProperties().put("http.proxyUser", user);
		System.getProperties().put("https.proxyUser", user);
		System.getProperties().put("http.proxyPassword", password);
		System.getProperties().put("https.proxyPassword", password);
		*/
	}
}
