<?php
chdir(dirname(__FILE__));
	include_once 'product.php';
	include_once '../services/Thrift/Generated/Retailer/Types.php';
	include_once '../services/thriftConfigClient.php';
	include_once 'ProductUI.php';
	include_once '../util/Util.php';
	include_once '../util/RefineBar.php';
	include_once '../util/CompareBar.php';
	include_once '../util/Pagination.php';
	include_once '../category/categoryClass.php';
	use Thrift\Generated\Retailer\ProductFilter;
	use Thrift\Generated\Retailer\ProductFilterType;
	use Thrift\Generated\Retailer\LookupIdx;
	use Thrift\Generated\Retailer\SortCriterion;
    use Thrift\Generated\Config\DateObj;

	//TODO: after error checking if some category, etc. is missing, redirect it to some homepage
	if(isset($_GET['category']))
		$categoryId = $_GET['category'];                           //ToDo:add error checking for missing categoryId
	
	if(isset($_GET['retailer']))
		$retailerId = $_GET['retailer'];
	//$subCategory = $_GET['name'];
	

	// getRetailer , to check supported sorting
	$retailerObj = $thriftClient->getRetailer($retailerId);
	$sortsSupported = $retailerObj->sortsSupported;
	$defaultSort = $retailerObj->defaultSort;
	foreach ($sortsSupported as $sortSupported) {
		$sort[$sortSupported] = $sortSupported;
	}
		
		
	// get the current page from url or set a default
	$page = 1;
	if (isset($_GET['page']) && is_numeric($_GET['page']))
		$page = (int) $_GET['page'];                  // cast var as int
	

	//getting filter criteria from url or set default
	$productFilters = array();
	$priceFilterFrom = 0;
	$priceFilterTo = -1;
	$reviewFilterFrom = 0;
	$reviewFilterTo = -1;
	if (isset($_GET['priceFilterFrom']) && is_numeric($_GET['priceFilterFrom'])) {
		if (isset($_GET['priceFilterTo']) && is_numeric($_GET['priceFilterTo'])) {
			$priceFilterFrom = (int) $_GET['priceFilterFrom'];              // cast var as int
			$priceFilterTo = (int) $_GET['priceFilterTo'];                  // cast var as int
			if($priceFilterTo>$priceFilterFrom && $priceFilterFrom>=0){
				$productFilter = new ProductFilter();
				$productFilter->type = ProductFilterType::PRICE;
				$productFilter->from = $priceFilterFrom;
				$productFilter->to = $priceFilterTo;
				array_push($productFilters, $productFilter);
			} else {
				$priceFilterFrom = 0;
				$priceFilterTo = -1;
			}
		}
	}
	if (isset($_GET['reviewFilterFrom']) && is_numeric($_GET['reviewFilterFrom'])) {
		if (isset($_GET['reviewFilterTo']) && is_numeric($_GET['reviewFilterTo'])) {
			$reviewFilterFrom = (int) $_GET['reviewFilterFrom'];              // cast var as int
			$reviewFilterTo = (int) $_GET['reviewFilterTo'];                  // cast var as int
			if($reviewFilterTo>$reviewFilterFrom){
				$productFilter = new ProductFilter();
				$productFilter->type = ProductFilterType::REVIEW_RATINGS;
				$productFilter->from = $reviewFilterFrom;
				$productFilter->to = $reviewFilterTo;
				array_push($productFilters, $productFilter);
			} else {
				$reviewFilterFrom = 0;
				$reviewFilterTo = -1;
			}
		}
	}
	
	//getting sortBy criteria or set default
	$sortBy = $defaultSort;                      //default sort criterion
	if (isset($_GET['sortBy']) && is_string($_GET['sortBy']))
		$sortBy = (int) $_GET['sortBy'];
		
	
	//setting page variables
	$rowLimit = Pagination::$rowLimit;                          //no of products per row
	$limit = Pagination::$limit;								//how many items to show per page


	//calculate the indices of the products which we have to show
	$lookupIdx = new LookupIdx();
	$lookupIdx->startIdx = ($limit*($page-1));
	$lookupIdx->endIdx = ($limit*$page);

	
	// Calling  getProducts to get products 
	$resultProductList = $thriftClient->getProducts($categoryId, $lookupIdx, $productFilters, $sortBy);
	$resultProducts = $resultProductList->products;


	$count = sizeof($resultProducts);									//total number of products on the current page
	$productsCount = $resultProductList->totalCount;                    //total number of products	

	if($lookupIdx->endIdx > $productsCount)
		$lookupIdx->endIdx = $productsCount;


	//getting dropdown for price filter
	$filterInfo['price'] = $resultProductList->priceFilterToNumProdMap;
	$filterInfo['review'] = $resultProductList->reviewFilterToNumProdMap;


	//setting up the url parameters
	$prefix = "terminal.php";
		
	$params = array();
	$params['retailer'] = $retailerId;
	$params['category'] = $categoryId;
	$params['page'] = $page;
	$params['sortBy'] = $sortBy;
	$params['priceFilterFrom'] = $priceFilterFrom;
	$params['priceFilterTo'] = $priceFilterTo;
	$params['reviewFilterFrom'] = $reviewFilterFrom;
	$params['reviewFilterTo'] = $reviewFilterTo;


	$configClient = new ThriftConfigClient();

	$startDate = new DateObj();
	$startDate->date = 1;
	$startDate->month = 1;
	$startDate->year = 2013;

	$endDate = new DateObj();
	$endDate->date = 31;
	$endDate->month = 12;
	$endDate->year = 2013;		
	
	$data = $configClient->getEvents($startDate, $endDate);
	
	foreach($data as $i =>$event) { 
		$eventHistoryDesc[$i] = $event->description;
		$y = $event->eventDate->year;
		$m = $event->eventDate->month;
		$d = $event->eventDate->date;
		$eventHistoryArray[$i] = date("M-d-Y",(mktime(0,0,0,$m,$d,$y)));
	}
?>


<link href="css/pagination.css" rel="stylesheet" type="text/css" />		 
<link rel="stylesheet" href="css/alert_box.css" type="text/css" />	
<script type='text/javascript' src='http://www.google.com/jsapi'></script>
<script type="text/javascript" src="js/googleCharts.js"></script>
<script type="text/javascript" src="js/ibox.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/login.js"></script>


<!-- Content -->
<div id="content" class="mg10">
	<!-- title Bar -->
	<div id="title_subcat" >
	<?php
	//chdir(dirname(__FILE__));
		include_once '../category/categoryPageTitle.php';
	?>
	</div>
	<!--title Bar ends -->

	<!-- refine bar -->
	<?php
		$params['page'] = 1;
		
		RefineBar::GetRefineBarForTerminalPage($prefix, $params, $filterInfo);
	?>
	<!-- refine bar ends -->
			
	<!-- Pagination starts-->
	<?php
		$params['page'] = $page;

		$pageInfo = array();
		$pageInfo['page'] = $page;
		$pageInfo['totalProducts'] = $productsCount;
		$pageInfo['start'] = $lookupIdx->startIdx + 1;
		$pageInfo['end'] = $lookupIdx->endIdx;

		Pagination::GetPagination($prefix, $params, $pageInfo);
	?>
	<!-- Pagination ends -->
	
	<!--Compare bar Starts-->
	<?php
		$params['page'] = 1;
		
		CompareBar::GetCompareBar($prefix, $params, $sort);
	?>
	<!-- Compare bar ends -->

	<!-- Product Display Starts-->
	<div id="overview" class="pad10 mg10lr">
	<script>
		window.eventHistoryDesc = <?php echo json_encode($eventHistoryDesc); ?>;
		window.eventHistory = <?php echo json_encode($eventHistoryArray); ?>;
	</script>
	<?php
		for ($i=1; $i<=$count; $i++){
			ProductUI::PrintTerminalProduct($resultProducts[$i-1]);

			if($i%$rowLimit == 0) {
				if($i != $count)
					echo "<hr />";
			}
		}
	?>            
	</div>
	<!--Products Display ends -->	
		
	<!-- Compare Bar ( bottom ) starts -->	
	<?php
		$params['page'] = 1;
		
		CompareBar::GetCompareBar($prefix, $params, $sort);
	?>
	<!-- Compare Bar (bottom) ends -->

</div>
<!-- Content ends -->


<!--Price Alert login popup starts-->
<?php
	include_once('../priceAlert/setupPriceAlert.php');
?>
<!-- Price Alert login 	popup ends-->