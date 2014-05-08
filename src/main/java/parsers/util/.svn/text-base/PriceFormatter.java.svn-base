package parsers.util;


public class PriceFormatter {		
	
	public static double formatDollarPrice(String dp){
		try{
			//Iterate over the price and ignore any non numbers or decimal or anything
			//beyond two decimal places			
			int decimal = '.';
			int zero = '0';
			int nine = '9';
			int decimalPlace = 0;
			StringBuilder price = new StringBuilder();
			for(int i = 0; i < dp.length(); i++){
				int x = dp.charAt(i);
				if(x == decimal){
					decimalPlace = i;
				}
				if(decimalPlace > 0 && i > decimalPlace + 2)
					break;
				if(x != decimal && (x < zero || x > nine))
					continue; //Ignore any non numbers
				price.append(dp.charAt(i));
			}
			return Double.parseDouble(price.toString());
		}catch(Exception e){
			if(dp != null && dp.equalsIgnoreCase("Free"))
				return 0;
			return PriceTypes.UN_PARSEABLE.getValue();
		}
	}
	
	public static double formatPrice(String priceString){
		return formatDollarPrice(priceString);
	}
	
	public static void main(String[] args){
		System.out.println(formatDollarPrice("$1,234.049"));
		System.out.println(formatDollarPrice("$1234.049-$1578.00"));
	}
		
}