<?php
/*
* @author: Chirag Maheshwari
*
* this file contains the class Category
*
* It makes a connection to the thrift
* service (if that does not happen, it directly makes
* a connection to the database.)
* It retrieves the categories and makes a
* category upto a specified level. 
* */
?>
<?php
/*
 * Changing the current working directory first.
*/
chdir(dirname(__FILE__));

	include_once '../services/thriftRetailerClient.php';
	include_once 'categoryClass.php';
	include_once '../services/dbClient.php';
	include_once 'Retailer.php';
	
?>
<?php
	class CategoryException extends Exception {};
?>

<?php
class CategoryList {
	private $retailer;
	const level = '2';
	private $categoryConstructed;
	private $ThriftSuccess;
	private $levels = array(1, 2);
	
	private $categories;
	public $categoryMap;
	
	public function __construct($retail) {
		$this->categoryConstructed = FALSE;
		$this->ThriftSuccess = FALSE;

		if(Retailer::isValid($retail))
			$this->retailer = $retail;
		else
			throw new CategoryException('Invalid argument: the retailer does not match any of the available retailers');
	}
	
	private function getCategoriesthrift() {
		try {
			$client = new ThriftRetailerClient();
			
			if($client->connected) {
				$this->categories = $client-> getAllCategoriesByLevel($this->retailer, $this->levels);
				if(!isset($this->categories) || $this->categories == NULL)
					$this->ThriftSuccess = FALSE;
				else
					$this->ThriftSuccess = TRUE;
			} else {
				$this->ThriftSuccess = FALSE;
			}
		} catch (Exception $e) {
			echo "Error in getting categories from THRIFT: ".$e->getMessage();
			$this->ThriftSuccess = FALSE;
		}
	}
	
	public function getChildCategoriesThrift($categoryId) {
		try {
			$client = new ThriftRetailerClient();
			if($client->connected) {
				$categories = $client->getChildCategories($categoryId);
				if(!isset($categories) || is_null($categories) || count($categories) == 0)
					return null;
				else
					return $categories;
			} else {
				return null;
			}
		} catch (Exception $e) {
			echo "Error in getting categories from THRIFT: ".$e->getMessage();
			return null;
		}
	}
	
	/*
	 * Get categories directly from database, if
	 * no connection to the thrift client is found
	 */
	private function getCategoriesDB() {
	}
	
	private function buildCategoryMap() {
		$this->categoryMap = array();
		
		/*
		 * If we get a connection from thrift,
		 * we fetch the categories from thrift or 
		 * else from the database
		 */
		$this->getCategoriesthrift();
		if(!$this->ThriftSuccess)
			$this->getCategoriesDB();
		/*
		 * Constructing the category map from the fetched categories
		 */
		if(isset($this->categories) && $this->categories != NULL) {
			$levelOneCategories = array();
			$levelTwoCategories = $this->categories['2'];

			if($levelTwoCategories != null || !empty($levelTwoCategories)) {
				foreach ($levelTwoCategories as $levelTwoCategory) {
					
					if(!array_key_exists($levelTwoCategory->parentCategoryId, $levelOneCategories)) {
						$levelOneCategories[$levelTwoCategory->parentCategoryId] = new Category(
							$levelTwoCategory->parentCategoryId,
							$levelTwoCategory->parentCategoryName,
							"", 1);
					}

					$parentCategory = $levelOneCategories[$levelTwoCategory->parentCategoryId];
					$category = new Category(
						$levelTwoCategory->categoryId,
						$levelTwoCategory->categoryName,
						$levelTwoCategory->url,
						$levelTwoCategory->parent);

					$parentCategory->addChildCategory($category);
				}
			}

			$levelOneCategoriesFromThrift = $this->categories['1'];
			foreach ($levelOneCategoriesFromThrift as $levelOneCategory) {
				if(!array_key_exists($levelOneCategory->parentCategoryId, $this->categoryMap)) {
					$this->categoryMap[$levelOneCategory->parentCategoryId] = new Category(
						$levelOneCategory->parentCategoryId,
						$levelOneCategory->parentCategoryName,
						"", 1);
				}

				$parentCategory = $this->categoryMap[$levelOneCategory->parentCategoryId];
				if(array_key_exists($levelOneCategory->categoryId, $levelOneCategories)) {
					$parentCategory->addChildCategory($levelOneCategories[$levelOneCategory->categoryId]);
				} else {
					$category = new Category(
						$levelOneCategory->categoryId,
						$levelOneCategory->categoryName,
						$levelOneCategory->url,
						$levelOneCategory->parent);
					$parentCategory->addChildCategory($category);
				}
			}
		}

		//sorting the map
		usort($this->categoryMap, function($obj1, $obj2){
			return strcmp($obj1->name, $obj2->name);
		});
		$this->categoryConstructed = true;
	}
	
	public function getCategories() {
		if(!$this->categoryConstructed)
			$this->buildCategoryMap();
		
		return $this->categoryMap;
	}

	public function getRootCategories(){
		if(!$this->categoryConstructed)
			$this->buildCategoryMap();

		return array_values($this->categoryMap);
	}
};
?>