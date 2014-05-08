package util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class URL {

	private final String url;
	private final java.net.URL urlObj;	
	private final HashSet<String> parmsToIgnore;
	private Map<String, HashSet<String>> queryParms = null;

	public URL(String url, HashSet<String> parmsToIgnore) throws MalformedURLException {
		this.url = url;
		this.urlObj = new java.net.URL(url);
		this.parmsToIgnore = parmsToIgnore;
	}

	public Map<String, HashSet<String>> getQueryParms()
			throws UnsupportedEncodingException {
		if(queryParms != null)
			return queryParms;
		queryParms = new HashMap<String, HashSet<String>>();
		String query = getQuery();

		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			String key = URLDecoder.decode(pair[0], "UTF-8").trim()
					.toLowerCase();
			if(parmsToIgnore != null && parmsToIgnore.contains(key))
				continue;
			String value = "";
			if (pair.length > 1) {
				value = URLDecoder.decode(pair[1], "UTF-8").trim()
						.toLowerCase();
			}
			HashSet<String> values = queryParms.get(key);
			if (values == null) {
				values = new HashSet<String>();
				queryParms.put(key, values);
			}
			values.add(value);
		}
		return queryParms;
	}

	public String getUrl() {
		return url;
	}

	public String getHost() {
		return urlObj.getHost();
	}

	public String getPath() {
		return urlObj.getPath();
	}

	public String getQuery() {
		return urlObj.getQuery();
	}
	
	@Override
	public boolean equals(Object that){
		try{
			URL thatURL = (URL)that;
			if(thatURL.getUrl().equalsIgnoreCase(getUrl()))
				return true;
			if(!getHost().equalsIgnoreCase(thatURL.getHost()))
				return false;
			if(!getPath().equalsIgnoreCase(thatURL.getPath()))
				return false;
			Map<String, HashSet<String>> thisParms = getQueryParms();
			Map<String, HashSet<String>> thatParms = thatURL.getQueryParms();
			if(thisParms.size() != thatParms.size())
				return false;
			for(Map.Entry<String, HashSet<String>> entry: thisParms.entrySet()){
				if(!thatParms.containsKey(entry.getKey()))
					return false;
				if(!thatParms.get(entry.getKey()).equals(entry.getValue()))
					return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public String toString(){
		return url;
	}

	public static boolean areEqual(String thisUrl, String thatUrl) {
		try {
			if (thatUrl.equalsIgnoreCase(thisUrl))
				return true;
			URL thatURL = new URL(thatUrl, null);
			URL thisURL = new URL(thisUrl, null);
			return thatURL.equals(thisURL);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean areEqual(URL thisURL, String thatUrl) {
		try {
			if (thisURL.getUrl().equalsIgnoreCase(thatUrl))
				return true;
			URL thatURL = new URL(thatUrl, null);			
			return thatURL.equals(thisURL);
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean areEqual(URL thisURL, URL thatURL){
		try{
			return thisURL.equals(thatURL);
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Gets the value corresponding to the query parameter param. 
	 * For example for the url, www.example.com?abc=123&xyz=345, if param is specified abc, it returns 123.
	 * @param url 
	 * @param param
	 * @return
	 */
	public static String getParam(String url, String param){
		try{
			java.net.URL u = new java.net.URL(url);
			String query = u.getQuery();
			String key = param + "=";
			int idx = query.indexOf(key);
			if(idx == -1)
				return null;
			int startIdx = idx + key.length();
			StringBuilder valueBldr = new StringBuilder();
			for(int i = startIdx; i < query.length(); i++){
				if(query.charAt(i) == '&'){
					break;
				}
				valueBldr.append(query.charAt(i));
			}
			String val = valueBldr.toString();
			return val.length() == 0?null: URLDecoder.decode(val, "UTF-8");
		}catch(Exception e){
			return null;
		}
	}
		
	/**
	 * Finds the sub path element beginning with <tt>precedes
	 * 1. www.example.com/path1/path2 will return path2 if precedes = 1/
	 * 2. www.example.com/path1/path2/ will return path2 if precedes = 1/
	 * 3. www.example.com/path1/path2?abc=123 will return path2 if precedes = 1/
	 * 4. www.example.com/path1/path1/path2#123 will return path2 if precedes = 1/ and pos=2
	 * @param url 
	 * @param precedes the string preceding the sub path element  
	 * @param pos look for posth occurence of precedes string.
	 * @return
	 */
	public static String getSubPathElement(String url, String precedes, int pos){
		int idx = url.indexOf(precedes);
		if(idx == -1)
			return null;
		if(pos == -1)
			pos = Integer.MAX_VALUE;
		int myPos = 1;
		int nextIdx = idx;
		while(nextIdx != -1 && pos > myPos){
			nextIdx = url.indexOf(precedes, nextIdx+1);
			if(nextIdx != -1)
				idx = nextIdx;
			myPos++;
		}
		int startIdx = idx + precedes.length();
		StringBuilder subpath = new StringBuilder();
		for(int i = startIdx; i < url.length(); i++){
			if(url.charAt(i) == '/'  || url.charAt(i) == '?' || url.charAt(i) == '#'){
				break;
			}
			subpath.append(url.charAt(i));
		}
		return subpath.toString();
	}
	
	public static String getSubPathElement(String url, String precedes){
		return getSubPathElement(url, precedes, 1);
	}
	
	public static boolean isValidUrl(String url){
		try {
			new java.net.URL(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void main(String[] args){
		System.out.println(getSubPathElement("www.example.com/path1/path1/path2", "/pat", -1));
		System.out.println(getSubPathElement("www.example.com/path1/path1/path2#123", "1/", 1));
	}
}