package parsers.html;

public class HomeDepotClassNames extends ClassNames {

	@Override
	public String[] productRows() {		
		return new String[]{"product"};
	}

	@Override
	public String[] productName() {		
		return new String[]{"item_description"};
	}

	@Override
	public String productImage() {
		return "content_image";
	}

	@Override
	public String[] price() {	
		String[] classes = {"item_price"}; 
		return classes;
	}

	@Override
	public String[] model() {
		return new String[]{"model_container"};
	}
	
	@Override
	public String[] decimalPrice(){
		return null;
	}

	@Override
	public String productDesc() {
		return "item_description"; 
	}

	@Override
	public String nextUrl() {
		return "page-nav";
	}

	@Override
	public String priceSuper() {
		// TODO Auto-generated method stub
		return null;
	}
}