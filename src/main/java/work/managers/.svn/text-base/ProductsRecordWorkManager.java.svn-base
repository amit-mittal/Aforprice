//package work.managers;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//import org.apache.log4j.Logger;
//
//import stores.FileStore;
//import stores.ProductStore;
//import util.Constants;
//import work.items.ProductsRecordTask;
//import work.items.Task;
//import entities.ProductSummary;
//
//public class ProductsRecordWorkManager extends WorkManagerDatedTasks {
	
//	private final static Logger logger = Logger.getLogger(ProductsRecordWorkManager.class);
//	
//	private final File productsDir = new File(Constants.PRODUCT_FILES.PRODUCT_DIR_PATH());
//	private final String[] productFiles;
//	private final ProductStore store;
//	
//	//private final  ProductStore unrecordedProds = ProductStore.Factory.getFileStore(retailerId, date)(Constants.UNRECORDED_PRODS_FILE);	
//	private final FileStore unparseableProdsStore = new FileStore(Constants.PRODUCT_FILES.PRODUCT_DIR_PATH(), Constants.PRODUCTS_SEP);
//	
//	private Scanner productsScanner = null;		
//	
//	private int id = 0;
//	
//	public ProductsRecordWorkManager(final List<String> retailers) {
//		super(null, "ProductsRecord");
//		store = ProductStore.Factory.get();
//		//final String filePrefix = DateTimeUtils.currentDateYYYYMMDD(runDate);	
//		productFiles = productsDir.list(new FilenameFilter() {
//			
//			@Override
//			public boolean accept(File dir, String name) {
//				if(!dir.equals(productsDir))
//					return false;
//				//The file which has been downloaded completely.
//				if(name.endsWith(Constants.PRODUCT_FILES.DOWN_DONE))
//					return true;				
//				return false;				 
//			}
//		});
//	}
//	
//	@Override
//	public void onError(Task task, Throwable t) {
//		super.onError(task, t);
//		ProductsRecordTask prodRecTask = (ProductsRecordTask)task;
//		//unrecordedProds.save(prodRecTask.getProducts());
//		//unrecordedProds.close();
//	}
//
//
//	@Override
//	public List<Task> getNewTasks(int max) {
//		List<Task> tasks = new ArrayList<>(max);
//		waitOnPendingTasks(max);
//		
//		try{						
//			if(!setNewReader(false))
//				return null;
//			String record = null;
//			while(true){
//				int i = 0;
//				int numTasks = 0;
//				List<ProductSummary> products = new ArrayList<ProductSummary>();
//				while(i < Constants.PRODUCTS_PER_TASK && productsScanner.hasNext()){
//					try{
//						record = productsScanner.next();					
//						String[] values = record.split(String.valueOf(Constants.PRODUCTS_ATTR_COL_SEP));					
//						ProductSummary prod = new ProductSummary(values);
//						products.add(prod);
//						i++;
//					}catch(Exception e){
//						logger.error("Unable to parse: " + record, e);
//						unparseableProdsStore.open(Constants.UNPARSEABLE_PRODS_FILE);
//						unparseableProdsStore.write(Constants.UNPARSEABLE_PRODS_FILE, record);
//						unparseableProdsStore.close(Constants.UNPARSEABLE_PRODS_FILE);
//					}						
//				}
//				Task task = new ProductsRecordTask(this, getNextTaskId(), 1, products);
//				tasks.add(task);
//				addPending(task);
//				if(!productsScanner.hasNext()){					
//					if(!setNewReader(true))
//						return tasks;
//				}
//				if(++numTasks >= (max - getNumPending()))
//					break;
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			logger.error("Finished due to error.", e);
//			return null;
//		}
//		return tasks;
//	}
//	
//	public boolean setNewReader(boolean force) throws IOException{
//		//TODO: Create a productreader
//		if(force && productsScanner != null){
//			productsScanner.close();
//			productsScanner = null;
//		}
//		if(productsScanner == null){
//			for(String fileName: productFiles){
//				if(!tryLock(fileName.hashCode(), 1))
//					continue;				
//				productsScanner = new Scanner(new BufferedReader(new FileReader(new File(productsDir, fileName))));
//				productsScanner.useDelimiter(String.valueOf(Constants.PRODUCTS_SEP));
//				return true;
//			}
//			return false;
//		}
//		return true;
//	}
//	
//	private int getNextTaskId(){
//		return ++id;
//	}
//}
