<?php
/* 
 * @author: Chirag Maheshwari
 * 
 *  This is the interface class to the services provided
 *  by the RetailerCacheServer.
 *  
 *  It initializes by making a thrift connection
 *  and setting the required variables.
 *  */
?>
<?php
/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));
require_once 'thriftClient.php';
?>
<?php
require_once 'Thrift/Generated/Retailer/RetailerCache.php';
require_once 'Thrift/Generated/Retailer/Types.php';

use Symfony\Component\ClassLoader\UniversalClassLoader;
use Thrift\Generated\Retailer\RetailerCacheClient;

?>
<?php
class ThriftRetailerClient {
	private $thriftClient;
	public $connected;

	private $socket;
	private $transport;
	private $protocol;
	private $client;

	public function __construct() {
		$this->thriftClient = new ThriftClient('RETAILER');
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

 		$this->client = new RetailerCacheClient($this->protocol);
	}

	function getRetailerCategoryMap(){
		try {
			$this->transport->open();
			$retailerCategoryMap = $GLOBALS['retailer_CONSTANTS']['RETAILER_TO_CATEGORY_ID_MAP'];
			$this->transport->close();
			
			return $retailerCategoryMap;
		} catch(Exception $tx) {
			throw $tx;
		}
	}
	
	/* All the interface functions */
	/* 
	 * Retailer getRetailer(1:string retailerId);
	 *  */
	function getRetailer($retailerId)
	{
		try {
			$this->transport->open();
			$retailer = $this->client->getRetailer($retailerId);
			$this->transport->close();
			
			return $retailer;
		} catch(Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * list<Category> getHomePageCategories(1:string retailerId);
	 *  */
	function getHomePageCategories($retailerId)
	{
		try {
			$this->transport->open();
			$categoryList = $this->client->getHomePageCategories($retailerId);
			$this->transport->close();
				
			return $categoryList;
		} catch(Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * list<Category> getChildCategories(1:i32 categoryId);
	 *  */
	function getChildCategories($categoryId)
	{
		try {
			$this->transport->open();
			$categoryList = $this->client->getChildCategories($categoryId);
			$this->transport->close();
		
			return $categoryList;
		} catch(Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * list<Category> getCategoryPath(1:i32 categoryId);
	 *  */
	function getCategoryPath($categoryId)
	{
		try {
			$this->transport->open();
			$categoryPath = $this->client->getCategoryPath($categoryId);
			$this->transport->close();
			
			return $categoryPath;
		} catch(Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * map<i32,list<PriceDrop>> getPriceDrops(1:i32 categoryId);
	 *  */
	function getPriceDrops($categoryId)
	{
		try {
			$this->transport->open();
			$categoryToPriceDropMap = $this->client->getPriceDrops($categoryId);
			$this->transport->close();
			
			return $categoryToPriceDropMap;
		} catch (Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * ProductList getProducts(1:i32 categoryId, 
	 * 2:LookupIdx lookup, 
	 * 3:list<ProductFilter> filter, 
	 * 4:SortCriterion sortCriterion);
	 *  */
	function getProducts($categoryId,
			$lookup,
			$filterList,
			$sortCriterion)
	{
		try {
			$this->transport->open();
			$productList = $this->client->getProducts($categoryId,$lookup, $filterList, $sortCriterion);
			$this->transport->close();
				
			return $productList;
		} catch (Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * list<Product> getProductsByIds(1:list<i32> productIdList);
	 *  */
	function getProductsByIds($productIdList)
	{
		try {
			$this->transport->open();
			$productList = $this->client->getProductsByIds($productIdList);
			$this->transport->close();
			
			return $productList;
		} catch (Exception $tx) {
			echo "am here!!";
			throw $tx;
		}
	}
	
	/* 
	 * ProductList getTopPriceDropsByCategory(1:i32 categoryId, 
	 * 2:LookupIdx lookup);
	 *  */
	function getTopPriceDropsByCategory($categoryId, $lookup)
	{
		try {
			$this->transport->open();
			$productList = $this->client->getTopPriceDropsByCategory($categoryId, $lookup);
			$this->transport->close();
				
			return $productList;
		} catch (Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * ProductList getTopPriceDropsByRetailer(1:string retailer, 2:LookupIdx lookup);
	 *  */
	function getTopPriceDropsByRetailer($retailer, $lookup)
	{
		try {
			$this->transport->open();
			$productList = $this->client->getTopPriceDropsByRetailer($retailer, $lookup);
			$this->transport->close();
		
			return $productList;
		} catch (Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * map<i32, ProductList> getPriceDropsByCategory(1:i32 categoryId, 2:i32 max);
	 *  */
	function getPriceDropsByCategory($categoryId, $max)
	{
		try {
			$this->transport->open();
			$categoryToProductListMap = $this->client->getPriceDropsByCategory($categoryId, $max);
			$this->transport->close();
		
			return $categoryToProductListMap;
		} catch (Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * map<i32, ProductList> getPriceDropsByRetailer(1:string retailer, 2:i32 max);
	 *  */
	function getPriceDropsByRetailer($retailer, $max)
	{
		try {
			$this->transport->open();
			$categoryToProductListMap = $this->client->getPriceDropsByRetailer($retailer, $max);
			$this->transport->close();
	
			return $categoryToProductListMap;
		} catch (Exception $tx) {
			throw $tx;
		}
	}

	/* map<i32, list<Category>> getAllCategoriesByLevelForRetailer(
			1:string retailerId,
			2:list<i32> levels) 
	*/
	function getAllCategoriesByLevel($retailer, $listoflevels)
	{
		try {
			$this->transport->open();
			$categoriestolevelmap = $this->client->getAllCategoriesByLevelForRetailer($retailer, $listoflevels);
			$this->transport->close();

			return $categoriestolevelmap;
		} catch(Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * map<string, Product> getProductsForURLs(1:string retailer, 2:list<string> urls)
	 *  */
	function getProductsForURLs($retailer, $urls)
	{
		try {
			$this->transport->open();
			$urlToProductMap = $this->client->getProductsForURLs($retailer, $urls);
			$this->transport->close();

			return $urlToProductMap;
		} catch(Exception $tx) {
			throw $tx;
		}
	}
	
	/* 
	 * map<string, ProductList> getPriceDropsForProductFamily(1:string retailer, 2:list<string> urls, 3:i32 max)
	 *  */
	function getPriceDropsForProductFamily($retailer, $urls, $max)
	{
		try {
			$this->transport->open();
			$urlToProductListMap = $this->client->getPriceDropsForProductFamily($retailer, $urls, $max);
			$this->transport->close();

			return $urlToProductListMap;
		} catch(Exception $tx) {
			throw $tx;
		}
	}
};
?>