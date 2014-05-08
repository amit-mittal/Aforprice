package db.dao;

import static entities.Retailer.TESTRETAILER2;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import thrift.genereated.retailer.Product;
import util.AbstractTest;
import util.DateTimeUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;
import global.exceptions.Bhagte2BandBajGaya;

public class ProductsDAOTest extends AbstractTest{
	private ProductsDAO dao = new ProductsDAO();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		dao.regTestRemoveProducts(TESTRETAILER2.getId());
	}

	@Test
	public void test_insertUpdateProductSummary() {
		HashMap<ProductSummary, ProductSummary> existingProds = new HashMap<>();
		List<ProductSummary> products = new ArrayList<>();
		long uniqId = System.currentTimeMillis();
		try {
			//This product should get inserted as its being seen for the first time.
			Date t0 = new Date();
			ProductSummaryBuilder prod1RetailerA = new ProductSummaryBuilder();
			prod1RetailerA.retailerId = Retailer.TESTRETAILER.getId(); Retailer.TESTRETAILER.getId();
			prod1RetailerA.categoryId = 1;
			prod1RetailerA.categoryName = "mycategory";
			prod1RetailerA.name = "product" + uniqId;
			prod1RetailerA.price = 10.0;
			prod1RetailerA.url = Retailer.TESTRETAILER.getLink() + uniqId;
			prod1RetailerA.imageUrl = "xyz";
			//prod1RetailerA.desc = "desc1";
			prod1RetailerA.model = "model1";
			prod1RetailerA.reviewRating = 1;
			prod1RetailerA.numReviews = 100;
			prod1RetailerA.salesRank = 100;
			prod1RetailerA.downloadTime = t0;
			ProductSummary prod_t0 = prod1RetailerA.build();
			products.add(prod_t0);
			dao.insertUpdateProductSummary(products, existingProds);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t0.getName() + "'", prod_t0);
			//existingProds should contain the newly added product
			assertTrue(existingProds.size() == 1);
			existingProds.get(DateTimeUtils.diff(existingProds.get(prod_t0).getDownloadTime(), existingProds.get(prod_t0).getDownloadTime(), TimeUnit.MILLISECONDS) == 0);
			
			//Same product with newer time stamp. The fields should get updated.
			Date t1 = new Date(t0.getTime() + 1000);
			prod1RetailerA.name = "newproduct" + uniqId;
			prod1RetailerA.categoryId = 2;
			prod1RetailerA.price = 12.0;
			//prod1RetailerA.desc = "desc2";
			prod1RetailerA.model = "model2";
			prod1RetailerA.reviewRating = 2;
			prod1RetailerA.numReviews = 101;
			prod1RetailerA.salesRank = 101;
			prod1RetailerA.downloadTime = t1;
			ProductSummary prod_t1 = prod1RetailerA.build();
			products = new ArrayList<>();
			products.add(prod_t1);
			dao.insertUpdateProductSummary(products, existingProds);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t1.getName() + "'", prod_t1);
			//existingProds should contain the updated product
			assertTrue(existingProds.size() == 1);
			existingProds.get(DateTimeUtils.diff(existingProds.get(prod_t1).getDownloadTime(), existingProds.get(prod_t1).getDownloadTime(), TimeUnit.MILLISECONDS) == 0);
			//Same product with only download time being changed. Only download time should get modified and TIME_MODIFIED field should remain the same
			prod1RetailerA.downloadTime = new Date(t1.getTime() + 1000);
			ProductSummary prod_t1_nochanges = prod1RetailerA.build();
			products = new ArrayList<>();
			products.add(prod_t1_nochanges);
			Thread.sleep(5000);//sleep so that if TIME_MODIFIED were to get updated, it is measurable 
			dao.insertUpdateProductSummary(products, existingProds);
			assertTimeUpdatedOlder("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t1_nochanges.getName() + "'", new Date(), 5);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t1_nochanges.getName() + "'", prod_t1_nochanges);
			//Same product with older time stamp. The fields should not get updated.
			Date tminus1 = new Date(t0.getTime() - 1000);
			prod1RetailerA.categoryId = 3;
			prod1RetailerA.name = "oldproduct" + uniqId;
			prod1RetailerA.price = 13.0;
			//prod1RetailerA.desc = "desc3";
			prod1RetailerA.model = "model3";
			prod1RetailerA.reviewRating = 3;
			prod1RetailerA.numReviews = 102;
			prod1RetailerA.salesRank = 102;
			prod1RetailerA.downloadTime = tminus1;
			ProductSummary prod_tminus1 = prod1RetailerA.build();
			products = new ArrayList<>();
			products.add(prod_tminus1);
			dao.insertUpdateProductSummary(products, existingProds);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_tminus1.getName() + "'", null);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t1.getName() + "'", prod_t1);
			//existingProds should not change
			assertTrue(existingProds.size() == 1);
			existingProds.get(DateTimeUtils.diff(existingProds.get(prod_t1).getDownloadTime(), existingProds.get(prod_t1).getDownloadTime(), TimeUnit.MILLISECONDS) == 0);
			
			//unmigratable products
			ProductSummaryBuilder prod1UnmigratableB = new ProductSummaryBuilder();
			prod1UnmigratableB.retailerId = Retailer.TESTRETAILER.getId();
			prod1UnmigratableB.categoryId = 1;
			prod1UnmigratableB.categoryName = "mycategory";
			prod1UnmigratableB.name = "product_unmigratable" + uniqId;
			prod1UnmigratableB.price = 10.0;
			prod1UnmigratableB.url = "http://www.randomurl.com";
			prod1UnmigratableB.imageUrl = "xyz";
			//prod1UnmigratableB.desc = "desc1";
			prod1UnmigratableB.model = "model1";
			prod1UnmigratableB.reviewRating = 1;
			prod1UnmigratableB.numReviews = 100;
			prod1UnmigratableB.salesRank = 100;
			prod1UnmigratableB.downloadTime = new Date();
			ProductSummary prodUnmigratable = prod1UnmigratableB.build();
			products = new ArrayList<>();
			products.add(prodUnmigratable);
			dao.insertUpdateProductSummary(products, existingProds);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prodUnmigratable.getName() + "'", null);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test_getProductSummaryByProductId(){
		HashMap<ProductSummary, ProductSummary> existingProds = new HashMap<>();
		List<ProductSummary> products = new ArrayList<>();
		long uniqId = System.currentTimeMillis();
		ResultSet rs = null;
		try {
			Date t0 = new Date();
			ProductSummary prod_t0= new ProductSummaryBuilder(Retailer.TESTRETAILER.getId(), "product"+uniqId, Retailer.TESTRETAILER.getLink()+uniqId, "www.image1.com", "model1").
					categoryId(1).categoryName("mycategory").price(10.0).reviewRating(2.3).numReviews(100).salesRank(100).downloadTime(t0).build();
			products.add(prod_t0);
			dao.insertUpdateProductSummary(products, existingProds);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t0.getName() + "'", prod_t0);

			assertTrue(existingProds.size() == 1);
			existingProds.get(DateTimeUtils.diff(existingProds.get(prod_t0).getDownloadTime(), existingProds.get(prod_t0).getDownloadTime(), TimeUnit.MILLISECONDS) == 0);
			
			rs = DataAccessObject.execute("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t0.getName() + "'");
			assertTrue(rs.next());
			ProductSummary expected = dao.getProductSummary(rs);

			ProductSummary prod = dao.getProductSummaryByProductId(expected.getId());

			assertTrue(prod.getRetailerId().equals(prod_t0.getRetailerId()));
			assertTrue(prod.getName().equals(prod_t0.getName()));
			assertTrue(prod.getUrl().equals(prod_t0.getUrl()));
			assertTrue(prod.getImageUrl().equals(prod_t0.getImageUrl()));
			assertTrue(prod.getModel().equals(prod_t0.getModel()));
			assertTrue(Math.abs(DateTimeUtils.diff(prod.getDownloadTime(), prod_t0.getDownloadTime(), TimeUnit.SECONDS)) <= 1);
			assertTrue(prod.getPrice() == prod_t0.getPrice());
			assertTrue(prod.getSalesRank() == prod_t0.getSalesRank());
			assertTrue(prod.getReviewRating() == prod_t0.getReviewRating());
			assertTrue(prod.getNumReviews() == prod_t0.getNumReviews());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void test_getProductsThriftForRetailer(){
		HashMap<ProductSummary, ProductSummary> existingProds = new HashMap<>();
		List<ProductSummary> products = new ArrayList<>();
		long uniqId = System.currentTimeMillis();
		ResultSet rs = null;
		try {
			Date t0 = new Date();
			ProductSummary prod_t0= new ProductSummaryBuilder(TESTRETAILER2.getId(), "product"+uniqId, Retailer.TESTRETAILER2.getLink()+uniqId, "www.image1.com", "model1").
					categoryId(1).categoryName("mycategory").price(10.0).reviewRating(2.3).numReviews(100).salesRank(100).downloadTime(t0).build();			
			products.add(prod_t0);
			dao.insertUpdateProductSummary(products, existingProds);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t0.getName() + "'", prod_t0);
			assertTrue(existingProds.size() == 1);
			existingProds.get(DateTimeUtils.diff(existingProds.get(prod_t0).getDownloadTime(), existingProds.get(prod_t0).getDownloadTime(), TimeUnit.MILLISECONDS) == 0);
			
			Date t1 = new Date();
			uniqId++;
			ProductSummary prod_t1= new ProductSummaryBuilder(TESTRETAILER2.getId(), "product"+uniqId, Retailer.TESTRETAILER2.getLink()+uniqId, "www.image2.com", "model2").
					categoryId(2).categoryName("mycategory").price(10.0).reviewRating(1.8).numReviews(182).salesRank(32).downloadTime(t1).build();		
			products.clear();
			existingProds.clear();
			products.add(prod_t1);
			dao.insertUpdateProductSummary(products, existingProds);
			assertExpected("SELECT * FROM PRODUCT_SUMMARY WHERE PRODUCT_NAME='" + prod_t1.getName() + "'", prod_t1);
			assertTrue(existingProds.size() == 1);
			existingProds.get(DateTimeUtils.diff(existingProds.get(prod_t1).getDownloadTime(), existingProds.get(prod_t1).getDownloadTime(), TimeUnit.MILLISECONDS) == 0);
			
			List<Product> prods = dao.getProductsThriftForRetailer(TESTRETAILER2.getId(), -1);

			_assertEquals(2, prods.size());
			boolean found_t0 = false;
			boolean found_t1 = false;
			for(int index=0;index<prods.size();++index){
				Product prod = prods.get(index);
				if(prod.getName().equals(prod_t0.getName())){
					assertTrue(prod.getName().equals(prod_t0.getName()));
					assertTrue(prod.getUrl().equals(prod_t0.getUrl()));
					assertTrue(prod.getImageUrl().equals(prod_t0.getImageUrl()));
					assertTrue(prod.getModelNo().equals(prod_t0.getModel()));
					found_t0 = true;
				}else if(prod.getName().equals(prod_t1.getName())){
					assertTrue(prod.getName().equals(prod_t1.getName()));
					assertTrue(prod.getUrl().equals(prod_t1.getUrl()));
					assertTrue(prod.getImageUrl().equals(prod_t1.getImageUrl()));
					assertTrue(prod.getModelNo().equals(prod_t1.getModel()));
					found_t1 = true;
				}
			}
			assertTrue(found_t0);
			assertTrue(found_t1);
			
			prods.clear();
			prods = dao.getProductsThriftForRetailer(TESTRETAILER2.getId(), 1);
			found_t0 = false;
			found_t1 = false;
			assertTrue(prods.size() == 1);
			for(int index=0;index<prods.size();++index){
				Product prod = prods.get(index);
				if(prod.getName().equals(prod_t0.getName())){
					assertTrue(prod.getName().equals(prod_t0.getName()));
					assertTrue(prod.getUrl().equals(prod_t0.getUrl()));
					assertTrue(prod.getImageUrl().equals(prod_t0.getImageUrl()));
					assertTrue(prod.getModelNo().equals(prod_t0.getModel()));
					found_t0 = true;
				}else if(prod.getName().equals(prod_t1.getName())){
					assertTrue(prod.getName().equals(prod_t1.getName()));
					assertTrue(prod.getUrl().equals(prod_t1.getUrl()));
					assertTrue(prod.getImageUrl().equals(prod_t1.getImageUrl()));
					assertTrue(prod.getModelNo().equals(prod_t1.getModel()));
					found_t1 = true;
				}
			}
			assertTrue(found_t0 || found_t1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
	
	@Test
	public void testRegTestRemoveProductsSafetyCheck() throws SQLException{
		String retailerId = "a&f";
		try {
			dao.regTestRemoveProducts(retailerId);
			fail("able to delete rows for non-test retailer");
		} catch (Bhagte2BandBajGaya e) {
			//remove product safety check worked
		}
	}
	
	private void assertExpected(String qryActual, ProductSummary expected){
		ResultSet rs = null;
		try{
			rs = DataAccessObject.execute(qryActual);
			if(expected == null){
				assertTrue(!rs.next());
				return;
			}
			assertTrue(rs.next());
			ProductSummary ps = dao.getProductSummary(rs);
			assertTrue(ps.getRetailerId().equals(expected.getRetailerId()));
			assertTrue(ps.getName().equals(expected.getName()));
			assertTrue(ps.getUrl().equals(expected.getUrl()));
			assertTrue(ps.getImageUrl().equals(expected.getImageUrl()));
			assertTrue(ps.getModel().equals(expected.getModel()));
			assertTrue(Math.abs(DateTimeUtils.diff(ps.getDownloadTime(), expected.getDownloadTime(), TimeUnit.SECONDS)) <= 1);
			assertTrue(ps.getPrice() == expected.getPrice());
			assertTrue(ps.getSalesRank() == expected.getSalesRank());
			assertTrue(ps.getReviewRating() == expected.getReviewRating());
			assertTrue(ps.getNumReviews() == expected.getNumReviews());
			assertTrue(!rs.next());
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
	
	private void assertTimeUpdatedOlder(String qry, Date time, int by){
		ResultSet rs = null;
		try{
			rs = DataAccessObject.execute(qry);
			assertTrue(rs.next());
			Timestamp ts = rs.getTimestamp(ProductSummary.Columns.TIME_MODIFIED);
			assertTrue(DateTimeUtils.diff(time, ts, TimeUnit.SECONDS) >= by);
			rs.close();
		}catch(Exception e){
			fail(e.getMessage());
		}
		finally{
			DataAccessObject.closeRS(rs);
		}
	}
}
