package parsers.html;

public class CVSClassNames extends ClassNames {

	@Override
	public String[] productRows() {		
		return new String[]{"product"};
	}

	@Override
	public String[] productName() {		
		return new String[]{"resultTitleLi"};
	}

	@Override
	public String productImage() {
		return "resultImgLi";
	}

	@Override
	public String[] price() {	
		String[] classes = {"productPrice"}; 
		return classes;
	}

	@Override
	public String[] model() {
		return new String[]{""};
	}
	
	@Override
	public String[] decimalPrice(){
		return null;
	}

	@Override
	public String productDesc() {
		return ""; 
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