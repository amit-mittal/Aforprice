namespace java thrift.genereated.category
namespace php Thrift.Generated.Category

//map of server-number to primary-backup port
//todo: change backup port and set up java server
const map<i32, list<i32>> CATEGORY_SERVER_PORT = { 1: [28101, 28101], 
												 2: [28102, 28102],
												 3: [28103, 28103],
												 4: [28104, 28104],
												 5: [28105, 28105 ],
												 6 : [28106, 28106 ]
												}
//map of server-number to retailer-list
//todo:php lookup of retailer to server may not be efficient with below map, may need to change it to retailer->port mapping
const map<string, i32> CATEGORY_SERVER_RETAILERS = {	 
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

service CategoryCache{
	list<Category> getHomePageCategories(1:string retailerId);
	list<Category> getChildCategories(1:i32 categoryId);
	#list<PriceDropByCategory> getPriceDrops(1:i32 categoryId);
	map<i32,list<PriceDrop>> getPriceDrops(1:i32 categoryId);
	/**
	* Gives the path of category starting from the root category upto
	* the category corresponding to the categoryId
	* @paramcategoryId
	*/
	list<Category> getCategoryPath(1:i32 categoryId);
}