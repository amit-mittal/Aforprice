<?php
	chdir(dirname(__FILE__));
	include 'product.php';
	include_once '../services/thriftRetailerClient.php';
	include_once '../services/Thrift/Generated/Retailer/Types.php';
	include_once '../category/category.php';
	use Thrift\Generated\Retailer\ProductFilter;
	use Thrift\Generated\Retailer\ProductFilterType;
	use Thrift\Generated\Retailer\LookupIdx;
	use Thrift\Generated\Retailer\SortCriterion;

function getCategoryProducts() {
	$thriftClient = new ThriftRetailerClient();
	$categoryId = $_GET['id'];//ToDo:add error checking for missing categoryId
	$retailer = $_GET['retailer'];
	
	//todo: send proper parameters here
	$productFilterList = array();
	$productFilter = new ProductFilter();
	$productFilter->type = ProductFilterType::PRICE;
	$productFilter->from = 32;
	$productFilter->to = 100;
	array_push($productFilterList, $productFilter);
	
	$lookupIdx = new LookupIdx();
	$lookupIdx->startIdx = 21;
	$lookupIdx->endIdx = 40;
	
	$resultProductList = $thriftClient->getProducts($categoryId, $lookupIdx, $productFilterList, SortCriterion::PRICE_ASC);
	echo "<b>product count: ".$resultProductList->totalCount."</b><br>";
	$resultProducts = $resultProductList->products;
	$products = array();
	foreach($resultProducts as $resultP){	
		$product = new Product($resultP->productId, $resultP->name, $resultP->modelNo, $resultP->imageUrl, $resultP->url, $retailer, 0);
		//echo $resultP->name."<br>".count($resultP->priceHistory->priceTicks)."<br>";
		foreach($resultP->priceHistory->priceTicks as $priceTick){
			$product->addPrice($priceTick->time, $priceTick->value);
		}
		$products[$resultP->productId]=$product;
	}
	return $products;
}//function getCategoryProducts() ends...

	$start = microtime(true);
	echo "start: ".$start."<br>";
	$products = getCategoryProducts();
	$end = microtime(true);
	echo "end:    ".$end."<br>";
	echo "time in sec:".(($end-$start))."<br>";
	
	echo "<div class=\"left ml100 mr25 \">";	
	$categoryName = $_GET['name'];//ToDo:add error checking for missing categoryName
	echo "<h4 class=\"category\">".$categoryName."(".count($products)." products)</h4>";
	//echo "<ul class=\"productList\">";
	$count=1;
	foreach($products as $product){
		echo "<div class=\"product\">";
		echo "<div class=\"image\">";
			echo "<a href=\"".$product->url."\">";
				echo "<img src=\"".$product->imageUrl."\" class=\"productImage\" alt=\"Product Details\">";
			echo "</a>";
		echo "</div>";//image ends
		
		echo "<div class=\"data\">";
			echo "<h3 class=\"name\">";
				echo "<a class=\"name\" href=\"".$product->url."\">".$product->name."</a>";
			echo "</h3>";
			echo "<div class=\"model\"> Model: ".$product->model."</div>";
			echo "<div class=\"price\"> Price:</div>";
			if(count($product->priceHistory)>0)
			{
				foreach($product->priceHistory as $time=>$price){
					echo $time.": $".$price."<br>";
				}
			};
		echo "</div>"; //data ends		
		echo "</div>"; //product ends
		if($count++%3==0)
			echo "<br class=\"unfloat\">";
	}
	echo "</div>";
?>