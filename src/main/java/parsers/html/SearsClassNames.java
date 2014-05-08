package parsers.html;
/*
 * some products are under class 'ars-products'
 */

public class SearsClassNames extends ClassNames{

	@Override
	public String[] productRows() {		
		return new String[]{"ars-products"};
	}

	@Override
	public String[] productName() {		
		return new String[]{"ars-product-link-wrapper"};
	}

	@Override
	public String productImage() {
		return "uri";
	}

	@Override
	public String[] price() {	
		String[] classes = {"ars-product-sale-price", "ars-product-regular-price"}; 
		return classes;
	}

	@Override
	public String[] model() {
		return new String[]{"model"};
	}
	
	@Override
	public String[] decimalPrice(){
		String[] classes = {"ourPrice2"};
		return classes;
	}

	@Override
	public String productDesc() {
		return "description"; 
	}

	@Override
	public String nextUrl() {
		return "nextPageResults";
	}

	@Override
	public String priceSuper() {
		// TODO Auto-generated method stub
		return null;
	}	
}