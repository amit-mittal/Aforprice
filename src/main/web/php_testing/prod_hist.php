<?php
	include 'product.php';
	
	$prods = count($_GET);
?>
<!DOCTYPE html>
<html>
<head>
	<link href="css/finance.css" rel="stylesheet" media="all" />
	<style type="text/css">
		table tr th, table tr td {
			border: 1px #000 solid;
			text-align: center;
		}
		table {
			border-collapse: collapse;
		}
		table tr td img {
			height: 100px;
			width: 100px;
		}
	</style>
	<script type="text/javascript" src="js/json2.js"></script>
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/canvas2image.js"></script>
	<script type="text/javascript" src="js/canvastext.js"></script>
	<script type="text/javascript" src="js/flotr.js"></script>
	<script type="text/javascript" src="js/graph.js"></script>
	
	<script type="text/javascript">
		var prodJSON = null;
		
		function is_int(value){
		  if((parseFloat(value) == parseInt(value)) && !isNaN(value)){
		      return true;
		  } else {
		      return false;
		  }
		}

		function setStatus(status) {
			div = document.getElementById('status');
			div.innerHTML = status;
		}

		function displayData() {
			var tbody = document.getElementById('products').getElementsByTagName('tbody')[0];

			var newRow = document.createElement('tr');
			
			var image = document.createElement('img');
				image.setAttribute("src", prodJSON.info.IMAGE_URL);
				image.setAttribute('alt', "");

			var url = document.createElement('a');
				url.setAttribute("href", prodJSON.info.URL);
				url.setAttribute("target", "_product");
				url.innerHTML = prodJSON.info.PRODUCT_NAME;

			var column = document.createElement('td');
				column.innerHTML = prodJSON.id;
				newRow.appendChild(column);

				column = document.createElement('td');
				column.appendChild(image);
				newRow.appendChild(column);

				column = document.createElement('td');
				column.appendChild(url);
				newRow.appendChild(column);

				column = document.createElement('td');
				column.innerHTML = prodJSON.info.CATEGORY_ID;
				newRow.appendChild(column);

				column = document.createElement('td');
				column.innerHTML = prodJSON.info.RETAILER_ID;
				newRow.appendChild(column);

				column = document.createElement('td');
				column.innerHTML = prodJSON.info.MODEL_NO;
				newRow.appendChild(column);

				column = document.createElement('td');
				column.innerHTML = prodJSON.info.DESCRIPTION;
				newRow.appendChild(column);

				column = document.createElement('td');
				column.innerHTML = "start: "+prodJSON.info.START_DATE+"<br />end: "+prodJSON.info.END_DATE+"<br />last modified: "+prodJSON.info.TIME_MODIFIED;
				newRow.appendChild(column);

			tbody.appendChild(newRow);

			newRow = document.createElement("tr");
				column = document.createElement("td");
				column.setAttribute("colspan", 8);
				var hist = "";

				for(var i=0; i< prodJSON.history.length; i++) {
					hist += prodJSON.history[i][2]+" --- "+prodJSON.history[i][1]+"<br />";
				}
				
				column.innerHTML = hist;
				newRow.appendChild(column);
			tbody.appendChild(newRow);

			var summaryData = [],
			priceData = [],
			flagData = [];

			for(var i=0; i< prodJSON.history.length; i++) {
				summaryData.push([prodJSON.history[i][0], prodJSON.history[i][1]]);
				priceData.push([prodJSON.history[i][0], prodJSON.history[i][1]]);
			}

			productFinance.addData(prodJSON.id, priceData, summaryData, flagData, "productfinance");
			
			prodJSON = null;
		}
		
		function getData() {
			ele = document.getElementById('prod_id');
			ele = ele.value;
			if(!is_int(ele)) {
				setStatus("The id entered is incorrect");
				setInterval("setStatus('')", 2000);
				return;
			}
			val = parseInt(ele);
			
			var xmlhttp;
			if (window.XMLHttpRequest)
			  {// code for IE7+, Firefox, Chrome, Opera, Safari
				setStatus("AJAX request object created");
				setInterval("setStatus('')", 1000);
			  	xmlhttp=new XMLHttpRequest();
			  }
			else
			  {// code for IE6, IE5
				setStatus("AJAX request object created(for IE)");
				setInterval("setStatus('')", 1000);
			  	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			  }
			xmlhttp.onreadystatechange=function() {
			  if (xmlhttp.readyState==4 && xmlhttp.status==200)
			    {
				 	setStatus("AJAX request successful");
					setInterval("setStatus('')", 5000);

			    	prodJSON = JSON.parse(xmlhttp.responseText);
			    	displayData();
			    }
			}
			xmlhttp.open("GET","get_prod.php?id="+val,true);
			xmlhttp.send();
		}
	</script>
</head>

<body>
	<header>
		<div id="add_prod" style="float: right;">
			<input type="number" id="prod_id" placeholder="enter id here" />
			<button onclick="getData();">add</button>
		</div>
		<div id="status" style="border: 3px solid red; width: 900px;">
		</div>
		<div style="clear: both;"></div>
	</header>
	<div id="content" style="width: 1200px; margin: 10px auto;">
		<div id="productfinance" style="width: 1000px;margin: 10px auto; border: 1px red solid;"></div>
		<div>
			<table id="products" style="margin-top: 50px;">
				<tr>
					<th>ID</th>
					<th>IMAGE</th>
					<th>NAME</th>
					<th>CATEGORY ID</th>
					<th>RETAILER</th>
					<th>MODEL #</th>
					<th>DESCRIPTION</th>
					<th>DATES</th>
				</tr>
		<?php
			if($prods != 0) {
				foreach($_GET as $id) {
					if(is_numeric($id)) {
						$prod[$id] = new product($id);
						echo "<tr><td>".$prod[$id]->product_id."</td><td><img src=\"".$prod[$id]->product_info['IMAGE_URL'].
						"\" /></td><td><a href=\"".$prod[$id]->product_info['URL']
						."\" target=\"_product\">".$prod[$id]->product_info['PRODUCT_NAME']."</a></td><td>".
						$prod[$id]->product_info['CATEGORY_ID']."</td><td>".
						$prod[$id]->product_info['RETAILER_ID']."</td><td>".
						$prod[$id]->product_info['MODEL_NO']."</td><td><ul>".
						$prod[$id]->product_info['DESCRIPTION']."</ul></td><td>start: ".
						$prod[$id]->product_info['START_DATE']."<br />end: ".$prod[$id]->product_info['END_DATE'].
						"<br />Last Modified: ".$prod[$id]->product_info['TIME_MODIFIED']."</td></tr>";
						
						echo "<tr><td colspan=\"8\">";
						foreach ($prod[$id]->product_hist as $hist) {
							echo $hist[2]."  ----  ".$hist[1]."<br />";
						}
						echo "</td></tr>";
					}
					else
						continue;
				}
			}
		?>
			</table>
		</div>
	</div>
	
	<script type="text/javascript">
	Event.observe(document, 'dom:loaded', function() {
	<?php
		echo "flagData = [];\n";
		
		foreach($_GET as $id) {
			if(is_numeric($id)) {
				echo "priceData = [";
				foreach($prod[$id]->product_hist as $hist) {
					echo "[".$hist[0].", ".$hist[1]."], ";
				}
				echo "];\n";
				
				echo "summaryData = [";
				foreach($prod[$id]->product_hist as $hist) {
					echo "[".$hist[0].", ".$hist[1]."], ";
				}
				echo "];\n";
				
				echo "productFinance.addData(\"".$prod[$id]->product_id."\", ".
						"priceData, summaryData, flagData, \"productfinance\");\n\n";
			}
			else
				continue;
		}
	?>
	});
	</script>
	
	<div id="ajax">
	</div>
</body>
</html>