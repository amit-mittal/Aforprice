package parsers.html;

public class AmazonClassNames extends ClassNames {
	@Override
	public String[] productRows() {		
		return new String[]{"result"};
	}
	
	@Override
	public String[] productName() {		
		return new String[]{"title"};
	}

	@Override
	public String productImage() {
		return "image";
	}

	@Override
	public String[] price() {	
		String[] classes = {"price"}; 
		return classes;
	}

	@Override
	public String[] decimalPrice() {
		return null;
	}

	@Override
	public String[] model() {
		return new String[]{null};
	}

	@Override
	public String productDesc() {
		return null; 
	}

	@Override
	public String nextUrl() {
		return "pagnNext";
	}

	@Override
	public String priceSuper() {
		return null;
	}
}
