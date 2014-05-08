<?php
/*
 * @author: Chirag Maheshwari
 * 
 * this file contains the generalised function
 * to make an connection to a thrift server.
 * 
 * This can be used to make connection to category
 * or product server.
 */
?>
<?php
/*
 * Changing the current working directory first.
 */
chdir(dirname(__FILE__));

	include_once 'THRIFT_constants.php';
?>
<?php
 	include_once 'Thrift/Symfony/UniversalClassLoader.php';
	include_once 'Thrift/Exception/TException.php';
	
 	use Symfony\Component\ClassLoader\UniversalClassLoader;
 	use Thrift\Transport\TSocket;
 	use Thrift\Transport\TBufferedTransport;
 	use Thrift\Protocol\TBinaryProtocol;
 	use Thrift\Exception\TException;
?>
<?php
	class ThriftException extends Exception {};
?>
<?php
class ThriftClient {
	private $serverType;
	
	private $port;
	private $host;
	
	private $connected;
	private $socket;
	private $transport;
	private $protocol;
	
	public function __construct($type) {
		if(!$this->checkType($type))
			throw new ThriftException('Invalid Argument: the type of thrift server called does not exist');
		$this->connected = FALSE;
		$this->setServerConstantsClass($type);

		$this->host = $this->serverType->getHost();
		
 		$loader = new UniversalClassLoader();
 		$loader->registerNamespace( 'Thrift', __DIR__.'/');
 		$loader->register();
	}
	
	public function getConnection() {
		if(!$this->connected)
			$this->makeConnection();
		return $this->connected;
	}
	
	private function makeConnection() {
		try {
			$this->port = $this->serverType->getServicePort();
			
 			$this->socket = new TSocket($this->host, $this->port);
 			$this->transport = new TBufferedTransport($this->socket);
 			$this->protocol = new TBinaryProtocol($this->transport);
 			$this->connected = TRUE;
		} catch (TException $e) {
			echo "Could not make connection with port: ".$this->port.", because: ".$e->getMessage();
			$this->connected = FALSE;
		}
	}
	
	/* private function makeSeed() {
		list($usec, $sec) = explode(' ', microtime());
		return (float) $sec + ((float) $usec * 100000);
	} */
	
	private function setServerConstantsClass($type) {
		switch($type) {
			case "RETAILER":
				$this->serverType = new THRIFT_RETAILER_CONSTANTS();
				return;
			
			case "PRICE_ALERT":
				$this->serverType = new THRIFT_PRICE_ALERT_CONSTANTS();
				return;
			
			case "USER":
				$this->serverType = new THRIFT_USER_CONSTANTS();
				return;

			case "CONFIG":
				$this->serverType = new THRIFT_CONFIG_CONSTANTS();
				return;

			default:
				return NULL;
		}
	}
	
	private function checkType($type) {
		switch($type) {
			case "RETAILER":
				return TRUE;
			case "PRICE_ALERT":
				return TRUE;
			case "USER":
				return TRUE;
			case "CONFIG":
				return TRUE;
			
			default:
				return FALSE;
		}
	}
	
	
	public function getSocket() {
		return $this->socket;
	}
	
	public function getTransport() {
		return $this->transport;
	}
	
	public function getProtocol() {
		return $this->protocol;
	}
};
?>