<?php
/**
 * User: chirag
 * Date: 19/12/13
 * Time: 6:00 PM
 */

chdir(dirname(__FILE__));
	include_once '../common/constants.php';
	include_once '../util/JsonClean.php';
	include_once '../category/Retailer.php';
	include_once 'ResponseError.php';
	include_once 'ResponseObject.php';
	include_once 'ProductFailedObject.php';
	
	include_once '../services/Thrift/Generated/Retailer/Types.php';
	include_once '../services/thriftRetailerClient.php';
	include_once '../services/thriftPriceAlertClient.php';
	use Thrift\Generated\PriceAlert\PriceAlertException;
	

class RequestParser {
	private static $request; //Chirag - will this create issue if multiple phps are running together?
	private static $expected_reply;

	/* Initial function called for parsing of $_REQUEST object for plugin requests */
	public static function parse() {
		self::$request = $_REQUEST;
		
		if (strtoupper(substr(PHP_OS, 0, 3)) !== 'WIN') {
		//temporary code to check Amrut's post messages
		$post_msg = print_r($_REQUEST,true);
		$file = "/var/log/crawler/post_body.txt";
		$txt = "POST msg: \n" . $post_msg . "\n\n";
		if (filesize($file) > 5*1024*1024) {
			$handle = fopen($file, 'r+');
			ftruncate($handle, 0);
			rewind($handle);
			fclose($handle);
		}		
		file_put_contents($file, $txt, FILE_APPEND);
		}
		/* Checking some always required parameters */
		if(isset(self::$request['data_type'])) {
			if(self::$request['data_type']!="real" && self::$request['data_type']!="dummy")	{
				return self::invalidArgument("Invalid data_type ".self::$request['data_type'].", expected value: real or dummy");
			}
		} else {
			return self::missingArgument("data_type is missing");
		}

		if(isset(self::$request['expected_reply']) && self::$request['data_type']=="real"){
			echo "expected_reply argument is only supported for dummy mode, not in ".self::$request['data_type']." mode";
			return self::invalidArgument("expected_reply argument is only supported for dummy mode, not in ".self::$request['data_type']." mode");
		}

		self::$expected_reply="normal";

		//input validations
		if(isset(self::$request['expected_reply'])){
			self::$expected_reply = self::$request['expected_reply'];
		}

		if(isset(self::$request['task'])) {
			return self::doTask();
		} else {
			return self::missingArgument("task is missing");
		}
	}

	/* Checking the task, and calling the required function */
	private static function doTask() {
				
		switch(self::$request['task']) {
			case 'get_retailers':
				return self::getRetailers();
				break;

			case 'get_product_by_upc':
				return self::getProductByUPC();
				break;

			case 'get_products_from_receipt':
				return self::getProductsFromReceipt();
				break;

			case 'track_products':
				return self::trackProducts();
				break;

			case 'change_alert':
				return self::changeAlert();
				break;

			case 'get_alerts':
				return self::getAlerts();
				break;
			case 'search_products':
				return self::searchProducts();
				break;

			case 'get_price_history':
				return self::getPriceHistory();
				break;
			case 'get_topdrops':
				return self::getTopDrops();
				break;
			case 'scan_receipt':
				return self::scanreceipt();
				break;
			case 'upload_credentials':
				return self::upload_credentials();
				break;
			default:
				return self::invalidArgument("Invalid task ".self::$request['task']);
		}
	}

	/* Function to return list of supported retailers*/
	private static function getRetailers() {
		if(self::$expected_reply=="failure"){
			echo '{
	 				"result": "failure",
	 				"text": "Unable to fetch retailer list due to system issue"
				}';
		}else{
			//echo dummy file
			include '../dummy/retailerlist.php';
		}
		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}

	/* Function to return top drops*/
	private static function getTopDrops() {
		if(!isset(self::$request['retailer'])) {
			echo "bad argument";
			return self::invalidArgument("Retailer argument is missing");
		}

		if(self::$expected_reply=="failure"){
			echo '{
	 				"result": "failure",
	 				"text": "Unable to fetch top drops "
				}';
		}else{
			//echo dummy file
			include '../dummy/topdrops.php';
		}
		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}

	private static function getProductByUPC() {
		//input validations
		if(!isset(self::$request['upc'])) {
			echo "bad argument";
			return self::invalidArgument("UPC argument is missing");
		}
		if(self::$expected_reply=="failure"){
			echo '{
	 				"result": "failure",
	 				"text": "Unable to fetch product due to system issue"
				}';
		}else{
			//echo dummy file
			include '../dummy/productbyupc.php';
		}

		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}

	private static function getProductsFromReceipt() {
		//input validations
		if(!isset(self::$request['receipt'])) {
			echo "bad argument";
			return self::invalidArgument("Receipt argument is missing");
		}
		if(self::$expected_reply=="failure"){
			echo '{
	 				"result": "failure",
	 				"text": "Unable to fetch products due to system issue"
				}';
		}else{
			//echo dummy file
			include '../dummy/productsfromreceipt.php';
		}

		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}

	private static function trackProducts() {
		//input validations
		if(!isset(self::$request['products'])) {
			echo "bad argument";
			return self::invalidArgument("products argument is missing");
		}
		if(!isset(self::$request['user'])) {
			echo "user argument is missing";
			return self::invalidArgument("user argument is missing");
		}

		if(self::$expected_reply=="failure"){
			echo '{
				"data":[],
				"error":true,
				"msg":"THRIFT: TSocket: Could not connect to localhost:28006 (Connection refused [111])",
			}';
		}else if(self::$expected_reply=="partial_success"){
			include '../dummy/trackproductspartialsuccess.php';
		}else{
				/*
				Products
				{[
					{"name":"product1", "productId":1000034, "purchasePrice":20.99, "alertPrice":10.99, "goodUntilDate":"20140707", "purchasedate":"20140507"},
					{"name":"product2", "productId":1000033, "purchasePrice":32.95, "alertPrice":15.99, "goodUntilDate":"20140806", "purchasedate":"20140404"}
				]}
				*/
				//iterate over each product and set alert
				$thriftClient = new ThriftPriceAlertClient();
				$products = JsonClean::decode(self::$request['products']);
				
				$products_failed = array();
				
				foreach($products as $product)
				{
					$alertType = 1;//alert when price is below this level						
					$result = $thriftClient->addPriceAlertThrift(self::$request['user'], $product['productId'], $product['alertPrice'], 
												$alertType, $product['goodUntilDate'], $product['purchasePrice']);
					//$result is either 1 (boolean) or error message(string)			
					if($result!=1){
						$x = new ProductFailedObject($product['productId'], $product['name'], $result);
						$products_failed[$product['productId']] = $x;
					}
				}
				$data = array();
				if(count($products_failed)>0){
					$data["products_failed"] = $products_failed;
					echo "products_failed".count($products_failed)."\n";
					return self::thriftError("some alerts failed in setup", $data);					
				};
				echo "here4";
				return new ResponseObject(ResponseErrorType::SUCCESS, $data);	
		
			/*
			//dummy data
			echo'{
				"data":[],

				"error":false,

				"msg":""
			}';
			*/
		}

		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}//trackProducts
	
	//=========================================
	private static function changeAlert() {
		//input validations
		if(!isset(self::$request['alerts'])) {
			echo "alerts argument is missing";
			return self::invalidArgument("products argument is missing");
		}
		if(!isset(self::$request['user'])) {
			echo "user argument is missing";
			return self::invalidArgument("products argument is missing");
		}


		if(self::$expected_reply=="failure"){
			echo '{
				"data":[],
				"error":true,
				"msg":"THRIFT: TSocket: Could not connect to localhost:28006 (Connection refused [111])",
			}';
		}else if(self::$expected_reply=="partial_success"){
			include '../dummy/changealertpartialsuccess.php';
		}else{
			//public function updatePriceAlertThrift($alertId, $emailId, $productId, $alertPrice, $alertType, $active, $goodUntilDate) {
		
			echo'{
				"data":[],
				"error":false,
				"msg":""
			}';
		}

		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}//changeAlert
	
	
	private static function codeToMessage($code) 
    { 
        switch ($code) { 
            case UPLOAD_ERR_INI_SIZE: 
                $message = "The uploaded file exceeds the upload_max_filesize directive in php.ini";
                break; 
            case UPLOAD_ERR_FORM_SIZE: 
                $message = "The uploaded file exceeds the MAX_FILE_SIZE directive that was specified in the HTML form"; 
                break; 
            case UPLOAD_ERR_PARTIAL: 
                $message = "The uploaded file was only partially uploaded"; 
                break; 
            case UPLOAD_ERR_NO_FILE: 
                $message = "No file was uploaded"; 
                break; 
            case UPLOAD_ERR_NO_TMP_DIR: 
                $message = "Missing a temporary folder"; 
                break; 
            case UPLOAD_ERR_CANT_WRITE: 
                $message = "Failed to write file to disk"; 
                break; 
            case UPLOAD_ERR_EXTENSION: 
                $message = "File upload stopped by extension"; 
                break; 

            default: 
                $message = "Unknown upload error"; 
                break; 
        } 
        return $message; 
    } 

	private static function scanreceipt() {
		//input validations
		if(!isset(self::$request['imageid'])) {
			echo "alerts argument is missing";
			return self::invalidArgument("imageid argument is missing");
		}

		if($_FILES["image"]["error"]>0)
  		{
  		  $message = self::codeToMessage($_FILES["image"]["error"]);	
		  $res = '{
			"data":{
		     "upload_finished": ["'.
        	    $_FILES["image"]["tmp_name"]
        		.'"]
				},
				"error":true,
				"msg":"'.$message.'"}';
				$file = "/var/log/crawler/post_body.txt";
				$txt = "Image upload error: \n" . $res . "\n\n";
				file_put_contents($file, $txt, FILE_APPEND);
				echo $res;
				return self::dummyResponse();
		      //return self::invalidArgument("No file was uploaded");
 		}

		///var/log/crawler
		//if ($_FILES["image"]["name"] == "more-0.jpg" )
		//{
        move_uploaded_file($_FILES['image']['tmp_name'], "/var/log/crawler/images/". $_FILES['image']['name']);
    	//}


		if(self::$expected_reply=="failure"){
			echo '{
				"data":[],
				"error":true,
				"msg":"THRIFT: TSocket: Could not connect to localhost:28006 (Connection refused [111])",
		}';
		}else if(self::$expected_reply=="partial_success"){
			include '../dummy/changealertpartialsuccess.php';
		}else{
			echo'{
			"data":{
		     "upload_finished": ["'.
        	    $_FILES["image"]["tmp_name"]
        	.'"]
		},
			"error":false,
			"msg":""
		}';
		}

		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}

	private static function upload_credentials() {
		//input validations
		$file = "/var/log/crawler/device_tokens/device_tokens.txt";
		$txt = "User Email: ".self::$request['user']."\n"."User UDID: ".self::$request['udid']."\n".
				 "User Device Token: ".self::$request['devicetoken']."\n\n";
	   file_put_contents($file, $txt, FILE_APPEND);
		if(self::$expected_reply=="failure"){
			echo '{
					"data":[],
					"error":true,
					"msg":"THRIFT: TSocket: Could not connect to localhost:28006 (Connection refused [111])"
				}';
		}else if(self::$expected_reply=="partial_success"){
			echo '{
					 "data":[],
					 "error":true,
					 "msg":"THRIFT: TSocket: Could not connect to localhost:28006 (Connection refused [111])"
				}';
		} else {
			echo '{
					 "data":[],
					 "error":false,
					 "success":true
					 "msg":"THRIFT: TSocket: Could not connect to localhost:28006 (Connection refused [111])"
				}';
		}
	}

	private static function getAlerts() {
		//input validations
		if(self::$expected_reply=="failure"){
			echo '{
					"data":[],
					"error":true,
					"msg":"THRIFT: TSocket: Could not connect to localhost:28006 (Connection refused [111])"
				}';
		}else if(self::$expected_reply=="partial_success"){
			include '../dummy/alerts_partial_success.php';
		} else {
			if(!isset(self::$request['user'])) {
				echo "user argument is missing";
				return self::invalidArgument("user argument is missing");
			}
			try {
				$thriftClient = new ThriftPriceAlertClient();
				$emailId = self::$request['user'];
				$data = $thriftClient->getPriceAlerts($emailId);
				if(count($data)!=0)
					return new ResponseObject(ResponseErrorType::SUCCESS, $data);	
			} catch(TException $ex) {
				//todo: log error here and return dummy data
				//include '../dummy/alerts.php';
				echo "$ex->getMessage()";
				return self::thriftError($ex->getMessage());
			} catch (Exception $ex) {
				//include '../dummy/alerts.php';
				$x = $ex->getMessage();
				echo "$x";
				return self::unknownError($ex->getMessage());
			}
			//default value
			include '../dummy/alerts.php';
			
		}
		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}

	private static function searchProducts() {
		//input validations
		if(!isset(self::$request['search_key'])) {
			echo "search_key argument is missing";
			return self::invalidArgument("search_key argument is missing");
		}
		if(self::$expected_reply=="failure"){
			echo '{	"result": "failure",
	 				"text": "No products found"}';
		}else {
			include '../dummy/searchresult.php';
		}

		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}

	private static function getPriceHistory() {
		//input validations
		if(!isset(self::$request['product'])) {
			echo "product argument is missing";
			return self::invalidArgument("product argument is missing");
		}
		if(self::$expected_reply=="failure"){
			echo '{	"result": "failure",
	 				"text": "No products found"}';
		}else {
			include '../dummy/pricehistoryofproduct.php';
		}

		//return dummy empty response so caller (request.php) doesn't encode above in json since its already json
		return self::dummyResponse();
	}

	/* To return Error response objects */
	public static function unknownError($msg = "", $data = array()) {
		return new ResponseObject(ResponseErrorType::UNKNOWN, $data, $msg);
	}

	public static function invalidArgument($msg = "", $data = array()) {
		return new ResponseObject(ResponseErrorType::INVALID_ARGUMENT, $data, $msg);
	}

	public static function missingArgument($msg = "", $data = array()) {
		return new ResponseObject(ResponseErrorType::MISSING_ARGUMENT, $data, $msg);
	}

	public static function thriftError($msg = "", $data = array()) {
		return new ResponseObject(ResponseErrorType::THRIFT, $data, $msg);
	}

	public static function invalidRequest($msg = "", $data = array()) {
		return new ResponseObject(ResponseErrorType::INVALID_REQUEST, $data, $msg);
	}

	public static function dummyResponse($msg = "", $data = array()) {
		return new ResponseObject(ResponseErrorType::DUMMY_RESPONSE, $data, $msg);
	}
}