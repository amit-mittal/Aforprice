<?php
/*
 * @author: Amit Mittal
 * 
 * This in particular is for testing price alert services.
 */
?>
<?php
/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));
	include '../services/thriftPriceAlertClient.php';
	
	use Thrift\Generated\PriceAlert\PriceAlertException;
?>
<?php
	try{
		$priceAlert = new ThriftPriceAlertClient();
		if(isset($_GET['action'])){
			if($_GET['action']=="add"){
				$emailId = $_GET['emailId'];
				$productId = $_GET['productId'];
				$alertPrice = $_GET['alertPrice'];
				$alertType = $_GET['alertType'];
				$goodUntilDate = $_GET['goodUntilDate'];
				$purchasePrice = $_GET['purchasePrice'];
				$name = $_GET['name'];
				$added = $priceAlert->addPriceAlertThrift($emailId, $productId, $alertPrice, $alertType, $goodUntilDate, $purchasePrice);
				
				if($added)
					echo "Alert successfully added for ".$name;
				else
					echo "Alert could not be added for ".$name;
			} else if($_GET['action']=="update"){
				$alertId = $_GET['alertId'];
				$emailId = $_GET['emailId'];
				$productId = $_GET['productId'];
				$alertPrice = $_GET['alertPrice'];
				$alertType = $_GET['alertType'];
				$goodUntilDate = $_GET['goodUntilDate'];
				$active = $_GET['active'];
				$name = $_GET['name'];
				$updated = $priceAlert->updatePriceAlertThrift($alertId, $emailId, $productId, $alertPrice, $alertType, $active, $goodUntilDate);
			
				if($updated)
					echo "Alert successfully updated for ".$name;
				else
					echo "Alert could not be updated for".$name;
			} else if($_GET['action']=="remove"){
				$alertId = $_GET['alertId'];
				
				$removed = $priceAlert->removePriceAlertThrift($alertId);
				
				if($removed)
					echo "Alert successfully added.";
				else
					echo "Alert could not be added.";
			} else if($_GET['action']=="get"){
				$emailId = $_GET['emailId'];
				$productId = $_GET['productId'];
				
				$getAlert = $priceAlert->getPriceAlertThrift($emailId, $productId);

				echo "Got alert id: ".$getAlert->alertId."</br>";
				echo "Got email id: ".$getAlert->emailId."</br>";
				echo "Got product id: ".$getAlert->productId."</br>";
				echo "Got alert price: ".$getAlert->alertPrice."</br>";
				echo "Got time modified time id: ".$getAlert->timeModified."</br>";
				echo "Got alert start time id: ".$getAlert->alertStartTime."</br>";
				echo "Got alert end time id: ".$getAlert->alertEndTime."</br>";
				echo "Got alert type id: ".$getAlert->alertType."</br>";
			} else if($_GET['action']=="getAll"){
				$emailId = $_GET['emailId'];
				
				$alertsList = $priceAlert->getPriceAlerts($emailId);
				echo "Displaying all alerts of the user: $emailId</br></br>";
				echo json_encode($alertsList);
			} else if($_GET['action']=="verify"){
				$alertId = $_GET['alertId'];
				
				$verified = $priceAlert->verifyPriceAlertThrift($alertId);
				
				if($verified)
					echo "Price Alert successfully verified</br>";
				else
					echo "Sorry!! Price Alert couldn't be verified</br>";
			} else{
				echo "Invalid action</br>";
			}
		} else{
			echo "Action not specified in the url</br>";
		}
	} catch(PriceAlertException $e){
		echo "PriceAlertException thrown by java server:</br>";
		echo "Error code: ".$e->error_code."</br>";
		echo "Error message: ".$e->error_msg."</br>";
	} catch(Exception $e) {
		echo "Unexpected error in making thrift client object: ".$e->getMessage();
	}
?>