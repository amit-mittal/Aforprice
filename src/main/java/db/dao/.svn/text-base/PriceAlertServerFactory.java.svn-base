package db.dao;

public class PriceAlertServerFactory {
	private static PriceAlertServerFactory instance = new PriceAlertServerFactory();
	private PriceAlertDAO priceAlertDAO;
	private PriceAlertHistoryDAO priceAlertHistDAO;
	private ProductPricesHistoryDAO productPriceHistDAO;
	private LastProcessedDAO lastProcDAO;
	private UserDAO userDAO;
	private ProductsDAO prodDAO;
	
	public static PriceAlertServerFactory getInstance(){
		return instance;
	}
	
	private PriceAlertServerFactory(){
		priceAlertDAO = new PriceAlertDAO();
		priceAlertHistDAO = new PriceAlertHistoryDAO();
		productPriceHistDAO = new ProductPricesHistoryDAO();
		lastProcDAO = new LastProcessedDAO();
		userDAO = new UserDAO();
		prodDAO = new ProductsDAO();
	}

	public PriceAlertDAO getPriceAlertDAO() {
		return priceAlertDAO;
	}
	
	public PriceAlertHistoryDAO getPriceAlertHistDAO() {
		return priceAlertHistDAO;
	}
	
	public ProductPricesHistoryDAO getProductPriceHistDAO() {
		return productPriceHistDAO;
	}

	public LastProcessedDAO getLastProcDAO() {
		return lastProcDAO;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public ProductsDAO getProdDAO() {
		return prodDAO;
	}

	public void setPriceAlertDAO(PriceAlertDAO priceAlertDAO) {
		this.priceAlertDAO = priceAlertDAO;
	}

	public void setPriceAlertHistDAO(PriceAlertHistoryDAO priceAlertHistDAO) {
		this.priceAlertHistDAO = priceAlertHistDAO;
	}

	public void setProductPriceHistDAO(ProductPricesHistoryDAO productPriceHistDAO) {
		this.productPriceHistDAO = productPriceHistDAO;
	}

	public void setLastProcDAO(LastProcessedDAO lastProcDAO) {
		this.lastProcDAO = lastProcDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setProdDAO(ProductsDAO prodDAO) {
		this.prodDAO = prodDAO;
	}

}
