<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Search Page</title>
	 
	<link href="css/reset.css" rel="stylesheet" type="text/css" />
	<link href="css/accordion.css" rel="stylesheet" type="text/css" />
	<link href="css/main.css" rel="stylesheet" type="text/css" />
	<link href="css/starify.css" rel="stylesheet" type="text/css" />
	<link href="css/slideshow.css" rel="stylesheet" type="text/css" />
	<link href="css/product.css" rel="stylesheet" type="text/css" />
	<link href="css/marketIndex.css" rel="stylesheet" type="text/css" />
	
	<link href="js/lightbox/lightbox.css" rel="stylesheet" type="text/css" />
	<link type="text/css" rel="stylesheet" href="css/toggleButtons.css"/>
	
</head>

<body>
<!-- Header -->
<header>
<?php
	include_once 'common/Header.php';
?>
</header>
<!-- Header ends 

<!-- Content --
<div id="content" class="mg10">
<!-- title Bar--
	<div id="title_subcat" class="bodbot">
		<div>
			<span class="title">On-Ear Headphones</span>
		</div>
		<div class="reference">
			<ul>
				<li><a href="sub_category.html">Electronics</a></li>
				<li class="child"></li>
				<li><a href="sub_category.html">Audio &amp; Mp3</a></li>
				<li class="child"></li>
				<li><a href="sub_category.html">Headphones</a></li>
				<li class="child"></li>
				<li class="last"><a href="terminal_category.html">On-Ear Headphones </a></li>
			</ul>
		</div>
	</div>
<  title Bar ends --

< refine bar  >

	<div id="refine_bar" class="bodbot">
		<ul>
			
			<select name="AllRetailer">
			<option value="">All Retailer</option>
			<option value="M">amazon</option>
			<option value="F">bjs</option>
			<option value="M">bestbuy</option>
			<option value="F"></option>
			</select>
			
			<select name="AllBrand">
			<option value="">All Brand</option>
			<option value="M">Sony</option>
			<option value="F">Apple</option>
			<option value="M">Philips</option>
			<option value="F"></option>
			</select>
			<select name="AnyPrice">
			<option value="">Any Price</option>
			<option value="M">$10-$15</option>
			<option value="F">$15-$20</option>
			<option value="M">$20-$25</option>
			<option value="F">$25-$30</option>
			</select>
			<select name="AnyRating">
			<option value="">Any Rating</option>
			<option value="M">1-2</option>
			<option value="F">2-3</option>
			<option value="M">3-4</option>
			<option value="F">4-5</option>
			</select>
			
			
			<select name="AllColor">
			<option value="">All Color</option>
			<option value="M">Black</option>
			<option value="F">White</option>
			<option value="M">Blue</option>
			<option value="F"></option>
			</select>
			
			<select name="AllType">
			<option value="">All Type</option>
			<option value="M"></option>
			<option value="F"></option>
			<option value="M"></option>
			<option value="F"></option>
			</select>
		
		</ul>
	</div>
 refine bar ends -->

<!-- Search Results -->


			<?php
				include_once 'search/productSearchCode.php';
			?>
	<!--
	<div class="page_info">
		<div class="right pagination">
			<ul>
				<li class="disabled prev"><a href="#">Previous</a></li>
				<li class="selected"><a href="#">1</a></li>
				<li><a href="#">2</a></li>
				<li><a href="#">3</a></li>
				<li><a href="#">4</a></li>
				<li class="dot_nav">...</li>
				<li class="next"><a href="#">Next</a></li>
			</ul>
		</div>
		<div>
			Showing <span class="start">1</span> - 
			<span class="end">10</span> of 
			<span class="num_results">123,456</span>   
		</div>
		<div class="clear"></div>
	</div>
	
	
	<div class="compare_bar">
		<div class="right sort_by">
			Sort By:
			<select class="sort_by_option">
				<option selected="selected">Best Selling</option>
				<option>Price: low to high</option>
				<option>Price: high to low</option>
			</select>
		</div>
		<div class="compare_list">
			Compare upto 5 items
			<ul>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
			</ul>
			<button class="compare_button"></button>
		</div>
	</div>
	
	
  Product Overview 
	<div id="overview" class="bodbot pad10 mg10lr">
		<div class="pad10">
			<div style="display: inline-block;width: 250px; margin-right: 40px;  border-right: 1px #666667 solid;">
				<div id="product_image">
					
					<img src="product/product.png" width="150" height="168" />
					<div class="product_header">Product Name comes here</div>
					<div class="overview"></span><div class="stars" data-type="normal" data-rating="3.5">
					</div><span class="value"> 4.4/5 (</span>100 Reviews <span class="value">)</span></div>
					<div class="overview">Best Buy: </span><span class="price">$19.99</span></div>
					<div class="overview">Max: </span><span class="price">$19.99</span></div>
					<div class="overview">Min: </span><span class="price">$19.99</span></div>
					<button class="add_workspace" title="add product to workspace"></button>
					<button class="add_pricealert" title="Set price alert"></button>
					
					<div style="text-align: center; vertical-align: middle;">
					<input type="checkbox" class="add_prod_compare"> Compare
					</div>
				</div>
				
				
			</div>
			
			<div style="display: inline-block;width: 250px; margin-right: 40px;  border-right: 1px #666667 solid;">
				<div id="product_image">
					
					<img src="product/product.png" width="150" height="168" />
					<div class="product_header">Product Name comes here</div>
					<div class="overview"></span><div class="stars" data-type="normal" data-rating="3.5">
					</div><span class="value"> 3.4/5 (</span>100 Reviews<span class="value">)</span></div>
					<div class="overview">Best Buy: </span><span class="price">$19.99</span></div>
					<div class="overview">Max: </span><span class="price">$19.99</span></div>
					<div class="overview">Min: </span><span class="price">$19.99</span></div>
					<button class="add_workspace"  title="add product to workspace"></button>
					<button class="add_pricealert" title="Set price alert"></button>
					
					<div style="text-align: center; vertical-align: middle;">
					<input type="checkbox" class="add_prod_compare"> Compare
					</div>
				</div>
				
			</div>
		</div>	
		<div class="pad10">
			
			<div style="display: inline-block;width: 250px; margin-right: 40px;  border-right: 1px #666667 solid;">
				<div id="product_image">
					
					<img src="product/product.png" width="150" height="168" />
					<div class="product_header">Product Name comes here</div>
					<div class="overview"></span><div class="stars" data-type="normal" data-rating="3.5">
					</div><span class="value"> 3.4/5 (</span>100 Reviews<span class="value">)</span></div>
					<div class="overview">Best Buy: </span><span class="price">$19.99</span></div>
					<div class="overview">Max: </span><span class="price">$19.99</span></div>
					<div class="overview">Min: </span><span class="price">$19.99</span></div>
					<button class="add_workspace" title="add product to workspace"></button>
					<button class="add_pricealert" title="Set price alert"></button>
					
					<div style="text-align: center; vertical-align: middle;">
					<input type="checkbox" class="add_prod_compare"> Compare
					</div>
				</div>
				
			</div>
			
			<div style="display: inline-block; margin-right: 10px;">
				<div id="product_image">
					
					<img src="product/product.png" width="150" height="168" />
					<div class="product_header">Product Name comes here</div>
					<div class="overview"></span><div class="stars" data-type="normal" data-rating="3.5">
					</div><span class="value"> 4.4/5 (</span>100 Reviews<span class="value">)</span></div>
					<div class="overview">Best Buy: </span><span class="price">$19.99</span></div>
					<div class="overview">Max: </span><span class="price">$19.99</span></div>
					<div class="overview">Min: </span><span class="price">$19.99</span></div>
					<button class="add_workspace" title="add product to workspace"></button>
					<button class="add_pricealert" title="Set price alert"></button>
					
					<div style="text-align: center; vertical-align: middle;">
					<input type="checkbox" class="add_prod_compare"> Compare
					</div>
				</div>
				
			</div>
		
		</div>
		<br>
		<br>
		
	</div>
 Product Overview ends 
	
	<div class="compare_bar">
		<div class="right sort_by">
			Sort By:
			<select class="sort_by_option">
				<option selected="selected">Best Selling</option>
				<option>Price: low to high</option>
				<option>Price: high to low</option>
			</select>
		</div>
		<div class="compare_list">
			Compare upto 5 items
			<ul>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
				<li></li>
			</ul>
			<button class="compare_button"></button>
		</div>
	</div>
	
-->

<!-- most/recently viewed items -->
<!-- most/recently viewed items ends -->

<!-- Content ends -->

<!-- News Feed -->
<!-- News Feed -->

<!-- Footer -->
<!-- Footer ends -->

<!-- Scripts -->
 <!--   <script type="text/javascript" src="js/jquery.js"></script> -->
	<script type="text/javascript" src="js/jquery.easing.js"></script>
	<script type="text/javascript" src="js/dropDown.js"></script>
	<script type="text/javascript" src="js/haccordion.js"></script>
<!-- 	<script type="text/javascript" src="js/newsrotate.js"></script> -->
	<script type="text/javascript" src="js/categories.js"></script>
	<script type="text/javascript" src="js/compareProduct.js"></script>
	<script type="text/javascript" src="js/retailSelector.js"></script>
	<script type="text/javascript" src="js/slideShow.js"></script>
	<script type="text/javascript" src="js/starify.js"></script>
	<script type="text/javascript" src="js/UrlFunctions.js"></script>

	<script type="text/javascript" src="js/productGraphs.js"></script>
	<script type="text/javascript" src="js/priceAlert.js"></script>
	<script type="text/javascript" src="js/facebookLogin.js"></script>
	<script type="text/javascript" src="js/bootstrap-2.0.2.js"></script>
	<script type="text/javascript" src="js/mouseHoverTooltip.js"></script>
	<script type="text/javascript">
		var RLW = 300,
			multi = false;
		function bindGraphButtons() {

			/* First binding the hover events (color changing) */
			$('.history_icon').hover(function() {
				var iconImage = $(this).attr('src');
				iconImage = iconImage.replace(/.png/gi, "_hover.png");

				$(this).attr('src', iconImage);
			}, function() {
				var iconImage = $(this).attr('src');
				iconImage = iconImage.replace(/_hover.png/gi, ".png");

				$(this).attr('src', iconImage);
			});

			/* binding the price history graph */
			$('.price_history').on('click', function() {
				var productName = jQuery.trim($(this).parent().parent().find('.product_name').html()),
					priceHistory = $(this).attr('data-history');

				plotPriceHistory(productName, priceHistory);
			});

			$('.review_history').on('click', function() {
				var productName = jQuery.trim($(this).parent().parent().find('.product_name').html()),
					ReviewHistory = $(this).attr('data-history');

				plotReviewHistory(productName, ReviewHistory);
			});

			$('.rank_history').on('click', function() {
				var productName = jQuery.trim($(this).parent().parent().find('.product_name').html()),
					RankHistory = $(this).attr('data-history');

				plotSellRankHistory(productName, RankHistory);
			});
		}

		function pageSpecificFunctions() {
			/* Binding events with graph buttons */
			bindGraphButtons();

			$('#overview').compareProduct();
		}
		function pageSpecificResize() {
			return;
		}
	</script>
	<script type="text/javascript" src="js/script.js"></script>
	<script type="text/javascript">
		mainContentHover();
	</script>
<!-- Scripts ends -->	



</body>
</html>