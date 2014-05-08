<?php
/* 
 * @author: Chirag Maheshwari
 * 
 *  recentlyViewed.php for now 4-5 top drops.
 *  TODO use real data to replace the top drops here
 *  
 *  Print the products in the form of recently viewed. */
?>
<?php
chdir(dirname(__FILE__));
	require_once '../product/ProductUI.php';
?>
<?php
function echoRecentlyViewed() {
	global $products, $retailer;
	
	$productIndex = 1;
	$recentlyViewedMaxItems = 4;
	
	if(!empty($products)) {
		foreach($products as $product) {
			$productIndex++;
			
			echo "<li>";
			ProductUI::HtmlSmallHorizontal($product);
			echo "</li>";
			
			if($productIndex > $recentlyViewedMaxItems) {
				break;
			}
		}
	} else {
		echo "<div>No products returned from the server!</div>";
	}
}
?>
<?php
	echoRecentlyViewed();
?>