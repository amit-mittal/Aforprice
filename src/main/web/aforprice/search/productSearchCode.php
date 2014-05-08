 <?php
	chdir(dirname(__FILE__));

	include_once 'searchClient.php';
	include_once '../common/constants.php';
	include_once'../services/thriftRetailerClient.php';
	include_once '../services/Thrift/Generated/Retailer/Types.php';
	include_once '../category/categoryClass.php';
	include_once '../product/ProductUI.php';
	include_once '../util/Util.php';
	include_once '../util/RefineBar.php';
	include_once '../util/CompareBar.php';
	include_once '../util/Pagination.php';
	use Thrift\Generated\Retailer\ProductFilter;
	use Thrift\Generated\Retailer\ProductFilterType;
	use Thrift\Generated\Retailer\LookupIdx;
	use Thrift\Generated\Retailer\SortCriterion;
?>
<?php
	$searchClient = new SearchClient();
	
	$retailerId = DEFAULT_RETAILER;
	if(array_key_exists('retailer', $_GET))
		$retailerId = $_GET['retailer'] ;
	$retailer = $retailerId;

	$thriftRetailerClient = new ThriftRetailerClient();
	
	//getting the supported sorts of the retailer
	$retailerObj = $thriftRetailerClient->getRetailer($retailer);
	$sortsSupported = $retailerObj->sortsSupported;
	$defaultSort = 5;//sort by relevance
	foreach ($sortsSupported as $sortSupported) {
		$sort[$sortSupported] = $sortSupported;
	}
	$sort[5] = 5;//relevance

	
	//setting up the category id if any
	$categoryId = -1;
	if(isset($_GET['category']) && is_numeric($_GET['category']))
		$categoryId = (int)$_GET['category'];


	//getting sortBy criteria or set default
	$sortBy = $defaultSort;										//default sort criterion 
	if (isset($_GET['sortBy']) && is_numeric($_GET['sortBy']))
		$sortBy = (int) $_GET['sortBy'];


	//getting filter criteria from url or set default
	$productFilters = array();
	$priceFilterFrom = 0;
	$priceFilterTo = -1;
	if (isset($_GET['priceFilterFrom']) && is_numeric($_GET['priceFilterFrom'])) {
		if (isset($_GET['priceFilterTo']) && is_numeric($_GET['priceFilterTo'])) {
			$priceFilterFrom = (int) $_GET['priceFilterFrom'];              // cast var as int
			$priceFilterTo = (int) $_GET['priceFilterTo'];                  // cast var as int
			if($priceFilterTo>$priceFilterFrom && $priceFilterFrom>=0){
				$productFilter = new ProductFilter();
				$productFilter->type = ProductFilterType::PRICE;
				$productFilter->from = $priceFilterFrom;
				$productFilter->to = $priceFilterTo;
				$productFilters[0] = $productFilter;
			} else {
				$priceFilterFrom = 0;
				$priceFilterTo = -1;
			}
		}
	}

	
	// get the current page from url or set a default
	$page = 1;
	if (isset($_GET['page']) && is_numeric($_GET['page']))
		$page = (int) $_GET['page'];                  			// cast var as int

	
	//setting page variables
	$rowLimit = Pagination::$rowLimit;                          //no of products per row
	$limit = Pagination::$limit;								//how many items to show per page
	
	$lookupIdx = new LookupIdx(); 
	$lookupIdx->startIdx = ($limit*($page-1) );
	$lookupIdx->endIdx = ($limit*$page);
	
	//setting search client variables
	$searchClient->limit = $limit;
	$searchClient->retailerId = $retailerId;
	$searchClient->categoryId = $categoryId;
	$searchClient->offset = ($limit*($page-1));
	$searchClient->sortBy = $sortBy;
	$searchClient->priceFilterFrom = (float)$priceFilterFrom;
	$searchClient->priceFilterTo = (float)$priceFilterTo;


	//query the sphinx client to get the resultSet
	$keyword = "";
	if(array_key_exists('keyword', $_GET))
		$keyword = $_GET['keyword'];

	$keyword = trim($keyword);//trimming to avoid white space making the search results irrevelant
	if(!(empty($keyword)))
		$resultProducts = $searchClient->getProducts($keyword);
	else 
		$resultProducts = array();

	
	//getting range of prices of products for setting filters
	$maxPrice = ceil($searchClient->getMaxPrice($keyword));                            //getting the max price among all products in a category
	$minPrice = floor($searchClient->getMinPrice($keyword));							 //getting the min price among all products in a category
	

	$count = sizeof($resultProducts);//number of products to show on current page
	$productsCount = $searchClient->totalCount;//total number of products
	
	if($lookupIdx->endIdx > $productsCount)
		$lookupIdx->endIdx = $productsCount;


	//setting up the url attributes
	$prefix = "search.php";
		
	$params = array();
	$params['retailer'] = $retailerId;
	$params['category'] = $categoryId;
	$params['keyword'] = $keyword;
	$params['page'] = $page;
	$params['sortBy'] = $sortBy;
	$params['priceFilterFrom'] = $priceFilterFrom;
	$params['priceFilterTo'] = $priceFilterTo;	
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
	<div id="title_subcat" class="bodbot">
		<div>
			<span class="title">Search Results</span>
		</div>
		<div class="reference">
			<ul>
				<li>
					<?php
						if((isset($keyword)) && !(empty($keyword)))
							echo "Query '".$keyword."' retrieved ".$searchClient->totalCount." matches in ".$searchClient->timeTaken." sec.<br>";
						if(isset($retailerId))
							echo "Filtering results by retailer: ".$retailerId."<br>";
						if($categoryId != -1)
							echo "Filtering results by categoryId: ".$categoryId."<br>";
						if($priceFilterFrom>0 && $priceFilterTo>=$priceFilterFrom)
							echo "Getting products within the price range: $".$priceFilterFrom." - $".$priceFilterTo."<br>";
					?>
				</li>
			</ul>
		</div>
	</div>
	<!--title Bar ends -->

	<!-- refine bar -->
	<?php
		$params['page'] = 1;
		
		$filterInfo['minPrice'] = $minPrice;
		$filterInfo['maxPrice'] = $maxPrice;

		RefineBar::GetRefineBarForSearchPage($prefix, $params, $filterInfo);
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
	<div id="overview" class="bodbot pad10 mg10lr">
	<?php
		$counter = 0;    
		for ($i=0; $i<$count; ){ ?>    
			<div class="pad10">
				<?php                    
					for ($j=1; $j<=$rowLimit; $j++) {
						if($counter <$count){
							ProductUI::PrintTerminalProduct($resultProducts[$i]);
							$i++;
							$counter+=1;
						}    
					}
					echo "<hr>";
				?>
			</div>
	<?php 
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