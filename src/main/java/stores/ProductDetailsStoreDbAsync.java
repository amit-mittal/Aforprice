package stores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import util.Constants;
import util.DateTimeUtils;
import db.dao.ProductsDAO;
import entities.ProductSummary;
import global.exceptions.Bhagte2BandBajGaya;

public class ProductDetailsStoreDbAsync implements ProductDetailsStore {

	private static Logger logger = Logger.getLogger(ProductDetailsStoreDbAsync.class);
	
	private final ProductsDAO recorder;
	private ThreadPoolExecutor dbService;
	
	private final FileStore fileStore;
	
	private static ProductStore instance = new ProductDetailsStoreDbAsync();
	private boolean isClosed = false;
	
	private ProductDetailsStoreDbAsync(){
		this.recorder = new ProductsDAO();
		this.dbService = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
		this.fileStore = new FileStore(Constants.PRODUCT_FILES.PRODUCT_DIR_PATH(), Constants.PRODUCTS_SEP);;
	}
	
	public static ProductStore get(){
		return instance;
	}
	
	@Override
	public boolean save(List<ProductSummary> products) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean save(List<ProductSummary> products, Map<ProductSummary, ProductSummary> existingProds) {
		if(isClosed)
			throw new Bhagte2BandBajGaya("ProductStore has been closed");
		dbService.execute(new UploadHandler(products, existingProds));
		return true;
	}
	
	@Override
	public boolean save(ProductSummary product, ProductSummary existingProd) {
		if(isClosed)
			throw new Bhagte2BandBajGaya("ProductStore has been closed");
		List<ProductSummary> products = new LinkedList<>();
		products.add(product);
		Map<ProductSummary, ProductSummary> existingProds = new HashMap<>();
		existingProds.put( product, existingProd);
		save(products,existingProds );
		return true;
	}	
	

	@Override
	public boolean save(ProductSummary product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		if(fileStore != null)
			fileStore.closeAll();
		isClosed = true;
	}
	
	private class UploadHandler implements Runnable{
		private List<ProductSummary> products;
		private Map<ProductSummary, ProductSummary> existingProds;
		
		UploadHandler(List<ProductSummary> prodsToUpload, Map<ProductSummary, ProductSummary> existingProds){
			this.products = prodsToUpload;
			this.existingProds = existingProds;
		}		
		@Override
		public void run() {
			try{
				List<ProductSummary> unsaved = new ArrayList<ProductSummary>();					
				try{
					 recorder.insertUpdateProductSummary(products, (HashMap<ProductSummary, ProductSummary>)existingProds);
				}catch(SQLException e){
					logger.error(e.getMessage(), e);
					unsaved = products;							
				}
				if(unsaved.size() > 0){
					saveTheUnsaved(unsaved);
				}
			}
			catch(Throwable e){
				logger.error(e.getMessage(), e);
			}
		}
		
		void saveTheUnsaved(List<ProductSummary> products){
			Map<String, List<String>> retailerProdMap = new HashMap<String, List<String>>();
			
			for(ProductSummary product: products){			
				List<String> prods = retailerProdMap.get(product.getRetailerId());
				if(prods == null){
					prods = new ArrayList<String>();
					retailerProdMap.put(product.getRetailerId(), prods);				
				}
				prods.add(product.toString());		
			}
			for(Map.Entry<String, List<String>> entry: retailerProdMap.entrySet()){
				String file = getErrFile(entry.getKey());
				fileStore.open(file);
				fileStore.write(file, entry.getValue());
			}
		}
		
		String getErrFile(String retailerId){
			return DateTimeUtils.currentDateYYYYMMDD() + "_" + retailerId + Constants.PRODUCT_FILES.UP_DBFAIL;
		}
		
		int numToProcess(){
			return products.size();
		}

	}
	
	@Override
	public void finalize(){
		close();
	}

	@Override
	public boolean allProcessed() {
		UploadHandler[] tasks = dbService.getQueue().toArray(new UploadHandler[0]);
		if(tasks.length == 0)
			return true;
		int remaining = 0;
		for(UploadHandler task: tasks){
			remaining += task.numToProcess();
		}
		logger.info(remaining + " products remaining to be uploaded");
		return false;
	}
}