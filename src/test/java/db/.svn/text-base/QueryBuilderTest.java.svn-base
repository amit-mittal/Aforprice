package db;

import static org.junit.Assert.assertTrue;

import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import db.types.ISQLType;
import db.types.SQLDouble;
import db.types.SQLInteger;
import db.types.SQLString;

public class QueryBuilderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdateQuery() {
		TreeMap<String, ISQLType> updateCols = new TreeMap<>();
		TreeMap<String, ISQLType> whereCols = new TreeMap<>();
		//Test when both numeric and non numeric columns need to be updated
		updateCols.put("NAME", new SQLString("my product"));
		updateCols.put("PRICE", new SQLDouble(1.99));
		updateCols.put("RANK", new SQLInteger(99));
		updateCols.put("URL", new SQLString("http://www.myretailer.com/myproduct"));
		whereCols.put("PRODUCT_ID", new SQLInteger(1));
		String query = QueryBuilder.updateQuery("PRODUCT", "PRODUCT_SUMMARY", updateCols, whereCols);
		assertTrue(query.equals("UPDATE PRODUCT.PRODUCT_SUMMARY SET NAME='my product', PRICE=1.99, RANK=99, URL='http://www.myretailer.com/myproduct' WHERE PRODUCT_ID=1"));
//		//Test when only non numeric columns need to be updated
//		updateCols.clear();
//		query = QueryBuilder.updateQuery("PRODUCT", "PRODUCT_SUMMARY", updateCols, whereCols);
//		assertTrue(query.equals("UPDATE TABLE PRODUCT.PRODUCT_SUMMARY SET NAME='my product', URL='http://www.myretailer.com/myproduct'"));
//		//Test when only numeric columns need to be updated
//		updateCols.put("PRICE", String.valueOf(1.99));
//		updateCols.put("RANK", String.valueOf(99));
//		whereCols.clear();
//		query = QueryBuilder.updateQuery("PRODUCT", "PRODUCT_SUMMARY", updateCols, whereCols);
//		assertTrue(query.equals("UPDATE TABLE PRODUCT.PRODUCT_SUMMARY SET PRICE=1.99, RANK=99"));
		
	}

}
