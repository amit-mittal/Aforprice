<?php
	class Product{
		var $productId;
		var $name;
		var $model;
		var $url;
		var $imageUrl;
		var $priceHistory;
		var $retailer;
		var $rank;
		
		function __construct($productId, $name, $model, $imageUrl, $url, $retailer, $rank) {
			$this->productId = $productId;
			$this->name = $name;
			$this->model = $model;			
			$this->imageUrl = $imageUrl;
			$this->url = $url;
			$this->retailer = $retailer;
			$this->rank = $rank;
		}		
		function addPrice($time, $price){
			if($this->priceHistory==NULL)
				$this->priceHistory = array();
			$this->priceHistory[$time]=$price;
		}
		//todo: add min, max, average price
		
	}	
	
?>