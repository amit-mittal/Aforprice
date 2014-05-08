package db;

import global.exceptions.Bhagte2BandBajGaya;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SelectQuery {
	private final String query;
	private final Map<String, Integer> selectCols;
	private final Map<String, Integer> whereCols;
	public SelectQuery(String query, Map<String, Integer> selectCols, Map<String, Integer> whereCols){
		this.query = query;
		this.selectCols = selectCols;
		this.whereCols = whereCols;
	}
	
	public String getQueryStr(){
		return query;
	}
	
	public int getWhereCol(String colName){
		return whereCols.get(colName);
	}
	
	public int getSelectCol(String colName){
		return selectCols.get(colName);
	}
	
	public PreparedStatement prepareStatement(DbConnection conn){
		try{
			return conn.getConnection().prepareStatement(query);
		}catch(SQLException sqe){
			throw new Bhagte2BandBajGaya("appropriate message", sqe);
		}
	}
}
