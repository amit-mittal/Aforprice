/**
 * 
 */
package products;

import java.sql.SQLException;
import java.util.Date;

import util.DateTimeUtils;
import util.Emailer;
import db.dao.ProductsCleanupDAO;

/**
 * @author Ashish
 *
 */
public class ProudctsCleanupServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProductsCleanupDAO cleanup = new ProductsCleanupDAO();
		try {
			int staleCount = cleanup.markStaleProductsInactive();
			int veryStaleCount = cleanup.moveVeryStaleProductsToStaleTables();
			generateSuccessAlert(staleCount, veryStaleCount);
		} catch (SQLException e) {
			e.printStackTrace();
			String error = "Failure in markStaleProductsInactive method";
			error += "Error: " + e.toString();
			generateAlert(error);
		}

	}
	
	/*
	 * send email alert with error
	 */
	private static void generateAlert(String error){
		java.util.Date runDate = new java.util.Date();
		String subject = DateTimeUtils.currentDateYYYYMMDD(runDate) + "- Product Cleanup Alert";
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		html.append(error);
		html.append("</body></html>");
		Emailer.getInstance().sendHTML(subject, html.toString());
	}
	/*
	 * send email alert with status
	 */
	private static void generateSuccessAlert(int staleCount, int veryStaleCount){
		java.util.Date runDate = new java.util.Date();
		String subject = DateTimeUtils.currentDateYYYYMMDD(runDate) + "- Product Cleanup Success ";
		StringBuilder html = new StringBuilder();
		html.append("<html><body>");
		html.append("Marked "+staleCount+"	products inactive	<br>");
		html.append("Archieved "+veryStaleCount+"	products	<br>");
		html.append("</body></html>");
		Emailer.getInstance().sendHTML(subject, html.toString());
	}
}
