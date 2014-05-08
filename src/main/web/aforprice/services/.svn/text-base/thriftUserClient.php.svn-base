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
 * This in particular is for user services.
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
	require_once 'Thrift/Generated/User/UserService.php';
	require_once 'Thrift/Generated/User/Types.php';
	
	use Symfony\Component\ClassLoader\UniversalClassLoader;
	use Thrift\Generated\User\UserServiceClient;
	use Thrift\Generated\User\UserThrift;
?>
<?php
class ThriftUserClient {
	private $thriftClient;
	public $connected;
	
	private $socket;
	private $transport;
	private $protocol;
	private $client;
	
	public function __construct() {
		$this->thriftClient = new ThriftClient("USER");
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
		
		$this->client = new UserServiceClient($this->protocol);
	}
	
	public function verifyUserThrift($emailId) {
		try {
			$this->transport->open();
	
			$verified = $this->client->verifyUserThrift($emailId);
			$this->transport->close();
			
			return $verified;
		} catch(Excpetion $e) {
			echo "thrift verifyUserThrift: ".$e->getMessage();
		}
	}
	
	public function addUserThrift($emailId, $name, $password, $country, $phone, $newsletter, $registered) {
		try {
			$this->transport->open();
	
			$user = new UserThrift();
			$user->emailId = $emailId;
			$user->name = $name;
			$user->password = $password;
			$user->country = $country;
			$user->phone = $phone;
			$user->newsletter = $newsletter;
			$user->registered = $registered;
			
			$added = $this->client->addUserThrift($user);
			$this->transport->close();
			
			return $added;
		} catch(Excpetion $e) {
			echo "thrift addUserThrift: ".$e->getMessage();
		}
	}
	
	public function updateUserThrift($emailId, $name, $password, $country, $phone, $active, $newsletter, $registered) {
		try {
			
			$this->transport->open();
	
			$user = new UserThrift();
			$user->emailId = $emailId;
			$user->name = $name;
			$user->password = $password;
			$user->country = $country;
			$user->phone = $phone;
			$user->active = $active;
			$user->newsletter = $newsletter;
			$user->registered = $registered;
			
			$updated = $this->client->updateUserThrift($user);
			$this->transport->close();
			
			return $updated;
		} catch(Excpetion $e) {
			echo "thrift updateUserThrift: ".$e->getMessage();
		}
	}
	
	public function loginUserThrift($emailId, $password) {
		try {
			$this->transport->open();
			$login = $this->client->loginUserThrift($emailId, $password);
			$this->transport->close();

			return $login;
		} catch(Excpetion $e) {
			echo "thrift loginUserThrift: ".$e->getMessage();
		}
	}
	
	public function getUserThrift($emailId) {
		try {
			$this->transport->open();
			$user = $this->client->getUserThrift($emailId);
			$this->transport->close();
			
			return $user;
		} catch(Excpetion $e) {
			echo "thrift getUserThrift: ".$e->getMessage();
		}
	}
};
?>