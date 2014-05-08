<?php
/**
 * User: chirag
 * Date: 19/12/13
 * Time: 5:20 PM
 *
 * Response object consists of data and the error object(if exists);
 */

class ProductFailedObject {
	var $productId;
	var $name;
	var $text;
	
	function __construct($productId, $name, $text) {
		$this->productId = $productId;
		$this->name = $name;
		$this->text = $text;
	}
}