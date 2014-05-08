<?php
/* 
 *  @Author: Amit Mittal
 * 
 *  This (Static) class contains utility functions
 *  which can be used in general 
 *  
 *  The name of the function is self explanatory
 */
?>
<?php
chdir(dirname(__FILE__));
?>
<?php
class Util {
	
	/* This constructir has been deliberatly made
	 * private so that initialization of this class is not
	 * possible and it could properly server as a static class */
	private function __construct() {
		/* If any initialization required for this class
		 * put it here, and call this construct from every
		 * function necessary */
	}
	
	
	public static function GetURL(&$prefix, &$params) {
		$url = "";
		$url = $prefix."?";
		foreach ($params as $key => $value) {
			$url = $url.$key."=".$value."&";
		}
		return $url;
	}

	public static function AppendURL(&$params) {
		$url = "";
		foreach ($params as $key => $value) {
			$url = $url.$key."=".$value."&";
		}
		return $url;
	}
}
?>