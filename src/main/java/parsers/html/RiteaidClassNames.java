package parsers.html;

public class RiteaidClassNames extends ClassNames{

	@Override
	public String[] productRows() {		
		return new String[] {"largeProduct"};
	}

	@Override
	public String[] productName() {		
		return new String[] {"title"};
	}

	@Override
	public String productImage() {
		return "productImage";
	}

	@Override
	public String[] price() {	
		return new String[] {"salePrice", "price"}; 
	}

	@Override
	public String[] decimalPrice() {
		return null;
	}

	@Override
	public String[] model() {
		return null;
	}

	@Override
	public String productDesc() {
		return null; 
	}

	@Override
	public String nextUrl() {
		return "nextPage";
	}

	@Override
	public String priceSuper() {
		// TODO Auto-generated method stub
		return null;
	}
}