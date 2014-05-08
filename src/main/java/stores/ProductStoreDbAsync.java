package stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import util.Constants;
import util.DateTimeUtils;
import db.dao.ProductsDownloadDAO;
import entities.ProductSummary;
import global.exceptions.Bhagte2BandBajGaya;

public class ProductStoreDbAsync implements ProductStore {

	private static Logger logger = Logger.getLogger(ProductStoreDbAsync.class);
	
	private final ProductsDownloadDAO recorder;
	private ThreadPoolExecutor dbService;
	
	private final FileStore fileStore;
	
	private static ProductStore instance = new ProductStoreDbAsync();
	private boolean isClosed = false;
	
	private ProductStoreDbAsync(){
		this.recorder = ProductsDownloadDAO.get();
		this.dbService = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);
		this.fileStore = new FileStore(Constants.PRODUCT_FILES.PRODUCT_DIR_PATH(), Constants.PRODUCTS_SEP);;
	}
	
	public static ProductStore get(){
		return instance;
	}
	
	@Override
	public boolean save(List<ProductSummary> products) {
		if(isClosed)
			throw new Bhagte2BandBajGaya("ProductStore has been closed");
		dbService.execute(new UploadHandler(products));
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
		UploadHandler(List<ProductSummary> prodsToUpload){
			this.products = prodsToUpload;
		}		
		@Override
		public void run() {
			try{
				List<ProductSummary> unsaved = new ArrayList<ProductSummary>();					
				try{
					unsaved = recorder.recordProducts(products);
				}catch(Exception e){
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