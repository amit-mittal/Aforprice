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
 * This in particular is for price alert services.
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
	include_once 'Thrift/Symfony/UniversalClassLoader.php';
	require_once 'Thrift/Generated/PriceAlert/PriceAlertService.php';
	require_once 'Thrift/Generated/PriceAlert/Types.php';
	
	use Symfony\Component\ClassLoader\UniversalClassLoader;
	use Thrift\Generated\PriceAlert\PriceAlertServiceClient;
	use Thrift\Generated\PriceAlert\PriceAlertThrift;
	use Thrift\Generated\PriceAlert\PriceAlertTypeThrift;
?>
<?php
//TODO: Should not echo anything here, instead return some value which the other class will interpret as an error
class ThriftPriceAlertClient {
	private $thriftClient;
	public $connected;
	
	private $socket;
	private $transport;
	private $protocol;
	private $client;
	
	public function __construct() {
		$this->thriftClient = new ThriftClient("PRICE_ALERT");
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
		
		$this->client = new PriceAlertServiceClient($this->protocol);
	}

	/**
	 * @param $emailId
	 * @param $productId
	 * @param $alertPrice
	 * @param $alertType
	 *      0->ALERT_WHEN_AT_PRICE
	 *      1->ALERT_WHEN_PRICE_DROPS
	 * @return mixed
	 */
	public function addPriceAlertThrift($emailId, $productId, $alertPrice, $alertType, $goodUntilDate, $purchasePrice) {
		try {
			$this->transport->open();
	
			$alert = new PriceAlertThrift();
			$alert->emailId = $emailId;
			$alert->productId = $productId;
			$alert->alertPrice = $alertPrice;
			$alert->alertExpirationDate = $goodUntilDate;
			$alert->purchasePrice = $purchasePrice;
			//send dummy value for required fields, this won't be used
			$alert->alertId=-1;
			$alert->currPrice=-1;
			if($alertType==0)
				$alert->alertType = PriceAlertTypeThrift::ALERT_WHEN_AT_PRICE;
			else
				$alert->alertType = PriceAlertTypeThrift::ALERT_WHEN_PRICE_DROPS;
			$alert->alertActive = true;
			
			$added = $this->client->addPriceAlertThrift($alert);
			$this->transport->close();
			
			return $added;
		} catch(Exception $e) {
			$this->transport->close();
			$error="error_code=".$e->error_code.",message=".$e->error_msg;
			return $error;
		}
		return false;
	}
	
	public function updatePriceAlertThrift($alertId, $emailId, $productId, $alertPrice, $alertType, $active, $goodUntilDate) {
		try {
			$this->transport->open();
			
			$alert = new PriceAlertThrift();
			$alert->alertId = $alertId;
			$alert->emailId = $emailId;
			$alert->productId = $productId;
			$alert->alertPrice = $alertPrice;
			$alert->alertExpirationDate = $goodUntilDate;
			//send dummy value for required fields, this won't be used
			$alert->currPrice=-1;
			$alert->purchasePrice=-1;
			if($alertType==0)
				$alert->alertType = PriceAlertTypeThrift::ALERT_WHEN_AT_PRICE;
			else
				$alert->alertType = PriceAlertTypeThrift::ALERT_WHEN_PRICE_DROPS;
			$alert->alertActive = $active;
			
			$updated = $this->client->updatePriceAlertThrift($alert);
			$this->transport->close();
			
			return $updated;
		} catch(Excpetion $e) {
			echo "thrift updatePriceAlertThrift: ".$e->getMessage();
		}
		return false;
	}
	
	public function removePriceAlertThrift($alertId) {
		try {
			$this->transport->open();
			$removed = $this->client->removePriceAlertThrift($alertId);
			$this->transport->close();
			
			return $removed;
		} catch(Excpetion $e) {
			echo "thrift removePriceAlertThrift: ".$e->getMessage();
		}
		return false;
	}
	
	public function verifyPriceAlertThrift($alertId) {
		try {
			$this->transport->open();
			$verified = $this->client->verifyPriceAlertThrift($alertId);
			$this->transport->close();
		
			return $verified;
		} catch(Excpetion $e) {
			echo "thrift verifyPriceAlertThrift: ".$e->getMessage();
		}
		return false;
	}
	
	public function getPriceAlertThrift($emailId, $productId) {
		try {
			$this->transport->open();
			$alert = $this->client->getPriceAlertThrift($emailId, $productId);
			$this->transport->close();
			
			return $alert;
		} catch(Excpetion $e) {
			echo "thrift getPriceAlertThrift: ".$e->getMessage();
		}
		return null;
	}
	
	public function getPriceAlerts($emailId) {
		try {
			$this->transport->open();
			$alerts = $this->client->getPriceAlerts($emailId);
			$this->transport->close();
			
			return $alerts;
		} catch(Excpetion $e) {
			echo "thrift getPriceAlerts: ".$e->getMessage();
		}
		return null;
	}
	
	public function isAlertSet($emailId, $productId) {
		try {
			$this->transport->open();
			$alert = $this->client->getPriceAlertThrift($emailId, $productId);
			$this->transport->close();

			if(is_null($alert))
				return false;
			
			return $alert->alertActive;
		} catch(Excpetion $e) {
			echo "thrift getPriceAlertThrift: ".$e->getMessage();
		}
		return false;
		//TODO: check which value should be returned if some error comes in thrift
	}
};
?>