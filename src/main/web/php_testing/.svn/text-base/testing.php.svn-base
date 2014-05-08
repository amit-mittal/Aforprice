<?php
include 'db_constants.php';
include 'db_class.php';
?>
<table>
<tr>
	<th>ID</th>
	<th>Name</th>
	<th>Parent</th>
	<th>fetchRule</th>
	<th>active</th>
	<th>parent</th>
</tr>
<?php
try {
	$con = new db_class(DB_HOST, DB_USER, DB_PASS, DB_PRODS);
	$con->connect();
		
	$query = "SELECT * FROM CATEGORY WHERE CATEGORY.RETAILER_ID = 'walmart'";
	$con->makeQuery($query);
		
	while($row = $con->getRow()) {
		echo "<tr>";
		echo "<th>".$row[0]."</th>";
		echo "<th><a href=\"".$row[6]."\">".$row[1]."</a></th>";
		echo "<th>".$row[3]."</th>";
		echo "<th>".$row[5]."</th>";
		echo "<th>".$row[7]."</th>";
		echo "<th>".$row[8]."</th>";
		echo "</tr>";
	}
}
catch(DBConnectionException $e) {
	echo $e->getMessage();
}
catch(DBQueryException $e) {
	echo $e->getMessage();
}
?>
</table>