<?php
/*
 * @author: Amit Mittal
 * 
 * This in particular is for testing user services.
 */
?>
<?php
/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));
	include '../services/thriftUserClient.php';
	
	use Thrift\Generated\User\UserException;
?>
<?php
	try{
		$user = new ThriftUserClient();
		if(isset($_GET['action'])){
			if($_GET['action']=="add"){
				$emailId = $_GET['emailId'];
				$name = $_GET['name'];
				$password = $_GET['password'];
				$country = $_GET['country'];
				$phone = $_GET['phone'];
				$newsletter = $_GET['newsletter'];
				$registered = $_GET['registered'];
				
				$val = $user->addUserThrift($emailId, $name, $password, $country, $phone, $newsletter, $registered);
				echo "Result of add user ".$val;
			} else if($_GET['action']=="update"){
				$emailId = $_GET['emailId'];
				$name = $_GET['name'];
				$password = $_GET['password'];
				$country = $_GET['country'];
				$phone = $_GET['phone'];
				$active = $_GET['active'];
				$newsletter = $_GET['newsletter'];
				$registered = $_GET['registered'];
				
				$val = $user->updateUserThrift($emailId, $name, $password, $country, $phone, $active, $newsletter, $registered);
				echo "Result of update user ".$val;
			} else if($_GET['action']=="login"){
				$emailId = $_GET['emailId'];
				$password = $_GET['password'];
				
				$val = $user->loginUserThrift($emailId, $password);
				echo "Result of login user ".$val;
			} else if($_GET['action']=="get"){
				$emailId = $_GET['emailId'];
				
				$getUser = $user->getUserThrift($emailId);

				echo "Got email id: ".$getUser->emailId."</br>";
				echo "Got name: ".$getUser->name."</br>";
				echo "Got password: ".$getUser->password."</br>";
				echo "Got country: ".$getUser->country."</br>";
				echo "Got phone: ".$getUser->phone."</br>";
				echo "Got last logged in: ".$getUser->lastLoggedIn."</br>";
				echo "Got active: ".$getUser->active."</br>";
				echo "Got newsletter: ".$getUser->newsletter."</br>";
				echo "Got registered: ".$getUser->registered."</br>";
			} else if($_GET['action']=="verify"){
				$emailId = $_GET['emailId'];
				
				$verified = $user->verifyUserThrift($emailId);
				
				if($verified)
					echo "User successfully verified</br>";
				else
					echo "Sorry!! User couldn't be verified</br>";
			} else{
				echo "Invalid action</br>";
			}
		} else{
			echo "Action not specified in the url</br>";
		}
	} catch(UserException $e) {
		echo "UserException thrown by java server:</br>";
		echo "Error code: ".$e->error_code."</br>";
		echo "Error message: ".$e->error_msg."</br>";
	} catch(Exception $e) {
		echo "Unexpected error in making thrift client object: ".$e->getMessage();
	} 
?>