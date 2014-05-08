package work.managers;

import global.exceptions.Bhagte2BandBajGaya;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import stores.ProductStore;
import work.items.ProductsParseTask;
import work.items.Task;
import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;
import entities.Category;

public class ProductsParseWorkManager extends WorkManagerLockedTasks {
	
	private final static Logger logger = Logger.getLogger(ProductsParseWorkManager.class);
	
	private final DbConnection conn;
	private final PreparedStatement catIdsToProc;
	private final PreparedStatement selectCat;	
	
	private final PreparedStatement errCatIdsToProc;
	private final String retailerOverride;
	
	public ProductsParseWorkManager(Date runDate, String retailerOverride, int id){
		super(runDate, "ProductsParse" + ((retailerOverride != null && !retailerOverride.isEmpty())?"-" + retailerOverride: "") + "-" + id);
		this.retailerOverride = retailerOverride;
		conn = DbConnectionPool.get().getConnection();//.getConnection();
		int i = 0, j = 0;
		try {
			errCatIdsToProc = conn.getConnection().prepareStatement(Queries.SELECT_ERR_CATIDS_TO_PROC);
			if(retailerOverride == null){
				catIdsToProc = conn.getConnection().prepareStatement(Queries.SELECT_CATIDS_TO_PROC);				
			}else{
				catIdsToProc = conn.getConnection().prepareStatement(Queries.SELECT_CATIDS_TO_PROC_RET_OVERRIDE);
				catIdsToProc.setString(++i, this.retailerOverride);				
			}
			catIdsToProc.setDate(++i, new java.sql.Date(runDate.getTime()));
			catIdsToProc.setString(++i, getName());
			
			errCatIdsToProc.setDate(++j, new java.sql.Date(runDate.getTime()));
			errCatIdsToProc.setInt(++j, 1); //Get the categories which were in error after first attempt
			errCatIdsToProc.setString(++j, getName()); //task name
			errCatIdsToProc.setDate(++j, new java.sql.Date(runDate.getTime()));
			errCatIdsToProc.setInt(++j, 2); //Exclude any cagtegory which has been attempted more than once
			errCatIdsToProc.setString(++j, getName()); //task name
			selectCat = conn.getConnection().prepareStatement(Queries.SELECT_CATEGORY);				
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);			
			throw new Bhagte2BandBajGaya(e);
		}
		finally{
			DbConnectionPool.get().releaseConnection(conn);
		}
	}
		
	@Override
	public List<Task> getNewTasks(int max) {
		List<Task> tasks = null;		
		waitOnPendingTasks(max);		
		try{	
			ResultSet results = null;
			try{
				results = catIdsToProc.executeQuery();			
			tasks = getTasks(results, max, 1);
			
			}catch(Throwable e){
				e.printStackTrace();
			}
			finally{
				if(results != null)
					results.close();	
			}
		try{
			if(tasks == null || tasks.size() == 0){
				results = errCatIdsToProc.executeQuery();
				tasks = getTasks(results, max, 2); //2nd trial				
			}
			}finally{
				if(results != null)
					results.close();
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);			
		}
		if(tasks == null || tasks.size() == 0){
			cleanUp();
		}
		return tasks;
	}

	private List<Task> getTasks(ResultSet catIdsResult, int max, int attempt) throws SQLException{
		List<Integer> catIds = new ArrayList<Integer>();
		int i = 0;
		while(catIdsResult.next()){
			int categoryId = catIdsResult.getInt(1);
			if(!tryLock(categoryId, attempt))
				continue;
			catIds.add(categoryId);									
			if(++i >= (max - getNumPending()))
				break;				
		}

		List<Task> tasks = new ArrayList<Task>();
		for(int catId: catIds){
			selectCat.setInt(1, catId);
			ResultSet categoryRS = selectCat.executeQuery();
			Category prodCat = null;
			if(categoryRS.next()){
				String retailer = categoryRS.getString(Category.Columns.RETAILER_ID);
				if(retailerOverride != null && !retailerOverride.equalsIgnoreCase(retailer))
					continue;
				prodCat = new Category(categoryRS.getInt(Category.Columns.CATEGORY_ID),
											  retailer, 
											  categoryRS.getString(Category.Columns.NAME),
											  categoryRS.getString(Category.Columns.URL));	
				Task task = new ProductsParseTask(this, attempt, prodCat, ProductStore.Factory.get()); 
				tasks.add(task);
				addPending(task);	
			}
		}
		return tasks;
	}
	
	private void cleanUp(){
		try{
			catIdsToProc.close();
			selectCat.close();
			errCatIdsToProc.close();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
}