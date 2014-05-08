<?php
include 'product.php';

	$id = $_GET['id'];
	
	$prod = new product($id);
	
	$data = array();
	$data['id'] = $prod->product_id;
	$data['info'] = $prod->product_info;
	$data['history'] = $prod->product_hist;
	
	echo json_encode($data);
?>