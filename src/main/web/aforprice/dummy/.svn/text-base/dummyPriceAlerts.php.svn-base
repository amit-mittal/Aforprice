<?php
/**
 * Created by JetBrains PhpStorm.
 * User: chirag
 * Date: 21/3/14
 * Time: 4:31 AM
 * To change this template use File | Settings | File Templates.
 */

include_once '../plugin/ResponseError.php';
include_once '../plugin/ResponseObject.php';

class dummyPriceAlerts {
	static function getPriceAlerts($user) {
		$randomNumber = rand(0, getrandmax())/getrandmax();

		if($randomNumber <= 0.9) {
			/* Success */
			return new ResponseObject(ResponseErrorType::SUCCESS, json_decode('[ 		{
 			"retailer"	: "toysrus",
			"alertId" : 1293,
			"productId" : 123456,
	 		"name"		: "product1",
			"url"	: "http://www.toysrus.com/product/index.jsp?productId=12530155&cp=&parentPage=search",
	 		"imageUrl" : "http://www.toysrus.com/graphics/product_images/pTRU1-12088700v150.jpg",
	 		"alertActive" : 1,
	 		"alertCreationDate" : "20140130",
	 		"alertExpirationDate" : "20140330",
	 		"alertPrice" : 89.99,
	 		"currPrice" : 99.99,
			"purchasePrice" : 101,
			"purchaseDate" : "20140130"
		},
 		{
 			"retailer"	: "walmart",
			"alertId" : 1276,
			"productId" : 123457,
	 		"name"		: "product1",
			"url"	: "http://www.toysrus.com/product/index.jsp?productId=12530155&cp=&parentPage=search",
	 		"imageUrl" : "http://www.toysrus.com/graphics/product_images/pTRU1-12088700v150.jpg",
	 		"alertActive" : 1,
	 		"alertCreationDate" : "20140130",
	 		"alertExpirationDate" : "20140330",
	 		"alertPrice" : 49.99,
	 		"currPrice" : 54.99,
			"purchasePrice" : 61,
			"purchaseDate" : "20140129"
		},
 		{
 			"retailer"	: "toysrus",
			"alertId" : 1223,
			"productId" : 1232256,
	 		"name"		: "product3",
			"url"	: "http://www.toysrus.com/product/index.jsp?productId=12530155&cp=&parentPage=search",
	 		"imageUrl" : "http://www.toysrus.com/graphics/product_images/pTRU1-12088700v150.jpg",
	 		"alertActive" : 0,
	 		"alertCreationDate" : "20130130",
	 		"alertExpirationDate" : "20130330",
	 		"alertPrice" : 89.99,
	 		"currPrice" : 99.99,
			"purchasePrice" : 79,
			"purchaseDate" : "20130129"
		}

	]'));
		} else if($randomNumber <= 0.95){
			/* Partial Success */
			return new ResponseObject(ResponseErrorType::PARTIAL_SUCCESS, json_decode('[ 		{
 			"retailer"	: "toysrus",
			"alertId" : 1293,
			"productId" : 123456,
	 		"name"		: "product1",
			"url"	: "http://www.toysrus.com/product/index.jsp?productId=12530155&cp=&parentPage=search",
	 		"imageUrl" : "http://www.toysrus.com/graphics/product_images/pTRU1-12088700v150.jpg",
	 		"alertActive" : 1,
	 		"alertCreationDate" : "20140130",
	 		"alertExpirationDate" : "20140330",
	 		"alertPrice" : 89.99,
	 		"currPrice" : 99.99,
			"purchasePrice" : 101,
			"purchaseDate" : "20140130"
		},
 		{
 			"retailer"	: "walmart",
			"alertId" : 1223,
			"productId" : 1232256,
	 		"name"		: "product3",
			"url"	: "http://www.toysrus.com/product/index.jsp?productId=12530155&cp=&parentPage=search",
	 		"imageUrl" : "http://www.toysrus.com/graphics/product_images/pTRU1-12088700v150.jpg",
	 		"alertActive" : 0,
	 		"alertCreationDate" : "20130130",
	 		"alertExpirationDate" : "20130330",
	 		"alertPrice" : 89.99,
	 		"currPrice" : 99.99,
			"purchasePrice" : 61,
			"purchaseDate" : "20140129"
		}

	]'), "All data was not fetched.");
		} else {
			return new ResponseObject(ResponseErrorType::THRIFT, json_decode('[]'), "TSocket: Could not connect to localhost:28006 (Connection refused [111])");
		}
	}

	static function addPriceAlerts($user, $alerts) {
		$randomNumber = rand(0, getrandmax())/getrandmax();

		if($randomNumber <= 0.9) {
			return new ResponseObject(ResponseErrorType::SUCCESS, json_decode('[]'));

		} else if($randomNumber <= 0.95) {
			return new ResponseObject(ResponseErrorType::PARTIAL_SUCCESS, json_decode('{"products_failed": [
{
	"productId": 123457,
	"alertPrice": 17.99
}
]}'), "Some alerts were not added.");

		} else {
			return new ResponseObject(ResponseErrorType::THRIFT, json_decode('[]'), "TSocket: Could not connect to localhost:28006 (Connection refused [111])");
		}
	}


	static function updatePriceAlerts($user, $alerts) {
		$randomNumber = rand(0, getrandmax())/getrandmax();

		if($randomNumber <= 0.9) {
			return new ResponseObject(ResponseErrorType::SUCCESS, json_decode('[]'));

		} else if($randomNumber <= 0.95) {
			return new ResponseObject(ResponseErrorType::PARTIAL_SUCCESS, json_decode('{"products_failed": [123457]}'), "Some alerts were not changed.");

		} else {
			return new ResponseObject(ResponseErrorType::THRIFT, json_decode('[]'), "TSocket: Could not connect to localhost:28006 (Connection refused [111])");
		}
	}
}