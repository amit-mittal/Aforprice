package work.items;

import org.apache.log4j.Logger;

import parsers.ProductsParser;
import parsers.ProductsParserFactory;
import stores.ProductStore;
import work.managers.WorkManager;
import entities.Category;
import global.exceptions.Bhagte2BandBajGaya;

public class ProductsParseTask extends Task {

	private final Category category;
	private final ProductStore store;
	private final static Logger logger = Logger.getLogger(ProductsParseTask.class);
		
	public ProductsParseTask(WorkManager manager, int attempt, Category category, ProductStore store) {
		super(manager, category.getCategoryId(), attempt);
		this.category = category;
		this.store = store;
	}

	@Override
	public void doWork() {
		ProductsParser parser = ProductsParserFactory.get(category.getRetailerId());
		logger.info("Parsing category: (" + attempt() + ")" + category.getRetailerId() + ":" + category.getCategoryId() + ":" + category.getName());				
		try{
			parser.parseAndSave(category.getCategoryId(), category.getName(), category.getUrl(), store);
		}catch(Exception ioe){				
			logger.error("Unable to parse: " + category.getRetailerId() + ":" + category.getName() + ":" + category.getUrl());
			throw new Bhagte2BandBajGaya(ioe);
		}			
	}
	
	public Category getProductCategory(){
		return category;
	}
}