package parsers.html;

public class WalmartClassNames extends ClassNames{

	@Override
	public String[] productRows() {		
		return new String[] {"item"};
	}

	@Override
	public String[] productName() {		
		return new String[] {"ListItemLink"};
	}

	@Override
	public String productImage() {
		return "prodImg";
	}

	@Override
	public String[] price() {	
		return new String[] {"bigPriceText2", "Price3XL", "bigPriceTextOutStock2", "submapPrice"}; 
	}

	@Override
	public String[] decimalPrice() {
		String[] classes = {"smallPriceText2", "smallPriceTextOutStock2"}; 
		return classes;
	}

	@Override
	public String[] model() {
		return new String[] {"ModelNo"};
	}

	@Override
	public String productDesc() {
		return "ProdDesc"; 
	}

	@Override
	public String nextUrl() {
		return "next";
	}

	@Override
	public String priceSuper() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}