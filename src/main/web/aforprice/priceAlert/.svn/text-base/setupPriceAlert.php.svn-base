<?php
	chdir(dirname(__FILE__));
	include_once '../services/thriftPriceAlertClient.php';
	use Thrift\Generated\PriceAlert\PriceAlertException;
?>

<?php
	//adding price alert if request received
	if (isset( $_POST["email"], $_POST["price"], $_POST["productId"], $_POST["emailID"])) {
		if ( $_POST["emailID"] != null )
			 $emailId = $_POST["emailID"]; 
		else if ( $_POST["email"] != null)		
			 $emailId =  $_POST["email"];
		$alertPrice =  $_POST["price"];
		$alertType = 0;
		$productId = $_POST["productId"];
		
		try{
			$priceAlert = new ThriftPriceAlertClient();
			$added = $priceAlert->addPriceAlertThrift($emailId, $productId, $alertPrice, $alertType);
			if($added)
				echo '<script type="text/javascript">alert("Alert added successfully");</script>';
			else
				echo '<script type="text/javascript">alert("Alert could not be added");</script>'; 		
		} catch(PriceAlertException $e){
			echo "PriceAlertException thrown by java server:</br>";
			echo "Error code: ".$e->error_code."</br>";
			echo "Error message: ".$e->error_msg."</br>";
		} catch(Exception $e) {
			echo "Unexpected error in making thrift client object: ".$e->getMessage();
		}
	}
?>

<div id="login_box" >
	<div id="login_box_content"></div>
	<div id="login_box_content">
		<form id="login_form" name = "login" method="post">
			<p></p>	
			<div id="alert_tab" class="btn-group" data-toggle="buttons-radio" >
			  <button type="button" class="btn active" data-toggle="button" >
				Member
				<input type="radio" name="is_private" value="1" />
			  </button>
			  <button type="button" class="btn" data-toggle="button">
				Guest
				<input type="radio" name="is_private" value="2" />
			  </button>
			</div>

			<div id="blk-1" class="toHide" style="">
				<br>
				<p><div id="fb-root"></div></p>
				<fb:login-button show-faces="true" width="200" max-rows="1" size="large" ></fb:login-button>
				<br>
				<p><input type="text" id="username" name = "username" placeholder="username" hidden /></p>	
				<input type="text" id="emailID" name = "emailID" placeholder="emailID" hidden /></p>
				<p><input type="text" id="productId" name = "productId" placeholder="productId" hidden /></p>
				<br>
				Set Alert point
				
				<input type="text" name = "price" id="sliderValue" placeholder="price"  />
				<input type="submit" value="Set !" /></p>		
			</div>
			
			<div id="blk-2" class="toHide" style="display:none">
				<br>
				<p><input type="text" id="email" name = "email" placeholder="email" /></p>	
				<br>
				<p><input type="text" id="productId" name = "productId" placeholder="productId" hidden /></p>
				
				Set Alert point
				
				<input type="text" name = "price" id="sliderValue" placeholder="price"  />
				<input type="submit" value="Set !" /></p>	
				<u><h2><?php echo "<a href=\"terminal.php?retailer=".$retailer."&category=".$categoryId."\">Sign up !!</a>" ?></h2></u>
				
			</div>
			
			<!--	 				
			<input type="text" name = "price" id="sliderValue" placeholder="price"  />
			<input type="submit" value="Set !" /></p>	-->
		</form>
	</div>			
</div>