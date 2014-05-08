<?php
/*
 * @author: Chirag Maheshwari
 * Generates the Full category tree from the
 * category map created by the categoryList
 * class.
 * If a get value for "async" is set to true,
 * it will generate a JSON of the category tree
 * for ajax requests.
 */
?>
<?php
/* 
 * This way was to print the category tree in the form of tables	
 * 
 * function printCategoryChildren(&$childCategories) {
		global $retailer, $categoryList;
		echo "<table class=\"child_category\">";
		foreach ($childCategories as $key => $value) {
			echo "<tr>";
				echo "<td>";
					echo "<a href=\"categoryPage.php?retailer=".$retailer."&amp;category=".$value->categoryId."\">".$value->name."</a>";
				echo "</td>";
			echo "<td>";
// 			$children = $categoryList->getChildCategoriesThrift($value->categoryId);
// 			printCategoryChildren($children);
			echo "</td>";
			echo "</tr>";
		}
		echo "</table>";
	}
	
	if(!isset($categories) || is_null($categories) || count($categories) == 0) {
		echo "No categories found for retailer, ".$retailer;
	} else {
		echo "<div id=\"category_directory\"><table>";
		foreach ($categories as $key => $value) {
			echo "<tr>";
				echo "<td>";
					echo "<a href=\"categoryPage.php?retailer=".$retailer."&amp;category=".$value->categoryId."\">".$value->name."</a>";
				echo "</td>";
				echo "<td>";
					if($value->hasChildren())
						printCategoryChildren($value->childCategories);
				echo "</td>";
			echo "</tr>";
		}
		echo "</table></div>";
	} */
/* 
 * Printing the category tree in the form of four 
 * column divisions
 * */

if(!isset($categories) || is_null($categories) || count($categories) == 0) {
	echo "No categories found for retailer, ".$retailer;
} else {
	$categories_per_column = floor((count($categories)/4));
	$counter_per_columns = 0;
	$counter_columns = 1;
	echo "<div>Categories for retailer: ".$retailer."</div>";
	echo "<div class=\"one_fourth\">";
	$counter = 0;
	foreach ($categories as $key => $value) {
		$counter++;
		echo "<div class=\"header\">";
		echo "<a href=\"category.php?retailer=".$retailer."&amp;category=".$value->categoryId."\">".$value->name."</a>";
		echo "</div>";
		
		echo "<ul>";
			foreach($value->childCategories as $key2 => $value2) {
				echo "<li>";
				if(!$value2->parent)
					echo "<a href=\"terminal.php?retailer=".$retailer."&amp;category=".$value2->categoryId."\">".$value2->name."</a>";
				else
					echo "<a href=\"category.php?retailer=".$retailer."&amp;category=".$value2->categoryId."\">".$value2->name."</a>";
				echo "</li>";
			}
		echo "</ul>";
		if($counter == $categories_per_column && $counter_columns < 4) {
			echo "</div>";
			echo "<div class=\"one_fourth\">";
			$counter = 0;
			$counter_columns++;
		}
	}
	echo "</div>";
}
?>