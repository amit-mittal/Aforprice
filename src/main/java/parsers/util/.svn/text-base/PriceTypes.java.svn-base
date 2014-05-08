package parsers.util;

public enum PriceTypes {
	UNKNOWN(-98),
	NOT_AVAILABLE(-99),
	UN_PARSEABLE(-100),
	AVAILABLE_IN_CART(-101),
	OUT_OF_STOCK(-102),
	IN_STORE_ONLY(-103),
	CLICK_FOR_PRICE(-104),
	IS_MULTI(-105);
	
	private double value;
	PriceTypes(double value){
		this.value = value;
	}
	
	public double getValue(){
		return value;
	}
	
	public static boolean isInvalidType(double priceToCheck){
		if(priceToCheck == UNKNOWN.getValue() || priceToCheck == UN_PARSEABLE.getValue())
			return true;
		return false;
	}

	public static boolean isIgnorablePrice(double priceToCheck){
		if(priceToCheck == UNKNOWN.getValue() || priceToCheck == UN_PARSEABLE.getValue()
			|| priceToCheck == NOT_AVAILABLE.getValue() || priceToCheck == AVAILABLE_IN_CART.getValue()
			|| priceToCheck == OUT_OF_STOCK.getValue() || priceToCheck == IN_STORE_ONLY.getValue() 
			|| priceToCheck == CLICK_FOR_PRICE.getValue())
			return true;
		return false;
	}

}
