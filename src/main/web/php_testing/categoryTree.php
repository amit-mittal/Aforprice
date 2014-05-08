<?php
ini_set('display_errors', 'On');
error_reporting(E_ALL);
	include 'retailerCategories.php';
?>
<!DOCTYPE html>
<html>

<head>
	<script type="text/javascript">
		function expandContractCategories(list) {
			elements = document.getElementsByClassName(list);

			for(var i=0; i < elements.length; i++) {
				prop = elements[i].style.display;
				if(prop == "none")
					elements[i].style.display = "block";
				else
					elements[i].style.display = "none";
			}
		}

		function expandAll() {
			elements = document.getElementsByClassName('sub_categories');

			for(var i=0; i < elements.length; i++)
				elements[i].style.display = "block";
		}

		function contractAll() {
			elements = document.getElementsByClassName('sub_categories');

			for(var i=0; i < elements.length; i++)
				elements[i].style.display = "none";
		}
	</script>
	
	<style>
		img {
			cursor: pointer;
		}
	</style>
</head>

<body>
expand all<img src="expand.gif" onclick="expandAll();" /><br />
contract all<img src="expand.gif" onclick="contractAll();" /><br /><br />
<?php
function printCat(&$categories, $level, $parent) {
	$parent = str_replace(" ", "_", $parent);
	echo "<ol class=\"sub_categories $parent\" style=\"margin-left:30px; display: none;\">";
	foreach($categories as $cat) {
		echo "<li><a href=\"".$cat->URL."\" target=\"_category\">".$cat->categoryName."</a> (".$level.")";
		if($cat->parentBool == 'Y') {
			$name = str_replace(" ", "_", $cat->categoryName);
			echo "<img src=\"expand.gif\" onclick=\"expandContractCategories('".$name."');\" />";
		}
		if($cat->subCategories)
			printCat($cat->subCategories, $level+1, $cat->categoryName);
		echo "</li>";
	}
	echo "</ol>";
}
?>
<?php
	$retail = new retailerCategory($_GET['retailer']);
	$retail->setCategoryStructure();
	
	$margin = 20;$level = 0;
	
	echo "<ol>";
	foreach($retail->categories as $category) {
		echo "<li><a href=\"".$category->URL."\" target=\"_category\">".$category->categoryName."</a> (".$level.")";
		if($category->parentBool == 'Y') {
			$name = str_replace(" ", "_", $category->categoryName);
			echo "<img src=\"expand.gif\" onclick=\"expandContractCategories('".$name."');\" />";
		}
		if($category->subCategories)
			printCat($category->subCategories, $level+1, $category->categoryName);
		echo "</li>";
	}
	echo "</ol>";
?>
</body>
</html>