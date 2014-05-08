<?php
/* homePagePriceAlerts.php */
/* Get 5 Top Drops from category cache and product server.
 * For the top drops, uses the 
 * TODO change this top-drops to actal price alert 
 * 
 * Generate slideshow HTML for price alerts. */
?>
<?php
	function echoPriceAlertSlideShow() {
		global $products, $retailer;
		?>
		<div class="slide_selected" style="height: 150px;">
		<?php
			$productIndex = 1;
			$priceAlertLimit = 5;
			if(!empty($products)) {
				foreach ($products as $product) {
					$productPrice = isset($product->priceHistory->priceTicks[0]) ? $product->priceHistory->priceTicks[0]->value : $product->priceHistory->minPrice;
				?>
				<div id="pricealert_<?php echo $productIndex; ?>" class="slide">
					<div class="product_medium_horizontal">
						<div class="prod_details" style="display: inline-block;">
						<div class="prod_details_header"><a href="product.php?prodid=<?php echo $product->productId; ?>" target="_blank"><?php echo $product->name; ?></a></div>
						<div class="prod_detail"><span class="name">Current Price: </span><span class="value" style="vertical-align: top;"><span class="price">$<?php echo $productPrice; ?></span></span></div>
						<div class="prod_detail"><span class="name">Price Alert at: </span><br/><span class="price_mb">$<?php echo ($productPrice + rand(1, 100)); ?></span>&nbsp;&nbsp;&nbsp;<button class="add_pricealert" title="Set price alert"></button></div>
					</div>
						<div class="product_thumbnail">
							<img src="<?php echo $product->imageUrl; ?>" />
						</div>
					</div>
				</div>
				<?php 
					$productIndex++;
					if($productIndex > $priceAlertLimit)
						break;
				}
			}
		?>
		</div>
		<div class="slidePrev" style="margin-left: 5px; float: left; display: inline-block; color: #666667; font-size: 12px; cursor: pointer;height: 15px;">Prev</div>
		<div id="pricealert_thumb" class="slide_thumbnail" style="height: 25px; padding: 5px; overflow: hidden; display: inline-block; width: 230px; height: 15px;">
			<ul>
		<?php
			for($i = 1; $i < $productIndex; $i++)
				echo "<li data-slide=\"pricealert_".$i."\"><div class=\"pricealert_thumb\" ></div></li>";
		?>
			</ul>
		</div>
		<div class="slideNext" style="margin-right: 5px; float: right; display: inline-block; color: #666667; font-size: 12px;cursor: pointer;height: 15px;">Next</div>
		<?php
	}
?>

<?php
	echoPriceAlertSlideShow();
?>