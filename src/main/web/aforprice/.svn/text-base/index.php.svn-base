<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Name yet to be thought of</title>
	 
	<link href="css/reset.css" rel="stylesheet" type="text/css" />
	<link href="css/accordion.css" rel="stylesheet" type="text/css" />
	<link href="css/main.css" rel="stylesheet" type="text/css" />
	<link href="css/starify.css" rel="stylesheet" type="text/css" />
	<link href="css/slideshow.css" rel="stylesheet" type="text/css" />
	<link href="css/product.css" rel="stylesheet" type="text/css" />
	<link href="css/marketIndex.css" rel="stylesheet" type="text/css" />
</head>

<body>

<!-- Header -->
<header>
<?php
	include_once 'common/Header.php';
?>
</header>
<!-- Header ends -->

<?php
	include_once 'product/getTopDrops.php';
?>
<!-- Content -->
<div id="content" class="mg10">

	<div id="main_content" style="height: 500px;">
		<div id="slideshow">
			<?php
				include_once 'product/slideShowGraphs.php';
			?>
		</div>
	</div>    
	<div class="clear"></div>


<!-- Hot Content -->
	<div id="top_drops" style="margin-right: 10px;">
		<div class="div_head pad10" style="margin: 0px;">
			Top Price Drops
		</div>
		<?php
			include_once 'product/subCategoryDrops.php';
		?>
	</div>
<!-- Hot Content ends -->

<!-- most/recently viewed items -->
<!-- <div id="viewed_items" class="mg10 mgdiv">
		<div id="recent_viewed" style="display: inline-block; width: 210px; padding: 10px; border-right: 1px #666667 solid;"> -->
			<!-- <h1 style="color: #666667; font-weight: 900;">Recently Viewed Items</h1> -->
<!-- 			<ul> -->
			<?php
// 				include 'user/recentlyViewed.php';
			?>
<!-- 			</ul> -->
<!-- 		</div> -->
		<!-- <div id="most_viewed" style="display: inline-block; padding: 10px;">
			<h1 style="color: #666667; font-weight: 900;">Most Viewed Items</h1>
			<ul>  -->
			<?php
// 				include_once 'common/mostViewed.php';
			?>
<!-- 			</ul> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- most/recently viewed items ends -->
</div>
<!-- Content ends -->

<!-- News Feed -->
<!-- News Feed -->

<!-- Footer -->
<!-- Footer ends -->

<!-- Scripts -->
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery.easing.js"></script>
	<script type="text/javascript" src="js/UrlFunctions.js"></script>
	<script type="text/javascript" src="js/dropDown.js"></script>
	<script type="text/javascript" src="js/haccordion.js"></script>
<!-- 	<script type="text/javascript" src="js/newsrotate.js"></script> -->
	<script type="text/javascript" src="js/categories.js"></script>
	<script type="text/javascript" src="js/retailSelector.js"></script>
	<script type="text/javascript" src="js/slideShow.js"></script>
	<script type="text/javascript" src="js/starify.js"></script>
	<!-- Scripts for graphs generation (data variables will be generated through
	php) -->
	<script type='text/javascript' src='http://www.google.com/jsapi'></script>
	<script type='text/javascript' src='js/googleCharts.js'></script>
	<script type="text/javascript">
		var RLW = 300,
			multi = false;
		
		function pageSpecificFunctions() {
			$('#slideshow').slideshow({
				navButtons: false,
				selectionArrow: true,
				userSlideDimensions: mainContentResize,
				autoplay: true,
				circularThumbnails: true,
				commonSlide: $("#main_graph"),
				slideChangeCallback: changeMainGraph
			});
			
			$(".hot_content").horizontalAccordion();

			$("#priceAlert_slideshow").slideshow({
				userSlideDimensions: priceAlertContentResize
			});
		}
		function pageSpecificResize() {
			$("#pricealert_thumb").width($("#pricealert_thumb").parent().innerWidth() - 75);
			return;
		}
	</script>
	<script type="text/javascript" src="js/script.js"></script>
	<script type="text/javascript">
		mainContentHover();
		function priceAlertContentResize(elem, container) {
			elem.children('.pricealert_pic').first().width(container.innerWidth());
			elem.children('.pricealert_pic').first().height(container.innerHeight());
		}
		$("#priceAlert_slideshow").slideshow({
			userSlideDimensions: priceAlertContentResize
		});
	</script>
<!-- Scripts ends -->
</body>
</html>
