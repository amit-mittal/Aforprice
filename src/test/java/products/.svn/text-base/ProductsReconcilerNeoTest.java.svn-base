package products;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import parsers.util.PriceTypes;
import util.RECON_FIELDS;
import util.MutableInt;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import db.dao.ProductsDAO;
import entities.ProductSummary;
import entities.Retailer;

public class ProductsReconcilerNeoTest {
	
	private ProductsReconcilerNeo recon = new ProductsReconcilerNeo();
	
	private final static String TEST_RETAILER = Retailer.WALMART.getId(); 
	@Before
	public void setUp(){
		System.setProperty("ENVIRONMENT", "QA");
	}

	@Test
	public void testMerge() {
		try {			
			Method method = ProductsReconcilerNeo.class.getDeclaredMethod("merge", new Class[]{Map.class, Map.class});
			method.setAccessible(true);
			Map<String, TreeMap<RECON_FIELDS, MutableInt>> result1 = new HashMap<String, TreeMap<RECON_FIELDS,MutableInt>>();
			Map<String, TreeMap<RECON_FIELDS, MutableInt>> result2 = new HashMap<String, TreeMap<RECON_FIELDS,MutableInt>>();
			result1.put("retailer1", new TreeMap<RECON_FIELDS, MutableInt>());
			result1.get("retailer1").put(RECON_FIELDS.FAILED, new MutableInt(1));
			result1.get("retailer1").put(RECON_FIELDS.NEWCOUNT, new MutableInt(2));
			result1.put("retailer2", new TreeMap<RECON_FIELDS, MutableInt>());
			result1.get("retailer2").put(RECON_FIELDS.NOIMGURLNEW, new MutableInt(3));
			result1.get("retailer2").put(RECON_FIELDS.NOIMGURL, new MutableInt(4));

			result2.put("retailer1", new TreeMap<RECON_FIELDS, MutableInt>());
			result2.get("retailer1").put(RECON_FIELDS.FAILED, new MutableInt(1));
			result2.get("retailer1").put(RECON_FIELDS.NOIMGURLNEW, new MutableInt(2));
			result2.put("retailer3", new TreeMap<RECON_FIELDS, MutableInt>());
			result2.get("retailer3").put(RECON_FIELDS.NOIMGURLNEW, new MutableInt(3));
			result2.get("retailer3").put(RECON_FIELDS.NOIMGURL, new MutableInt(4));
			
			method.invoke(recon, result1, result2);
			assertTrue(result1.get("retailer1").get(RECON_FIELDS.FAILED).getValue() == 2);
			assertTrue(result1.get("retailer1").get(RECON_FIELDS.FAILED).getValue() == 2);
			assertTrue(result1.get("retailer1").get(RECON_FIELDS.NOIMGURLNEW).getValue() == 2);
			assertTrue(result1.get("retailer2").get(RECON_FIELDS.NOIMGURLNEW).getValue() == 3);
			assertTrue(result1.get("retailer2").get(RECON_FIELDS.NOIMGURL).getValue() == 4);
			assertTrue(result1.get("retailer3").get(RECON_FIELDS.NOIMGURLNEW).getValue() == 3);
			assertTrue(result1.get("retailer3").get(RECON_FIELDS.NOIMGURL).getValue() == 4);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReconcile(){
		try {
			ProductsDAO prodDao = mock(ProductsDAO.class);
			Method method = ProductsReconcilerNeo.class.getDeclaredMethod("reconcile", new Class[]{HashMap.class, HashMap.class, ProductsDAO.class});
			method.setAccessible(true);
			HashMap<ProductSummary, ProductSummary> allProds;
			HashMap<ProductSummary, ProductSummary> prods;
			Map<String, TreeMap<RECON_FIELDS, MutableInt>> result;
			//1. we get same products as earlier
			allProds = getProducts(1, 7, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			prods = getProducts(1, 7, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			result = (Map<String, TreeMap<RECON_FIELDS, MutableInt>>)method.invoke(recon, prods, allProds, prodDao);
			System.out.println("1. " + result);
			for(RECON_FIELDS f: RECON_FIELDS.productFields()){
				if(f.equals(RECON_FIELDS.TOTAL) || f.equals(RECON_FIELDS.HASREVIEWS) || f.equals(RECON_FIELDS.HASREVIEWERS)){
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 7);
				}
				else
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 0);
			}
			
			//2. we get some new products than earlier. 
			allProds = getProducts(1, 7, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			prods = getProducts(1, 10, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			result = (Map<String, TreeMap<RECON_FIELDS, MutableInt>>)method.invoke(recon, prods, allProds, prodDao);
			System.out.println("2. " + result);
			for(RECON_FIELDS f: RECON_FIELDS.productFields()){
				System.out.println(f.name());
				if(f.equals(RECON_FIELDS.TOTAL))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 10);
				else if(f.equals(RECON_FIELDS.NEWCOUNT))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 3);
				else if(f.equals(RECON_FIELDS.HASREVIEWS) || f.equals(RECON_FIELDS.HASREVIEWERS)){
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 10);
				}
				else
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 0);
			}
			//3. we get less number of products but with missing fields
			allProds = getProducts(1, 10, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			prods = getProducts(1, 7, new int[]{0}, new int[]{1}, new int[]{2}, new int[]{3}, new int[]{4}, new int[]{5}, new int[]{6}, new int[]{6}, new int[]{6});			
			result = (Map<String, TreeMap<RECON_FIELDS, MutableInt>>)method.invoke(recon, prods, allProds, prodDao);
			for(RECON_FIELDS f: RECON_FIELDS.productFields()){
				if(f.equals(RECON_FIELDS.TOTAL))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 7);
				else if(f.equals(RECON_FIELDS.NOUID) ||
						f.equals(RECON_FIELDS.NONAME) || f.equals(RECON_FIELDS.NONAMENEW) 
						|| f.equals(RECON_FIELDS.NOPRICE) || f.equals(RECON_FIELDS.NOPRICENEW)
						|| f.equals(RECON_FIELDS.NORANK) || f.equals(RECON_FIELDS.NORANKNEW)
						|| f.equals(RECON_FIELDS.NOURL)
						|| f.equals(RECON_FIELDS.NOIMGURL) || f.equals(RECON_FIELDS.NOIMGURLNEW) 
						|| f.equals(RECON_FIELDS.NOMODELNEW)
					    || f.equals(RECON_FIELDS.NOREVIEWSNEW)
						|| f.equals(RECON_FIELDS.NONUMREVIEWERSNEW) || f.equals(RECON_FIELDS.NORANKNEW))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 1);
				else if(f.equals(RECON_FIELDS.FAILED))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 2);
				else if(f.equals(RECON_FIELDS.HASREVIEWS) || f.equals(RECON_FIELDS.HASREVIEWERS))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 6);
				else
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 0);
			}
			
			//4. we get different products than the current ones but with missing fields. The ones for which price, name,
			//url or imgurl is missing are not counted towards new product. Any product for which these fields are missing
			//are counted towards failures. Missing reviews, review count, product rank are counted towards failure only if they were present earlier
			allProds = getProducts(1, 10, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			prods = getProducts(10, 7, new int[]{1}, new int[]{2}, new int[]{3}, new int[]{4}, new int[]{5}, new int[]{6}, new int[]{0}, new int[]{7}, new int[]{8});			
			result = (Map<String, TreeMap<RECON_FIELDS, MutableInt>>)method.invoke(recon, prods, allProds, prodDao);
			System.out.println("4. " + result);
			for(RECON_FIELDS f: RECON_FIELDS.productFields()){
				if(f.equals(RECON_FIELDS.TOTAL))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 7);
				else if(f.equals(RECON_FIELDS.NOUID) 
						|| f.equals(RECON_FIELDS.NONAME) 
						|| f.equals(RECON_FIELDS.NOPRICE) 
						|| f.equals(RECON_FIELDS.NOURL)
						|| f.equals(RECON_FIELDS.NOIMGURL)
						|| f.equals(RECON_FIELDS.NOREVIEWSNEW))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 1);
				else if(f.equals(RECON_FIELDS.FAILED))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 2);
				else if(f.equals(RECON_FIELDS.NEWCOUNT))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 4);
				else if(f.equals(RECON_FIELDS.HASREVIEWERS))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 7);
				else if(f.equals(RECON_FIELDS.HASREVIEWS))
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 6);
				else
					assertTrue(result.get(TEST_RETAILER).get(f).getValue() == 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testMergeLatestProducts(){
		try {
			Method method = ProductsReconcilerNeo.class.getDeclaredMethod("mergeLatestProducts", new Class[]{HashMap.class, HashMap.class});
			method.setAccessible(true);
			HashMap<ProductSummary, ProductSummary> prods = getProducts(1, 10, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			Thread.sleep(1000);
			HashMap<ProductSummary, ProductSummary> latest = getProducts(9, 12, new int[0], new int[]{0}, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			method.invoke(recon, prods, latest);
			assertTrue(prods.size() == 20);
			
			//make sure that the products actually got added
			for(ProductSummary prod: prods.keySet()){
				ProductSummary matchedLatest = ProductUtils.findMatch(prod, latest);
				ProductSummary matchedAll = ProductUtils.findMatch(prod, prods);
				int id = 0;
				try{
					id = Integer.parseInt(matchedAll.getName().substring("Product".length()));
				}catch(Exception e){
				}
				assertTrue(matchedAll != null); //Should always be there
				if(matchedLatest != null){
					assertTrue(id >= 9);
					assertTrue(matchedLatest.getName().equals(matchedAll.getName()));
					if(id == 9){
						//This one should not get merged
						assertTrue(matchedAll.getDownloadTime().before(matchedLatest.getDownloadTime()));
					}
					else
					assertTrue(matchedLatest.getDownloadTime().equals(matchedAll.getDownloadTime()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testStaleProducts(){
		try {
			Method method = ProductsReconcilerNeo.class.getDeclaredMethod("getStaleProducts", new Class[]{Date.class, HashMap.class});
			method.setAccessible(true);
			Date start = new Date();
			Thread.sleep(1000);
			HashMap<ProductSummary, ProductSummary> prods = getProducts(1, 10, new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
			Thread.sleep(1000);
			Date end = new Date();
			Map<String, MutableInt> result = (Map<String, MutableInt>)method.invoke(recon, start, prods);
			//There should not be any stale products
			assertTrue(result.size() == 0);
			result = (Map<String, MutableInt>)method.invoke(recon, end, prods);
			//all of them should be stale now
			assertTrue(result.get(TEST_RETAILER).getValue() == prods.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}
	
	private HashMap<ProductSummary, ProductSummary> getProducts(
			int startIdx, 
			int howMany, int[] noName, int[] noPrice, 
			int[] noUrl, int[] noImgUrl, int[] noModel, 
			int[] noDesc, int[] noReview, int[] noReviewCount, int[] noRank){
		HashMap<ProductSummary, ProductSummary> products = new HashMap<>();
		for(int i = 0; i < howMany; i++){
			String name = "Product" + startIdx;
			if(Arrays.binarySearch(noName, i) >= 0)
				name = "";
			double price = 1.99; 
			if(Arrays.binarySearch(noPrice, i) >= 0)
				price = PriceTypes.UNKNOWN.getValue();
			String url = "http://www.walmart.com/ip/a-product/" + startIdx;
			if(Arrays.binarySearch(noUrl, i) >= 0)
				url = "";
			String imgUrl = "http://www.retailer.com/images/" + startIdx;
			if(Arrays.binarySearch(noImgUrl, i) >= 0)
				imgUrl = "";
			String model = "Model" + startIdx;
			if(Arrays.binarySearch(noModel, i) >= 0)
				model = "";
			String desc = "Desc" + startIdx;
			if(Arrays.binarySearch(noDesc, i) >= 0)
				desc = "";
			double review = 1.1;
			if(Arrays.binarySearch(noReview, i) >= 0)
				review = -1;
			int reviewCount = 10;
			if(Arrays.binarySearch(noReviewCount, i) >= 0)
				reviewCount = -1;
			int rank = 10;
			if(Arrays.binarySearch(noRank, i) >= 0)
				rank = -1;
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = TEST_RETAILER;
			b.categoryId = 1;
			b.categoryName = "category";
			b.name = name;
			b.price = price;
			b.url = url;
			b.imageUrl = imgUrl;
			//b.desc = desc;
			b.model = model;
			b.reviewRating = review;
			b.numReviews = reviewCount;
			b.salesRank = rank;
			b.downloadTime = new Date();
			ProductSummary prod = b.build();
			products.put(prod, prod);
			startIdx++;
		}
		return products;
	}
}
