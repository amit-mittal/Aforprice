package adhoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import parsers.util.ProductUID;
import util.Constants;
import util.DateTimeUtils;
import util.MutableInt;
import util.build.ProductSummaryBuilder;
import db.DbConnection;
import db.Queries;
import db.dao.DataAccessObject;
import entities.ProductSummary;

public class TestProductUID extends DataAccessObject{
	
	private static final Logger logger = Logger.getLogger(TestProductUID.class);
	private static final String UPDATE = "UPDATE PRODUCT_SUMMARY SET UID=? WHERE PRODUCT_ID=?";
	
	public void getActiveProducts(boolean needAllValues) throws SQLException {
		logger.info("getActiveProducts(" + needAllValues + ")");
		DbConnection conn = null;
		Connection sqlConn = null;
		PreparedStatement prodStmt = null, prodCountStmt = null, updateStmt = null;
		ResultSet resultsRS = null, countRS = null;
		int count = 0;		
		try {
			conn = pool.getConnection();
			sqlConn = conn.getConnection();
			if(needAllValues)
				prodStmt = sqlConn.prepareStatement(Queries.GET_ALL_ACTIVE_PRODUCTS);
			else
				prodStmt = sqlConn.prepareStatement(Queries.GET_ALL_ACTIVE_PRODS_AVLBL_INFO);
			prodCountStmt = sqlConn.prepareStatement(Queries.GET_ALL_ACTIVE_PRODUCTS_COUNT);
			updateStmt = sqlConn.prepareStatement(UPDATE);
			countRS = prodCountStmt.executeQuery();
			if(countRS.next())
				count = countRS.getInt(1);
			int start = 0;
			int end = 0;
			HashMap<String, Set<String>> products = new HashMap<>(count);
			HashMap<String, MutableInt> productUnknowUID = new HashMap<>(count);
			int fetchedProds = 0;
			while(true){
				start = end;
				end = start + Constants.MAX_FETCH_SIZE_PROD_SUMMARY;
				if(fetchedProds >= count){
					end = Integer.MAX_VALUE;
				}
				prodStmt.setInt(1, start);
				prodStmt.setInt(2, end);
				resultsRS = prodStmt.executeQuery();
				while (resultsRS.next()) {
					fetchedProds++;
					Double reviewRating = resultsRS.getDouble(ProductSummary.Columns.REVIEW_RATING); 
					Integer numReviews = resultsRS.getInt(ProductSummary.Columns.NUM_REVIEWS);
					Integer salesRank = resultsRS.getInt(ProductSummary.Columns.BEST_SELLER_RANK);
					ProductSummaryBuilder b = new ProductSummaryBuilder();
					b.retailerId = resultsRS.getString(ProductSummary.Columns.RETAILER_ID);
					b.name = resultsRS.getString(ProductSummary.Columns.PRODUCT_NAME);
					b.price = resultsRS.getDouble(ProductSummary.Columns.PRICE);
					b.url = resultsRS.getString(ProductSummary.Columns.URL);
					b.imageUrl = resultsRS.getString(ProductSummary.Columns.IMAGE_URL);
					//b.desc = resultsRS.getString(ProductSummary.Columns.DESCRIPTION);
					b.model = resultsRS.getString(ProductSummary.Columns.MODEL_NO);
					b.prodId = resultsRS.getInt(ProductSummary.Columns.PRODUCT_ID);
					b.reviewRating = reviewRating == null?-1: reviewRating;
					b.numReviews = numReviews == null?-1: numReviews;
					b.salesRank = salesRank == null?-1: salesRank;
					b.downloadTime = resultsRS.getTimestamp(ProductSummary.Columns.LAST_DOWNLOAD_TIME);
					ProductSummary product = b.build();
					String key = product.getRetailerId();

//					if(key.equals("lowes") || key.equals("amazon") || key.equals("bestbuy"))
//						continue;
					if(!products.containsKey(key)){
						products.put(key, new HashSet<String>());
						productUnknowUID.put(key, new MutableInt(0));
					}
					String uid = ProductUID.get(product.getRetailerId(), product.getUrl());
					if(uid.equals(ProductUID.UNKNOWN)){
						productUnknowUID.get(key).add(1);
						System.out.println("Inlvaid-UID:" + product);
					}
					else
						products.get(key).add(uid);
					updateStmt.setString(1, uid);
					updateStmt.setInt(2, resultsRS.getInt("PRODUCT_ID"));
					updateStmt.addBatch();
					if(fetchedProds%100==0){
						int[] results = updateStmt.executeBatch();
						printUnprocessed(results);
					}
				}
				int[] results = updateStmt.executeBatch();
				printUnprocessed(results);
				logger.info("Fetched " + fetchedProds + "/" + count + " active products");
				closeRS(resultsRS);
				if(end == Integer.MAX_VALUE)
					break;
			}
			logger.info("Returning " + count + " number of active products");
			print(products, productUnknowUID);
		} finally {		
			closeRS(countRS);
			closeStmt(prodStmt);
			closeStmt(prodCountStmt);
			closeStmt(updateStmt);
			pool.releaseConnection(conn);
		}
	}

	private void testCostco(){
		ProductSummaryBuilder b = new ProductSummaryBuilder();
		b.downloadId = 1;
		b.retailerId = "costco";
		b.categoryId = 2;
		b.name = "abc";
		b.price = 1;
		b.url = "http://www.costco.com/Adrianne-Chestnut-Modular-Quad.product.100004985.html";
		b.imageUrl = "xyz";
		b.model = "ooh";
		b.reviewRating = -1;
		b.numReviews = -1;
		b.salesRank = -1;
		b.downloadTime = DateTimeUtils.getPrevNDaysMidNight(DateTimeUtils.getTodaysMidNight(), 20).getTime();
		ProductSummary prod = b.build();
		HashMap<ProductSummary, ProductSummary> products = new HashMap<ProductSummary, ProductSummary>();

		products.put(prod, prod);
		b.downloadTime = DateTimeUtils.getTodaysMidNight();
		ProductSummary prod2 = b.build();
		System.out.println(products.get(prod2));
	}
	private void printUnprocessed(int[] results){
		for(int res: results){
			if(res != 1)
				System.out.println("Update Failed with status " + res);
		}
	}
	private void print(HashMap<String, Set<String>> products, HashMap<String, MutableInt> productUnknown) {
		for(Map.Entry<String, Set<String>> entry: products.entrySet()){
			System.out.println(entry.getKey() + "\t\t" + entry.getValue().size());
			System.out.println(entry.getKey() + "-unknown\t\t" + productUnknown.get(entry.getKey()).getValue());
		}
	}
	
	public static void main(String[] args) throws SQLException{
		TestProductUID t = new TestProductUID();
		t.testCostco();
		//t.getActiveProducts(false);
	}

}
