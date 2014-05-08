namespace java thrift.genereated.product
namespace php Thrift.Generated.Product

//map of server-number to primary-backup port 
//todo: change backup port and set up java server
const map<i32, list<i32>> PRODUCT_SERVER_PORT = { 1: [28001, 28001], 
												 2: [28002, 28002],
												 3: [28003, 28003],
												 4: [28004, 28004],
												 5: [28005, 28005 ],
												 6 : [28006, 28006 ]
												}
const map<string, i32> PRODUCT_SERVER_RETAILERS = {	 
													"bananarepublic" : 1,
													"bjs" : 1,
													"bestbuy" : 1,
													"costco" : 1,
													"cvs" : 1,
													"gap" : 1,
													"homedepot" : 1,
													"jcpenny" : 1,
													
													"kohls" : 2,
													"lowes" : 2,
													"macys" : 2,
													"nordstrom" : 2,
													"oldnavy" : 2,
													"riteaid" : 2,
													"sears" : 2,
													"samsclub" : 2,
													"staples" : 2,
													"walgreens" : 2,
													
													"walmart" : 3,
													
													"target" : 4,
													
													"amazon" : 5,
													"amazonbs" : 5,
													
													"babysrus" : 6,
													"toysrus" : 6
													
											  }
	
  struct Tick{
 	1: required i64 time;
 	2: required double value;
 }
 
 struct PriceHistory{
	1: required list<Tick> priceTicks;//always make sure this is sorted by time
	2: required double currPrice;
	3: required double maxPrice;
	4: required double minPrice;
	5: required i64 currPriceFromTime;
	6: required i64 minPriceFromTime;
	7: required i64 minPriceToTime;
	8: required i64 maxPriceFromTime;
	9: required i64 maxPriceToTime;
	10: optional double averagePrice;
}

/*
struct SellRankHistory{
	1: required list<Tick> sellRankTicks;
}

struct ReviewHistory{
	1: required list<Tick> reviewTicks;
}
*/
struct LookupIdx{
	1: required i32 startIdx;
	2: required i32 endIdx;
}

 struct Product{
 	1: required i32 productId;
 	2: required string name;
 	3: required string modelNo;
 	4: required string imageUrl;
 	5: required string url;
 	6: required PriceHistory priceHistory;  
 	7: required list<Tick> sellRankHistory;
 	8: required list<Tick> reviewHistory;  
// 	7: required SellRankHistory sellRankHistory; 
// 	8: required ReviewHistory reviewHistory;  
 }
 
 enum SortCriterion{
 	PRICE_ASC,
	PRICE_DESC,
 	DROP_PERCENTAGE,
 	BEST_SELLERS,
 	REVIEW_RATINGS
 }
 
 struct ProductList{
 	1: required list<Product> products;
 	2: required i32 totalCount; 	
 }
 
 enum ProductFilterType{
 	PRICE,
 	SELL_RANK
 }
 
 struct ProductFilter{
 	1: required ProductFilterType type;
 	2: required double from;
 	3: required double to;
 }

 struct Retailer{
	1: required string id;
	2: required string displayName;
	3: required string url;
	4: required list<SortCriterion> sortsSupported;
	5: required SortCriterion defaultSort;
 }

 service ProductCache{
	/**
	* Gives the products for a category
	* @paramcategoryId Category Id
	* @param lookup Lookup index specifying start and end index of the list of all the products
	* @filter products filter specifying the range for the filter
	* @sortCritirion what to sort by.
	*/
 	ProductList getProducts(1:i32 categoryId, 2:LookupIdx lookup, 3:list<ProductFilter> filter, 4:SortCriterion sortCriterion);
 	list<Product> getProductsByIds(1:list<i32> productIdList);

	/**
	* Returns the top drops for all the products for all the terminal categories under the
	* category with id=categoryId.
	* @param categoryId Category Id
	* @param lookup Lookup index specifying start and end index of the list
	*		 of all the products
	*/
	ProductList getTopPriceDropsByCategory(1:i32 categoryId, 2:LookupIdx lookup);
	ProductList getTopPriceDropsByRetailer(1:string retailer, 2:LookupIdx lookup);

	/**
	* Returns a map of ids of categories one level below the category with id=categoryId and pricedrops for those categories.
	* @param categoryId Category Id
	* @param max maximum number of pricedrops for any category in the map.
	*/
	map<i32, ProductList> getPriceDropsByCategory(1:i32 categoryId, 2:i32 max);
	map<i32, ProductList> getPriceDropsByRetailer(1:string retailer, 2:i32 max);
 }