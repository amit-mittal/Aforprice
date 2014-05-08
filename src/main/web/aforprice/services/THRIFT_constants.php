<?php
/*
 * @author: Chirag Maheshwari
 * This file contains constants 
 * for the connection to a THRIFT
 * service.
 * */
?>
<?php
	include_once 'Thrift/Generated/Retailer/Types.php';
?>
<?php
/*
 * Thrift constant for the retailer thrift server
 */
class THRIFT_RETAILER_CONSTANTS {
	static $RETAILERS;
	static $RETAILER_SERVER_PORT;
	
	static $TOTAL_SERVER = '6';
	static $SERVER_HOST = 'localhost';
	#static $SERVER_HOST = '192.168.1.120';
	
	public static function setValues() {
		self::$RETAILERS = $GLOBALS['retailer_CONSTANTS']['RETAILERS'];
		self::$RETAILER_SERVER_PORT = $GLOBALS['retailer_CONSTANTS']['RETAILER_SERVER_PORT'];
	}
	
	public function getServicePort() {
		global $retailer;
		if(!array_key_exists($retailer, self::$RETAILERS)){
			echo "TECHNICAL_ERROR cannot find service for $retailer";
			return -1;
		}
				
		$serverId = self::$RETAILERS[$retailer];
		if(!array_key_exists($serverId, self::$RETAILER_SERVER_PORT)){
				echo "TECHNICAL_ERROR cannot find service port for serverid $serverid";
				return -1;
		}
		$portList = self::$RETAILER_SERVER_PORT[$serverId];
		
		//pick one service randomly
		return $portList[array_rand($portList, 1)];
	}
	
	public function getHost() {
		return self::$SERVER_HOST;
	}
};
THRIFT_RETAILER_CONSTANTS::setValues();
?>
<?php
/*
 * Thrift constant for the price alert thrift server
 */
class THRIFT_PRICE_ALERT_CONSTANTS {
	static $PORTS = array('25001', '25001');
	static $TOTAL_SERVER = '2';
	
	static $SERVER_HOST = 'localhost';
	
	public function getServicePort() {
		return self::$PORTS[array_rand(self::$PORTS, 1)];
	}
	
	public function getHost() {
		return self::$SERVER_HOST;
	}
};
?>
<?php
/*
 * Thrift constant for the user thrift server
 */
class THRIFT_USER_CONSTANTS {
	static $PORTS = array('26001', '26001');
	static $TOTAL_SERVER = '2';
	
	static $SERVER_HOST = 'localhost';
	
	public function getServicePort() {
		return self::$PORTS[array_rand(self::$PORTS, 1)];
	}
	
	public function getHost() {
		return self::$SERVER_HOST;
	}
};
/*
 * Thrift constant for the config thrift server
 */
class THRIFT_CONFIG_CONSTANTS {
	static $PORTS = array('27001', '27001');
	static $TOTAL_SERVER = '2';
	
	static $SERVER_HOST = 'localhost';
	
	public function getServicePort() {
		return self::$PORTS[array_rand(self::$PORTS, 1)];
	}
	
	public function getHost() {
		return self::$SERVER_HOST;
	}
};
?>