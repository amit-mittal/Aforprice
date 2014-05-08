package stores;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Constants;
import util.DateTimeUtils;
import entities.ProductSummary;

public class ProductFileStore implements ProductStore{
	
	private FileStore fileStore;
	private Date date;
	
	public ProductFileStore(Date date){	
		fileStore = new FileStore(Constants.PRODUCT_FILES.PRODUCT_DIR_PATH(), Constants.PRODUCTS_SEP);
		this.date = date;
	}
	
	@Override
	public boolean save(List<ProductSummary> products){
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
			String file = getDownFileName(entry.getKey(), date);
			fileStore.open(file);
			fileStore.write(file, entry.getValue());
		}
		return true;
	}
	
	
	@Override
	public void close(){
		fileStore.closeAll();
	}	
	
	@Override
	public boolean save(ProductSummary product){
		return true;
	}
	
	private String getDownFileName(String retailerId, Date date){		
		return DateTimeUtils.currentDateYYYYMMDD(date) + "_" + retailerId + Constants.PRODUCT_FILES.DOWN_INTERMEDIATE;		
	}

	private String getDownDoneFileName(String retailerId, Date date){		
		return DateTimeUtils.currentDateYYYYMMDD(date) + "_" + retailerId + Constants.PRODUCT_FILES.DOWN_DONE;		
	}
	
	private String getUpParseErrFileName(String retailerId, Date date){
		return DateTimeUtils.currentDateYYYYMMDD(date) + "_" + retailerId + Constants.PRODUCT_FILES.UP_PARSE_ERR;
	}

	@Override
	public boolean allProcessed() {
		// TODO Auto-generated method stub
		return false;
	}
}