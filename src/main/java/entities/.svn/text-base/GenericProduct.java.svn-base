/******************************************
** GenericProduct.java Created @Jun 12, 2012 10:05:22 PM
** @author: Jayanta Hazra
**
******************************************/
package entities;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class GenericProduct {
	
	private static final Logger logger = Logger.getLogger("GenericProduct");
	public static Date currentTime = new Date();
	public static final String COL_PROD_ID_INTERNAL = "PROD_ID_INTERNAL";
	public static final String COL_FROM_DATE = "FROM_DATE";
	public static final String COL_THRU_DATE = "THRU_DATE";
	public static final String COL_UPDATE_TIME = "UPDATE_TIME";
	
	
	public static final String TABLE_NAME = "tableName";

	private Map<String, ProdAttributeDetails> attribMapper = null;
	private Map<String, Object> attribs = new HashMap<String, Object>();
	//private Map<String, Integer> attribTypes = newHashMap<String, Integer>(); //to be used later
	private String prodIdInternal;
	private Date fromDate = currentTime;
	private Date thruDate;
	private Date updateTime;
	
	public GenericProduct(Map<String, ProdAttributeDetails> attribMapper) {
		this.attribMapper = attribMapper;
		//this.attribTypes = attribTypes;
	}
	
	public Map<String, ProdAttributeDetails> getAttribMapper() {
		return attribMapper;
	}
	public String getProdIdInternal() {
		return prodIdInternal;
	}
	public void setProdIdInternal(String prodIdInternal) {
		this.prodIdInternal = prodIdInternal;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getThruDate() {
		return thruDate;
	}
	public void setThruDate(Date thruDate) {
		this.thruDate = thruDate;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Object getAttribute( String attrName) {
		return attribs.get(attrName);
	}
	public void setAttribute(String attrName, String val) {
		attribs.put(attrName, val);
	}		
//	public Map<String, Integer> getAttribTypes() {
//		return attribTypes;
//	}
//	public void setAttribTypes(Map<String, Integer> attribTypes) {
//		this.attribTypes = attribTypes;
//	}
	public void populateProductAttrs(HashMap<String, String> feedAttrValues) {
		for(  Iterator<Entry<String, String>> it = feedAttrValues.entrySet().iterator(); it.hasNext(); ) {
			Entry<String, String> keyMap = it.next();
			String vendorAttrName = keyMap.getKey();
			ProdAttributeDetails attrDetails = attribMapper.get(vendorAttrName);
			if( attrDetails == null) continue;
			String internalKey = attrDetails.getInternalName();
			Object val = keyMap.getValue();
			try {
				if( attrDetails.getType() == Types.FLOAT || attrDetails.getType() == Types.REAL) {
					if( val != null && val.toString().length() > 0 ) val = Float.parseFloat((String)val);
					else val = new Float(0);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			attribs.put(internalKey, val);
		}		
	}
	public void populateProductAttrs(ResultSet rs) {
		try{
			ResultSetMetaData colMetaData = rs.getMetaData();
			for( int i = 1 ; i <= colMetaData.getColumnCount(); i++) {
				String colName = colMetaData.getColumnName(i);				
				//String colVal = rs.getString(i);
				if( COL_PROD_ID_INTERNAL.equalsIgnoreCase(colName) )
					prodIdInternal = rs.getString(i);
				else if( COL_FROM_DATE.equalsIgnoreCase(colName))
					fromDate = rs.getTimestamp(i);
				else if ( COL_THRU_DATE.equalsIgnoreCase(colName))
					thruDate = rs.getTimestamp(i);
				else if ( COL_UPDATE_TIME.equalsIgnoreCase(colName))
					updateTime = rs.getTimestamp(i);
				else {
					if( colMetaData.getColumnType(i) == Types.FLOAT || colMetaData.getColumnType(i) == Types.REAL)
						attribs.put(colName, rs.getFloat(i));
					else
						attribs.put(colName, rs.getString(i));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public String getExpireSQL() {
		String tableName = attribMapper.get(TABLE_NAME).getInternalName();
		StringBuilder sql = new StringBuilder("update ").append(tableName ) .append(" set THRU_DATE='" + new Timestamp( currentTime.getTime()-1000)+ "', ");
		sql.append(" UPDATE_TIME = '" + new Timestamp(currentTime.getTime())+ "' where PROD_ID_INTERNAL='" + prodIdInternal + "' AND THRU_DATE IS NULL");
//		sql.append(" AND FROM_DATE='" + new Timestamp(fromDate.getTime()) + "'");
//		if( thruDate == null )
//			sql.append(" AND THRU_DATE IS NULL");
//		else
//			sql.append(" AND THRU_DATE='" + new Timestamp(thruDate.getTime()) + "'");
		return sql.toString();
	}

	
	public String getUpdateSQL() {
		String tableName = attribMapper.get(TABLE_NAME).getInternalName();
		StringBuilder sql = new StringBuilder("update ").append(tableName ) .append(" set ");
		List<String> l = new ArrayList<String>(attribs.keySet());
		for( int i = 0; i < l.size(); i++) {
			String k = l.get(i);
			Object val = attribs.get(k);
			if( val instanceof Float) {
				sql.append(k).append("=").append(val).append(", ");
			}else {
				String valStr = (String)val;
				valStr = escape( valStr);
				if( valStr == null) valStr = "";
				sql.append(k).append("='").append(valStr).append("', ");
			}
//			if( i < l.size() -1 )
//				sql.append(",");
		}
		sql.append(" UPDATE_TIME = '" + new Timestamp(currentTime.getTime())+ "' where PROD_ID_INTERNAL='" + prodIdInternal + "' AND THRU_DATE IS NULL");
		//sql.append(" AND FROM_DATE='" + new Timestamp(fromDate.getTime()) + "'");
//		if( thruDate == null )
//			sql.append(" AND THRU_DATE IS NULL");
//		else
//			sql.append(" AND THRU_DATE='" + new Timestamp(thruDate.getTime()) + "'");
		return sql.toString();
}

	public String getInsertSQL() {
		String tableName = attribMapper.get(TABLE_NAME).getInternalName();
		StringBuilder sql = new StringBuilder("insert into ").append(tableName).append( " (");
		List<String> l = new ArrayList<String>(attribs.keySet());
		for( int i = 0; i < l.size(); i++) {
			String k = l.get(i);
			Object val = attribs.get(k);
			//if( val != null && !val.equals(""))
			sql.append(k).append(" ,");
		}
		sql.append(" FROM_DATE,"); 
		if( thruDate != null) sql.append(" THRU_DATE, " );
		sql.append(" UPDATE_TIME, PROD_ID_INTERNAL) VALUES( " );
		for( int i = 0; i < l.size(); i++) {
			String k = l.get(i);
			Object val = attribs.get(k);		
			if(  val instanceof Float) {
				sql.append("'").append(val).append("', ");
			}
			else {
				String valStr = escape( (String)val);
				if(valStr == null) valStr = "";
				//if( valStr != null && !valStr.equals(""))
				sql.append("'").append(valStr).append("', ");
			}
		}
		Timestamp fromTs = new Timestamp( fromDate.getTime());
		Timestamp updateTs = new Timestamp( currentTime.getTime());
		sql.append("'" + fromTs+ "', ");
		if( thruDate != null) sql.append( "'" +  new Timestamp( thruDate.getTime()) + "'," );
		sql.append("'" + updateTs + "', '" + prodIdInternal + "')");
		return sql.toString();		
	}	
	private String escape(String val) {
		if( val == null) return "";
		val = val.replace("'", "''");
		return val;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribs == null) ? 0 : attribs.hashCode());
		result = prime * result
				+ ((prodIdInternal == null) ? 0 : prodIdInternal.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericProduct other = (GenericProduct) obj;
		if( prodIdInternal.equals(other.prodIdInternal) && Math.abs((fromDate.getTime() - other.fromDate.getTime())/1000) <  1) 
			return true;
		if( !attribsEqual(this.attribs, other.attribs))
			return false;
		if (prodIdInternal == null) {
			if (other.prodIdInternal != null)
				return false;
		} else if (!prodIdInternal.equals(other.prodIdInternal))
			return false;
		return true;
	}
	
	private boolean attribsEqual(Map<String, Object> attrM1, Map<String, Object> attrM2) {
		if( attrM1 == null)
			return attrM2 == null;
		if( attrM2 == null)
			return attrM1 == null;
		for( Iterator<Entry<String, Object>> it = attrM1.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			Object val1 = entry.getValue();
			Object val2 = attrM2.get(key);
			if( val1 == null && val2 == null)
				continue;
			if( val1 == null) {
				if( val2 instanceof Float ) val1 = new Float( 0 );
				else val1 = "";
			}
			if( val2 == null) {
				if( val1 instanceof Float ) val2 = new Float( 0 );
				else val2 = "";
			}
			if( !val1.equals(val2)) {
				logger.info("mismatch:" + key + ":: " + val1 + "<=>" + val2);
				return false;
			}
		}
		return true;
	}
	
}
