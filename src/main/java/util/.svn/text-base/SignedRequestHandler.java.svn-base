package util;
/**
 * Amazon advertising API signed request handler
 * @author Chirag Maheshwari
 * 
 * This class contains all the logic for signing requests 
 * to the Amazon Product Advertising API
 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;

import org.apache.commons.codec.binary.Base64;

public class SignedRequestHandler {
	
	// All String should be handled as UTF-8 charset
	private static final String CHARSET = "UTF-8";
	
	//The HMAC256 algorithm needed by the amazon API
	private static final String SHA_ALGORITHM = "HmacSHA256";
	
	/*
	 * Request_URI - URI for the service (fixed by amazon)
	 * The request as a GET one, can be changed to POT if required.
	 * */
	private static final String REQUEST_URI = "/onca/xml";
	private static final String REQUEST_METHOD = "GET";
	
	/*
	 * Parameters used for authentication and location identification
	 * by AMAZON.
	 */
	private String endpoint = null;
	private String awsAccessKeyID = null;
	private String awsSecretKey = null;
	
	/*
	 * Java objects used to sign the request+secret_key and attach a
	 * message authentication code to it with the specified algorithm.
	 * */
	private SecretKeySpec secretKeySpec = null;
	private Mac mac = null;
	
	/**
     * You must provide the three values below to initialize the handler.
     *  
     * @param endpoint          Destination for the requests.
     * @param awsAccessKeyId    Your AWS Access Key ID
     * @param awsSecretKey      Your AWS Secret Key
     */
	public static SignedRequestHandler getInstance(
			String endpoint,
			String awsAccessKeyID,
			String awsSecretKey
			) throws IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException 
	{
		/*
		 * checks for invalid/empty parameters and accordingly throw
		 * exception.
		 */
		if(endpoint == null || endpoint.length() == 0)
			throw new IllegalArgumentException("AWS endpoint is empty.");
		if(awsAccessKeyID == null || awsAccessKeyID.length() == 0)
			throw new IllegalArgumentException("AWS acess key ID is empty.");
		if(awsSecretKey == null || awsSecretKey.length() == 0)
			throw new IllegalArgumentException("AWS secret key is empty.");
		/*
		 * Initialize a instance of the type SignedRequestHandler
		 * and set object with the given parameters 
		 */
		SignedRequestHandler instance = new SignedRequestHandler();
		instance.endpoint = endpoint.toLowerCase();
		instance.awsAccessKeyID = awsAccessKeyID;
		instance.awsSecretKey = awsSecretKey;
		
		/*
		 * initialize the mac object with the 
		 * authentication code(AWS secret key).
		 */
		byte[] secretKeyBytes = instance.awsSecretKey.getBytes(CHARSET);
		instance.secretKeySpec = new SecretKeySpec(secretKeyBytes, SHA_ALGORITHM);
		instance.mac = Mac.getInstance(SHA_ALGORITHM);
		instance.mac.init(instance.secretKeySpec);
		
		return instance;
	}
	
	/**
	 * constructor is private as we would use getInstance instead.
	 */
	private SignedRequestHandler() {}
	
	/**
     * This method signs requests in query-string form. It returns a URL that
     * should be used to fetch the response. The URL returned should not be
     * modified in any way, doing so will invalidate the signature and Amazon
     * will reject the request.
     */
	public String sign(String query) {
		Map<String, String> params = this.createParameterMap(query);
		
		return this.sign(params);
	}
	
	/**
     * This method signs requests in hashmap form. It returns a URL that should
     * be used to fetch the response. The URL returned should not be modified in
     * any way, doing so will invalidate the signature and Amazon will reject
     * the request.
     */
	public String sign(Map<String, String> params) {
		params.put("AWSAccessKeyId", this.awsAccessKeyID);
		params.put("Timestamp", this.timestamp());
		
		SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
		
		String canonicalQuery = this.canonicalize(sortedParamMap);
		
		String stringToSign = REQUEST_METHOD + "\n" 
				+ this.endpoint + "\n" 
				+ REQUEST_URI + "\n" 
				+ canonicalQuery;
		
		String hmac = this.hmac(stringToSign);
		String sig = this.percentEncodeRFC3986(hmac);
		
		String url = "http://" + this.endpoint + REQUEST_URI + "?" + canonicalQuery + "&Signature=" + sig;
		return url;
	}
	
	/**
	 * If the query is given in the form a String,
	 * then separate the parameters and put them in 
	 * a parameter Map.
	 */
	private Map<String, String> createParameterMap(String query) {
		Map<String, String> map = new HashMap<String, String>();
		String[] pairs = query.split("&");
		
		for(String pair: pairs) {
			if(pair.length() < 1)
				continue;
			
			String[] tokens = pair.split("=", 2);
			for(int i=0; i < tokens.length; i++) {
				try {
					tokens[i] = URLDecoder.decode(tokens[i], CHARSET);
				} catch(UnsupportedEncodingException e) {
					e.getMessage();
					e.getStackTrace();
				}
			}
			
			switch (tokens.length) {
			case 1:
				if(pair.charAt(0) == '=')
					map.put("", tokens[0]);
				else
					map.put(tokens[0], "");
				break;
				
			case 2:
				map.put(tokens[0], tokens[1]);
				break;
				
			default:
				break;
			}
		}
		return map;
	}
	
	/**
     * Generate a ISO-8601 format timestamp as required by Amazon.
     *  
     * @return  ISO-8601 format timestamp.
     */
	private String timestamp() {
		String timestamp = null;
		Calendar cal = Calendar.getInstance();
		DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		date.setTimeZone(TimeZone.getTimeZone("GMT"));
		timestamp = date.format(cal.getTime());
		return timestamp;
	}
	
	/**
     * Canonicalize the query string as required by Amazon.
     * 
     * @param sortedParamMap    Parameter name-value pairs in lexicographical order.
     * @return                  Canonical form of query string.
     */
	private String canonicalize(SortedMap<String, String> sortedParamMap) {
		if(sortedParamMap.isEmpty())
			return "";
		
		StringBuffer buffer = new StringBuffer();
		Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();
		
		while(iter.hasNext()) {
			Map.Entry<String, String> kvpair = iter.next();
			buffer.append(percentEncodeRFC3986(kvpair.getKey()));
			buffer.append('=');
			buffer.append(percentEncodeRFC3986(kvpair.getValue()));
			if(iter.hasNext())
				buffer.append('&');
		}
		String canonical = buffer.toString();
		return canonical;
	}
	
	/**
     * Compute the HMAC.
     *  
     * @param stringToSign  String to compute the HMAC over.
     * @return              base64-encoded hmac value.
     */
	private String hmac(String stringToSign) {
		String signature = null;
		byte[] data;
		byte[] rawHMAC;
		try {
			data = stringToSign.getBytes(CHARSET);
			rawHMAC = mac.doFinal(data);
			Base64 encoder = new Base64();
			signature = new String(encoder.encode(rawHMAC));
		} catch(UnsupportedEncodingException e) {
			 throw new RuntimeException(CHARSET + " is unsupported!", e);
		}
		return signature;
	}
	
	/**
     * Percent-encode values according the RFC 3986. The built-in Java
     * URLEncoder does not encode according to the RFC, so we make the
     * extra replacements.
     * 
     * @param s decoded string
     * @return  encoded string per RFC 3986
     */
	private String percentEncodeRFC3986(String s) {
		String out;
		try {
			out = URLEncoder.encode(s, CHARSET)
					.replace("+", "%20")
					.replace("*", "%2A")
					.replace("%7E", "~");
		} catch(UnsupportedEncodingException e) {
			out = s;
		}
		return out;
	}
}