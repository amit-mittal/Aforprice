package db;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import db.types.ISQLType;

public class QueryBuilder {
	
	private static final Logger LOGGER = Logger.getLogger(QueryBuilder.class);
	public static String updateQuery(String db, String table, TreeMap<String, ISQLType> updateCols, TreeMap<String, ISQLType> whereCols){
		if(updateCols.size() == 0)
			return null;
		StringBuilder query = new StringBuilder("UPDATE ").append(db).append(".").append(table).append(" SET ");
		int idx = 0;
		for(Map.Entry<String, ISQLType> entry: updateCols.entrySet()){
			query.append(entry.getKey()).append("=").append(entry.getValue().getRHValue());
			if(++idx < updateCols.size())
				query.append(", ");
		}
		if(whereCols.size() > 0){
			query.append(" WHERE ");
			idx = 0;
			for(Map.Entry<String, ISQLType> entry: whereCols.entrySet()){
				query.append(entry.getKey()).append("=").append(entry.getValue().getRHValue());
				if(++idx < whereCols.size())
					query.append(" AND ");
			}
		}
		return query.toString();
	}
}
/* 
* we replace special characters (i.e. %) with other characters (i.e. \\%) which can make URL length more than 500 so this hack  
* is to reduce it to below 500 
*/
