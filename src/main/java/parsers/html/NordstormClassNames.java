package parsers.html;

public class NordstormClassNames extends ClassNames{
	@Override
	public String[] productRows() {		
		return new String[]{"fashion-item"};
	}
	
	@Override
	public String[] productName() {		
		return new String[]{"title"};
	}

	@Override
	public String productImage() {
		return "fashion-photo";
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
		return "next";
	}

	@Override
	public String priceSuper() {
		return null;
	}
}
