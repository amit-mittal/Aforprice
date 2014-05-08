package categories.checks;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import util.Constants;
import util.EmailAlerts;
import util.MutableInt;
import util.RECON_FIELDS;
import categories.CategoryTreeNode;
import datastruct.ITreeNode;
import db.dao.CategoryDAO;
import db.dao.DAOException;
import db.dao.ReconilationDAO;
import entities.Category;
import entities.Retailer;

/**
 * @author Anurag
 * This class is responsible for marking the categories which have not been updated since 
 * the <tt>category filter date</tt> as inactive. Following sanity checks are done before marking
 * any category inactive<br>.
 * 1. If a category is a parent category and is stale, and some of its children are not stale, do not mark the parent category inactive.<br>
 * 2. If all children categories of a parent category are stale, mark the parent category inactive<br>
 * This class also generates alert for the following 
 * 1. If a category is a parent category and is stale, and some of its children are not stale
 * 2. If all children categories of a parent category are stale and the parent category is not stale.
 * 3. If more than <tt>threshold %</tt> of categories are marked stale.
 */
public class CategoriesReconciler {
	private static final Logger LOGGER = Logger.getLogger(CategoriesReconciler.class);
	private CategoryDAO dao = null;
	
	public CategoriesReconciler() {
		try {
			dao = new CategoryDAO();
		} catch (DAOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	public void start(){
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Retailer[] retailers = Retailer.values();
		Map<String, TreeMap<RECON_FIELDS, MutableInt>> result = new TreeMap<String, TreeMap<RECON_FIELDS, MutableInt>>();
		for(Retailer r: retailers){
			if(!r.isActive())
				continue;
			result.put(r.getId(), reconcile(r.getId()));
		}		
		for(Map.Entry<String, TreeMap<RECON_FIELDS, MutableInt>> entry: result.entrySet()){
			for(Map.Entry<RECON_FIELDS, MutableInt> subEntry: entry.getValue().entrySet()){
				try{
					ReconilationDAO.getInstance().storeRec(entry.getKey(), ts, ReconilationDAO.NAME.CATEGORY, subEntry.getKey().getType(), "", subEntry.getValue().getValue(), ts);
				} catch (DAOException e) {
					LOGGER.error("Error saving reconciliation data", e);
				}
			}
		}

		EmailAlerts.categoryReconAlert(result);
	}
	
	public TreeMap<RECON_FIELDS, MutableInt> reconcile(String r){
		TreeMap<RECON_FIELDS, MutableInt> counts = new TreeMap<>(); //<Field, Count for Field>
		 //Initialize counts for each field. The map for each field will be updated with the count of that field for each retailer
		for(RECON_FIELDS t: RECON_FIELDS.categoryFields()){
			counts.put(t, new MutableInt(0));
		}
		process(new CategoryTreeNode(r, null), counts);
		return counts;
	}
	
	private void process(ITreeNode<Category> node, Map<RECON_FIELDS, MutableInt> counts){		
		if(node == null)
			return;
		Category cat = node.getData();
		List<ITreeNode<Category>> children = node.getChildren();
		if(cat != null){
			boolean isStale = false;
			boolean allChildrenStale = true;
			if(cat.getTimeModified().before(Constants.FILTER_DATE.category(cat.getRetailerId()))){
				isStale = true;
			}			
			for(ITreeNode<Category> child: children){
				if(!child.getData().getTimeModified().before(Constants.FILTER_DATE.category(cat.getRetailerId()))){
					allChildrenStale = false;
					break;
				}
			}
			boolean isActive = true;
			if(node.isTerminal()){
				if(isStale){
					isActive = false;
					counts.get(RECON_FIELDS.CATSTALE).add(1);
					LOGGER.warn("stale:" + logInfo(cat));
				}
			}
			else{
				if(isStale && allChildrenStale){
					isActive = false;
					counts.get(RECON_FIELDS.CATSTALE).add(1);
					LOGGER.warn("stale:" + logInfo(cat));
				}
				else if(isStale && !allChildrenStale){
					counts.get(RECON_FIELDS.CATSTALE_CHILDRENNOTSTALE).add(1);
					LOGGER.warn("stale but children not stale:" + logInfo(cat));
				}
				else if(!isStale && allChildrenStale){
					counts.get(RECON_FIELDS.CATNOTSTALE_ALLCHILDRENSTALE).add(1);
					LOGGER.warn("not stale but all children stale:" + logInfo(cat));
				}
			}
			if(isActive){
				counts.get(RECON_FIELDS.ACTIVECATSCOUNT).add(1);
			}
			else
				setInactive(cat);
		}
		for(ITreeNode<Category> child: children){
			process(child, counts);
		}
	}
	
	private String logInfo(Category cat){
		return cat.getRetailerId() + ":" + cat.getCategoryId(); 
	}
	
	private void setInactive(Category cat){
		LOGGER.info("setInactive:" + cat.getCategoryId());		
		dao.setInactive(cat.getCategoryId());
	}
	
	public static void main(String[] args){
		new CategoriesReconciler().start();
	}
}