<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Name yet to be thought of</title>
	 
	<link href="css/reset.css" rel="stylesheet" type="text/css" />
	<link href="css/accordion.css" rel="stylesheet" type="text/css" />
	<link href="css/main.css" rel="stylesheet" type="text/css" />
	<link href="css/starify.css" rel="stylesheet" type="text/css" />
	
	<link href="css/category_directory.css" rel="stylesheet" type="text/css" />
</head>

<body>
<!-- Debug division -->
<div id="debug" style="width: 100%; min-height: 100px;max-height: 150px; overflow: auto; color: black; border-bottom: 1px black solid;">
	<div><h1>Debug division (will not be visible in the real website)</h1></div>
	<div id="debug_info">
	</div>
</div>
<!-- /Debug division -->

<!-- Header -->
<header>
	<div id="header">
		<div id="logo">
			<a href="index.php"><img src="img/logo.png" alt="PriceRite" /></a>
		</div>
		<div id="search">
			<form method="get" action="search.html">
				<div style="font-weight: 800; color: #FFF; height: 30px; display: inline-block; cursor: pointer;">
					<div id="select_cat">
						<span id="selected_cat">All</span>
						<div class="drop" style="margin: 8px 0px 0px -10px; z-index: 2;">
							<select name="search_cat" id="search_cat" size="10" style="display: none;">
								<option value="All" selected="selected">All Categories</option>
								<option value="cat1" >Category 1</option>
								<option value="cat2" >Category 2</option>
								<option value="cat3" >Category 3</option>
								<option value="cat4" >Category 4</option>
								<option value="cat5" >Category 5</option>
								<option value="cat6" >Category 6</option>
								<option value="cat7" >Category 7</option>
								<option value="cat8" >Category 8</option>
								<option value="cat9" >Category 9</option>
								<option value="cat10" >Category 10</option>
								<option value="cat11" >Category 11</option>
								<option value="cat12" >Category 12</option>
								<option value="cat13" >Category 13</option>
								<option value="cat14" >Category 14</option>
							</select>
						</div>
						<div style="display: inline-block; height:7px; width: 9px; background: url('img/spry_1.png') -4px -4px no-repeat;"></div>
					</div>
				</div>
				<input id="searchBar" type="text" placeholder="Search" style="padding-left: 10px; height: 27px; outline: 0px; border: none; display: inline-block; width: 751px;">
				<div style="font-weight: 800; color: #FFF; height: 30px; display: inline-block; cursor: pointer;">
					<input type="submit" value="GO" style="background: -webkit-linear-gradient(top, #65a3ca 10%, #0066a8 100%);background: -moz-linear-gradient(top, #65a3ca 10%, #0066a8 100%); outline: none; border: none; color: #FFF; font-weight: 800; height: 30px; border-radius: 0px 3px 3px 0px; margin: 0px; font-size: 14px;">
				</div>
			</form>
		</div>
		<div id="account">
			<div id="account_button">
				Chirag <div id="down"></div>
			</div>
			<div class="drop">
				<form method="post" action="home_login.html">
				</form>
			</div>
		</div>
	</div>
<!-- Nav+retailer Header -->
	<div id="nav">
		<div id="navigation">
			<div id="nav_button">
				Shop by<br /><span class="bolder">Department</span>
				<div class="down_big"></div>
			</div>
			<div id="categories" class="dropdown">
				<?php
					include 'category/getCategoryMenu.php';
				?>
			</div>
		</div>
		<div class="right" style="margin-right: 20px;" id="retail_select">
			<div id="retailers">
				<div id="retail_prev_button"></div>
				<div id="retailer_logos">
					<ul>
						<li data-select="amazonbs"><img src="img/logos/amazon.png" height="39" width="50" /></li>
						<li data-select="babysrus"><img src="img/logos/babiesrus.png" height="39" width="50" /></li>
						<li data-select="bananarepublic"><img src="img/logos/bananarepublic.png" height="39" width="50" /></li>
						<li data-select="bestbuy"><img src="img/logos/bestbuy.png" height="39" width="50" /></li>
						<li data-select="bjs"><img src="img/logos/bjs.png" height="39" width="50" /></li>
						<li data-select="costco"><img src="img/logos/costco.png" height="39" width="50" /></li>
						<li data-select="cvs"><img src="img/logos/cvscaremark.png" height="39" width="50" /></li>
						<li data-select="gap"><img src="img/logos/gap.png" height="39" width="50" /></li>
						<li data-select="homedepot"><img src="img/logos/homedepot.png" height="39" width="50" /></li>
						<li data-select="jcpenny"><img src="img/logos/jcp.png" height="39" width="50" /></li>
						<li data-select="kohls"><img src="img/logos/kohls.png" height="39" width="50" /></li>
						<li data-select="lowes"><img src="img/logos/lowes.png" height="39" width="50" /></li>
						<li data-select="macys"><img src="img/logos/macys.png" height="39" width="50" /></li>
						<li data-select="nordstrom"><img src="img/logos/nordstrom.png" height="39" width="50" /></li>
						<li data-select="oldnavy"><img src="img/logos/oldnavy.png" height="39" width="50" /></li>
						<li data-select="riteaid"><img src="img/logos/riteaid.png" height="39" width="50" /></li>
						<li data-select="samsclub"><img src="img/logos/samsclub.png" height="39" width="50" /></li>
						<li data-select="sears"><img src="img/logos/sears.png" height="39" width="50" /></li>
						<li data-select="staples"><img src="img/logos/staples.png" height="39" width="50" /></li>
						<li data-select="target"><img src="img/logos/target.png" height="39" width="50" /></li>
						<li data-select="toysrus"><img src="img/logos/toysrus.png" height="39" width="50" /></li>
						<li data-select="victoriassecret"><img src="img/logos/vs.png" height="39" width="50" /></li>
						<li data-select="walgreens"><img src="img/logos/walgreens.png" height="39" width="50" /></li>
						<li data-select="walmart"><img src="img/logos/walmart.png" height="39" width="50" /></li>
					</ul>
				</div>
				<div id="retail_next_button"></div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
<!-- Nav+retailer Header ends -->
</header>
<!-- Header ends -->

<!-- Content -->
<div id="content" class="mg10">
	<!-- <div id="category_directory">
		<table>
			<tr>
				<td>Category 1</td>
				<td>
					<table>
						<tr class="last">
							<td>
								sub-cat1
							</td>
							<td>
								<table>
									<tr>
										<td>sub-cat1.1</td>
									</tr>
									<tr>
										<td>sub-cat1.2</td>
									</tr>
									<tr class="last">
										<td>sub-cat1.3</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>Category 1</td>
				<td>
					<table>
						<tr class="last">
							<td>
								sub-cat1
							</td>
							<td class="">
								<table>
									<tr>
										<td>sub-cat1.1</td>
									</tr>
									<tr>
										<td>sub-cat1.2</td>
									</tr>
									<tr class="last">
										<td>sub-cat1.3</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div> -->
	<div id="category_directory">
		<?php
			include_once 'category/fullCategory.php';
		?>
		<div class="clear"></div>
		<div></div>
	</div>
	
	
<!-- most/recently viewed items -->
	<div id="viewed_items" class="mg10 mgdiv">
		<div id="recent_viewed" style="display: inline-block; width: 210px; padding: 10px; border-right: 1px #666667 solid;">
			<h1 style="color: #666667; font-weight: 900;">Recently Viewed Items</h1>
			<ul>
				<li><img src="recent_viewed.png"/></li>
				<li><img src="recent_viewed.png"/></li>
				<li><img src="recent_viewed.png" /></li>
				<li><img src="recent_viewed.png" /></li>
			</ul>
		</div>
		<div id="most_viewed" style="display: inline-block; padding: 10px;">
			<h1 style="color: #666667; font-weight: 900;">Most Viewed Items</h1>
			<ul>
				<li><img src="most_viewed.png" height="228" width="149"  /></li>
				<li><img src="most_viewed.png" height="228" width="149"  /></li>
				<li><img src="most_viewed.png" height="228" width="149"  /></li>
				<li><img src="most_viewed.png" height="228" width="149"  /></li>
				<li><img src="most_viewed.png" height="228" width="149"  /></li>
				<li><img src="most_viewed.png" height="228" width="149"  /></li>
			</ul>
		</div>
	</div>
<!-- most/recently viewed items ends -->
</div>
<!-- Content ends -->

<!-- Scripts -->
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery.easing.js"></script>
	<script type="text/javascript" src="js/UrlFunctions.js"></script>
	<script type="text/javascript" src="js/dropDown.js"></script> 
	<script type="text/javascript" src="js/categories.js"></script>
	<script type="text/javascript" src="js/retailSelector.js"></script>
	<script type="text/javascript" src="js/slideShow.js"></script>
	<script type="text/javascript" src="js/starify.js"></script>
	<script type="text/javascript">
		var	RLW = 300,
			multi = false;
		
		function pageSpecificFunctions() {
		}
		function pageSpecificResize() {
			return;
		}
	</script>
	<script type="text/javascript" src="js/script.js"></script>
	<script type="text/javascript">
	</script>
<!-- Scripts ends -->
</body>
</html>