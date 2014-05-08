package thrift.servers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import thrift.genereated.retailer.Category;
import uploader.util.CategoryType;
import util.ConfigParms;
import util.ConfigParms.RUNTIME_MODE;
import util.Metric;
import db.dao.CategoryDAO;
import db.dao.CategoryFactory;
import db.dao.DAOException;
import entities.Retailer;
import global.exceptions.Bhagte2BandBajGaya;

/**
 * @author Ashish
 * Test: CategoryCacheImplTest
 */
public class CategoryCacheImpl {
	private static final Logger logger = Logger.getLogger(CategoryCacheImpl.class);
	Map<String, List<Category>> _retailerAllCategoriesMap = new HashMap<String, List<Category>>();
	Map<String, List<Category>> _retailerHomepageCategoriesMap = new HashMap<String, List<Category>>();//contains 1st and 2nd level categories
	Map<Integer, List<Category>> _parentChildCategoriesMap = new HashMap<Integer, List<Category>>();
	Map<String, List<Category>> _retailerRootCategoriesMap = new HashMap<String, List<Category>>();//container 1st level category
	Map<Integer, Category> _categoryIdCategoryMap = new HashMap<Integer, Category>();
	Map<Integer,List<Integer>> _existingCategoryParentMap = new HashMap<Integer, List<Integer>>();
	Map<String, Map<Integer,List<Category>>> _retailerCategoriesByLevelMap = new HashMap<String, Map<Integer,List<Category>>>();
	Map<String,Category> urlToCategoryMap = new HashMap<String,Category>();
	private Set<String> retailerSet = new HashSet<String>(30);
	private Retailer[] retailers;
	private ProductCacheImpl prodCache;
	private CategoryComparator categoryComparator = new CategoryComparator();
	private Metric metCatParentMap = new Metric("metCatParentMap");
	
	public CategoryCacheImpl(){
		retailers = Retailer.values();
		for(Retailer retailer:Retailer.values())
			retailerSet.add(retailer.getId());
		init();
	}
	public CategoryCacheImpl(Retailer[] retailers){
		this.retailers = retailers;
		for(Retailer retailer:retailers)
			retailerSet.add(retailer.getId());
		init();
	}
	public CategoryCacheImpl(List<Retailer> retailers){
		this.retailers = new Retailer[retailers.size()];
		for(int index = 0;index<retailers.size();++index){
			this.retailers[index] = retailers.get(index);
			retailerSet.add(retailers.get(index).getId());
		}
		init();
	}
	public CategoryCacheImpl(ProductCacheImpl prodCache, List<Retailer> retailers){
		this.prodCache = prodCache;
		this.retailers = new Retailer[retailers.size()];
		for(int index = 0;index<retailers.size();++index){
			this.retailers[index] = retailers.get(index);
			retailerSet.add(retailers.get(index).getId());
		}
		init();
	}
	public void init(){
		try {

			CategoryDAO reader = CategoryFactory.getInstance().getDAO();
			logger.info("Loading categories of all retailers");
			
			for(Retailer retailer: retailers){
				logger.info("loading "+retailer.getId());
				List<entities.Category> dbCategories = reader.getActiveCategoriesForRetailer(retailer.getId());
				List<Category> allCategories = new ArrayList<Category>(dbCategories.size());
				List<Category> homepageCategories = new ArrayList<Category>();
				List<Category> rootCategories = new ArrayList<Category>();
				for(entities.Category dbCat:dbCategories){
					Category cat = new Category(dbCat.getCategoryId(),dbCat.getName(), dbCat.getRetailerId(), 
							dbCat.getParentCategoryId(), dbCat.getParentName(), dbCat.getUrl(), dbCat.getType()==CategoryType.PARENT);
					allCategories.add(cat);					
					if(cat.isRootCategory())
						rootCategories.add(cat);					
					List<Category> childCategories = _parentChildCategoriesMap.get(dbCat.getParentCategoryId());					
					if(childCategories==null){
						childCategories = new ArrayList<Category>();
						_parentChildCategoriesMap.put(dbCat.getParentCategoryId(), childCategories);						
					}
					childCategories.add(cat);
					_categoryIdCategoryMap.put(dbCat.getCategoryId(), cat);
					urlToCategoryMap.put(dbCat.getUrl(), cat);
				}
				for(Category root:rootCategories){
					List<Category> childCategories = _parentChildCategoriesMap.get(root.categoryId);
					
					if(childCategories!=null)
						homepageCategories.addAll(childCategories);
				}
				_retailerAllCategoriesMap.put(retailer.getId(), allCategories);
				_retailerHomepageCategoriesMap.put(retailer.getId(), homepageCategories);
				_retailerRootCategoriesMap.put(retailer.getId(), rootCategories);
				populateRetailerCategoriesByLevelMap(retailer, rootCategories);
				logger.info("loading "+retailer.getId()+" done, "+allCategories.size()+" categories");
			}
			sortCategories();
			if(ConfigParms.MODE == RUNTIME_MODE.UNITTEST || ConfigParms.isWindows())
				logger.info("skip populating category->ancestor map on windows or in unit test mode");
			else
				populateCatToAncestorsMap();
			logger.info("Loading categories of all retailers...done");
			//PRICE DROPS
//			initTopDrops();
		} catch (DAOException e) {
			e.printStackTrace();
			throw new Bhagte2BandBajGaya(e.toString());
		}
	}
	
	private void populateRetailerCategoriesByLevelMap(Retailer retailer, List<Category> rootCategories){
		logger.info("loading retailerCategoriesByLevelMap for retailer: "+retailer.getId());
		int level = 0;
		Map<Integer, List<Category>> levelCategoriesMap = new HashMap<Integer, List<Category>>();
		List<Category> levelCategoriesList = new ArrayList<Category>(rootCategories);
		while(levelCategoriesList.size()>0){
			levelCategoriesMap.put(level, new ArrayList<>(levelCategoriesList));
			List<Category> tempList = new ArrayList<Category>();
			for(Category category : levelCategoriesList){
				List<Category> childCategories = _parentChildCategoriesMap.get(category.categoryId);
				if(childCategories!=null)
					tempList.addAll(childCategories);
			}
			levelCategoriesList.clear();
			levelCategoriesList.addAll(tempList);
			level+=1;
		}
		for(List<Category> categoryList : levelCategoriesMap.values()){
			Collections.sort(categoryList, categoryComparator);
		}
		_retailerCategoriesByLevelMap.put(retailer.getId(), levelCategoriesMap);
		logger.info("loading retailerCategoriesByLevelMap for retailer: "+retailer.getId()+"...done");
	}

	private class CategoryComparator implements Comparator<Category>{
		@Override
		public int compare(Category o1, Category o2) {
			return o1.getCategoryName().compareTo(o2.getCategoryName());
		}
	}
	/*
	 * store categories in alphabetical order
	 */
	private void sortCategories(){
		logger.info("sorting categories");
		for(List<Category> categoryList :_retailerHomepageCategoriesMap.values())
			Collections.sort(categoryList, categoryComparator);
		for(List<Category> categoryList :_parentChildCategoriesMap.values())
			Collections.sort(categoryList, categoryComparator);
		logger.info("sorting categories...done");	
	}

	//TODO write its updated tests
	public void updateCategories(List<entities.Category> category){
		for(entities.Category categoryInfo : category){
			if(!retailerSet.contains(categoryInfo.getRetailerId())){
				continue;
			}
			if(categoryInfo.isActive()){
				if(_categoryIdCategoryMap.containsKey(categoryInfo.getCategoryId())){
					this.updateCategory(categoryInfo);
				}
				else{
					this.addCategory(categoryInfo);
				}
				if(!(ConfigParms.MODE == RUNTIME_MODE.UNITTEST || ConfigParms.isWindows())) //skip cat->ancestory map updates
					addCategoryAncestorMap(categoryInfo);
			}
			else{
				this.removeCategory(categoryInfo);
				if(categoryInfo.getType() == CategoryType.TERMINAL && !(ConfigParms.MODE == RUNTIME_MODE.UNITTEST))
					ProductData.getInstance().removeTerminalCategory(categoryInfo.getCategoryId());
			}
		}
	}

	
	public void addCategory(entities.Category newCategory){
		Category cat = new Category(newCategory.getCategoryId(),newCategory.getName(), newCategory.getRetailerId(), 
				newCategory.getParentCategoryId(), newCategory.getParentName(), newCategory.getUrl(), newCategory.getType()==CategoryType.PARENT);
		_categoryIdCategoryMap.put(newCategory.getCategoryId(), cat);

		if(_retailerAllCategoriesMap.containsKey(newCategory.getRetailerId())){
			_retailerAllCategoriesMap.get(newCategory.getRetailerId()).add(cat);
		}
		else{//ashish: little too much to think that we will reach in below 'else', remove it in future
			List<Category> categories = new ArrayList<Category>(1);
			categories.add(cat);
			_retailerAllCategoriesMap.put(newCategory.getRetailerId(), categories);
			if(newCategory.getParentCategoryId()==0){
				_retailerHomepageCategoriesMap.put(newCategory.getRetailerId(), categories);
			}
		}
		List<Category> categoryList;
		if(_parentChildCategoriesMap.containsKey(newCategory.getParentCategoryId()))
			categoryList = _parentChildCategoriesMap.get(newCategory.getParentCategoryId());
		else{
			categoryList = new ArrayList<Category>();
			_parentChildCategoriesMap.put(newCategory.getParentCategoryId(), categoryList);
		}
		categoryList.add(cat);
		Collections.sort(categoryList, categoryComparator);
		
		if(newCategory.isRootCategory()){
			if(_retailerHomepageCategoriesMap.containsKey(newCategory.getRetailerId())){
				_retailerHomepageCategoriesMap.get(newCategory.getRetailerId()).add(cat);
				_retailerRootCategoriesMap.get(newCategory.getRetailerId()).add(cat);
				Collections.sort(_retailerHomepageCategoriesMap.get(newCategory.getRetailerId()), categoryComparator);
			}
			else
				logger.error("we got a root category update for a retailer "+ newCategory.getRetailerId()+" which is not in our cache, weird!!!");
			
		}
	}
	

	/*
	 * update existing category. category's parentcategoryid doesn't change
	 */
	public void updateCategory(entities.Category updatedCategory){
		Category curCategory = _categoryIdCategoryMap.get(updatedCategory.getCategoryId());
		curCategory.setCategoryName(updatedCategory.getName());
		curCategory.setParentCategoryId(updatedCategory.getParentCategoryId());
		curCategory.setParentCategoryName(updatedCategory.getParentName());
		curCategory.setUrl(updatedCategory.getUrl());
		curCategory.setRetailerId(updatedCategory.getRetailerId());
		
		List<Category> categoryList = _parentChildCategoriesMap.get(curCategory.getParentCategoryId());
		if(categoryList!=null)//just to be safe
			Collections.sort(categoryList, categoryComparator);
	}
	
	public void removeCategory(entities.Category deleteCategory){
		Category removedCategory = _categoryIdCategoryMap.remove(deleteCategory.getCategoryId());
		if(removedCategory==null)
			logger.info("removeCategory called for nonexistent categoryId:"+deleteCategory.getCategoryId());
		if(_retailerAllCategoriesMap.get(deleteCategory.getRetailerId())!=null)
			_retailerAllCategoriesMap.get(deleteCategory.getRetailerId()).remove(removedCategory);
		else
			logger.info("removeCategory called for nonexistent retailerId:"+deleteCategory.getRetailerId());
		if(deleteCategory.isRootCategory()){
			if(_retailerHomepageCategoriesMap.get(deleteCategory.getRetailerId())!=null)//sanity check
				_retailerHomepageCategoriesMap.get(deleteCategory.getRetailerId()).remove(removedCategory);
			if(_retailerRootCategoriesMap.get(deleteCategory.getRetailerId())!=null)//sanity check
				_retailerRootCategoriesMap.get(deleteCategory.getRetailerId()).remove(removedCategory);
		}
		if(_parentChildCategoriesMap.get(deleteCategory.getParentCategoryId())!=null)
			_parentChildCategoriesMap.get(deleteCategory.getParentCategoryId()).remove(removedCategory);
		else
			logger.info("removeCategory called for nonexistent parentId:"+deleteCategory.getParentCategoryId());
	}

	/*
	 * populate categorty->ancestor map table to be used by sphinx search
	 */
	private void populateCatToAncestorsMap() throws DAOException{
		logger.info("Getting cateogry to ancestor map from database");
		_existingCategoryParentMap = CategoryFactory.getInstance().getDAO().getCategoryParentMapping();
		Iterator<Category> catIter = _categoryIdCategoryMap.values().iterator();
		int count=0;
		while(catIter.hasNext()){
			Category cat = catIter.next();
			addCategoryAncestorMap(cat);
			if(count++%1000==0)
				logger.info(metCatParentMap.currentStats());
		}
	}
	
	private void addCategoryAncestorMap(entities.Category cat){
		try {
			addCategoryAncestorMap(_categoryIdCategoryMap.get(cat.getCategoryId()));
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * returns true if catid->parentid already exists in the database
	 */
	private boolean existsCatParent(Integer categoryId, Integer parentId){
		if(_existingCategoryParentMap.containsKey(categoryId) && 
				_existingCategoryParentMap.get(categoryId).contains(parentId))
			return true;
		return false;
	}
	
	/*
	 * add this category->category, category->all_ancestors mapping in database
	 */
	private void addCategoryAncestorMap(Category cat)
			throws DAOException {
		List<Integer> categoryIds = new ArrayList<Integer>();
		List<Integer> parentIds = new ArrayList<Integer>();
		//insert self mapping
		if(!existsCatParent(cat.getCategoryId(), cat.getCategoryId())){
			categoryIds.add(cat.getCategoryId());
			parentIds.add(cat.getCategoryId());
		}
		Integer origCatId = cat.getCategoryId();
		//get all its ancestors
		int count=0;
		while(cat.getParentCategoryId()!=0){
			//insert ancestor
			if(!existsCatParent(origCatId, cat.getParentCategoryId())){
				categoryIds.add(origCatId);
				parentIds.add(cat.getParentCategoryId());
			}
			cat = _categoryIdCategoryMap.get(cat.getParentCategoryId());
			if (count++ == 10){
				logger.info("potential circular link for categoryid "+cat.getCategoryId());
				break;
			}
			if(cat==null)//sanity check
				break;
		}//while(cat.getParentCategoryId()!=0){ ends...
		if(categoryIds.size()>0){
			metCatParentMap.start();
			CategoryFactory.getInstance().getDAO().insertCategoryParentMapping(categoryIds, parentIds);
			metCatParentMap.end();
		}
	}
	
	public Map<String, List<Category>> getRetailerAllCategoriesMap() {
		return _retailerAllCategoriesMap;
	}

	public Map<Integer, Category> getCategoryIdCategoryMap() {
		return _categoryIdCategoryMap;
	}

	public Map<String, List<Category>> getRetailerHomepageCategoriesMap() {
		return _retailerHomepageCategoriesMap;
	}

	public Map<Integer, List<Category>> getParentChildCategoriesMap() {
		return _parentChildCategoriesMap;
	}
	
	public ProductCacheImpl getProductCache() {
		return prodCache;
	}
	
	public void setProductCache(ProductCacheImpl prodCache) {
		this.prodCache = prodCache;
	}
	
	public Category getCategoryFromId(Integer categoryId) {
		return this._categoryIdCategoryMap.get(categoryId);
	}
	
	public Category getCategoryFromUrl(String url){
		Category cat = urlToCategoryMap.get(url);
		if(cat==null)
			logger.warn("Could not find Category for url "+ url);
		return cat;
	}
	//thrift api methods starts here
	
	public Map<Integer, List<Category>> getAllCategoriesByLevelForRetailer(String retailerId, List<Integer> levels){
		logger.info("getAllCategoriesByLevelForRetailer("+retailerId+") called, number of levels: "+levels.size());
		Map<Integer, List<Category>> result = new HashMap<Integer, List<Category>>();
		Map<Integer, List<Category>> levelCategoriesMap = _retailerCategoriesByLevelMap.get(retailerId.toLowerCase());
		if(levelCategoriesMap!=null){
			for(int level : levels){
				List<Category> tempList = levelCategoriesMap.get(level);
				if(tempList!=null)
					result.put(level, tempList);
				else
					logger.error("0 categories for the level: "+level);
			}
		}
		else
			logger.error("No entry for the retailer: "+retailerId);
		return result;
	}
	
	public List<Category> getHomePageCategories(String retailerId) {
		logger.info("getHomePageCategories("+retailerId+") called");
		retailerId = retailerId!=null?retailerId.toLowerCase():"";
		List<Category> result = _retailerHomepageCategoriesMap.get(retailerId.toLowerCase());
		if(result!=null){
			logger.info("returning "+result.size()+" categories");
			return result;
		} else{
			logger.error("0 homepage categories for the retailer "+retailerId);
			return new ArrayList<Category>();
		}
	}
	
	public List<Category> getChildCategories(int categoryId) {
		logger.info("getChildCategories("+categoryId+") called");
		List<Category> result = _parentChildCategoriesMap.get(categoryId);
		if(result!=null){
			logger.info("returning "+result.size()+" categories");
			return result;
		} else {
			logger.error("0 child categories of "+categoryId);
			return new ArrayList<Category>();
		}
	}
	
	/**
	 * @param retailer
	 * @return
	 */
	public List<Category> getRootCategories(String retailer){
		return _retailerRootCategoriesMap.get(retailer);
	}
	/*
	 * (non-Javadoc)
	 * @see thrift.genereated.retailer.CategoryCache.Iface#getCategoryPath(int)
	 * return category path from root to this category
	 */	
	public List<Category> getCategoryPath(int categoryId) {
		logger.info("getCategoryPath("+categoryId+") called");
		List<Category> path = new LinkedList<Category>();
		Category cat = _categoryIdCategoryMap.get(categoryId);
		if(cat==null){
			logger.error("Invalid categoryid "+categoryId);
			return path;
		}
		path.add(cat);
		while(cat.getParentCategoryId()!=0){
			Category parent = _categoryIdCategoryMap.get(cat.getParentCategoryId());
			if(parent==null){
				logger.error("category "+categoryId+" has parent categoryid "+cat.getParentCategoryId()+", but missing in cache");
				break;
			}
			//detect circular link
			if(parent.getParentCategoryId()==cat.getCategoryId()){
				logger.info("detected circular link for categoryid "+cat.getCategoryId());
				break;
			}
			path.add(parent);
			cat=parent;
		}
//		logger.info(path.get(0));
		return path;
	}
}
