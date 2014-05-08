<?php
/* 
 * @author: Chirag Maheshwari
 * 
 *  Gets the path of the current category,
 *  and displays the title information for the
 *  category
 *  */
?>
<?php
function echoCategoryPageTitlebar() {
	global $thriftClient,
			$retailer,
			$categoryId,
			$categoryPath;
	
	if(!empty($categoryPath)) {
	?>
		<div>
			<span class="title">
		<?php
				$category = reset($categoryPath);
// 				if(!$category->parent)
// 					header('Location: terminal.php?retailer='.$retailer."&category=".$category->categoryId);
				echo "<a href=\"category.php?retailer=".$retailer."&amp;category=".$category->categoryId."\">".$category->categoryName."</a>";
				$category = null;
		?>
			</span>
		</div>
		<div class="reference">
			<ul>
		<?php
			$categoryCount = count($categoryPath);
			$categoryIndex = 1;
			/* Adding the home page link in the category path */
		?>
		<li>
			<a href="/?retailer=<?php echo $retailer; ?>"><?php echo $retailer; ?></a>
		</li>
		<?php
			foreach($categoryPath as $category) {
				if($categoryIndex > $categoryCount-1)
					break;
				
				echo "<li>";
				echo "<a href=\"category.php?retailer=".$retailer."&amp;category=".$category->categoryId."\">".$category->categoryName."</a>";
				echo "</li>";
				
				$categoryIndex++;
			}
		?>
				<li class="last">
				<?php
					$category = end($categoryPath);
					echo "<a href=\"category.php?retailer=".$retailer."&amp;category=".$category->categoryId."\">".$category->categoryName."</a>";
				?>
				</li>
			</ul>
		</div>
	<?php
	}
}
?>
<?php
	echoCategoryPageTitleBar();
?>