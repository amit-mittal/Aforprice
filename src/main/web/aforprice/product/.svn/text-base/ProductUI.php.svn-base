<?php
/* 
 * @Author: Chirag Maheshwari
 * 
 *  This (Static) class contains functions to
 *  generate different parts HTML UI for a
 *  given product 
 *  
 *  The name of the function is self explanatory
 *  
 *   If name: Small Horizontal
 *   it will use the class small and will be displayed horizontally */
?>
<?php
chdir(dirname(__FILE__));
	require_once 'PriceUI.php';
	require_once 'DateUI.php';
	require_once '../common/constants.php'
?>
<?php
class ProductUI {

	static $currentProduct;
	
	/* This constructir has been deliberatly made
	 * private so that initialization of this class is not
	 * possible and it could properly server as a static class */
	private function __construct() {
		/* If any initialization required for this class
		 * put it here, and call this construct from every
		 * function necessary */
	}
	
	/* If the product name is too long to be shown in a fixed width 
	area, it shortens it to show only a part of it appended with three dots to show 
	incomplecy */
	private static function formattedProductName($productName) {
		if(strlen($productName) < PRODUCT_NAME_LENGTH) {
			return $productName;
		}
		else {
			return substr($productName, 0, PRODUCT_NAME_LENGTH - 5)."...";
		}
	}

	/* Product name header is printed anywhere a product is printed, thus separated
	into another function. */
	private static function productNameHeader() {
		global $retailer;
		?>
			<div class="prod_details_header">
				<a href="product.php?retailer=<?php echo $retailer; ?>&amp;prodid=<?php echo self::$currentProduct->productId; ?>" 
					target="_blank"
					class="product_name">
					<?php echo self::formattedProductName(self::$currentProduct->name); ?>
				</a>
			</div>
		<?php
	}
	
	public static function HtmlSmallHorizontal(&$product) {
		global $retailer;
		self::$currentProduct = $product;
		?>
			<div class="product_small_horizontal">
				<div class="product_thumbnail">
					<img src="<?php echo $product->imageUrl; ?>" />
				</div>
				<div class="prod_details">
					<?php
						self::productNameHeader();
					?>
					<div class="prod_detail">
						<span class="name">Price: </span>
						<span class="value" style="vertical-align: top;">
							<?php 
									PriceUI::SmallDollar($product->priceHistory->curPrice); 
							?>
						</span>
					</div>
				</div>
				<div class="clear"></div>
			</div>
		<?php
	}
	
	public static function HtmlSmallVertical(&$product) {
		global $retailer;
		self::$currentProduct = $product;
		?>
		<div class="product_small">
			<div class="product_thumbnail">
				<img src="<?php echo $product->imageUrl;?>" />
			</div>
			<div class="prod_details">
				<?php
					self::productNameHeader();
				?>
				<div class="prod_detail">
					<span class="name">Current Price: 
					</span>
					<?php 
						PriceUI::MediumBoldDollar($product->priceHistory->currPrice); 
					?>
				</div>
				<div class="prod_detail">
					<span class="name">Max Price: </span>
					<?php
						PriceUI::SmallDollar($product->priceHistory->maxPrice);
					?>
				</div>
				<div class="prod_detail">
					<span class="name">Min Price: </span>
					<?php
						PriceUI::SmallDollar($product->priceHistory->minPrice);
					?>
				</div>
			</div>
		</div>
		<?php
	}
	
	public static function HtmlMediumVertical(&$product) {
		global $retailer;
		self::$currentProduct = $product;
	?>
		<div class="product_medium">
			<div class="product_thumbnail">
				<img src="<?php echo $product->imageUrl; ?>" />
			</div>
			<div class="prod_details" style="display: inline-block;">
				<?php
					self::productNameHeader();
				
					$sizeOfReviewHistory = sizeof($product->reviewHistory);
					if ($sizeOfReviewHistory!=0){
						$rating = $product->reviewHistory[$sizeOfReviewHistory-1]->reviewRating;
						$numReviews = $product->reviewHistory[$sizeOfReviewHistory-1]->numReviews;
					}else{
						$rating = -1;
						$numReviews = -1;
					}
					if( $rating != -1 ) {
				?>
						<div class="prod_detail">
							<span class="name">Customer Reviews: </span>
							<br/>
							<div class="stars normal" data-type="normal" data-rating="<?php echo $rating; ?>" style="height: 20px; width: 105px;">
								<div class="filling" style="height: 20px; width: 90px;"></div>
							</div>
							<br/>
							<span class="value"><?php echo $rating; ?> out of 5 (<?php echo $numReviews; ?> Reviews)</span>
						</div>
				<?php
					}
				?>
				<div class="prod_detail">
					<span class="name">Current Price: </span>
					<span class="value" style="vertical-align: top;">
						<?php
							PriceUI::MediumBoldDollar($product->priceHistory->currPrice);
						?> 
						(<?php DateUI::PrintDateStyle1($product->priceHistory->currPriceFromTime); ?> - present)
					</span>
				</div>
				<div class="prod_detail">
					<span class="name">Min Price: </span>
					<span class="value" style="vertical-align: top;">
						<?php
							PriceUI::SmallDollar($product->priceHistory->minPrice);
						?>
						(<?php DateUI::PrintDateStyle1($product->priceHistory->minPriceFromTime); ?> - <?php DateUI::PrintDateStyle1($product->priceHistory->minPriceToTime); ?>)
					</span>
				</div>
				<div class="prod_detail">
					<span class="name">Max Price: </span>
					<span class="value" style="vertical-align: top;">
						<?php
							PriceUI::SmallDollar($product->priceHistory->maxPrice);
						?>
						(<?php DateUI::PrintDateStyle1($product->priceHistory->maxPriceFromTime); ?> - <?php DateUI::PrintDateStyle1($product->priceHistory->maxPriceToTime); ?>)
					</span>
				</div>
			</div>
		</div>
	<?php
	}
	
	public static function PrintTerminalProduct(&$product){
		global $retailer;
		self::$currentProduct = $product;
		$productName = json_encode(htmlspecialchars($product->name, ENT_QUOTES));
		$productId = $product->productId;
	?>
		<div class="terminal_product">
			<!-- Display all the images -->
			<img src="<?php echo $product->imageUrl; ?>" width="100" height="100" class="product_image" />
			
			<div class="product_history">
			<ul>
				<li><img src="img/Symbols/price_history.png" title="Price History" class="icon price_history history_icon" 
					data-history='<?php echo json_encode($product->priceHistory->priceTicks); ?>'>
					<!--event-history='<?php //echo json_encode($eventHistoryArray); ?>'> -->
				</li>	

				<li><img src="img/Symbols/review_history.png" title="Review History" class="icon review_history history_icon"
					data-history='<?php echo json_encode($product->reviewHistory); ?>'>
				</li>
			</ul>
				<!--
				<img src="img/Symbols/rank_history.png" class="icon rank_history history_icon"
					data-history='<?php //echo json_encode($product->sellRankHistory); ?>'> -->
			</div>
			
			<!-- Print Product Name -->
			<?php
				self::productNameHeader();
			?>

			<!-- Start of Show the latest Review -->
			<?php
				$sizeOfReviewHistory = sizeof($product->reviewHistory);
				if ($sizeOfReviewHistory!=0){
					$rating = $product->reviewHistory[$sizeOfReviewHistory-1]->reviewRating;
					$numReviews = $product->reviewHistory[$sizeOfReviewHistory-1]->numReviews;
				}else{
					$rating = 0;
					$numReviews = 0;
				}

				if($rating!=0){			
					echo '<div class="overview">';
					echo '<span class="name"></span>';
					echo '<div class="stars" data-type="normal" data-rating='.$rating.'></div>';
					//<!-- TODO: Currently it does not deal with the case if reviewRating>0 but numReviews<0 -->
					
					//echo '<span class="value">'.$rating.'/5 ( </span>'.$numReviews.' Reviews <span class="value">)</span>'; 
					if($numReviews!=0){
						echo '&nbsp;&nbsp;[ </span>'.$numReviews.' Reviews <span class="value">]</span>'; //removed the numerical display of rating
						}
					echo '</div>';
          					
				} else{
					echo "<br>No Review<br>";
				}
			?>
			<!-- End of Show the latest Review -->

			<?php
				$currPrice = $product->priceHistory->currPrice;
				$currPriceFromTime = $product->priceHistory->currPriceFromTime;
				$currPriceToTime = time()*1000;
				$maxPrice = $product->priceHistory->maxPrice;
				$maxPriceFromTime = $product->priceHistory->maxPriceFromTime;
				$maxPriceToTime = $product->priceHistory->maxPriceToTime;
				if($maxPriceToTime == -1)//handling the special value
					$maxPriceToTime = $currPriceToTime;
				$minPrice = $product->priceHistory->minPrice;
				$minPriceFromTime = $product->priceHistory->minPriceFromTime;
				$minPriceToTime = $product->priceHistory->minPriceToTime;
				if($minPriceToTime == -1)//handling the special value
					$minPriceToTime = $currPriceToTime;
			/*	echo "Product Id:=".$product->productId; echo "<br>"; 
				echo "Sizeof priceHistory array:=".sizeof($product->priceHistory->priceTicks); echo "<br>";
				echo "Sizeof reviewHistory array:=".sizeof($product->reviewHistory); echo "<br>";
				echo "Sizeof sellRankHistory array:=".sizeof($product->sellRankHistory);
			*/
			?>

			<!-- Start of Show the Price Summary -->
			
				<div class="overview">
					<span style="font-size: 120%;font-weight:bold;font-family:serif;color:#FF6600;"><?php echo "$".$currPrice ;?> </span>
					<?php echo " &nbsp;[".date("dMy",(int)($currPriceFromTime/1000))."-".date("dMy",(int)($currPriceToTime/1000))."]"; ?>
				</div>
			
				<div class="overview">
					<span>Max Price:</span>
					<?php self::PrintPriceAndDate($maxPrice, $maxPriceFromTime, $maxPriceToTime); ?>
				</div>
			
				<div class="overview">
					<span>Min Price:</span>
					<?php self::PrintPriceAndDate($minPrice, $minPriceFromTime, $minPriceToTime); ?>
				</div>
			
			<!-- End of Show the latest Review -->

			<!-- Add Workspace button -->
			<button class="add_workspace" title="add product to workspace" onclick='plotCombinedGraph(<?php echo $productName;?>, <?php echo json_encode($product->reviewHistory);?>, <?php echo json_encode($product->sellRankHistory);?>)';/></button>
			
			<!-- Add Price Alert button -->
			<a class="login_btn">
				<button class="add_pricealert" id="<?php echo $product->productId;?>" title="Set price alert" onclick = "priceAlert('<?php echo $product->productId;?>','<?php echo $product->priceHistory->currPrice;?>')";  ></button>
			</a>

			<!-- Add product for comparison -->
			<div style="text-align: left; vertical-align: middle;">
				<input type="checkbox" class="add_prod_compare" id="<?php echo $product->productId; ?>"> Compare </input>
			</div>
			
		</div>
	<?php
	}
/*	private static function PrintDate($JavaTimestamp) {
		echo date("m/d/y", (int)($JavaTimestamp/1000));
	}

	private static function PrintDateOnTooltip($StartJavaTimestamp, $EndJavaTimestamp){
		DateUI::PrintDateStyle3($StartJavaTimestamp);
		echo '-';
		DateUI::PrintDateStyle3($EndJavaTimestamp);
	}
*/
	private static function PrintPriceAndDate($Price, $StartJavaTimestamp, $EndJavaTimestamp){
		PriceUI::SmallDollar($Price);
		echo '[';
		DateUI::PrintDateStyle3($StartJavaTimestamp);
		echo '-';
		DateUI::PrintDateStyle3($EndJavaTimestamp);
		echo ']';
	}
}
?>