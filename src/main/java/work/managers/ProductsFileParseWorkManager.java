package work.managers;

import java.io.File;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import servers.ProductsFileParsingServer;
import stores.ProductStore;
import stores.ProductStoreDbAsync;
import util.Constants;
import util.DateTimeUtils;
import work.items.ProductsFileParseTask;
import work.items.Task;
import entities.Category;

public class ProductsFileParseWorkManager implements WorkManager {
	
	private final static Logger logger = Logger.getLogger(ProductsFileParseWorkManager.class);
	private LinkedList<Task> parseFileTasks = new LinkedList<Task>();
	private String name;
	
	public ProductsFileParseWorkManager(String retailerId, String runDate){
		createTasks(retailerId, runDate);
		this.name = retailerId;
	}
	
	/*
	 * Read all files for given retailer/date and create one task per file
	 */
	private void createTasks(String retailerId, String runDate){
		Date date = DateTimeUtils.dateFromyyyyMMdd(runDate);
		logger.info("Creating task for retailer="+retailerId+",date="+runDate);
		String rootPath = Constants.PRODUCT_SUMMARY_HTML_FILES.path(retailerId, date, false);
		logger.info("Find files under "+rootPath);
		ArrayList<String> filePaths = new ArrayList<String>(100);
		getAllFilePaths(rootPath, filePaths);
		int taskId=1;
		 for(String filePath : filePaths){
			 String[] split;
			 if(Constants.isWindows())
				 split = filePath.split(File.separator + File.separator);
			 else
				 split = filePath.split(File.separator);
			 String categoryName = split[split.length-2];
			 String temp = split[split.length-1];
			 int categoryId = Integer.parseInt(temp.substring(0, temp.indexOf('_')));
			 System.out.println(categoryName + "-" + categoryId);
			 Category category = new Category(categoryId, retailerId, categoryName, null);
		 	 Task task = new ProductsFileParseTask(this, category, ProductStoreDbAsync.get(), filePath, date, taskId++);
		 	 parseFileTasks.add(task);
		 }
	}
	
	private void getAllFilePaths(String path, ArrayList<String> filePaths){
		File file = new File(path);
		if(file.isFile())
			filePaths.add(path);
		else{
			String[] children = file.list();
			for(String child : children){
				getAllFilePaths(path+File.separator+child, filePaths);
			}
		}		  
	}
	
	@Override
	public String getName(){
		return name;
	}
		 
	@Override
	public List<Task> getNewTasks(int max) {
		List<Task> tasks = new LinkedList<Task>();		
		waitOnPendingTasks(max);
		int newTaskCapacity = max - getNumPending();
		while(newTaskCapacity>0){
			if(parseFileTasks.peek()!=null){
				Task task = parseFileTasks.removeFirst(); 
				tasks.add(task);
				addPending(task);
			}
			else
				break;
			newTaskCapacity--;
		}
		if(tasks.size()==0){
			//wait for 10 seconds before closing db service since worker threads can be still working on already scheduled tasks
			//todo: apply same logic to other worker manager + change logic on how db service is created.. just create once at startup
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info(this.getClass().getCanonicalName() + " calling cleanUp" );
			cleanUp();
		}
		return tasks;
	}

	private void cleanUp(){
		try{
			ProductStore.Factory.get().close();
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}


	private final Map<Integer, Task> pendingTasks = new HashMap<Integer, Task>();	

	
	
	@Override
	public void onStart(Task task) {				
//		try {			
//			
//		} catch (SQLException e) {
//			logger.error(e.getMessage(), e);
//			// TODO Handle exception
//		}		
	}

	@Override
	public void onFinish(Task task) {		
		synchronized(this){			
			removePending(task.getId());
			notifyAll();
		}
	}

	@Override
	public void onError(Task task, Throwable t) {
		synchronized(this){			
			removePending(task.getId());							
			notifyAll();
		}
		//todo: what should we do here??? how to notify main program that we can't finish this task
	}
	
	
	public void waitOnPendingTasks(int max){
		int numUnfinished = getNumPending();
		if(numUnfinished == max){
			synchronized(this){
				numUnfinished = getNumPending();
				while(true)
				{					
					if(numUnfinished < max)
						break;
					try {
						logger.info("waiting on unfinished tasks: count = " + numUnfinished);
						wait();
					} catch (InterruptedException e) {						
					}											
					numUnfinished = getNumPending();					
				}								
			}
		}
	}
	
	protected void removePending(int id){
		pendingTasks.remove(id);
	}
	
	protected void addPending(Task task){
		pendingTasks.put(task.getId(), task);
	}
	
	protected int getNumPending(){
		return pendingTasks.size();
	}


}