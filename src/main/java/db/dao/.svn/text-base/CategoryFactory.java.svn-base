package db.dao;

import global.exceptions.Bhagte2BandBajGaya;


public class CategoryFactory {
	private static CategoryFactory instance = new CategoryFactory();
	private CategoryDAO dao;
	
	public static CategoryFactory getInstance(){
		return instance;
	}
	
	private CategoryFactory(){
		try {
			dao = new CategoryDAO();
		} catch (DAOException e) {
			e.printStackTrace();
			throw new Bhagte2BandBajGaya(e.toString());
		}
	}
	public CategoryDAO getDAO(){
		return this.dao;
	}
	public void setCategoryDAOOverride(CategoryDAO override){
		this.dao = override;
	}
}
