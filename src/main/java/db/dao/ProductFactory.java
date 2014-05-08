package db.dao;

public class ProductFactory {
	private static ProductFactory instance = new ProductFactory();
	private ProductsDAO prodDAO;
	
	public static ProductFactory getInstance(){
		return instance;
	}
	
	private ProductFactory(){
		prodDAO = new ProductsDAO();
	}
	public ProductsDAO getProductsDAO(){
		return this.prodDAO;
	}
	public void setProductDAOOverride(ProductsDAO override){
		this.prodDAO = override;
	}
}
