<?php
/*
 * @author: Amit Mittal
 * 
 * This in particular is for testing config services.
 */
?>
<?php
/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));
	include '../services/thriftConfigClient.php';

use Thrift\Generated\Config\DateObj;
?>
<?php
	try{
		$configClient = new ThriftConfigClient();

		$startDate = new DateObj();
		$startDate->date = 1;
		$startDate->month = 1;
		$startDate->year = 2013;

		$endDate = new DateObj();
		$endDate->date = 4;
		$endDate->month = 7;
		$endDate->year = 2013;		

		$data = $configClient->getEvents($startDate, $endDate);

		var_dump($data);
	} catch(Exception $e) {
		echo "Unexpected error in making thrift client object: ".$e->getMessage();
	}
?>