<?php
	include_once 'db_constants.php';
	include_once 'db_class.php';
?>
<?php
class Category {
	var $categoryId;
	var $retailerId;
	var $categoryName;
	var $genericCategory;
	var $parentCategory;
	var $parentCategoryId;
	var $fetchRule;
	var $URL;
	var $activeBool;
	var $parentBool;
	var $timeModified;
	var $level;
	var $subCategories;
	
	function __construct($catId, $setValues = true) {
		$this->categoryId = $catId;
		
		if($setValues)
			$this->setCategoryInfo();
		else {
			$this->categoryName = NULL;
			$this->retailerId = NULL;
			$this->genericCategory = NULL;
			$this->parentCategory = NULL;
			$this->parentCategoryId = NULL;
			$this->URL = NULL;
			$this->activeBool = NULL;
			$this->parentBool = NULL;
			$this->fetchRule = NULL;
			$this->level = NULL;
			$this->timeModified = NULL;
			$this->subCategories = NULL;
		}
	}
	
	function setCategory($row, $parentId, $level) {
		$this->categoryName = $row[1];
		$this->genericCategory = $row[2];
		$this->parentCategory = $row[3];
		$this->parentCategoryId = $parentId;
		$this->retailerId = $row[4];
		$this->fetchRule = $row[5];
		$this->URL = $row[6];
		$this->activeBool = $row[7];
		$this->parentBool = $row[8];
		$this->level = $level;
		$this->timeModified = $row[9];
		$this->subCategories = NULL;
	}
	
	function setSubCategories($subCats) {
		$this->subCategories = $subCats;
	}
	
	function setCategoryInfo() {
		try {
			$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
			$con->connect();
			
			$query = "SELECT CATEGORY.CATEGORY_ID, CATEGORY.CATEGORY_NAME, CATEGORY.GENERIC_CATEGORY_ID,".
					 "CATEGORY.PARENT_CATEGORY_ID, CATEGORY.RETAILER_ID, CATEGORY.CATEGORY_FETCH_RULE_ID,".
					 "CATEGORY.URL, CATEGORY.ACTIVE, CATEGORY.PARENT, CATEGORY.TIME_MODIFIED FROM ".
					 "CATEGORY WHERE CATEGORY.CATEGORY_ID = ".$this->categoryId;
			$con->makeQuery($query);
			
			if($row = $con->getRow()) {
				$this->categoryName = $row[1];
				$this->genericCategory = $row[2];
				$this->parentCategory = $row[3];
				$this->parentCategoryId = NULL;
				$this->retailerId = $row[4];
				$this->fetchRule = $row[5];
				$this->URL = $row[6];
				$this->activeBool = $row[7];
				$this->parentBool = $row[8];
				$this->level = NULL;
				$this->timeModified = $row[9];
				$this->subCategories = NULL;
			}
			$con->closeResult();
		}
		catch(DBConnectionException $e) {
			echo $e->getMessage();
		}
		catch(DBQueryException $e) {
			echo $e->getMessage();
		}
	}
	
	function setLevel($newLevel) {
		$this->level = $newLevel;
	}
};
?>