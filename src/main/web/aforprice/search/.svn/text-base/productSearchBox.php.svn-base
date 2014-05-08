<?php
	chdir(dirname(__FILE__));
	include_once '../common/constants.php';
	include_once '../category/categoryList.php';
	include_once '../util/Util.php';
?>
<?php
	$keyword="";
	if(array_key_exists('keyword', $_GET))
		$keyword = trim($_GET['keyword']);

	function getSelectedCategoryName(){
		global $categoryId,
				$categoryPath;

		if($categoryId != -1){
			$currCategory = end($categoryPath);
			$categoryName = $currCategory->categoryName;
		}
		else
			$categoryName = "All";
		return $categoryName;
	}
?>
<body onLoad="document.forms.productsearch.keyword.focus();">
	<div id="search">
		<form method="get" action="search.php" name="productsearch" class="nav-searchbar">
			<?php
				echo "<input type='hidden' name='retailer' value='$retailer'/>";
			?>
			<div style="font-weight: 800; color: #FFF; height: 30px; display: inline-block; cursor: pointer;">
				<div id="select_cat">
					<?php
						function echoSelectedCategory(){
							$categoryName = getSelectedCategoryName();
							echo '<span id="selected_cat">'.$categoryName.'</span>';
						}
						echoSelectedCategory();
					?>
					
					<div class="drop" style="margin: 8px 0px 0px -10px; z-index: 2;">
						<select name="category" id="search_cat" size="10" style="display: none;" >
						<?php
							function echoCategoryOptions() {
								global $categories,
										$categoryId;

								$categoryName = getSelectedCategoryName();

								if($categoryId != -1){
									echo '<option value="'.$categoryId.'" selected="selected">'.$categoryName.'</option>';
									echo '<option value="-1" data-nickname="All">All Departments</option>';
								} else {
									echo '<option value="-1" selected="selected" data-nickname="All">All Departments</option>';
								}

								foreach ($categories as $key => $category) {
									$id = $category->categoryId;
									$name = $category->name;
									if($id!=$categoryId)
										echo '<option value="'.$id.'">'.$name.'</option>';
								}
							}
							echoCategoryOptions();
						?>
						</select>
					</div>
					<div style="display: inline-block; height:7px; width: 9px; background: url('img/spry_1.png') -4px -4px no-repeat;"></div>
				</div>
			</div>
			<input id="searchBar" type="text" placeholder="Search" style="padding-left: 10px; height: 27px; outline: 0px; border: none; display: inline-block; width: 751px;"  name="keyword" value= "<?php echo $keyword; ?>" autocomplete="off">
			<div style="font-weight: 800; color: #FFF; height: 30px; display: inline-block;">
				<input type="submit" value="GO" style="background: -webkit-linear-gradient(top, #65a3ca 10%, #0066a8 100%);background: -moz-linear-gradient(top, #65a3ca 10%, #0066a8 100%); outline: none; border: none; color: #FFF; font-weight: 800; height: 30px; border-radius: 0px 3px 3px 0px; margin: 0px; font-size: 14px;cursor: pointer;">
			</div>
		</form>
	</div>
</body>