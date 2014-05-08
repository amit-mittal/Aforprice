package parsers.html;

public class KohlsClassNames extends ClassNames{

	@Override
	public String[] productRows() {		
		return new String[]{"productitem"};
	}

	@Override
	public String[] productName() {		
		return new String[]{"title"};
	}

	@Override
	public String productImage() {
		return "productphoto";
	}

	@Override
	public String[] price() {	
		String[] classes = {"oprice", "sprice"}; 
		return classes;
	}

	@Override
	public String[] model() {
		return new String[]{"model"};
	}
	
	@Override
	public String[] decimalPrice(){
		String[] classes = {"oprice", "sprice"};
		return classes;
	}

	@Override
	public String productDesc() {
		return "description";
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