<?php
/*
 * @author: Chirag Maheshwari
 * 
 * this file contains the class Category
 * Stores the attributes related to
 * a category
 * */
?>

<?php
class Category{
	var $categoryId;
	var $name;		
	var $url;
	var $childCategories;
	var $parentCategory;
	var $parent;
	var $numberOfChildren;
	
	function __construct($categoryId, $name, $url, $parent) {
		$this->categoryId = $categoryId;
		$this->name = $name;
		$this->url = $url;
		$this->parent = $parent;
		$this->numberOfChildren = 0;
		$this->childCategories = null;
	}
	
	function hasChildren() {
		if($this->numberOfChildren)
			return true;
		else
			return false;
	}
	
	function addChildCategory($childCategory){
		if(is_null($this->childCategories))
			$this->childCategories = array();
		
		$this->childCategories[$this->numberOfChildren] = $childCategory;
		$this->numberOfChildren++;
	}
	
	function isParent(){
		return $this->parent;
	}
};
?>