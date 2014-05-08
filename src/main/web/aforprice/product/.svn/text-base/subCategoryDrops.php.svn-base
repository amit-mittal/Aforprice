<?php
/* 
 * @author: Chirag Maheshwari
 * 
 *  It generates the sub-category Drops for the
 *  Main page and category page. */
?>
<?php
chdir(dirname(__FILE__));
	require_once 'ProductUI.php';
?>
<?php
function echoSubCategoryDrops() {
	global $retailer,
	$categoryIdToPriceDrops;
	
	if(!empty($categoryIdToPriceDrops)) {
		$categoryIndex = 1;
		$productsPerCategory = 1;
		$newAccordion = true;

		foreach($categoryIdToPriceDrops as $categoryId => $priceDrops){
		    if( strcmp($categoryId , "ROOT" ) == 0 ) continue;
			if($newAccordion == true) {
				$newAccordion = false;
				?>
				<div class="mg10 horizontalAccordion mgdiv hot_content">
					<ol>
				<?php
			}

			?>
			<li class="slide">
				<h2>
					<span>
						<?php 
							$categoryName = explode("|$|", $categoryId);
							echo end($categoryName);
						 ?>
					</span>
				</h2>
				<div>
					<ul class="top_drop_prods">
			<?php
			$productsPerCategory = 1;
			foreach($priceDrops->products as $product) {
			?>
				<li>
				<?php
					ProductUI::HtmlSmallVertical($product);
				?>
				</li>
			<?php
				$productsPerCategory++;
				if($productsPerCategory > MAX_SUB_DROPS_PER_CATEGORY)
					break;
			}
			?>
					</ul>
				</div>
			</li>
			<?php

			if($categoryIndex == MAX_SUB_CATEGORY_FOR_DROPS) {
				$categoryIndex = 0;
				$newAccordion = true;
			?>
					</ol>
				</div>
			<?php
			}

			$categoryIndex++;
		}
	}
}
?>
<?php
	echoSubCategoryDrops();
?>