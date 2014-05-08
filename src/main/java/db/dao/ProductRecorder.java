package db.dao;


/**
 * @author Anurag

*/
public class ProductRecorder {

//	private static final Logger logger= Logger.getLogger(ProductRecorder.class);
//	
//	private static ProductRecorder instance = new ProductRecorder();
//	
//	private final Connection _connection;	
//	private final PreparedStatement insertProductStmt;
//	private final PreparedStatement insertProdHistStmt;
//	private final PreparedStatement prodsByCatStmt;
//	private final PreparedStatement prodStmt;
//	private final PreparedStatement prodsCountByCatStmt;
//	private final PreparedStatement updateProdStmt;	
//	private final PreparedStatement lastPriceStmt;
//
//	private final PreparedStatement insertProductErrStmt;
//	private final PreparedStatement insertProdHistErrStmt;
//
//	private final UniqIdGenerator idGen;
//	//Map of <retailer, <category id, <product name, ProdInfo>>>
//	//A category can have only one product with one name
//	private final Map<String, HashMap<Integer, HashMap<String, ProductInfo>>> retailerCatProds = 
//											new HashMap<String, HashMap<Integer, HashMap<String, ProductInfo>>>();
//
//	private ProductRecorder(){
//		_connection = DbConnectionPool.get().getConnection().getConnection();
//		try {			
//			insertProductStmt = _connection.prepareStatement(Queries.INSERT_PRODUCT);
//			insertProdHistStmt = _connection.prepareStatement(Queries.INSERT_PRODUCT_HIST);
//			prodsByCatStmt = _connection.prepareStatement(Queries.ALL_PRODS_BY_CATEGORY);
//			prodStmt = _connection.prepareStatement(Queries.PRODUCT_GET);
//			prodsCountByCatStmt = _connection.prepareStatement(Queries.PRODS_COUNT_BY_CATEGORY);
//			updateProdStmt = _connection.prepareStatement(Queries.UPDATE_PRODUCT);			
//			lastPriceStmt = _connection.prepareStatement(Queries.LAST_PRICE);			
//			
//			insertProductErrStmt = _connection.prepareStatement(Queries.INSERT_PRODUCT_ERR);
//			insertProdHistErrStmt = _connection.prepareStatement(Queries.INSERT_PRODUCT_HIST_ERR);
//			
//			idGen = UniqIdGenerator.get();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new Bhagte2BandBajGaya(e.toString(), e);
//		}
//	}
//	
//	public synchronized static ProductRecorder get(){
//		if(!instance.isOpen()){
//			instance.close(); //In case there is one or more statements open
//			instance = new ProductRecorder();
//		}
//		return instance;
//	}
//	
//	private void close(){
//		try {
//			insertProductStmt.close();
//			insertProdHistStmt.close();
//			prodsByCatStmt.close();
//			prodStmt.close();
//			prodsCountByCatStmt.close();
//			updateProdStmt.close();
//			lastPriceStmt.close();
//			insertProductErrStmt.close();
//			insertProdHistErrStmt.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	private boolean isOpen(){
//		boolean open = false;
//		try{
//			open = !insertProductStmt.isClosed() &&
//				   !insertProdHistStmt.isClosed() &&
//				   !prodsByCatStmt.isClosed() &&
//				   !prodStmt.isClosed() &&
//				   !prodsCountByCatStmt.isClosed() &&
//				   !updateProdStmt.isClosed() &&
//				   !lastPriceStmt.isClosed()&&
//				   !insertProductErrStmt.isClosed()&&
//				   !insertProdHistErrStmt.isClosed();
//		}catch(Exception e){
//			close();
//		}
//		return open;
//	}
//	
//	/**
//	 * Fetch all products for this category for this retailer and store it in retailerCatProds. If retailerCatProds
//	 * contains products from different category for this retailer, then those products are substituted with the products
//	 * of this categoryId. This is done to make sure that we do end up caching too many products.
//	 * @param retailerId
//	 * @param categoryId
//	 * @throws SQLException
//	 */
//	private void fetchProducts(String retailerId, int categoryId) throws SQLException{
//		HashMap<Integer, HashMap<String, ProductInfo>> catProdsMap = retailerCatProds.get(retailerId);
//		if(catProdsMap == null){
//			catProdsMap = new HashMap<Integer, HashMap<String, ProductInfo>>();
//			retailerCatProds.put(retailerId, catProdsMap);
//		}
//		if(catProdsMap.containsKey(categoryId) || !Constants.CACHE_PRODS_PROD_STORE){
//			return;
//		}
//		//At a time, only have products of one category for a retailer, as for one retailer
//		//we will process products of a category first, before moving on to the next category
//		catProdsMap.remove(categoryId);
//		HashMap<String, ProductInfo> productInfos = new HashMap<String, ProductInfo>();
//		prodsCountByCatStmt.setInt(1, categoryId);
//		prodsCountByCatStmt.setString(2, retailerId);
//		ResultSet prodCountRS = prodsCountByCatStmt.executeQuery();
//		if(!prodCountRS.next())
//			throw new Bhagte2BandBajGaya();
//		int count = prodCountRS.getInt("COUNT");
//		if(count > Constants.MAX_PRODUCTS_TO_CACHE){
//			return;
//		}
//		prodsByCatStmt.setInt(1, categoryId);
//		prodsByCatStmt.setString(2, retailerId);
//		ResultSet results = prodsByCatStmt.executeQuery();
//		
//		while(results.next()){
//			int prodId = results.getInt(ProductSummary.Columns.PRODUCT_ID);
//			String prodName = results.getString(ProductSummary.Columns.PRODUCT_NAME);			
//			Timestamp timMod = results.getTimestamp(ProductSummary.Columns.TIME_MODIFIED);
//			ProductInfo prodInfo = new ProductInfo(prodId, prodName, timMod);
//			productInfos.put(prodName, prodInfo);						
//		}
//		catProdsMap.put(categoryId, productInfos);
//	}
//	
//	private ProductInfo getProduct(String retailerId, int categoryId, String prodName) throws SQLException{		
//		prodStmt.setInt(1, categoryId);
//		prodStmt.setString(2, retailerId);
//		prodStmt.setString(3, prodName);		
//		ResultSet results = prodStmt.executeQuery();
//		if(results.next()){
//			int prodId = results.getInt(ProductSummary.Columns.PRODUCT_ID);						
//			Timestamp timMod = results.getTimestamp(ProductSummary.Columns.TIME_MODIFIED);
//			ProductInfo prodInfo = new ProductInfo(prodId, prodName, timMod);
//			return prodInfo;
//						
//		}
//		return null;
//
//	}
//	
//	/**
//	 * @param retailerId
//	 * @param categoryId
//	 * @param productName
//	 * @param url
//	 * @return
//	 * @throws SQLException 
//	 */
//	private ProductInfo getFromCache(String retailerId, int categoryId, String productName) throws SQLException{
//		HashMap<String, ProductInfo> catProds = retailerCatProds.get(retailerId).get(categoryId);
//		if(catProds == null || catProds.size() == 0){
//			//The cache was not populated, so we need to get the product info from the database.
//			return getProduct(retailerId, categoryId, productName);
//		}
//		return catProds.get(productName);				
//	}
//	
//	private void addToCache(String retailerId, int categoryId, int prodId, String productName, Timestamp ts){
//		HashMap<String, ProductInfo> catProds = retailerCatProds.get(retailerId).get(categoryId);
//		if(catProds == null || catProds.size() == 0){
//			return; //Do not add to cache if the cache was not populated to start with.
//		}
//		ProductInfo prodInfo = catProds.get(productName);		
//		if(prodInfo == null){			
//			ProductInfo p = new ProductInfo(prodId, productName, ts);
//			catProds.put(productName, p);			
//		}		
//	}
//	
//	/**
//	 * @param category
//	 * @return
//	 * @throws DAOException
//	 */
//	public List<ProductSummary> recordProducts(List<ProductSummary> products){
//		int i;
//		boolean[] prodInserted = new boolean[products.size()];
//		int numProdInserted = 0;
//		boolean[] prodHistInserted = new boolean[products.size()];;
//		int numProdHistInserted = 0;
//		boolean[] prodUpdated = new boolean[products.size()];;
//		int numProdUpdated = 0;
//		
//		List<ProductSummary> errProds = new ArrayList<ProductSummary>();
//
//		try{			
//			for(int idx = 0; idx < products.size(); idx++){
//				ProductSummary product = products.get(idx);
//				boolean newProduct = true;
//				double lastPrice = Double.NaN;
//				int prodId = -1;
//				fetchProducts(product.getRetailerId(), product.getCategoryId());
//				ProductInfo p = getFromCache(product.getRetailerId(), product.getCategoryId(), product.getName());
//				if(p != null){					
//					newProduct = false;
//					product.setId(p.prodId);
//					//UPDATE PRODUCT set MODEL_NO=?, DESCRIPTION=?, IMAGE_URL=?, TIME_MODIFIED=? WHERE PRODUCT_ID=?  
//					//TODO: Create a trigger to automatically back up the previous version of the product if it 
//					//is different from current version
//					//TODO: It should not be updated if has been updated/inserted within last configured time(currently set at 6 hour)
//					if(Math.abs(p.timeMod.getTime() - product.getCreationTime().getTime()) > TimeUnit.MILLISECONDS.convert(6L, TimeUnit.HOURS)){
//						prodUpdated[ idx] = true;
//						numProdUpdated++;
//						i = 1;						
//						updateProdStmt.setString(i++, product.getModel() == null? "": product.getModel());
//						updateProdStmt.setString(i++, product.getDesc() ==null?"":product.getDesc() );//DESCRIPTION
//						updateProdStmt.setString(i++, product.getImageUrl());//IMAGE_URL					
//						updateProdStmt.setTimestamp(i++, product.getCreationTime());//TIME_MODIFIED
//						updateProdStmt.setInt(i++, product.getId());//PRODUCT_NAME
//						updateProdStmt.addBatch();
//					}
//					/*SELECT PRICE FROM PRODUCTS.PRODUCT_PRICE_HISTORY " +
//							  WHERE PRODUCT_ID =? AND
//							  TIME = (SELECT MAX(TIME) FROM PRODUCTS.PRODUCT_PRICE_HISTORY WHERE PRODUCT_ID=?)*/
//					i = 1;
//					lastPriceStmt.setInt(i++, p.prodId);//PRODUCT_ID
//					lastPriceStmt.setInt(i++, p.prodId);//PRODUCT_ID
//					ResultSet result = lastPriceStmt.executeQuery();
//					if(result.next()){
//						lastPrice = result.getDouble(1);
//					}
//				}
//				else{
//					prodInserted[idx] = true;
//					numProdInserted++;
//					/*INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, CATEGORY_ID, RETAILER_ID, MODEL_NO, DESCRIPTION, IMAGE_URL, START_DATE, URL, ACTIVE, TIME_MODIFIED)
//					  VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )*/
//					prodId = idGen.getNextProdId();
//					product.setId(prodId);
//					setInsertProdStmt(product, insertProductStmt, true);
//					addToCache(product.getRetailerId(), product.getCategoryId(), prodId, product.getName(), product.getCreationTime());			
//				}
//				//If it is a new product, or the price has changed
//				if(!Double.isNaN(lastPrice) && Math.abs(lastPrice - product.getPrice()) > Constants.MIN_PRICE_DELTA || newProduct){
//					prodHistInserted[idx] = true;
//					numProdHistInserted++;
//					setInsertProdHistStmt(product, insertProdHistStmt, true);
//				}				
//			}		
//			
//			if(numProdInserted > 0){
//				try{
//					insertProductStmt.executeBatch();
//				}catch(BatchUpdateException be){
//					logger.error(be.getMessage(), be);
//					errProds.addAll(recordProdInsertErr(products, be.getUpdateCounts(), prodInserted));
//				}
//			}
//			if(numProdHistInserted > 0){
//				try{
//					insertProdHistStmt.executeBatch();
//				}catch(BatchUpdateException be){
//					logger.error(be.getMessage(), be);
//					errProds.addAll(recordProdHistErr(products, be.getUpdateCounts(), prodHistInserted));
//				}
//			}
//			if(numProdUpdated > 0){
//				try{
//					updateProdStmt.executeBatch();
//				}catch(BatchUpdateException be){
//					logger.error(be.getMessage(), be);
//					errProds.addAll(getProdsInErr(products, be.getUpdateCounts(), prodUpdated));
//				}
//			}
//		}
//		catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return products;
//		}	
//		return errProds;
//	}
//	
//	
//	private void setInsertProdStmt(ProductSummary product,								  								  
//								   PreparedStatement insertProductStmt,								   
//								   boolean addToBatch) throws SQLException{
//		int i = 1;		
//		insertProductStmt.setInt(i++, product.getId());
//		insertProductStmt.setString(i++, product.getName());//PRODUCT_NAME
//		insertProductStmt.setInt(i++, product.getCategoryId());//CATEGORY_ID
//		insertProductStmt.setString(i++, "");//GENERIC_CATEGORY_ID
//		insertProductStmt.setString(i++, product.getRetailerId());//RETAILER_ID
//		insertProductStmt.setString(i++, product.getModel() ==null?"":product.getModel() );//MODEL
//		insertProductStmt.setString(i++, product.getDesc() ==null?"":product.getDesc() );//DESCRIPTION
//		insertProductStmt.setString(i++, product.getImageUrl());//IMAGE_URL
//		insertProductStmt.setTimestamp(i++, product.getCreationTime());//START_DATE
//		insertProductStmt.setString(i++, product.getUrl());//URL
//		insertProductStmt.setBoolean(i++, true);//ACTIVE
//		insertProductStmt.setTimestamp(i++, product.getCreationTime());//TIME_MODIFIED
//		if(addToBatch)
//			insertProductStmt.addBatch();
//		
//	}
//	
//	private void setInsertProdHistStmt(ProductSummary product, PreparedStatement insertProdHistStmt, boolean addToBatch) throws SQLException{
//		int i = 1;	
//		insertProdHistStmt.setInt(i++, product.getId());//PRODUCT_ID
//		//TODO: TIME SHOULD COME FROM THE TIME WHEN THE PRODUCT WAS PARSED
//		insertProdHistStmt.setTimestamp(i++, product.getCreationTime());//TIME
//		insertProdHistStmt.setDouble(i++, product.getPrice());//PRICE
//		insertProdHistStmt.setString(i++, product.getRetailerId());//RETAILER_ID					
//		insertProdHistStmt.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));//TIME
//		if(addToBatch)
//			insertProdHistStmt.addBatch();
//		
//	}
//	
//	private List<ProductSummary> getProdsInErr(List<ProductSummary> products, int[] retCodes, boolean[] updates){
//		List<ProductSummary> errProds = new ArrayList<ProductSummary>();
//		int j = 0;
//		for(boolean updated: updates){
//			if(!updated)
//				continue;
//			if(retCodes.length >= j + 1){
//				if(retCodes[j] == Statement.EXECUTE_FAILED){
//					errProds.add(products.get(j));
//				}
//				j++;
//				continue;
//			}
//			errProds.add(products.get(j));			
//		}
//		return errProds;
//	}
//	
//	private List<ProductSummary> recordProdHistErr(List<ProductSummary> products, int[] retCodes, boolean[] updates){
//		List<ProductSummary> errProds = getProdsInErr(products, retCodes, updates);
//		List<ProductSummary> unrecorded = new ArrayList<ProductSummary>();
//		for(ProductSummary prod: errProds){
//			try{
//				setInsertProdHistStmt(prod, insertProdHistErrStmt, false);
//				insertProdHistErrStmt.executeUpdate();
//			}catch(SQLException sqe){
//				logger.error(sqe.getMessage(), sqe);
//				unrecorded.add(prod);
//			}
//		}
//		return unrecorded;
//	}
//
//	private List<ProductSummary> recordProdInsertErr(List<ProductSummary> products, int[] retCodes, boolean[] updates){
//		List<ProductSummary> errProds = getProdsInErr(products, retCodes, updates);
//		List<ProductSummary> unrecorded = new ArrayList<ProductSummary>();
//		for(ProductSummary prod: errProds){
//			try{
//				setInsertProdStmt(prod, insertProductErrStmt, false);
//				insertProductErrStmt.executeUpdate();
//			}catch(SQLException sqe){
//				logger.error(sqe.getMessage(), sqe);
//				unrecorded.add(prod);
//			}
//		}
//		return unrecorded;
//	}
//	
//	private class ProductInfo{
//		final int prodId;		
//		final String prodName;		
//		final Timestamp timeMod;
//		ProductInfo(int prodId, String prodName, Timestamp timeMod){
//			this.prodId = prodId;			
//			this.prodName = prodName;
//			this.timeMod = timeMod;
//		}
//		@Override
//		public boolean equals(Object that){
//			if(that == this)
//				return true;
//			if(that instanceof ProductInfo){
//				if(((ProductInfo) that).prodName.equals(this.prodName)){
//					return true;
//				}
//			}
//			return false;
//		}
//		
//		@Override
//		public int hashCode(){
//			return prodName.hashCode();
//		}
//
//	}
}