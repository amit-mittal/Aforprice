<?php
/* 
 * @auhtor: Chirag Maheshwari
 * 
 * 	generates javascripts array in a fixed format 
 *  for the google annotated timeline charts.
 *  Finally generates HTML for the slideshow to be effective */
?>
<?php
chdir(dirname(__FILE__));
	require_once 'ProductUI.php';
?>
<?php
	function echoHomePageGraphs() {
		global $products;
		/* Echoing the price array in JSON format */
		?>
		<script type="text/javascript">
		var main_graph_prices = {
			<?php
			$productIndex = 1;
			if(!empty($products)) {
				$totalProductIndex = count($products);
				foreach ($products as $product) {
					$priceTicks = $product->priceHistory->priceTicks;
					
					if(strlen($product->name) > 50)
						$name = substr($product->name, 0, 50)."...";
					else
						$name = $product->name;
					echo "'productName_".$productIndex."' : '".addslashes($name)."',\n";
					
					echo "'priceTimeArray_".$productIndex."' : ";
					$priceArray = array();
					foreach ($priceTicks as $priceTick) {
						if($priceTick->value == -99)
							continue;
						array_push($priceArray, ($priceTick->time));
					}
					echo json_encode($priceArray).",\n";
					
					
					echo "'priceArray_".$productIndex."' : ";
					$priceArray = array();
					foreach ($priceTicks as $priceTick) {
						if($priceTick->value == -99)
							continue;
						array_push($priceArray, $priceTick->value);
					}
					echo json_encode($priceArray).",\n";
					$productIndex++;
				}
			}
		?>
		};
		</script>
		
		<div class="slide_selected" style="height: 420px; border-bottom: 1px solid #666;">
		<?php
			$productIndex = 1;
			if(!empty($products)) {
				foreach($products as $product) {
			?>
				<div id="slide_<?php echo $productIndex; ?>" class="slide" data-widthratio="1" data-widthparts="4">
				<?php
					ProductUI::HtmlMediumVertical($product);
				?>
				</div>
			<?php
					$productIndex++;
				}
			}
		?>
		<div class="product_graph" id="main_graph" data-widthratio="3" data-widthparts="4">
		</div>
		</div>
		<div class="slide_thumbnail" class="bod" style="height: 70px; overflow: hidden;">
			<div id="slide_select_arrow" style="width: 10px; height: 9px; background: url('img/spry_1.png') -53px -4px; position: relative; left: 0px; top: 1px; "></div>
			<ul>
		<?php
			$productIndex = 1;
			if(!empty($products)) {
				foreach($products as $product) {
			?>
					<li data-slide="slide_<?php echo $productIndex; ?>"><img src="<?php echo $product->imageUrl; ?>" height="50" /></li>
			<?php
					$productIndex++;
				}
			}
		?>
			</ul>
		</div>
	<?php
	}
?>
<?php
	echoHomePageGraphs();
?>