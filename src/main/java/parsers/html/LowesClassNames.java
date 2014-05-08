package parsers.html;

public class LowesClassNames extends ClassNames{

	@Override
	public String[] productRows() {		
		return new String[]{"productWrapper", "productLeft"};
	}

	@Override
	public String[] productName() {		
		return new String[]{"productTitle", "descCont"};
	}

	@Override
	public String productImage() {
		return "img";
	}

	@Override
	public String[] price() {	
		String[] classes = {"pricing"}; 
		return classes;
	}

	@Override
	public String[] model() {
		return new String[] {"productInfo", "itemmodel"};
	}
	
	@Override
	public String[] decimalPrice(){
		return new String[]{"ourPrice2"};//not used
	}

	@Override
	public String productDesc() {
		return "prod-detail"; 
	}

	@Override
	public String nextUrl() {
		return "nav-control-forward";
	}

	@Override
	public String priceSuper() {
		// TODO Auto-generated method stub
		return null;
	}	
}