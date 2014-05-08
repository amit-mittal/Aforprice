package parsers.html;

public class AWSAmazonClassNames extends ClassNames {
	@Override
	public String[] productRows() {		
		return new String[]{"Item"};
	}
	
	@Override
	public String[] productName() {		
		return new String[]{"Title", "DetailPageURL"};
	}

	@Override
	public String productImage() {
		return "LargeImage";
	}

	@Override
	public String[] price() {	
		String[] classes = {"ListPrice", "Price"}; 
		return classes;
	}

	@Override
	public String[] decimalPrice() {
		return null;
	}

	@Override
	public String[] model() {
		return new String[]{"Model"};
	}

	@Override
	public String productDesc() {
		return "Feature"; 
	}

	@Override
	public String nextUrl() {
		return null;
	}

	@Override
	public String priceSuper() {
		return null;
	}
}
