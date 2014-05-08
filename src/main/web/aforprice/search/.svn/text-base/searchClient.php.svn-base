 <?php

	//
	// $Id: test.php 2903 2011-08-04 13:30:49Z shodan $
	//

	/*
	 * Changing the current working directory first.
	 */
	chdir(dirname(__FILE__));

	require ("api/sphinxapi.php");
	include_once '../services/thriftRetailerClient.php';
	use Thrift\Generated\Retailer\SortCriterion;

	//////////////////////
	// parse command line
	//////////////////////

	// for very old PHP versions, like at my home test server

	// build query
	/*
	if ( !is_array($_SERVER["argv"]) || empty($_SERVER["argv"]) )
	{
		print ( "Usage: php -f test.php [OPTIONS] query words\n\n" );
		print ( "Options are:\n" );
		print ( "-h, --host <HOST>\tconnect to searchd at host HOST\n" );
		print ( "-p, --port\t\tconnect to searchd at port PORT\n" );
		print ( "-i, --index <IDX>\tsearch through index(es) specified by IDX\n" );
		print ( "-s, --sortby <CLAUSE>\tsort matches by 'CLAUSE' in sort_extended mode\n" );
		print ( "-S, --sortexpr <EXPR>\tsort matches by 'EXPR' DESC in sort_expr mode\n" );
		print ( "-a, --any\t\tuse 'match any word' matching mode\n" );
		print ( "-b, --boolean\t\tuse 'boolean query' matching mode\n" );
		print ( "-e, --extended\t\tuse 'extended query' matching mode\n" );
		print ( "-ph,--phrase\t\tuse 'exact phrase' matching mode\n" );
		print ( "-f, --filter <ATTR>\tfilter by attribute 'ATTR' (default is 'group_id')\n" );
		print ( "-fr,--filterrange <ATTR> <MIN> <MAX>\n\t\t\tadd specified range filter\n" );
		print ( "-v, --value <VAL>\tadd VAL to allowed 'group_id' values list\n" );
		print ( "-g, --groupby <EXPR>\tgroup matches by 'EXPR'\n" );
		print ( "-gs,--groupsort <EXPR>\tsort groups by 'EXPR'\n" );
		print ( "-d, --distinct <ATTR>\tcount distinct values of 'ATTR''\n" );
		print ( "-l, --limit <COUNT>\tretrieve COUNT matches (default: 20)\n" );
		print ( "--select <EXPRLIST>\tuse 'EXPRLIST' as select-list (default: *)\n" );
		exit;
	}
	$args = array();
	foreach ( $_SERVER["argv"] as $arg )
		$args[] = $arg;
	*/

	/*
	for ( $i=0; $i<count($args); $i++ )
	{
		$arg = $args[$i];

		if ( $arg=="-h" || $arg=="--host" )				$host = $args[++$i];
		else if ( $arg=="-p" || $arg=="--port" )		$port = (int)$args[++$i];
		else if ( $arg=="-i" || $arg=="--index" )		$index = $args[++$i];
		else if ( $arg=="-s" || $arg=="--sortby" )		{ $sortby = $args[++$i]; $sortexpr = ""; }
		else if ( $arg=="-S" || $arg=="--sortexpr" )	{ $sortexpr = $args[++$i]; $sortby = ""; }
		else if ( $arg=="-a" || $arg=="--any" )			$mode = SPH_MATCH_ANY;
		else if ( $arg=="-b" || $arg=="--boolean" )		$mode = SPH_MATCH_BOOLEAN;
		else if ( $arg=="-e" || $arg=="--extended" )	$mode = SPH_MATCH_EXTENDED;
		else if ( $arg=="-e2" )							$mode = SPH_MATCH_EXTENDED2;
		else if ( $arg=="-ph"|| $arg=="--phrase" )		$mode = SPH_MATCH_PHRASE;
		else if ( $arg=="-f" || $arg=="--filter" )		$filter = $args[++$i];
		else if ( $arg=="-v" || $arg=="--value" )		$filtervals[] = $args[++$i];
		else if ( $arg=="-g" || $arg=="--groupby" )		$groupby = $args[++$i];
		else if ( $arg=="-gs"|| $arg=="--groupsort" )	$groupsort = $args[++$i];
		else if ( $arg=="-d" || $arg=="--distinct" )	$distinct = $args[++$i];
		else if ( $arg=="-l" || $arg=="--limit" )		$limit = (int)$args[++$i];
		else if ( $arg=="--select" )					$select = $args[++$i];
		else if ( $arg=="-fr"|| $arg=="--filterrange" )	$cl->SetFilterRange ( $args[++$i], $args[++$i], $args[++$i] );
		else if ( $arg=="-r" )
		{
			$arg = strtolower($args[++$i]);
			if ( $arg=="bm25" )		$ranker = SPH_RANK_BM25;
			if ( $arg=="none" )		$ranker = SPH_RANK_NONE;
			if ( $arg=="wordcount" )$ranker = SPH_RANK_WORDCOUNT;
			if ( $arg=="fieldmask" )$ranker = SPH_RANK_FIELDMASK;
			if ( $arg=="sph04" )	$ranker = SPH_RANK_SPH04;
		}
		else
			$q .= $args[$i] . " ";
	}
	*/
	////////////
	// do query
	////////////
	/*
	if ( count($filtervals) )	$cl->SetFilter ( $filter, $filtervals );
	if ( $groupby )				$cl->SetGroupBy ( $groupby, SPH_GROUPBY_ATTR, $groupsort );
	if ( $sortby )				$cl->SetSortMode ( SPH_SORT_EXTENDED, $sortby );
	if ( $sortexpr )			$cl->SetSortMode ( SPH_SORT_EXPR, $sortexpr );
	if ( $distinct )			$cl->SetGroupDistinct ( $distinct );
	if ( $select )				$cl->SetSelect ( $select );
	if ( $limit )				$cl->SetLimits ( 0, $limit, ( $limit>1000 ) ? $limit : 1000 );
	*/
	//
	// $Id: test.php 2903 2011-08-04 13:30:49Z shodan $
	//
?>
<?php
//TODO the product is not shown if it does not has any review as it is sorted by reviews so fix that
class SearchClient {
	private $sphinxClient;
	public $q;
	public $sql;
	private $host;
	private $port;
	public $index;
	public $groupby;
	public $groupsort;
	public $filter;
	public $filtervals;
	public $distinct;
	public $sortexpr;
	public $ranker;
	public $select;

	public $mode;
	public $limit;//no. of products to be shown on 1 page
	public $retailerId;//retailerId
	public $categoryId;//-1 if no filter applied on it
	public $totalCount;//contains total count of the products
	public $timeTaken;//time taken by the query
	public $offset;//starting index of the list of products
	public $sortBy;//sort criterion
	public $priceFilterFrom;//minimum price of product
	public $priceFilterTo;//maximum price of product
	
	public function __construct() {
		$this->sphinxClient = new SphinxClient ();
		
		$this->q = "";
		$this->sql = "";
		$this->mode = SPH_MATCH_ALL;
		$this->host = "192.168.1.120";//production ip
		$this->port = 9312;
		$this->index = "productMain";
		$this->groupby = "";
		$this->groupsort = "@group desc";
		$this->filter = "group_id";
		$this->filtervals = array();
		$this->distinct = "";
		$this->sortBy = "";
		$this->sortexpr = "";
		$this->limit = 20;//default value
		$this->ranker = SPH_RANK_PROXIMITY_BM25;
		$this->select = "";
		if(strcmp(gethostname(), "Ashish-Laptop")=='0')
			$this->host = "127.0.0.1";
		else if(strcmp(gethostname(), "AMIT-PC")=='0')
			$this->host = "127.0.0.1";
		$this->sphinxClient->SetServer ( $this->host, $this->port );
		$this->sphinxClient->SetConnectTimeout ( 1 );
		$this->sphinxClient->SetArrayResult ( true );
		$this->sphinxClient->SetWeights ( array ( 100, 1 ) );
		$this->sphinxClient->SetMatchMode ( $this->mode );
		//$this->sphinxClient->SetRankingMode ( $ranker );
	}

	//returns minimum price of the resultant set
	public function getMinPrice($keyword){
		$tempSphinxClient = new SphinxClient();
		$tempSphinxClient->SetServer ( $this->host, $this->port );
		$tempSphinxClient->SetConnectTimeout ( 1 );
		$tempSphinxClient->SetArrayResult ( true );
		$tempSphinxClient->SetWeights ( array ( 100, 1 ) );
		$tempSphinxClient->SetMatchMode ( $this->mode );
		
		$minPrice = 0;
		if($this->categoryId != -1){
			$tempSphinxClient->SetFilter("PARENT_CATEGORY_ID", array($this->categoryId));
		};

		$tempSphinxClient->SetLimits (0, 1, 0, 0);
		$tempSphinxClient->SetSortMode (SPH_SORT_ATTR_ASC, "PRICE");
		$minResult = $tempSphinxClient->Query ($keyword, $this->index);
		if($minResult['total']>0){
			$minPrice = $minResult['matches'][0]['attrs']['price'];
		}
		return $minPrice;
	}

	//returns maximum price of the resultant set
	public function getMaxPrice($keyword){
		$tempSphinxClient = new SphinxClient();
		$tempSphinxClient->SetServer ( $this->host, $this->port );
		$tempSphinxClient->SetConnectTimeout ( 1 );
		$tempSphinxClient->SetArrayResult ( true );
		$tempSphinxClient->SetWeights ( array ( 100, 1 ) );
		$tempSphinxClient->SetMatchMode ( $this->mode );
		
		$maxPrice = 10000;
		if($this->categoryId != -1){
			$tempSphinxClient->SetFilter("PARENT_CATEGORY_ID", array($this->categoryId));
		};

		$tempSphinxClient->SetLimits (0, 1, 0, 0);
		$tempSphinxClient->SetSortMode (SPH_SORT_ATTR_DESC, "PRICE");
		$maxResult = $tempSphinxClient->Query ($keyword, $this->index);
		if($maxResult['total']>0){
			$maxPrice = $maxResult['matches'][0]['attrs']['price'];
		}
		return $maxPrice;
	}

	private function printLog($result){
		if(isset($result['matches'])) {
			$matches = $result['matches'];
			foreach($matches as $key => $product){
				var_dump($product);
			}
		}
	}

	public function getProducts($keyword) {
		//only search for given category
		if($this->categoryId != -1){
			$this->sphinxClient->SetFilter("PARENT_CATEGORY_ID", array($this->categoryId));
		};

		//sorting mode
		if(isset($this->sortBy)){
			if($this->sortBy==SortCriterion::PRICE_ASC)
				$this->sphinxClient->SetSortMode (SPH_SORT_ATTR_ASC, "PRICE");
			else if($this->sortBy==SortCriterion::PRICE_DESC)
				$this->sphinxClient->SetSortMode (SPH_SORT_ATTR_DESC, "PRICE");
			else if($this->sortBy==SortCriterion::BEST_SELLERS)
				$this->sphinxClient->SetSortMode (SPH_SORT_ATTR_ASC, "BEST_SELLER_RANK");
			else if($this->sortBy==SortCriterion::REVIEW_RATINGS)
				$this->sphinxClient->SetSortMode (SPH_SORT_ATTR_DESC, "REVIEW_RATING");
			else
				$this->sphinxClient->SetSortMode (SPH_SORT_RELEVANCE);
		}
		else{
			$this->sphinxClient->SetSortMode (SPH_SORT_RELEVANCE);
		}

		//price filter
		//TODO write now one problem as the filter in search is inclusive, so have to make sure no repetition in prods
		if($this->priceFilterFrom<=$this->priceFilterTo && $this->priceFilterFrom>=0){
			$this->sphinxClient->SetFilterFloatRange("PRICE", $this->priceFilterFrom, $this->priceFilterTo);
		}
		
		//putting offsets and limit
		$this->sphinxClient->SetLimits ($this->offset, $this->limit, 0, 0);

		//executing the query
		$result = $this->sphinxClient->Query ($keyword, $this->index);
		
		////////////////
		// print me out
		////////////////
		$matchProductIds=array();
		$products = array();//empty array of products
		
		//Printing the log
//		$this->printLog($result);
		
		if ( $result===false ) {
			print "<b>Query failed:</b> " . $this->sphinxClient->GetLastError() . ".\n";
			return $products;
		}

		if ( $this->sphinxClient->GetLastWarning() )
			print "<b>WARNING:</b> " . $this->sphinxClient->GetLastWarning() . "\n\n";

		print "Query '$keyword' retrieved $result[total] of $result[total_found] matches in $result[time] sec.<br>";
		print "<b>Query stats: </b><br>";
		if ( is_array($result["words"]) )
			foreach ( $result["words"] as $word => $info )
				print "'$word' found $info[hits] times in $info[docs] documents. $result[total] products left after filter.<br>";
		
		$this->totalCount = $result['total'];
		$this->timeTaken = $result['time'];

		if(!array_key_exists('matches', $result))
			return $products;
		
		if ( is_array($result["matches"]) )
		{
			$n = 1;
			foreach ( $result["matches"] as $docinfo )
			{
				$matchProductIds[$docinfo["id"]]=$docinfo["id"];
				$n++;
			}
		}

		$thriftRetailerClient = new ThriftRetailerClient();

		//it is must that this api return products in the same order as provided by the input proudctIds, since that is sorted
		try{
			$products = $thriftRetailerClient->getProductsByIds($matchProductIds);
		} catch(TException $tx) {
			echo "Thrift Exception: ".$tx->getMessage()."\r\n";
		}

	/*
	//	foreach($matchProductIds as $matchProductId)
		//{
			//todo: get connection from pool
			$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
			$con->connect();
			//todo: create queries file to store all queries
			//echo $_GET['categoryid'];
			//print_r($_GET);//to print get variables
			//$categoryId = $_GET['categoryid'];//ToDo:add error checking for missing categoryId
			//echo "<b>categoryid=".$categoryId."</b>";
			//TODO: below needs categoryid index on product_category table
			//todo: add microsecond timestamp for each query
			//todo: search for multiple productids at a time. also add 'active=1' in sphinx.conf for product_summary
			$productsInList= join ( ",", $matchProductIds );
			$query = "select PRODUCT_ID, PRODUCT_NAME, MODEL_NO, IMAGE_URL, URL, RETAILER_ID, BEST_SELLER_RANK from PRODUCT_SUMMARY".
					" where PRODUCT_ID in (".$productsInList.");"; //todo: change from limit to multip page display
		
			echo $query;
			$con->makeQuery($query);
		
			while($row = $con->getRow()) {
				$productId = $row[0];
				$product = new Product($row[0], $row[1], $row[2], $row[3], $row[4], $row[5], $row[6]);
				//get price history
				$priceQuery = "select TIME, PRICE from PRODUCT_PRICES_HISTORY where PRODUCT_ID=".$productId;
				$result = $con->executeQuery($priceQuery);
				while($priceRow = $result->fetch_row()){
					$product->addPrice($priceRow[0], $priceRow[1]);
				}
				$result->free();
				$products[$productId] = $product;
			}
		//}
	*/
		return $products;
	}
}
?>