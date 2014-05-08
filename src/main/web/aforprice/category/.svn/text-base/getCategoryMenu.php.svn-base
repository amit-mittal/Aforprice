<?php
/*
 * @author: Chirag Maheshwari
 * Generates the category tree(two level) from the
 * category map created by the categoryList
 * class.
 * If a get value for "async" is set to true,
 * it will generate a JSON of the category tree
 * for ajax requests.
 */
?>
<?php
chdir(dirname(__FILE__));
	require_once 'getCategories.php';
?>
<?php	
	if(isset($_GET['async'])) {
		$async = intval($_GET['async']);
		if($async == 1)
			$async = TRUE;
		else
			$async = FALSE;
	} else {
		$async = FALSE;
	}
?>
<?php
function echoCategoryMenuJSON() {
	/* TODO: (Chirag) - extend the 3 level category menu to JSON too. */
	global $retailer,
		$categories;
	
	$json = array();
	if(!isset($categories) || $categories == NULL || count($categories) == 0) {
		array_push($json, array(
				"retailer" => $retailer,
				"categories" => NULL));
	} else {
		array_push($json, array(
				"retailer" => $retailer));
		$cat = NULL;
		$children = NULL;
		foreach($categories as $category) {
			$children = array();
			foreach($category->childCategories as $subCat) {
				array_push($children, array(
						"name" => $subCat->name,
						"ID" => $subCat->categoryId,
						"child" => NULL,
						"parent" => $subCat->parent));
			}
			$cat = array(
					"name" => $category->name,
					"ID" => $category->categoryId,
					"child" => $children);
				
			array_push($json, $cat);
			$children = NULL;
			$cat = NULL;
		}
	
		echo json_encode($json);
	}
}
?>
<?php
function checkNewCategoryColumn(&$categoriesPerColumn, $totalCats) {
	$categoriesPerColumn++;
	if($totalCats > 25)
		$totalCats = 25;

	if($categoriesPerColumn > $totalCats) {
		echo "</ul></div><div><ul>";
		$categoriesPerColumn = 0;
	}
}

function echoCategoryMenu() {
	global $retailer,
		$categories;
	
	if(!isset($categories) || $categories == NULL || count($categories) == 0) {
		echo "<div>No categories found for retailer, ".$retailer."!</div>";
	} else {
		/*
		 * If everything is correct we will reach here with the
		* categories.
		*
		* Thus forming a defined structure (as the HTML for the tree)
		*/
	
		echo "<div class=\"main_menu\"><ul>";
	
		/*
		 * Echoing the list of parent Categories
		*/
		$subCategories = array(); $i=1;
		foreach($categories as $category) {
			echo "<li data-catid=\"cat_".$i."\"><a href=\"category.php?retailer=".$retailer."&category=".$category->categoryId."\">".$category->name."</a><div class=\"rightArrow\"></div></li>";
			$subCategories[$i] = $category->childCategories;
				
			$i++;
		}
	
		echo "</ul><div style=\"border-top: 1px solid #0066a8; margin: 10px 5px 0px; font-weight: normal; padding: 10px;\"><a href=\"categoryDirectory.php?retailer=".$retailer."\"> Full Category Directory</a></div>";
		echo "</div><div class=\"sub_menu\" style=\"position: relative;\">	<div class=\"menu_divider\" style=\"position: absolute; height: 100%; left: 0px; top: 0px; border-left: 1px solid #DEDEDE; border-right: 2px solid #F7F7F7;\"></div>";
		/*
		 * Echoing the second level of categories
		*/
		$totalCats = count($subCategories);
		$categoriesPerColumn = 0;
		for($i=1; $i < $totalCats; $i++) {
			$categoriesPerColumn = 0;
			echo "<div id=\"cat_".$i."\" class=\"sub_menu_data\"><div><ul>";
			foreach($subCategories[$i] as $category) {
				if(!$category->parent) {
					echo "<li><a href=\"terminal.php?retailer=".$retailer."&category=".$category->categoryId."\">".$category->name."</a></li>";
				}
				else {
					echo "<li class=\"parent\"><a href=\"category.php?retailer=".$retailer."&category=".$category->categoryId."\">".$category->name."</a></li>";

					if($category->numberOfChildren > 0) {
						/* 3rd level of categories */
						foreach ($category->childCategories as $childCategory) {
							if(!$childCategory->parent) {
								echo "<li><a href=\"terminal.php?retailer=".$retailer."&category=".$childCategory->categoryId."\">".$childCategory->name."</a></li>";
							}
							else {
								echo "<li class=\"parent\"><a href=\"category.php?retailer=".$retailer."&category=".$childCategory->categoryId."\">".$childCategory->name."</a></li>";
							}

							checkNewCategoryColumn($categoriesPerColumn, $totalCats);
						}
					}
				}

				checkNewCategoryColumn($categoriesPerColumn, $totalCats);
			}
			echo "</ul></div></div>";
		}
	
		echo "</div>";
	}
}
?>
<?php
if(!$async) {
	/* 
	 * Echo the default retailer from the url. */
	echo "<script type=\"text/javascript\">var retailer=\"".$retailer."\"</script>";
	
	echoCategoryMenu();
} else {
	echoCategoryMenuJSON();
}
?>