<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Name yet to be thought of</title>
	 
	<link href="css/reset.css" rel="stylesheet" type="text/css" />
	<link href="css/accordion.css" rel="stylesheet" type="text/css" />
	<link href="css/main.css" rel="stylesheet" type="text/css" />
	<link href="css/starify.css" rel="stylesheet" type="text/css" />

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
<?php
	include_once 'common/Header.php';
?>
</header>
<!-- Header ends -->

<!-- Content -->
<div id="content" class="mg10">

<!-- title Bar -->
	<div id="title" class="bodbot mg10 pad10">
		<div>
			<span class="title">Product Name</span>
		</div>
		<div class="reference">
			<ul>
				<li><a href="sub_category.html">Electronics</a></li>
				<li class="child"></li>
				<li><a href="sub_category.html">Audio &amp; Mp3</a></li>
				<li class="child"></li>
				<li><a href="sub_category.html">Headphones</a></li>
				<li class="child"></li>
				<li class="last"><a href="terminal_category.html">and so on</a></li>
			</ul>
		</div>
	</div>
<!-- title Bar ends -->

<!-- anchor division -->
	<div id="anchors" class="bodbot pad10 mg10lr">
		<ul>
			<li><a href="#overview" class="anchor selected">Overview</a></li>
			<li><a href="#chart" class="anchor">Product Chart</a></li>
			<li><a href="#pricing" class="anchor">Price</a></li>
			<li><a href="#details" class="anchor">Details</a></li>
			<li><a href="#reviews" class="anchor">Reviews</a></li>
			<li><a href="#viewed_items" class="anchor">Similar Product</a></li>
		</ul>
	</div>
<!-- anchor division ends -->

<!-- Product Overview -->
	<div id="overview" class="bodbot pad10 mg10lr">
		<div class="pad10">
			<div style="display: inline-block; margin-right: 40px;">
				<div id="product_image">
					<div style="position: absolute;">pic 1</div>
					<img src="product/product.png" width="205" height="218" />
				</div>
				<ul id="product_image_thumbs">
					<li><img src="product/product_thumb.png" width="46" height="48"/></li>
					<li><img src="product/product_thumb.png" width="46" height="48"/></li>
					<li><img src="product/product_thumb.png" width="46" height="48"/></li>
				</ul>
			</div>
			<div style="display: inline-block;">
				<div class="product_header">Product Name comes here</div>
				<div class="overview"><span class="name">Model: </span><span class="value">MASD123</span></div>
				<div class="overview"><span class="name">SKU: </span><span class="value">MASD123</span></div>
				<div class="overview"><span class="name">Customer Reviews: </span><div class="stars" data-type="normal" data-rating="4.5"></div><span class="value">4.4 out of 5 (</span>100 Reviews<span class="value">)</span></div>
				<div class="overview"><span class="name">Best Price: </span><span class="price_bb">$19.99</span></div>
				<button class="add_workspace" title="add product to workspace"></button>
				<button class="add_pricealert" title="Set price alert"></button>
			</div>
		</div>
	</div>
<!-- Product Overview ends -->

<!-- product chart -->
	<div id="chart" class="bodbot pad10 mg10lr">
		<div>
			<h1 class="product_header">Product Chart</h1>
		</div>
		<div class="pad10">
			<div id="product_chart" style="display: inline-block;width: 74%; background: url('product/product_chart.png') no-repeat; border-right: 1px solid #666667; height: 452px;backgroun-size: 100%;">
				<!-- here the graph will come -->
			</div>
			<div id="product_news" style="display: inline-block;width: 25%; background: url('product/product_news.png') no-repeat; height: 378px; backgroun-size: 100%;">
				<!-- here the product new will come -->
			</div>
		</div>
	</div>
<!-- product chart ends -->

<!-- pricing -->
	<div id="pricing" class="bodbot pad10 mg10lr">
		<div>
			<h1 class="product_header">Price</h1>
		</div>
		<div class="pad10">
			<div id="prices">
				<div class="mg10"><span class="grey_b">Minimum Price:</span> <span class="price_b">$19.99</span> (20 Dec - 29 Dec, 2012) - walmart</div>
				<div class="mg10"><span class="grey_b">Maximum Price:</span> <span class="price_b">$29.99</span> (20 Dec - 29 Dec, 2012) - BestBuy</div>
			</div>
			<div style="margin: 30px 10px 15px 10px;">
				<table id="store_prices">
					<tr>
						<th class="first">Retailer</th>
						<th>Seller Rating</th>
						<th>Seller Rank</th>
						<th>Since</th>
						<th>Tax</th>
						<th>Shipping</th>
						<th>Total</th>
						<th>Base</th>
					</tr>
					<tr>
						<td class="first">Walmart</td>
						<td><div class="stars" data-type="normal" data-rating="4.5"></div></td>
						<td>#1</td>
						<td>12 Dec, 2012</td>
						<td>- -</td>
						<td>Free</td>
						<td>$19.99</td>
						<td>$19.99</td>
					</tr>
					<tr>
						<td class="first">BestBuy</td>
						<td><div class="stars" data-type="normal" data-rating="3.2"></div></td>
						<td>#2</td>
						<td>12 Dec, 2012</td>
						<td>- -</td>
						<td>$1.00</td>
						<td>$20.99</td>
						<td>$19.99</td>
					</tr>
					<tr>
						<td class="first">Amazon</td>
						<td><div class="stars" data-type="normal" data-rating="4"></div></td>
						<td>#3</td>
						<td>12 Dec, 2012</td>
						<td>- -</td>
						<td>$2.00</td>
						<td>$21.99</td>
						<td>$19.99</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
<!-- pricing ends -->

<!-- details -->
	<div id="details" class="bodbot pad10 mg10lr">
		<div>
			<h1 class="product_header">Details</h1>
		</div>
		<div class="pad10">
			<div id="product_desc" class="mg10">
				Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse 
				cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat 
				non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
			</div>
			<div style="margin: 30px 10px 15px 10px;">
				<table id="product_specs">
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
					<tr>
						<th>SpecName</th>
						<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
<!-- details ends -->

<!-- reviews -->
	<div id="reviews" class="bodbot pad10 mg10lr">
		<div>
			<h1 class="product_header">User Review</h1>
		</div>
		<div class="pad10">
			<div class="mg10">
				<div class="stars" data-type="big" data-rating="4.4"></div>
				<div style="margin: 15px 0px; font-size: 150%;">4.4 average, based on 196 reviews</div>
			</div>
			<div style="margin: 30px 10px 15px 10px">
				<div id="review_tab_buttons" class="pad10">
					<ul>
						<li>Most Recent</li>
						<li>Highest Rated</li>
						<li>lowest Rated</li>
					</ul>
				</div>
				<div id="review_tab">
					<table id="most_recent" class="reviews">
						<tr>
							<th><span class="review_rating">4</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Amazon</span></span></td>
						</tr>
						<tr>
							<th><span class="review_rating">4</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Amazon</span></span></td>
						</tr>
						<tr>
							<th><span class="review_rating">4</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Amazon</span></span></td>
						</tr>
						<tr>
							<th><span class="review_rating">4</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Amazon</span></span></td>
						</tr>
					</table>
					<table id="highest_rated" class="reviews">
						<tr>
							<th><span class="review_rating">5</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Walmart</span></span></td>
						</tr>
						<tr>
							<th><span class="review_rating">5</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Walmart</span></span></td>
						</tr>
						<tr>
							<th><span class="review_rating">5</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Walmart</span></span></td>
						</tr>
					</table>
					<table id="lowest_rated" class="reviews">
						<tr>
							<th><span class="review_rating">1</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Best-Buy</span></span></td>
						</tr>
						<tr>
							<th><span class="review_rating">1</span> out of 5<br /><span class="reviewer">Alan D. Paul<br />12-4-2012</span></th>
							<td>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod 
				tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam
				, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 
				consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
				<br /><span class="reviewer">Review provided by <span class="review_retailer">Best-Buy</span></span></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
<!-- reviews ends -->

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

<!-- News Feed -->
<!-- <div id="news_feed" class="mg10 pad10">
	<div id="news_slider">	
		<ul>
			<li><a href="#">1. News Snippet News Snippet News Snippet News Snippet News Snippet</a> <span class="news_info">| News Source - News Date</span></li>
			<li><a href="#">2. News Snippet News Snippet News Snippet News Snippet News Snippet</a> <span class="news_info">| News Source - News Date</span></li>
			<li><a href="#">3. News Snippet News Snippet News Snippet News Snippet News Snippet</a> <span class="news_info">| News Source - News Date</span></li>
			<li><a href="#">4. News Snippet News Snippet News Snippet News Snippet News Snippet</a> <span class="news_info">| News Source - News Date</span></li>
			<li><a href="#">5. News Snippet News Snippet News Snippet News Snippet News Snippet</a> <span class="news_info">| News Source - News Date</span></li>
			<li><a href="#">6. News Snippet News Snippet News Snippet News Snippet News Snippet</a> <span class="news_info">| News Source - News Date</span></li>
			<li><a href="#">7. News Snippet News Snippet News Snippet News Snippet News Snippet</a> <span class="news_info">| News Source - News Date</span></li>
		</ul>
	</div>
</div> -->
<!-- News Feed -->

<!-- Footer -->
<!-- Footer ends -->

<!-- Scripts -->
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery.easing.js"></script>
	<script type="text/javascript" src="js/dropDown.js"></script>
	<script type="text/javascript" src="js/haccordion.js"></script>
<!-- 	<script type="text/javascript" src="js/newsrotate.js"></script> -->
	<script type="text/javascript" src="js/categories.js"></script>
	<script type="text/javascript" src="js/retailSelector.js"></script>
	<script type="text/javascript" src="js/slideShow.js"></script>
	<script type="text/javascript" src="js/starify.js"></script>
	<script type="text/javascript">
		var RLW = 300,
			multi = false;
		
		function pageSpecificFunctions() {
			$('#slideshow').slideshow({
				navButtons: false,
				selectionArrow: true,
				userSlideDimensions: mainContentResize,
				autoplay: true,
				circularThumbnails: true
			});
			
			$('#hot_content').horizontalAccordion();

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