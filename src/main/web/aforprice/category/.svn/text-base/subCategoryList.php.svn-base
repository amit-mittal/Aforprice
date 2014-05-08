<?php
/* 
 * @author: Chirag Maheshwari
 * 
 *  It gets the child categories for a category
 *  and presents in the required format of
 *  category page. */
?>
<?php
function echoChildCategories() {
	global $thriftClient,
		$retailer,
		$categoryId,
		$childCategoryList;
	
	if(!empty($childCategoryList)) {
		foreach($childCategoryList as $category) {
			?>
			<li>
			<?php
				echo "<a href=\"".(!$category->parent ? "terminal" : "category" ).".php?retailer=".$retailer."&amp;category=".$category->categoryId."\">".$category->categoryName."</a>";
			?>
			</li>
			<?php
		}
	}
}
?>
<?php
	echoChildCategories();
?>