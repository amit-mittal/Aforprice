package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DbConnectionPool;
import db.dao.CategoryRecorder;
import db.dao.DAOException;

public class TempWalmartStaleCategoryChecker {
//	private String DATE = "2012-08-18";
//	Connection sqlConn;
//	CategoryRecorder categoryRecorder;
//	public TempWalmartStaleCategoryChecker(){
//		sqlConn = DbConnectionPool.get().getConnection().getConnection();
//		categoryRecorder = new CategoryRecorder();
//	}
//	public void confirmCategoriesAreStale(Map<Integer, Boolean> staleCategories){
//		//iterate over each category and find all its products
//		for(Integer staleCategory : staleCategories.keySet()){
//			boolean goodCategory = true;
//			List<String> productNames = getProducts(staleCategory);
//			//iterate over each product and find different category for same product
//			for(String product : productNames){
//				System.out.println("checking" + product);
//				//confirm any one other category is not part of stale category..product found under these categories also
//				List<Integer> otherCategories = getOtherCategories(product, staleCategory);
//				boolean good = false;
//				for(Integer otherCategory : otherCategories){
//					if(!staleCategories.containsKey(otherCategory)){
//						System.out.println("category is fine for this product");
//						good = true;
//						break;
//					}
//				}//for ends...
//				if(!good){
//					System.out.println("BAD: Category " + staleCategory + " - " + "Product '" + product + "' not found elsewhere");
//					goodCategory=false;
//					break;
//				}
//			}//for(String product : productNames) ends...
//			if(goodCategory){
//				System.out.println("GOOD: Category " + staleCategory);
//				try {
//					categoryRecorder.disableCategory(staleCategory);
//				} catch (DAOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}				
//			}
//		}
//		
//	}
//	
//	private List<String> getProducts(Integer category){
//		List<String> productNames = new ArrayList<String>(100);
//		try {
//			String productQuery = "select PRODUCT_NAME from PRODUCT_DOWNLOAD where " +
//					"DOWNLOAD_TIME>'"+DATE+"' and RETAILER_ID='walmart' and CATEGORY_ID = " + category; 
//			PreparedStatement getProduct = sqlConn.prepareStatement(productQuery);
//			System.out.println(productQuery);
//			ResultSet productRS = getProduct.executeQuery();
//			while(productRS.next()){
//				productNames.add(productRS.getString("PRODUCT_NAME"));
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return productNames;
//	}
//
//	private List<Integer> getOtherCategories(String product, Integer category){
//		List<Integer> otherCategories = new ArrayList<Integer>();
//		try {
//			
//			 
//			String categoryQuery = "select CATEGORY_ID from PRODUCT_DOWNLOAD where " +
//					"DOWNLOAD_TIME>'"+DATE+"'" +
//					"and PRODUCT_NAME = \"" + product + "\" and RETAILER_ID='walmart' and CATEGORY_ID !=" + category;
//			System.out.println(categoryQuery);
//			PreparedStatement getcategory = sqlConn.prepareStatement(categoryQuery);
//			ResultSet categoryRS = getcategory.executeQuery();
//			while(categoryRS.next()){
//				otherCategories.add(categoryRS.getInt("CATEGORY_ID"));
//			}
//			System.out.println(otherCategories);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return otherCategories;
//	}
///**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		if(args.length!=1){
//			System.out.println("usage: TempWalmartStaleCategoryChecker <filename>");
//			System.exit(1);
//		}
//			
//		String filename = args[0];
//		Map<Integer, Boolean> inputCategories = new HashMap<Integer, Boolean>(200);
//		try{
//			FileInputStream fstream = new FileInputStream(filename);
//			DataInputStream in = new DataInputStream(fstream);
//			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//			String line;
//			while((line = reader.readLine())!=null){
//				Integer categoryId = Integer.parseInt(line);
//				inputCategories.put(categoryId, true);
//			}
//			TempWalmartStaleCategoryChecker checker = new TempWalmartStaleCategoryChecker();
//			checker.confirmCategoriesAreStale(inputCategories);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//	}
//	
//
}
