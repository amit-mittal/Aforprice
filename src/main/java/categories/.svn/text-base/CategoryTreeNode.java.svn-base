package categories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import util.Utils;
import datastruct.ITreeNode;
import db.dao.CategoryDAO;
import db.dao.DAOException;
import entities.Category;

public class CategoryTreeNode implements ITreeNode<Category> {
	private static final Logger LOGGER = Logger.getLogger(CategoryTreeNode.class);
	private CategoryDAO catDAO;
	private List<ITreeNode<Category>> children = new ArrayList<>();
	private ITreeNode<Category> parent;
	private Category data;
	private int level = 1;
	public static HashMap<Integer, Integer> terminalCategoryMap = new HashMap<Integer, Integer>();
	
	public CategoryTreeNode(Category data, ITreeNode<Category> root, int level, int topCategoryId) {
		LOGGER.info(Utils.getTabs(level) + data.getName());
		try {
			this.data = data;
			this.catDAO = new CategoryDAO();
			List<Category> catChildren = catDAO.getChildCategories(data.getCategoryId());
			for(Category catChild: catChildren){
				children.add(new CategoryTreeNode(catChild, this, level + 1, topCategoryId));
			}
			this.parent = root;
			this.level = level;
			if(catChildren == null || catChildren.size() == 0)
				terminalCategoryMap.put(Integer.valueOf(data.getCategoryId()), topCategoryId);
			
			
		} catch (DAOException e) {
		}
	}


	
	public CategoryTreeNode(String retailer, String topLevelCategory) {
		LOGGER.info(retailer);
		try {
			this.catDAO = new CategoryDAO();
			List<Category> catChildren = catDAO.getChildCategories(retailer);
			for(Category catChild: catChildren){
				if( topLevelCategory != null )
				{
					if( catChild.getName().equalsIgnoreCase(topLevelCategory) ) 
						children.add(new CategoryTreeNode(catChild, this, 1, catChild.getCategoryId()));//catChild.getTimeModified().before(new Date())
				}
				else
						children.add(new CategoryTreeNode(catChild, this, 1, catChild.getCategoryId()));//catChild.getTimeModified().before(new Date())
					
			}	
			this.parent = null;
			this.data = null;
			this.level = 0;
		} catch (DAOException e) {
		}
	}
	
	public CategoryTreeNode(int id){
		LOGGER.info(id);
		try {
			this.catDAO = new CategoryDAO();
			List<Category> catChildren = catDAO.getChildCategories(id);
			for(Category catChild: catChildren){
				children.add(new CategoryTreeNode(catChild, this, 1, catChild.getCategoryId()));
			}
			this.parent = null;
			this.data = null;
		} catch (DAOException e) {
		}
	}
	public CategoryTreeNode(String retailer)
	{
		this(retailer, null);
	}

	/*
	 * Map constructed will be of type
	 * 1. Integer - categoryId: just below the retailer
	 * 2. Integer - categoryId: 1 level down child categories
	 * 3. list<Integer> - categoryIds: all the terminal category ids
	 */
	//TODO not tested
	public void constructMap(Map<Integer, Map<Integer, List<Integer>>> categoryTreeMap){
		if(this.getParent()!=null)
			return;
		List<ITreeNode<Category>> homepageCategories = this.getChildren();
		for(ITreeNode<Category> homepageCategory : homepageCategories){
			List<ITreeNode<Category>> list = new ArrayList<>();
			list.add(homepageCategory);
			recurMap(list, 0, categoryTreeMap);
		}
	}
	
	//TODO not tested
	public void recurMap(List<ITreeNode<Category>> list, int level, Map<Integer, Map<Integer, List<Integer>>> categoryTreeMap){
		if(list.get(level).isTerminal()){
			int terminalCategoryId = list.get(level).getData().getCategoryId();
			for(int index = 0 ; index<level ; ++index){
				if(categoryTreeMap.containsKey(list.get(index).getData().getCategoryId())){
					Map<Integer, List<Integer>> childrenMap = categoryTreeMap.get(list.get(index).getData().getCategoryId());
					if(childrenMap.containsKey(list.get(index+1).getData().getCategoryId())){
						childrenMap.get(list.get(index+1).getData().getCategoryId()).add(terminalCategoryId);
					} else{
						List<Integer> terminalList = new ArrayList<>(1);
						terminalList.add(terminalCategoryId);
						childrenMap.put(list.get(index+1).getData().getCategoryId(), terminalList);
					}
				} else{
					Map<Integer, List<Integer>> childrenMap = new HashMap<>();
					categoryTreeMap.put(list.get(index).getData().getCategoryId(), childrenMap);
					List<Integer> terminalList = new ArrayList<>(1);
					terminalList.add(terminalCategoryId);
					childrenMap.put(list.get(index+1).getData().getCategoryId(), terminalList);
				}
			}
			//adding terminal category into map
			Map<Integer, List<Integer>> childrenMap = new HashMap<>();
			List<Integer> terminalList = new ArrayList<>(1);
			terminalList.add(terminalCategoryId);
			childrenMap.put(list.get(level).getData().getCategoryId(), terminalList);
			categoryTreeMap.put(list.get(level).getData().getCategoryId(), childrenMap);
			return;
		}
		List<ITreeNode<Category>> childrenCategories = this.getChildren();
		for(ITreeNode<Category> childCategory : childrenCategories){
			list.add(childCategory);
			recurMap(list, level+1, categoryTreeMap);
			list.remove(childCategory);
		}
	}

	@Override
	public ITreeNode<Category> getParent() {
		return parent;
	}

	@Override
	public List<ITreeNode<Category>> getChildren() {
		return children; 
	}

	@Override
	public boolean isTerminal() {
		return children.size() == 0;
	}
	
	@Override
	public Category getData(){
		return data;
	}
	
	@Override
	public int getLevel(){
		return level;
	}
	
	public ITreeNode<Category> getParentNoeAtLevel( CategoryTreeNode categoryTreeNode, int level )
	{
		while( categoryTreeNode.getParent() != null )
		{
			if( categoryTreeNode.getParent().getLevel() == level)
				return categoryTreeNode.getParent();
			categoryTreeNode = (CategoryTreeNode) categoryTreeNode.getParent();
		}
		return categoryTreeNode;
	}
	
	public static void main(String[] args){
		args = new String[]{"toysrus", "Clearance" };
		String retailerId = args[0];
		String topLevelCategory = args[1];
		CategoryTreeNode root = new CategoryTreeNode(retailerId, null);
		for( Integer catId : CategoryTreeNode.terminalCategoryMap.keySet() )
		{ 

			System.out.println(catId);
		}
	}
}