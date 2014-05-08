<?php
/**
 * User: chirag
 * Date: 19/12/13
 * Time: 6:30 PM
 *
 * Retailer entity.
 */

$retailer;

class Retailer {

	public static function isValid($retail) {
		global $retailer;

		switch($retail) {
			case "amazonbs":
			case "babysrus":
			case "bananarepublic":
			case "bestbuy":
			case "bjs":
			case "costco":
			case "cvs":
			case "gap":
			case "homedepot":
			case "jcpenny":
			case "kohls":
			case "lowes":
			case "macys":
			case "nordstrom":
			case "oldnavy":
			case "riteaid":
			case "samsclub":
			case "sears":
			case "staples":
			case "target":
			case "toysrus":
			case "victoriassecret":
			case "walgreens":
			case "walmart":

			/* for some anonymous logging of events by plugin */
			case "pluginLogger":
				$retailer = $retail;
				return TRUE;

			default:
				$retailer = DEFAULT_RETAILER;
				return FALSE;
		}
	}
}