<?php
include_once 'db_constants.php';
include_once 'db_class.php';
?>
<?php
class product {
	var $product_id;
	var $product_info = array();
	var $num_hist_prices;
	var $product_hist = array();
	
	function __construct($prod_id, $infoType = GET_PROD_ALL) {
		$this->product_id = $prod_id;
		
		switch($infoType) {
			case GET_PROD_ALL : self::setProductAll(); break;
			case GET_PROD_INFO : self::setProductInfo(); break;
			case GET_PROD_HIST : self::setProductHistory(); break;
			case GET_PROD_NONE : break;
		}
	}
	
	private function getDayOfYear($date) {
		$date = explode("-", $date);
		
		$month = intval($date[1]);
		$day = intval($date[2]);
		
		return (($month * 31) + $day);
	}
	
	function setProductInfo() {
		try {
			$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
			$con->connect();
			
			$query = "select PRODUCT.PRODUCT_NAME, PRODUCT.CATEGORY_ID, PRODUCT.RETAILER_ID, PRODUCT.GENERIC_PRODUCT_ID, PRODUCT.MODEL_NO, ".
			"PRODUCT.DESCRIPTION, PRODUCT.IMAGE_URL, PRODUCT.START_DATE, PRODUCT.END_DATE, PRODUCT.URL, PRODUCT.TIME_MODIFIED".		
			" from PRODUCT where `PRODUCT_ID` = ".$this->product_id." and `ACTIVE` = '1'";
			
			$con->makeQuery($query);
			
			$this->product_info = $con->getRow(true);
			$con->closeResult();
		}
		catch(DBConnectionException $e) {
			echo $e->getMessage();
		}
		catch(DBQueryException $e) {
			echo $e->getMessage();
		}
	}
	
	function setProductHistory() {
		try {
			$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
			$con->connect();
				
			$query = "select PRODUCT_PRICE_HISTORY.TIME, PRODUCT_PRICE_HISTORY.PRICE".
			" from PRODUCT_PRICE_HISTORY where `PRODUCT_ID` = ".$this->product_id;
			$con->makeQuery($query);
			
			$i = 0;
			while($row = $con->getRow()) {
				if($row[1] != '-99') {
					if($i) {
						if($this->product_hist[$i-1][1] != floatval($row[1])) {
							$this->product_hist[$i][0] = $this->getDayOfYear($row[0]);
							$this->product_hist[$i][1] = $this->product_hist[$i-1][1];
							$this->product_hist[$i][2] = $row[0];
							$i++;
						}
					}
					$this->product_hist[$i][0] = $this->getDayOfYear($row[0]);
					$this->product_hist[$i][1] = floatval($row[1]);
					$this->product_hist[$i][2] = $row[0];
					$i++;
				}
			}
			if($i) {
				$this->product_hist[$i][0] = $this->getDayOfYear(date("Y-m-d"));
				$this->product_hist[$i][1] = $this->product_hist[$i-1][1];	
			}
		}
		catch(DBConnectionException $e) {
			echo $e->getMessage();
		}
		catch(DBQueryException $e) {
			echo $e->getMessage();
		}
	}
	
	function setProductAll() {
		try {
			$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
			$con->connect();
			
			$query = "select PRODUCT.PRODUCT_NAME, PRODUCT.CATEGORY_ID, PRODUCT.RETAILER_ID, PRODUCT.GENERIC_PRODUCT_ID, PRODUCT.MODEL_NO, ".
			"PRODUCT.DESCRIPTION, PRODUCT.IMAGE_URL, PRODUCT.START_DATE, PRODUCT.END_DATE, PRODUCT.URL, PRODUCT.TIME_MODIFIED".		
			" from PRODUCT where `PRODUCT_ID` = ".$this->product_id." and `ACTIVE` = '1'";
			$con->makeQuery($query);
				
			$this->product_info = $con->getRow(true);
			$con->closeResult();
		
			$query = "select PRODUCT_PRICE_HISTORY.TIME, PRODUCT_PRICE_HISTORY.PRICE".
			" from PRODUCT_PRICE_HISTORY where `PRODUCT_ID` = ".$this->product_id;
			$con->makeQuery($query);
				
			$i = 0;
			while($row = $con->getRow()) {
				if($row[1] != '-99') {
					if($i) {
						if($this->product_hist[$i-1][1] != floatval($row[1])) {
							$this->product_hist[$i][0] = $this->getDayOfYear($row[0]);
							$this->product_hist[$i][1] = $this->product_hist[$i-1][1];
							$this->product_hist[$i][2] = $row[0];
							$i++;
						}
					}
					$this->product_hist[$i][0] = $this->getDayOfYear($row[0]);
					$this->product_hist[$i][1] = floatval($row[1]);
					$this->product_hist[$i][2] = $row[0];
					$i++;
				}
			}
			if($i) {
				$this->product_hist[$i][0] = $this->getDayOfYear(date("Y-m-d"));
				$this->product_hist[$i][1] = $this->product_hist[$i-1][1];	
			}
		}
		catch(DBConnectionException $e) {
			echo $e->getMessage();
		}
		catch(DBQueryException $e) {
			echo $e->getMessage();
		}
	}
};
?>