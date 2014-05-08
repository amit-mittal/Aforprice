<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Terminal Page</title>
	 
	<link href="css/reset.css" rel="stylesheet" type="text/css" />
	<link href="css/accordion.css" rel="stylesheet" type="text/css" />
	<link href="css/main.css" rel="stylesheet" type="text/css" />
	<link href="css/starify.css" rel="stylesheet" type="text/css" />
	<link href="css/slideshow.css" rel="stylesheet" type="text/css" />
	<link href="css/product.css" rel="stylesheet" type="text/css" />
	<link href="css/marketIndex.css" rel="stylesheet" type="text/css" />
	<link href="js/lightbox/lightbox.css" rel="stylesheet" type="text/css" />
	
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
	include_once 'product/pagination.php';
?>

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
	<script type="text/javascript">
		var RLW = 300,
			multi = false;

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