<?php
/* 
 * marketIndexes.php
 * Generates random price alerts for displaying dummy data on home
 * page in market indexes part.
 * TODO Change this random data with real data.
 * 
 * Displays the market index data in textual and visual manner.
 * 
 * @author: Chirag Maheshwari*/
?>
<?php
	/* @param: numOfRetailers - number of retailers for which random data is required.
	 * @output: marketIndexes - randomly generated array of market indexes containing
	 * current value and change.  */
	function generateRandomMarketPrices($numOfRetailers) {
		$marketIndex = array();
		$value = 0;
		$change = 0;
		
		for($i = 0; $i < $numOfRetailers; $i++) {
			$value = rand(100, 150);
			$change = rand(100, 500)/100;
			$change = $change * (rand(0,1) == 1 ? 1 : -1);
			
			array_push($marketIndex, array("value" => $value, "change" => $change));
		}
		
		return $marketIndex;
	}
?>
<?php
	function echoMarketIndexVisuals() {
		/* Array containing all the retailers. */
		$all_retailers = array(
				"amazon",
				"babysrus",
				"bananaR",
				"bestbuy",
				"bjs",
				"costco",
				"cvs",
				"gap",
				"homedepot",
				"jcpenny",
				"kohls",
				"lowes",
				"macys",
				"nordstrom",
				"oldnavy",
				"riteaid",
				"samsclub",
				"sears",
				"staples",
				"target",
				"toysrus",
				"victorias",
				"walgreens",
				"walmart"
		);
		/* Getting market indexes to display */
		$marketIndexes = generateRandomMarketPrices(6);
		/* contains the maximum of all the changes in market,
		 * used for visuals in market index */
		$marketIndexChangeMax = -1;
		?>
		<div style="display: inline-block; width: 70%;">
			<table id="market_index_quotes">
			<?php
			/* Echoing the market index levels and changes */
				foreach ($marketIndexes as $key => $marketIndex) {
					echo "<tr>";
						echo "<td width=\"50%\">".$all_retailers[array_rand($all_retailers)]."</td>";
						echo "<td width=\"10%\">$".$marketIndex["value"]."</td>";
						echo "<td width=\"30%\"><span class=\"".($marketIndex["change"] < 0 ? "market_loss" : "market_gain")."\">".$marketIndex["change"]."</span></td>";
					echo "</tr>";
					
					if(abs($marketIndex["change"]) > $marketIndexChangeMax)
						$marketIndexChangeMax = abs($marketIndex["change"]);
				}
			?>
			</table>
		</div>
		<?php
			/* Echoing the code for visual change in market indexes */
		?>
		<div style="display: inline-block; width: 27%;">
			<table id="market_index_charts">
			<?php
				foreach ($marketIndexes as $key => $marketIndex) {
					$marketIndexRatio = ((abs($marketIndex["change"])/$marketIndexChangeMax)*100);
					
					echo "<tr>";
					if($marketIndex["change"] < 0) {
						echo "<td style=\"width: 50%;\" class=\"bodrt\">".
									"<table class=\"market_index_charts\">".
										"<tr>".
												"<td style=\"width: ".(100 - $marketIndexRatio)."%;\"></td>".
												"<td class=\"bar_left\" width=\"".$marketIndexRatio."%\" style=\"width: ".$marketIndexRatio."%;\"></td>".
										"</tr>".
										"</table>".
									"</td>".
									"<td style=\"width: 50%;\"></td>";
					} else {
						echo "<td style=\"width: 50%;\" class=\"bodrt\"></td>".
								"<td style=\"width: 50%;\">".
									"<table class=\"market_index_charts\">".
										"<tr>".
											"<td class=\"bar_right\" width=\"".$marketIndexRatio."%\" style=\"width: ".$marketIndexRatio."%;\"></td>".
											"<td style=\"width: ".(100 - $marketIndexRatio)."%;\"></td>".
											"</tr>".
										"</table>".
									"</td>";
					}
					echo "</tr>";
				}
			?>
			</table>
		</div>
	<?php
	}
	?>
<?php
	echoMarketIndexVisuals();
?>