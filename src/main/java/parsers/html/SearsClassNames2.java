package parsers.html;
/*
 * some products are under class 'cardInner'
 */
public class SearsClassNames2 extends ClassNames{

	@Override
	public String[] productRows() {		
		return new String[]{"cardInner"};
	}

	@Override
	public String[] productName() {		
		return new String[]{"cardProdTitle"};
	}

	@Override
	public String productImage() {
		return "uri";
	}

	@Override
	public String[] price() {	
		String[] classes = {"price_v2", "oldPrice_v2"}; 
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