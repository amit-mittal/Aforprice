package util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parsers.util.PriceTypes;

import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;

public class ProductUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_isProductMigratable() {
		ProductSummaryBuilder b = new ProductSummaryBuilder();
		b.retailerId = Retailer.WALMART.getId();
		b.categoryId = 1;
		b.categoryName = "mycategory";
		b.name = "myproduct";
		b.price = 10.0;
		b.url = "http://www.walmart.com/ip/V7-Vantage-Webcam-300-with-Built-In-Microphone/16419432";
		b.imageUrl = "http://www.walmart.com/image.jpg";
		//b.desc = "";
		b.model = "";
		b.reviewRating = 1;
		b.numReviews = 100;
		b.salesRank = 100;
		b.downloadTime = new Date();
		ProductSummary prod = b.build();
		ProductSummary existing = b.build();
		assertTrue(ProductUtils.isProductMigratable(prod));
		b.price = PriceTypes.UNKNOWN.getValue();
		prod = b.build();
		//new product has invalid price
		assertTrue(!ProductUtils.isProductMigratable(prod));
		b.price = 10.0;
		b.url = "http://www.walmart.com";
		//new product uid is absent
		assertTrue(!ProductUtils.isProductMigratable(prod));
		b.url = "http://www.walmart.com/ip/V7-Vantage-Webcam-300-with-Built-In-Microphone/16419432";
		b.reviewRating = 0;
		prod = b.build();
		b.reviewRating = 1;
		existing = b.build();
		//Existing has review rating, new product does not have one, still this product should be migratable
		assertTrue(ProductUtils.isProductMigratable(prod));
	}
	
	@Test
	public void test_areEqual(){
		ProductSummaryBuilder b = new ProductSummaryBuilder();
		b.retailerId = Retailer.WALMART.getId();
		b.categoryId = 1;
		b.categoryName = "mycategory";
		b.name = "myproduct";
		b.price = 10.0;
		b.url = "http://www.walmart.com/ip/V7-Vantage-Webcam-300-with-Built-In-Microphone/16419432";
		b.imageUrl = "http://www.walmart.com/image.jpg";
		//b.desc = "";
		b.model = "";
		b.reviewRating = 1;
		b.numReviews = 100;
		b.salesRank = 100;
		b.downloadTime = new Date();
		ProductSummary prod1 = b.build();
		ProductSummary prod2 = b.build();
		assertTrue(ProductUtils.areEqual(prod1, prod2));
		assertTrue(prod1.hashCode() == prod2.hashCode());
		assertTrue(prod1.equals(prod2));
		//Change name. Products should still be equal
		b.name = b.name + "another";
		prod2 = b.build();
		assertTrue(ProductUtils.areEqual(prod1, prod2));
		assertTrue(prod1.hashCode() == prod2.hashCode());
		assertTrue(prod1.equals(prod2));
		b.name = prod1.getName();
		//Change price, reviews and sell rank. Products should be equal		
		b.price = b.price + 1;
		b.numReviews = b.numReviews + 1;
		b.reviewRating = b.reviewRating + 1;
		b.salesRank = b.salesRank + 1;
		prod2 = b.build();
		assertTrue(ProductUtils.areEqual(prod1, prod2));
		assertTrue(prod1.hashCode() == prod2.hashCode());
		assertTrue(prod1.equals(prod2));
		//Change retailer, product should not be equal
		b.retailerId = Retailer.TARGET.getId();
		prod2 = b.build();
		assertTrue(!ProductUtils.areEqual(prod1, prod2));
		assertTrue(prod1.hashCode() != prod2.hashCode());
		assertTrue(!prod1.equals(prod2));
		//Switch retailer back, products should be equal now
		b.retailerId = prod1.getRetailerId();
		prod2 = b.build();
		assertTrue(ProductUtils.areEqual(prod1, prod2));
		assertTrue(prod1.hashCode() == prod2.hashCode());
		assertTrue(prod1.equals(prod2));
		//Changing url, changes the uid, so products should not be equal
		b.url = b.url + "another";
		prod2 = b.build();
		assertTrue(!ProductUtils.areEqual(prod1, prod2));
		assertTrue(prod1.hashCode() != prod2.hashCode());
		assertTrue(!prod1.equals(prod2));
	}
}
