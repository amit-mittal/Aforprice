<?php
/* 
 * @Author: Amit Mittal
 * 
 *  This (Static) class contains functions to
 *  generate different parts HTML UI for a
 *  given date 
 */
?>
<?php
class DateUI {
	
	/* This constructir has been deliberatly made
	 * private so that initialization of this class is not
	* possible and it could properly server as a static class */
	private function __construct() {
		/* If any initialization required for this class
		 * put it here, and call this construct from every
		* function necessary */
	}
	
	public static function PrintDateStyle1($JavaTimestamp) {
		echo date("m/d/y", (int)($JavaTimestamp/1000));
	}

	public static function PrintDateStyle2($JavaTimestamp) {
		echo date("M y", (int)($JavaTimestamp/1000));
	}

	public static function PrintDateStyle3($JavaTimestamp) {
		echo date("dMy", (int)($JavaTimestamp/1000));
	}
}
?>