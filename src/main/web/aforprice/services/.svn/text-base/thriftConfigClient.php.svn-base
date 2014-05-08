<?php
/*
 * @author: Amit Mittal
 * 
 * This is interface class for the services provided
 * by the thrift server.
 * 
 * It takes a thrift connection to initialize 
 * and can be called to return the results
 * of thrift services
 * 
 * This in particular is for config services.
 */
?>
<?php
/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));
	require_once 'thriftClient.php';
?>
<?php
	require_once 'Thrift/Generated/Config/ConfigService.php';
	require_once 'Thrift/Generated/Config/Types.php';
	
	use Symfony\Component\ClassLoader\UniversalClassLoader;
	use Thrift\Generated\Config\ConfigServiceClient;
	use Thrift\Generated\Config\Event;
	use Thrift\Generated\Config\DateObj;
?>
<?php
class ThriftConfigClient {
	private $thriftClient;
	public $connected;
	
	private $socket;
	private $transport;
	private $protocol;
	private $client;
	
	public function __construct() {
		$this->thriftClient = new ThriftClient("CONFIG");
		$tries = 3;
		do {
			$this->connected = $this->thriftClient->getConnection();
			$tries--;
		}while(!$this->connected && $tries);
		
		$loader = new UniversalClassLoader();
		$loader->registerNamespace( 'Thrift', __DIR__.'/');
		$loader->register();
		
		$this->setVariables();
	}
	
	private function setVariables() {
		$this->socket = $this->thriftClient->getSocket();
		$this->transport = $this->thriftClient->getTransport();
		$this->protocol = $this->thriftClient->getProtocol();
		
		$this->client = new ConfigServiceClient($this->protocol);
	}
	
	public function getEvents($startDate, $endDate) {
		try {
			$this->transport->open();
	
			$events = $this->client->getEvents($startDate, $endDate);
			$this->transport->close();
			
			return $events;
		} catch(Excpetion $e) {
			echo "thrift getEvents: ".$e->getMessage();
		}
	}
};
?>