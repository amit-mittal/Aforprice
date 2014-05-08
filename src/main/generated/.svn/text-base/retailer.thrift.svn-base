namespace java thrift.genereated.retailer
namespace php Thrift.Generated.Retailer

const map<i32, list<i32>> RETAILER_SERVER_PORT = {
													1: [28001, 28001], 
												 	2: [28002, 28002],
												 	3: [28003, 28003],
												 	4: [28004, 28004],
												 	5: [28005, 28005],
												 	6: [28006, 28006]
												 }

const map<string, i32> RETAILERS = {	 
									"bananarepublic" : 1,
									"bjs" : 1,
									"bestbuy" : 1,
									"costco" : 1,
									"cvs" : 1,
									"gap" : 1,
									"homedepot" : 1,
									"jcpenny" : 1,
									"testretailer" : 1,
									
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
									"testretailer2" : 2,
									
									"walmart" : 3,
									
									"target" : 4,
									
									"amazon" : 5,
									"amazonbs" : 5,
									
									"babysrus" : 6,
									"toysrus" : 6
							  		}

enum SortCriterion{
	PRICE_ASC,
	PRICE_DESC,
	DROP_PERCENTAGE,
	BEST_SELLERS,
	REVIEW_RATINGS
}

enum ProductFilterType{
	PRICE,
	REVIEW_RATINGS
}

struct Retailer{
	1: required string id;
	2: required string displayName;
	3: required string url;
	4: required list<SortCriterion> sortsSupported;
	5: required SortCriterion defaultSort;
}

struct Category{
	1: required i32 categoryId; 
	2: required string categoryName;
	3: required string retailerId;
	4: required i32 parentCategoryId;
	5: required string parentCategoryName;
	6: required string url;
	7: required bool parent;
}

struct PriceDrop{
	1: required i32 categoryId;
	2: required i32 productId;
	3: required string url;
	4: required string name;
	5: required string imageUrl;
	6: required double currentPrice;
	7: required double previousPrice;
	8: required double averagePrice;
	9: required double maxPrice;
	10: required byte reviewStars;
}

struct PriceDropByCategory{
	1: required i32 categoryId;
	2: required string categoryName;
	3: required list<PriceDrop> priceDrops; 	
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
}

struct Review{
	1: required i64 time;
	2: required double reviewRating;
	3: required i32 numReviews;
}

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
	7: required list<Tick> sellRankHistory;//always make sure this is sorted by time
	8: required list<Review> reviewHistory;//always make sure this is sorted by time
	9: optional string uid; 
}

struct ProductList{
	1: required list<Product> products;
	2: required i32 totalCount;
	3: optional map<string, i32> priceFilterToNumProdMap;//keeping it optional as we dont need it for priceDrops, etc. so why to waste memory
	4: optional map<string, i32> reviewFilterToNumProdMap;//keeping it optional as we dont need it for priceDrops, etc. so why to waste memory
}
 
struct ProductFilter{
	1: required ProductFilterType type;
	2: required double from;
	3: required double to;
}

service RetailerCache{
	Retailer getRetailer(1:string retailerId);

	map<i32, list<Category>> getAllCategoriesByLevelForRetailer(1:string retailerId, 2:list<i32> levels)
	list<Category> getHomePageCategories(1:string retailerId);
	list<Category> getChildCategories(1:i32 categoryId);
	list<Category> getCategoryPath(1:i32 categoryId);
	#list<PriceDropByCategory> getPriceDrops(1:i32 categoryId);
	#map<i32,list<PriceDrop>> getPriceDrops(1:i32 categoryId);

	ProductList getProducts(1:i32 categoryId, 2:LookupIdx lookup, 3:list<ProductFilter> filters, 4:SortCriterion sortCriterion);
	list<Product> getProductsByIds(1:list<i32> productIdList);

	ProductList getTopPriceDropsByCategory(1:i32 categoryId, 2:LookupIdx lookup);
	ProductList getTopPriceDropsByRetailer(1:string retailer, 2:LookupIdx lookup);

	map<string, ProductList> getPriceDropsByCategory(1:i32 categoryId, 2:i32 max);
	map<string, ProductList> getPriceDropsByRetailer(1:string retailer, 2:i32 max);
	
	map<string, Product> getProductsForURLs(1:string retailer, 2:list<string> urls);
	map<string, ProductList> getPriceDropsForProductFamily(1:string retailer, 2:list<string> urls, 3:i32 max);

	map<string, Product> getProductsForReceiptIds(1:string retailer, 2:list<string> receiptIds);
		
}