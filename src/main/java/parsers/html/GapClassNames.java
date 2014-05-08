package parsers.html;

public class GapClassNames extends ClassNames {
	
	@Override
	public String[] productRows() {		
		return new String[]{"productCatItem"};
	}
	
	@Override
	public String[] productName() {		
		return new String[]{"productItemName"};
	}

	@Override
	public String productImage() {
		return "imgDiv";
	}

	@Override
	public String[] price() {	
		String[] classes = {"priceDisplay", "priceDisplaySale"}; 
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
