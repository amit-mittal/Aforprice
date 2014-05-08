<?php
/**
 * User: chirag
 * Date: 19/12/13
 * Time: 6:00 PM
 */

chdir(dirname(__FILE__));
	include_once '../common/constants.php';
	include_once '../util/JsonClean.php';
	include_once '../util/Logger.php';
	include_once '../category/Retailer.php';
	include_once 'ResponseError.php';
	include_once 'ResponseObject.php';
	include_once 'RequestType.php';

	include_once '../services/Thrift/Generated/Retailer/Types.php';
	include_once '../services/thriftRetailerClient.php';

	include_once '../dummy/dummyPriceAlerts.php';

	use Thrift\Exception\TException;

class RequestParser {
	private static $request;

	/* Initial function called for parsing of $_REQUEST object for plugin requests */
	public static function parse() {
		self::$request = $_REQUEST;

		if(isset(self::$request['task'])) {
			return self::doTask();
		} else {
			return self::missingArgument("task is missing");
		}
	}

    /* Checking the task, and calling the required function */
    private static function doTask() {
        $isValid = RequestType::isValid(self::$request['task']);

        if(!$isValid)
            return self::invalidArgument("Invalid task ".self::$request['task']);
        elseif($isValid === 2)
            $request = RequestType::getValueFromName(self::$request['task']);
        else
            $request = intval(self::$request['task']);

        switch($request) {
            case RequestType::GET_PRODUCTS:
                return self::getProducts();

            case RequestType::GET_PRODUCT_DROPS:
                return self::getPriceDrops();

            case RequestType::GET_ALERTS:
				return self::getPriceAlerts();

			case RequestType::ADD_ALERTS:
				return self::addPriceAlerts();

			case RequestType::UPDATE_ALERT:
				return self::updatePriceAlert();

            case RequestType::LOG_MESSAGE:
                return self::logMessage();

            default:
                return self::invalidArgument("Invalid task ".self::$request['task']);
        }
    }

	/* Function to return products given URL*/
	private static function getProducts() {
        /* Checking some always required parameters */
        if(isset(self::$request['retailer'])) {
            if(!Retailer::isValid(self::$request['retailer'])) {
                return self::invalidArgument("Invalid retailer ".self::$request['retailer']);
            }
        } else {
            return self::missingArgument("retailer is missing");
        }

		if(isset(self::$request['urls'])) {
			$urls = JsonClean::decode(self::$request['urls']);

			if($urls === NULL || !is_array($urls)) {
				return self::invalidArgument("URLs are invalid or is not an array.");
			} else {

				try {
					$thriftClient = new ThriftRetailerClient();
					$productData = $thriftClient->getProductsForURLs(
						self::$request['retailer'],
						$urls);

					$data = array(
						"productData" => $productData,

						/* TODO: Add event data here */
						"eventData" => array()
					);

					return new ResponseObject(ResponseErrorType::SUCCESS, $data);

				} catch(TException $ex) {
					return self::thriftError($ex->getMessage());
				} catch (Exception $ex) {
					return self::unknownError($ex->getMessage());
				}
			}
		} else {
			return self::missingArgument("Urls missing for products to be fetched");
		}
	}

	private static function getPriceDrops() {
        /* Checking some always required parameters */
        if(isset(self::$request['retailer'])) {
            if(!Retailer::isValid(self::$request['retailer'])) {
                return self::invalidArgument("Invalid retailer ".self::$request['retailer']);
            }
        } else {
            return self::missingArgument("retailer is missing");
        }

		if(isset(self::$request['urls'])) {
			$urls = JsonClean::decode(self::$request['urls']);

			if($urls == NULL || empty($urls))
				$urls = array();

			if(JsonClean::$error_id !== 0) {
				return self::invalidArgument("URLs are not proper");
			}
		} else {
			$urls = array();
		}

		if(isset(self::$request['max_products'])) {
			$max = intval(self::$request['max_products']);
			if($max === 0)
				$max = MAX_DROPS;
		} else {
			$max = MAX_DROPS;
		}

		try {
			$thriftClient = new ThriftRetailerClient();
			$data = $thriftClient->getPriceDropsForProductFamily(
				self::$request['retailer'],
				$urls,
				$max);

			return new ResponseObject(ResponseErrorType::SUCCESS, $data);

		} catch(TException $ex) {
			return self::thriftError($ex->getMessage());
		} catch (Exception $ex) {
			return self::unknownError($ex->getMessage());
		}
	}

	private static function logMessage() {
		if(!isset(self::$request['message'])) {
			return self::missingArgument("Missing argument message for logging.");
		}

		global $LOGGER;
		$LOGGER->logMessage(stripslashes($_REQUEST['message']));

		return new ResponseObject(ResponseErrorType::SUCCESS, "");
	}

    private static function getPriceAlerts() {
		if(!isset(self::$request['user']))
			return self::missingArgument("Missing user identity to fetch price alerts.");

		return dummyPriceAlerts::getPriceAlerts(self::$request['user']);
	}

    private static function addPriceAlerts() {
		if(!isset(self::$request['user']))
			return self::missingArgument("Missing user identity to fetch price alerts.");

		if(!isset(self::$request['alerts']))
			return self::missingArgument("Missing alerts to add.");

		return dummyPriceAlerts::addPriceAlerts(self::$request['user'], JsonClean::decode(self::$request['alerts']));
	}

	private static function updatePriceAlert() {
		if(!isset(self::$request['user']))
			return self::missingArgument("Missing user identity to fetch price alerts.");

		if(!isset(self::$request['alerts']))
			return self::missingArgument("Missing alerts to add.");

		return dummyPriceAlerts::updatePriceAlerts(self::$request['user'], JsonClean::decode(self::$request['alerts']));
	}

	/* To return Error response objects */
	public static function unknownError($msg = "", $data = array()) {
		global $LOGGER;
		$LOGGER->logError('[PLUGIN_REQUEST] : [UNKNOWN]: '.$msg);
		return new ResponseObject(ResponseErrorType::UNKNOWN, $data, $msg);
	}

	public static function invalidArgument($msg = "", $data = array()) {
		global $LOGGER;
		$LOGGER->logError('[PLUGIN_REQUEST] : [INVALID_ARGUMENT]: '.$msg);
		return new ResponseObject(ResponseErrorType::INVALID_ARGUMENT, $data, $msg);
	}

	public static function missingArgument($msg = "", $data = array()) {
		global $LOGGER;
		$LOGGER->logError('[PLUGIN_REQUEST] : [MISSING_ARGUMENT]: '.$msg);
		return new ResponseObject(ResponseErrorType::MISSING_ARGUMENT, $data, $msg);
	}

	public static function thriftError($msg = "", $data = array()) {
		global $LOGGER;
		$LOGGER->logError('[PLUGIN_REQUEST] : [THRIFT_ERROR]: '.$msg);
		return new ResponseObject(ResponseErrorType::THRIFT, $data, $msg);
	}

	public static function invalidRequest($msg = "", $data = array()) {
		global $LOGGER;
		$LOGGER->logError('[PLUGIN_REQUEST] : [INVALID_REQUEST]: '.$msg);
		return new ResponseObject(ResponseErrorType::INVALID_REQUEST, $data, $msg);
	}
}