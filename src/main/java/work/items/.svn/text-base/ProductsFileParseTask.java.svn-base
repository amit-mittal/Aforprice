package work.items;

import java.util.Date;

import org.apache.log4j.Logger;

import parsers.ProductsParser;
import parsers.ProductsParserFactory;
import stores.ProductStore;
import work.managers.WorkManager;
import entities.Category;
import global.exceptions.Bhagte2BandBajGaya;

public class ProductsFileParseTask extends Task {
	private final String filePath;
	private final Category category;
	private final ProductStore store;
	private final static Logger logger = Logger.getLogger(ProductsFileParseTask.class);
	
	public ProductsFileParseTask(WorkManager manager, Category category, ProductStore store, String filePath, Date date, int taskId) {
		super(manager, taskId, 1);
		this.category = category;
		this.store = store;
		this.filePath=filePath;
	}

	@Override
	public void doWork() {
		ProductsParser parser = ProductsParserFactory.get(category.getRetailerId());
		logger.info("Parsing category: (" + attempt() + ")" + category.getRetailerId() + ":" + category.getCategoryId() + ":" + category.getName());
		
		while(true){
			try{
				parser.parseFileAndSave(category.getCategoryId(), category.getName(), filePath, store);
			}catch(Exception ioe){
				logger.error("Unable to parse: " + category.getRetailerId() + ":" + category.getName() + ":" + category.getUrl());
				throw new Bhagte2BandBajGaya(ioe);
			}			
			break;
		}
		
	}
	
	public Category getProductCategory(){
		return category;
	}
}