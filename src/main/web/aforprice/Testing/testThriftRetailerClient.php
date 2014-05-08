<?php
/*
 * @author: Amit Mittal
 * 
 * This in particular is for testing product cache impl services.
 */
?>
<?php
/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));
	include '../common/constants.php';
	include_once '../services/thriftRetailerClient.php';
	include_once '../services/Thrift/Generated/Retailer/Types.php';
	
	use Thrift\Generated\Retailer\ProductFilter;
	use Thrift\Generated\Retailer\ProductFilterType;
	use Thrift\Generated\Retailer\LookupIdx;
	use Thrift\Generated\Retailer\SortCriterion;
?>
<?php
	
	function printProduct($resultP){
		echo "<b>Product Id:</b> ".$resultP->productId."<br>";
		echo "<b>Product Name:</b> ".$resultP->name."<br>";
		echo "<b>Current Price:</b> ".$resultP->priceHistory->currPrice."<br>";
		echo "<b>Review History:</b></br>";
		foreach($resultP->reviewHistory as $review)
			echo "Time: ".$review->time." Review Rating: ".$review->reviewRating." NumReviews: ".$review->numReviews."</br>";
		echo "<b>Sell Rank History:</b></br>";
		foreach($resultP->sellRankHistory as $rank)
			echo "Time: ".$rank->time."	Value: ".$rank->value."</br>";
		echo "</br>";
	}

	try{
		$retailer = DEFAULT_RETAILER;
		$thriftRetailerClient = new ThriftRetailerClient();
		
		$categoryId = 7304;

		$max = 1000;
		
		$productFilters = array();
		$productFilter = new ProductFilter();
		$productFilter->type = ProductFilterType::PRICE;
		$productFilter->from = 0;
		$productFilter->to = 20;
		$productFilters[0] = $productFilter;
		
		$lookupIdx = new LookupIdx();
		$lookupIdx->startIdx = 0;
		$lookupIdx->endIdx = 20;

		//TESTING getRetailer
		$retailerObj = $thriftRetailerClient->getRetailer($retailer);
		$retailerName = $retailerObj->displayName;
		$retailerUrl = $retailerObj->url;
		$sortsSupported = $retailerObj->sortsSupported;
		$defaultSort = $retailerObj->defaultSort;
		echo "<h2>Retailer Name: ".$retailerName."</h2>";
		echo "<b>Retailer URL:</b> ".$retailerUrl."<br>";
		echo "<b>Default Sort:</b> ".$defaultSort."<br>";
		foreach ($sortsSupported as $key => $sortSupported) {
			echo "<b>Sort Supported:</b> ".$sortSupported."<br>";
		}
		
		//TESTING getProducts
		$resultProductList = $thriftRetailerClient->getProducts($categoryId, $lookupIdx, $productFilters, SortCriterion::PRICE_ASC);
		echo "<h1>product count: ".$resultProductList->totalCount."</h1><br>";
		$resultProducts = $resultProductList->products;
		$products = array();
		foreach($resultProducts as $resultP){	
			printProduct($resultP);
		}

		//TESTING getTopPriceDropsByCategory
		$resultProductList = $thriftRetailerClient->getTopPriceDropsByCategory($categoryId, $lookupIdx);
		echo "<h1>product count: ".$resultProductList->totalCount."</h1><br>";
		$resultProducts = $resultProductList->products;
		$products = array();
		foreach($resultProducts as $resultP){	
			printProduct($resultP);
		}

		$categoryId = 5472;

		$resultProductList = $thriftRetailerClient->getTopPriceDropsByCategory($categoryId, $lookupIdx);
		echo "<h1>product count: ".$resultProductList->totalCount."</h1><br>";
		$resultProducts = $resultProductList->products;
		$products = array();
		foreach($resultProducts as $resultP){	
			printProduct($resultP);
		}

		$resultMap = $thriftRetailerClient->getPriceDropsByCategory($categoryId, $max);
		echo "<h1>Printing Map, categoryId: ".$categoryId."</h1>";
		foreach ($resultMap as $category => $productList) {
			echo "<h2>Child Category Id: ".$category."</h2>";
			$products = $productList->products;
			echo "<h3>Total products: ".$productList->totalCount."</h3>";
			foreach ($products as $resultP) {
				printProduct($resultP);	
			}
		}

		$levels = array(0);
		$categories = $thriftRetailerClient->getAllCategoriesByLevel($retailer, $levels);
		var_dump($categories);
	} catch(Exception $e) {
		echo "Unexpected error in making thrift client object: ".$e->getMessage();
	}
?>