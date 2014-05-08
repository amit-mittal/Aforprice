package db.types;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLString extends Qualitative {

	private static final HashMap<String, String> sqlTokens;
	private static Pattern sqlTokenPattern;
	static {
		// MySQL escape sequences:
		// http://dev.mysql.com/doc/refman/5.1/en/string-literals.html#character-escape-sequences
		String[][] search_regex_replacement = new String[][] {
			 // {search string, search regex, sql replacement regex}
				{ "'"         ,"'"          , "\\\\'" }, 
				{ "%"         , "%"         , "\\\\%" },
				{ "\""        , "\""        , "\\\\\"" }, 
				{ "\\"        , "\\\\"      , "\\\\\\\\" } };
		sqlTokens = new HashMap<String, String>();
		String patternStr = "";
		for (String[] srr : search_regex_replacement) {
			sqlTokens.put(srr[0], srr[2]);
			patternStr += (patternStr.isEmpty() ? "" : "|") + srr[1];
		}
		sqlTokenPattern = Pattern.compile('(' + patternStr + ')');
	}

	private static String escape(String s) {
		Matcher matcher = sqlTokenPattern.matcher(s);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, sqlTokens.get(matcher.group(1)));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private final String value;
	public SQLString(String value) {
		this.value = value;
	}
	
	public String toString(){
		return escape(value);
	}
}