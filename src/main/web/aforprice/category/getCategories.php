<?php
/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));
	require_once '../common/constants.php';
	include_once 'categoryList.php';
	include_once '../common/constants.php';

	/*
	 * Initializes a category list with the 
	 * given retailer.
	 * If no retailer is found or the retailer is an
	 * invalid one, it goes ahead with the default
	 * retailer
	 */
	if(isset($_GET['retailer'])) {
		$retailer = $_GET['retailer'];
	} else {
		$retailer = DEFAULT_RETAILER;
	}

	if(isset($_GET['category'])) {
		$categoryId = $_GET['category'];
	} else {
		$categoryId = -1;
	}
	
	try {
		$categoryList = new CategoryList($retailer);
	} catch(Exception $e) {
		echo "Unexpected error in getting the categories: ".$e->getMessage();
	}
?>
<?php
	$categories = null;
	try {
		$categories = $categoryList->getCategories();
	} catch (Exception $e) {
		echo "Unexpected error in getting the categories: ".$e->getMessage();
	}

	$childCategoryList = null;
	global $thriftClient;

	try {
		if(!isset($thriftClient)) {
			$thriftClient = new ThriftRetailerClient();
		}
	
		if($thriftClient->connected) {
			if($categoryId != -1) {
				$childCategoryList = $thriftClient->getChildCategories($categoryId);
			} else {
				$childCategoryList = array();
			}
		}
	} catch(Exception $e) {
		echo "Error in getting category children: ".$e->getMessage();
	}
	
	$categoryPath = null;
	try {
		if($thriftClient->connected) {
			if($categoryId != -1) {
				$categoryPath = $thriftClient->getCategoryPath($categoryId);
				$categoryPath = array_reverse($categoryPath);
			} else {
				$categoryPath = array();
			}
		}
	} catch(Exception $e) {
		echo "Error in getting category Path: ".$e->getMessage();
	}
?>