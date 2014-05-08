<?php
chdir(dirname(__FILE__));
	include_once 'RequestParser.php';
/* So that header and data is send together and not otherwise which may cause
	invalid connections */
ob_start();
if($_SERVER['REQUEST_METHOD'] === "POST" || $_SERVER['REQUEST_METHOD'] === "GET") {
	$toReturn = RequestParser::parse();
} else {
	$toReturn = RequestParser::invalidRequest("Not a GET or POST request.");
}
header("Content-type: application/json"); //return the content as JSON
echo json_encode($toReturn);

ob_flush();

