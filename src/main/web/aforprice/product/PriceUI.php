<?php
/* 
 * @Author: Chirag Maheshwari
 * 
 *  This (Static) class contains functions to
 *  generate different parts HTML UI for a
 *  given price 
 *  
 *  The name is self explanatory,
 *  For eg: MediumBoldDollar - prints price in dollar format with
 *  in medium size and bold text */
?>
<?php
class PriceUI {
	
	/* This constructir has been deliberatly made
	 * private so that initialization of this class is not
	* possible and it could properly server as a static class */
	private function __construct() {
		/* If any initialization required for this class
		 * put it here, and call this construct from every
		* function necessary */
	}
	
	public static function SmallDollar($price) {
		self::priceDollar($price, "price");
	}
	
	public static function MediumBoldDollar($price) {
		self::priceDollar($price, "price_mb");
	}
	
	private static function priceDollar($price, $className) {
		?>
		<span class="<?php echo $className; ?>">
			<?php echo "$".$price; ?>
		</span>
		<?php
	}
}
?>