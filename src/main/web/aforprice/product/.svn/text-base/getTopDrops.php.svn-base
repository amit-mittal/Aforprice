<?php
/*
 * @auhtor: Chirag Maheshwari
*
*  This script takes the category-ID from the URL($_GET)
*  and get the price drops for the given categoryID.
*  TODO make the category-ID dynamic (done!)
*  Then stores "MAX" of them in an array to get the full details+price history of
*  these products. */
?>
<?php
chdir(dirname(__FILE__));
	include_once '../services/thriftRetailerClient.php';
?>
<?php
	/* Get the category ID here, but for now, the category ID
	 * is fixed to "2" */
	if(isset($_GET['category'])) {
		$categoryId = $_GET['category'];
	} else {
		$categoryId = null;
	}
?>
<?php
	/* Get the price drops from the category ID using the thrift product client */
	try {
		$thriftClient = new ThriftRetailerClient();
		$categoryIdToPriceDrops = null;
		if($thriftClient->connected) {
			if(is_null($categoryId))
				$categoryIdToPriceDrops = $thriftClient->getPriceDropsByRetailer($retailer, MAX_DROPS_PER_CATEGORY);
			else {
				$categoryIdToPriceDrops = $thriftClient->getPriceDropsByCategory($categoryId, MAX_DROPS_PER_CATEGORY);
			}
		}
	} catch(Exception $e) {
		echo "Error in getting price drops: ".$e->getMessage();
	}
	
// 	var_dump($categoryIdToPriceDrops);

	$productCount = 0;
	$products = array();
	
	if(!empty($categoryIdToPriceDrops)) {
		foreach($categoryIdToPriceDrops as $categoryId => $priceDrops){
		    if( strcmp($categoryId , "ROOT" ) != 0 ) continue;
			foreach($priceDrops->products as $product){
				$productCount++;
				array_push($products, $product);
				if($productCount >= MAX_DROPS)
					break;
			}
			if($productCount >= MAX_DROPS)
				break;
		}
	}
// 	var_dump($products);
?>