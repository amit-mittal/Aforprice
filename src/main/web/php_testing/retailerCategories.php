<?php
	include 'category.php';
?>
<?php
class retailerCategory {
	var $retailer;
	var $categories = array();
	const levels = 10;
	
	function __construct($retailer, $setCategory = true) {
		$this->retailer = $retailer;
		
		if($setCategory)
			$this->setCategoryStructure();
	}
	
	function setCategoryRecursive(&$con, $level, &$category) {		
		if($category->parentBool != 'Y' || $level > self::levels)
			return;
		$data = array();
		
		$query = "select CATEGORY.CATEGORY_ID, CATEGORY.CATEGORY_NAME, CATEGORY.GENERIC_CATEGORY_ID, CATEGORY.PARENT_CATEGORY_ID, ".
			"CATEGORY.RETAILER_ID, CATEGORY.CATEGORY_FETCH_RULE_ID, CATEGORY.URL, CATEGORY.ACTIVE, CATEGORY.PARENT, CATEGORY.TIME_MODIFIED".
		" from CATEGORY where CATEGORY.RETAILER_ID = '".$this->retailer."' and CATEGORY.PARENT_CATEGORY_ID = '".addslashes($category->categoryName)."' and CATEGORY.ACTIVE = '1' or CATEGORY.ACTIVE = 'Y'";
		$con->makeQuery($query);
		
		$i = 0;
		while($row = $con->getRow()) {
			$data[$i] = new Category($row[0], false);
			$data[$i]->setCategory($row, $category->categoryId, $level);
			$i++;
		}
		$category->setSubCategories($data);
		foreach($category->subCategories as $cat) {
			$this->setCategoryRecursive($con, $level+1, $cat);
		}
	}
	
	function setCategoryStructure() {
		$level = 0;
		$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
		try {
			$con->connect();
				
			$query = "select CATEGORY.CATEGORY_ID, CATEGORY.CATEGORY_NAME, CATEGORY.GENERIC_CATEGORY_ID, CATEGORY.PARENT_CATEGORY_ID, ".
			"CATEGORY.RETAILER_ID, CATEGORY.CATEGORY_FETCH_RULE_ID, CATEGORY.URL, CATEGORY.ACTIVE, CATEGORY.PARENT, CATEGORY.TIME_MODIFIED".
			" from CATEGORY where CATEGORY.RETAILER_ID = '".$this->retailer."' and CATEGORY.PARENT_CATEGORY_ID = '' and CATEGORY.ACTIVE = '1' or CATEGORY.ACTIVE = 'Y'";
			$con->makeQuery($query);
			
			$i = 0;
			while($row = $con->getRow()) {
				$this->categories[$i] = new Category($row[0], false);
				$this->categories[$i]->setCategory($row, NULL, $level);
				$i++;
			}
		}
		catch(DBConnectionException $e) {
			echo $e->getMessage();
		}
		catch(DBQueryException $e) {
			echo $e->getMessage();
		}

		$level++;
		foreach($this->categories as $category) {
			$this->setCategoryRecursive($con, $level, $category);
		}
	}
	
	function setGetCategoriesParent($parent, $level, $parentID) {
		$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
		try {
			$con->connect();
		
			$query = "select CATEGORY.CATEGORY_ID, CATEGORY.CATEGORY_NAME, CATEGORY.GENERIC_CATEGORY_ID, CATEGORY.PARENT_CATEGORY_ID, ".
			"CATEGORY.RETAILER_ID, CATEGORY.CATEGORY_FETCH_RULE_ID, CATEGORY.URL, CATEGORY.ACTIVE, CATEGORY.PARENT, CATEGORY.TIME_MODIFIED".
			" from CATEGORY where CATEGORY.RETAILER_ID = '".$this->retailer."' and CATEGORY.PARENT_CATEGORY_ID = '$parent' and CATEGORY.ACTIVE = '1' or CATEGORY.ACTIVE = 'Y'";
			$con->makeQuery($query);
				
			$i = 0;
			while($row = $con->getRow()) {
				$this->categories[$i] = new Category($row[0], false);
				$this->categories[$i]->setCategory($row, $parentID, $level);
				$i++;
			}
		}
		catch(DBConnectionException $e) {
			echo $e->getMessage();
		}
		catch(DBQueryException $e) {
			echo $e->getMessage();
		}
	}
	
	function printCategoriesTableFormat() {
		try {
			$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
			$con->connect();
		
			$query = "SELECT CATEGORY.CATEGORY_ID, CATEGORY.CATEGORY_NAME, CATEGORY.GENERIC_CATEGORY_ID, CATEGORY.PARENT_CATEGORY_ID, ".
			"CATEGORY.CATEGORY_FETCH_RULE_ID, CATEGORY.URL, CATEGORY.ACTIVE, CATEGORY.PARENT, CATEGORY.TIME_MODIFIED".
			" FROM CATEGORY WHERE CATEGORY.RETAILER_ID = '".$this->retailer."' and CATEGORY.ACTIVE = '1' or CATEGORY.ACTIVE = 'Y'";
			$con->makeQuery($query);
			
			echo "<table><tr><th>ID</th><th>Name</th><th>GenericCategory</th><th>ParentCategory</th><th>fetchRule</th>
			<th>active</th><th>parent</th></tr>";
			
			while($row = $con->getRow()) {
				echo "<tr>";
				echo "<td>".$row[0]."</td>";
				echo "<td><a href=\"".$row[5]."\">".$row[1]."</a></td>";
				echo "<td>".$row[2]."</td>";
				echo "<td>".$row[3]."</td>";
				echo "<td>".$row[4]."</td>";
				echo "<td>".$row[6]."</td>";
				echo "<td>".$row[7]."</td>";
				echo "</tr>";
			}
			echo "</table>";
		}
		catch(DBConnectionException $e) {
			echo $e->getMessage();
		}
		catch(DBQueryException $e) {
			echo $e->getMessage();
		}
	}
}
?>