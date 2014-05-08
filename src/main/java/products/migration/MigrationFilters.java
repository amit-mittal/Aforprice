package products.migration;

import java.util.HashSet;

import entities.ProductSummary;
import entities.Retailer;

public class MigrationFilters {

	private static MigrationFilters instance = new MigrationFilters();
	
	private static HashSet<String> nameFilters = new HashSet<>();
	static{
		nameFilters.add("cd".toLowerCase());
		nameFilters.add("greatest hits".toLowerCase());
		nameFilters.add("best of".toLowerCase());
		nameFilters.add("greatest hits - cd".toLowerCase());
		nameFilters.add("1-year protection plan - geek squad".toLowerCase());
		nameFilters.add("2-year protection plan - geek squad".toLowerCase());
		nameFilters.add("3-year protection plan - geek squad".toLowerCase());
		nameFilters.add("4-year protection plan - geek squad".toLowerCase());
		nameFilters.add("5-Year Protection Plan- Geek Squad".toLowerCase());
		nameFilters.add("1-Year Accidental Protection Plan - Geek Squad".toLowerCase());
		nameFilters.add("2-Year Accidental Protection Plan - Geek Squad".toLowerCase());
		nameFilters.add("3-Year Accidental Protection Plan - Geek Squad".toLowerCase());
		nameFilters.add("4-Year Accidental Protection Plan - Geek Squad".toLowerCase());
		nameFilters.add("5-Year Accidental Protection Plan - Geek Squad".toLowerCase());
		nameFilters.add("various japan - cd".toLowerCase());
		nameFilters.add("ultimate collection".toLowerCase());
		nameFilters.add("live".toLowerCase());
		nameFilters.add("very best of".toLowerCase());
		nameFilters.add("definitive collection".toLowerCase());
		nameFilters.add("live - cd".toLowerCase());
		nameFilters.add("best of - cd".toLowerCase());
		nameFilters.add("super hits".toLowerCase());
		nameFilters.add("super hits - cd".toLowerCase());
		nameFilters.add("christmas".toLowerCase());
		nameFilters.add("collection".toLowerCase());
		nameFilters.add("songs - cd".toLowerCase());
		nameFilters.add("Very Best Of - CD".toLowerCase());
		nameFilters.add("Platinum Collection".toLowerCase());
		nameFilters.add("The Definitive Collection".toLowerCase());
		nameFilters.add("Greatest Hits".toLowerCase());
		nameFilters.add("The Collection".toLowerCase());
		nameFilters.add("Essential".toLowerCase());
		nameFilters.add("Super Hits (Greatest Hits)".toLowerCase());
		nameFilters.add("Original Album Classics - CD".toLowerCase());
	}
	private MigrationFilters() {
		// TODO Auto-generated constructor stub
	}
	
	public static MigrationFilters get(){
		return instance;
	}
	
	public static boolean filter(ProductSummary prod){
		//Migrate of product has name, url and price
		if(prod == null)
			return true;
		//Do not migrate amazon products. We will migrate only amazon best selling products
		if(prod.getRetailerId().equals(Retailer.AWSAMAZON.getId()))
			return true;
		//Following products are too generic and should not be migrated
		String name = prod.getName().toLowerCase();
		return nameFilters.contains(name);
	}

}
