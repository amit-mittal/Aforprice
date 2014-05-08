package parsers;

import entities.Retailer;
import global.errorhandling.ErrorCodes;
import global.exceptions.Bhagte2BandBajGaya;
import parsers.details.*;

public class ProductsParserFactory {
	public static ProductsParser get(String id){		
		if(id.equals(Retailer.AWSAMAZON.getId()))
			return new AWSAmazonParser();
		else if(id.equalsIgnoreCase(Retailer.AMAZONBESTSELLER.getId()))
			return new AmazonMostSellingParser();	
		else if(id.equalsIgnoreCase(Retailer.BANANAREPUBLIC.getId()))
			return new BananaRepublicParser();		
		else if(id.equalsIgnoreCase(Retailer.BABYSRUS.getId()))
			return new BabysrusParser();
		else if(id.equalsIgnoreCase(Retailer.BJS.getId()))
			return new BJsParser();
		else if(id.equalsIgnoreCase(Retailer.COSTCO.getId()))
			return new CostcoParser();
		else if(id.equalsIgnoreCase(Retailer.CVS.getId()))
			return new CVSParser();
		else if(id.equalsIgnoreCase(Retailer.GAP.getId()))
			return new GapParser();		
		else if(id.equalsIgnoreCase(Retailer.HOMEDEPOT.getId()))
			return new HomeDepotParser();
		else if(id.equalsIgnoreCase(Retailer.JCPENNY.getId()))
			return new JCPParser();	
		else if(id.equalsIgnoreCase(Retailer.KOHLS.getId()))
			return new KohlsParser();
		else if(id.equalsIgnoreCase(Retailer.LOWES.getId()))
			return new LowesParser();
		else if(id.equalsIgnoreCase(Retailer.MACYS.getId()))
			return new MacysParser();
		else if(id.equalsIgnoreCase(Retailer.NORDSTROM.getId()))
			return new NordstormParser();		
		else if(id.equalsIgnoreCase(Retailer.OLDNAVY.getId()))
			return new OldNavyParser();
		else if(id.equalsIgnoreCase(Retailer.RITEAID.getId()))
			return new RiteaidParser();
		else if(id.equalsIgnoreCase(Retailer.SAMSCLUB.getId()))
			return new SamsClubParser();
		else if(id.equalsIgnoreCase(Retailer.SEARS.getId()))
			return new SearsParser();
		else if(id.equalsIgnoreCase(Retailer.STAPLES.getId()))
			return new StaplesParser();		
		else if(id.equalsIgnoreCase(Retailer.TARGET_MOBILE.getId()))
			return new TargetMobileParser();
		else if(id.equalsIgnoreCase(Retailer.TOYSRUS.getId()))
			return new ToysrusParser();		
		else if(id.equalsIgnoreCase(Retailer.WALGREENS.getId()))
			return new WalgreensParser();
		else if(id.equalsIgnoreCase(Retailer.WALMART.getId()))
			return new WalmartParser();
		throw new Bhagte2BandBajGaya(ErrorCodes.INVALID_RET_ID);			 
	}
	
	public static ProductDetailsParser getDetailsParser(String id){
		if(id.equalsIgnoreCase(Retailer.ID.WALMART))
			return new WalmartProductDetailsParser();
		
		if(id.equalsIgnoreCase(Retailer.ID.TARGET))
				return new TargetProductDetailsParser();

		if(id.equalsIgnoreCase(Retailer.ID.HOMEDEPOT))
			return new HomeDepotProductDetailsParser();

		if(id.equalsIgnoreCase(Retailer.ID.TOYSRUS))
			return new ToysrusProductDetailsParser();
		
		return null;
	}
}
