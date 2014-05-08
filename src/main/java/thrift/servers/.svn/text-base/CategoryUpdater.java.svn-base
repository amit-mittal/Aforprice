package thrift.servers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import util.ConfigParms;
import util.Constants;
import util.Emailer;
import util.ConfigParms.RUNTIME_MODE;
import db.dao.CategoryDAO;
import db.dao.CategoryFactory;
import db.dao.DAOException;
import entities.Category;

public class CategoryUpdater extends TimerTask{
	private static final Logger logger = Logger.getLogger(CategoryUpdater.class);
	private final CategoryDAO categoryReader;
	private CategoryCacheImpl cache;
	private Timestamp latestModifiedTime;
	private List<Category> emptyCategoryList = new ArrayList<Category>();
	/*
	 * @param cache - reference to the cache to be updated
	 * @latestModifiedTime - starting point in the CATEGORY table
	 */
	public CategoryUpdater(CategoryCacheImpl cache, Timestamp latestModifiedTime 
			) throws DAOException{
		this.cache = cache;
		this.latestModifiedTime = latestModifiedTime;
		categoryReader = CategoryFactory.getInstance().getDAO();
	}
	public void run() {
		try {
			doWork();
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			StringWriter sw = new StringWriter();
			PrintWriter p = new PrintWriter(sw);
			e.printStackTrace(p);
			if(!ConfigParms.isUnitTestMode())
				Emailer.getInstance().sendText("CategoryUpdater Exception restarting Retailer Cache", sw.toString());
			System.exit(Constants.RESTARTABLE_ERROR);
		}		
	}
	
	public void doWorkRegTest() throws SQLException, DAOException{
		doWork();
	}
	private void doWork() throws SQLException, DAOException{
		//category updates
		List<Category> categories = pollDbForUpdates();
		if(categories.size()>0)
			cache.updateCategories(categories);
	}
	private List<Category> pollDbForUpdates() throws SQLException, DAOException{
		Timestamp newModifiedTime = categoryReader.getCategoryMaxModifiedTime();
		if(newModifiedTime.before(latestModifiedTime)){//possible if we are not getting any categories
			logger.info("no new category");
			return emptyCategoryList;
		}
		
		logger.info("Get all categories updated after "+latestModifiedTime);
		List<Category> categories = categoryReader.getUpdatedCategories(this.latestModifiedTime);
		//if new time is same as old time, then add 1 sec so we don't keep getting same products over and over
		if(ConfigParms.MODE != RUNTIME_MODE.UNITTEST && newModifiedTime.equals(latestModifiedTime))//no optimization in test mode, it messes it up 
										//since time_modified field may not be different for two different rows for one product inserted very quickly
			latestModifiedTime = new Timestamp(latestModifiedTime.getTime()+1000);
		else
			latestModifiedTime = newModifiedTime;
		logger.info("Got "+categories.size()+" updated categories");
		return categories;
	}
	
	public void shutdown(){
		this.cancel();
	}
}
	