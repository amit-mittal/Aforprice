package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Metrics {
	
	private static Map<String, Metric> metricsMap = new HashMap<>();
	/**
	 * @param name
	 * @return
	 */
	public static Metric getMetric(String name){
		if(metricsMap.containsKey(name)){
			return metricsMap.get(name);
		}
		metricsMap.put(name, new Metric(name, TimeUnit.MILLISECONDS));
		return metricsMap.get(name);
	}	
	
	public static String getProductDetailsParserMetricKey(String retailer){
		return "PDPMetric-" + retailer;
	}
	
	public static void publish(String subject, Set<String> keys){
		Emailer.getInstance().sendHTML(subject, toStringHTML(keys));
	}
	
	public static String toStringHTML(Set<String> keys){
		StringBuilder html = new StringBuilder();
		html.append("<html>").append("<body>");
		html.append("<table border=\"1\">");
		String[] headers = {"Name", "StartTime", "EndTime", "ProcessTime(s)", "Total", "SuccessCount", "Errors"};
		for(String header: headers){
			html.append("<th>").append(header).append("</th>");			
		}
		for(String key: keys){
			Metric m = metricsMap.get(key);
			if(m == null)
				continue;
			html.append(m.toStringHTML());
		}
		html.append("</table>");
		html.append("</body>").append("</html>");
		return html.toString();
	}
}