<?php
/* 
 * @author: Chirag Maheshwari
 * 
 *  mostViewed.php for now 4-5 top drops.
 *  TODO use real data to replace the top drops here
 *  
 *  Print the products in the form of most viewed. */
?>
<?php
chdir(dirname(__FILE__));
	require_once '../product/ProductUI.php';
?>
<?php
function echoMostViewed() {
	global $products, $retailer;
	
	$productIndex = 1;
	$mostViewedMaxItems = 6;
	if(!empty($products)) {
		foreach($products as $product) {
		?>
			<li>
			<?php
				ProductUI:: HtmlSmallVertical($product);
			?>
			</li>
		<?php
			$productIndex++;
			if($productIndex > $mostViewedMaxItems)
				break;
		}
	}
}
?>
<?php
	echoMostViewed();
?>