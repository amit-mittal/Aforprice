/******************************************
** ProductAttrMappingMgr.java Created @Jun 23, 2012 8:45:30 PM
** @author: Jayanta Hazra
**
******************************************/
package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import db.DbConnectionPool;
import db.Queries;
import entities.ProdAttributeDetails;

public class ProductAttrMappingMgr {

	public static enum ATTR_TYPES { PROD, PRICE };
//	public static String GENERIC_PROD_TABLE = "GENERIC_PRODUCTS";
//	public static String GENERIC_PROD_ATTRS_TABLE = "GENERIC_PROD_ATTRS";
	public static String VENDOR_ATTR_NAME = "VENDOR_ATTR_NAME";
	public static String ATTR_NAME = "ATTR_NAME";
	public static String ATTR_TYPE = "ATTR_TYPE";
	private HashMap<String, HashMap<String, ProdAttributeDetails>> productAttrMapping = new HashMap<String, HashMap<String, ProdAttributeDetails>>();
	private Map<String, Integer> productAttrDataTypeMapping  = new HashMap<String, Integer>();
	private static ProductAttrMappingMgr instance = new ProductAttrMappingMgr();
	private ProductAttrMappingMgr() {
		loadAttrDataTypeMapping();
		loadProductAttrMapping();
	}
	public static ProductAttrMappingMgr getInstance() {
		return instance;
	}
	private void loadAttrDataTypeMapping() {
		Connection connection = DbConnectionPool.get().getConnection().getConnection();
		try {
			PreparedStatement stmt = connection.prepareStatement(Queries.GET_GENERIC_PRODS_DATA_TYPES);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			for( int i =1; i<= metaData.getColumnCount(); i++)
				productAttrDataTypeMapping.put(metaData.getColumnName(i), metaData.getColumnType(i));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	private void loadProductAttrMapping() {
		Connection connection = DbConnectionPool.get().getConnection().getConnection();
		try {
			PreparedStatement stmt = connection.prepareStatement(Queries.GET_PROD_ATTR_MAPPING);
			ResultSet rs = stmt.executeQuery();
			while( rs.next()) {
				String vendorAttrName = rs.getString(VENDOR_ATTR_NAME);
				String attrName = rs.getString(ATTR_NAME);
				String attrType = rs.getString(ATTR_TYPE);
				HashMap<String, ProdAttributeDetails> map = productAttrMapping.get(attrType);
				if( map == null) {
					map = new HashMap<String, ProdAttributeDetails>();
					productAttrMapping.put(attrType, map);
				}				
				Integer dataType = productAttrDataTypeMapping.get(attrName);
				if( dataType == null )
					dataType = Types.VARCHAR;
				ProdAttributeDetails prodAttrDetails = new ProdAttributeDetails(attrName, dataType);
				map.put(vendorAttrName, prodAttrDetails);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public Map<String, ProdAttributeDetails> getAttributeMapping( ATTR_TYPES type) {
		return productAttrMapping.get(type.toString());
	}
	public String getInternalAttributeName(String vendorAttrName, ATTR_TYPES type) {
		return productAttrMapping.get(type.toString()).get(vendorAttrName).getInternalName();
	}	
}
