package util;

import static util.Constants.RECON.cellColor;
import static util.Constants.RECON.statSummary;
import static util.Constants.RECON.statsFrom;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import db.dao.DAOException;
import db.dao.ReconilationDAO;
import entities.Retailer;


public class EmailAlerts {
	
	/**
	 * @param result Map of retailer and recon results.
	 */
	public static void productsReconAlert(Date from, Date to, Map<String, TreeMap<RECON_FIELDS, MutableInt>> result){
		String header = "Summary of products reconciliation for each retailer";
		String html = reconHtml(header, result, RECON_FIELDS.orderedProductReconFields(), ReconilationDAO.NAME.PRODUCT, to);
		Emailer.getInstance().sendHTML("Products Reconciliation " + (from == null?"unknown": from) + " to " + (to == null?"unknown": to), html);
	}
	
	public static void categoryReconAlert(Map<String, TreeMap<RECON_FIELDS, MutableInt>> result){
		String header = "Category Reconciliation - " + DateTimeUtils.currentDateYYYYMMDD();
		String html = reconHtml(header, result, RECON_FIELDS.orderedCategoryReconFields(), ReconilationDAO.NAME.CATEGORY, new Date());
		Emailer.getInstance().sendHTML(header, html);
	}
	
	private static String reconHtml(String header, Map<String, TreeMap<RECON_FIELDS, MutableInt>> result, TreeSet<RECON_FIELDS> headerFields, ReconilationDAO.NAME name, Date to){
		StringBuilder html = new StringBuilder();
		html.append("<html>").append("<body>");		
		html.append("<h2>").append(header).append("</h2>");
		html.append("<table border=\"1\">");		
		html.append("<tr>");
		html.append("<th>").append("Retailer").append("</th>");
		html.append("<th>").append("Owner").append("</th>");
		
		for(RECON_FIELDS header1: headerFields){			
			html.append("<th>").append(header1.getDisplayName()).append("</th>");			
		}		
		html.append("</tr>");
		for(Map.Entry<String, TreeMap<RECON_FIELDS, MutableInt>> entry: result.entrySet()){
			html.append("<tr>");
			String retailer = entry.getKey();
			html.append("<td>").append(retailer).append("</td>");
			String owner = Retailer.retailerToOwnerMap.get(retailer);
			html.append("<td>").append( owner == null ? "UNKNOWN" : owner).append("</td>");

			for(Map.Entry<RECON_FIELDS, MutableInt> subEntry: entry.getValue().entrySet()){
				int value = subEntry.getValue().getValue();
				if(value == -1){
					//we should not display anything
					html.append("<td></td>");
					continue;
				}
				RECON_FIELDS field = subEntry.getKey();
				if(subEntry.getKey().showStats()){					
					try {
						ReconStat stat = ReconilationDAO.getInstance().getReconStats(retailer, name, field, statsFrom(to, name), to );
						html.append("<td ").append("bgcolor=" + cellColor(field, value, stat, field.isErrField())).append(">").append(subEntry.getValue().getValue()).append("<br>").append(statSummary(stat)).append("</td>");
					} catch (DAOException e) {
					}
				}
				else
					html.append("<td>").append(subEntry.getValue().getValue()).append("</td>");
			}
			html.append("</tr>");
		}
		html.append("</table>").append("</body>").append("</html>");
		return html.toString();
	}
}