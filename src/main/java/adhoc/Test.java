package adhoc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import db.dao.ProductCategoryDAO;
import util.Constants;
import util.MyMemoryUsage;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProductCategoryDAO dao = new ProductCategoryDAO();
		try {
			System.out.println(Double.NaN>Double.NaN);
//			Map<Integer, List<Integer>> a = dao.getProductCategoriesMap();
//			System.gc();
//			System.out.println("sleep");
//			Thread.sleep(60*1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
