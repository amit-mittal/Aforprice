<?php
/* 
 *  @Author: Amit Mittal
 * 
 *  This (Static) class deals with refine bar
 */
?>
<?php
chdir(dirname(__FILE__));
	include_once 'Util.php';
	include_once '../category/categoryList.php';
	include_once '../category/getCategories.php';
?>
<?php
class RefineBar {
	
	/* This constructir has been deliberatly made
	 * private so that initialization of this class is not
	 * possible and it could properly server as a static class */
	private function __construct() {
		/* If any initialization required for this class
		 * put it here, and call this construct from every
		 * function necessary */
	}
	
	private static function GetAboveCategories($retailer, $categoryId){
		global $thriftClient,
				$categoryPath;

		if(!isset($categoryPath)){
			try {
				if(!isset($thriftClient)) {
					$thriftClient = new ThriftRetailerClient();
				}
			
				if($thriftClient->connected) {
					$categoryPath = $thriftClient->getCategoryPath($categoryId);
				}
				$categoryPath = array_reverse($categoryPath);
			}
			catch(Exception $e) {
				echo "Error in getting category Path: ".$e->getMessage();
			}
		}

		return $categoryPath;
	}

	private static function GetBelowCategories($retailer, $categoryId){
		global $categoryList,
				$childCategoryList;

		if(!isset($childCategoryList)){
			try {
				if(!isset($categoryList)) {
					$categoryList = new CategoryList($retailer);
				}

				$childCategoryList = $categoryList->getChildCategoriesThrift($categoryId);
			} catch (Exception $e) {
				echo "Error in getting child categories: ".$e->getMessage();
			}
		}
		
		if($childCategoryList != null)
			return $childCategoryList;
		else
			return array();
	}	
	
	/*
	 * @param prefix: prefix of the url
	 * @param params: extra parameters of the url
	 * @param filterInfo: filter related values
	 * @param extra: extra values if required
	 */
	public static function GetRefineBarForTerminalPage($prefix, $params, $filterInfo, $extra=array()) {
		$retailer = $params['retailer'];
		$categoryId = $params['category'];
		$page = $params['page'];
		$sortBy = $params['sortBy'];
		$priceFilterFrom = $params['priceFilterFrom'];
		$priceFilterTo = $params['priceFilterTo'];
		$reviewFilterFrom = $params['reviewFilterFrom'];
		$reviewFilterTo = $params['reviewFilterTo'];
		?>
			<div id="refine_bar" >	
				<ul>
					<select name="AnyPrice" onchange="window.location.href=this.value;" >
					<?php
						unset($params['priceFilterFrom']);
						unset($params['priceFilterTo']);
						echo '<option value="'.Util::GetURL($prefix, $params).' selected="selected"> Any Price </option>';
						foreach ($filterInfo['price'] as $filterInterval => $numProds){
							$interval = explode("_", $filterInterval);
							$params['priceFilterFrom'] = $interval[0];
							$params['priceFilterTo'] = $interval[1];
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($priceFilterTo==$params['priceFilterTo'] && $priceFilterFrom==$params['priceFilterFrom'])?'selected="selected"':"").' > $'.$params['priceFilterFrom'].'  -  $'.$params['priceFilterTo'].' ('.$numProds.')</option>';
						}
					?>
					</select>
					<?php
						if($filterInfo['review'] != null){
							echo '<select name="AnyRating" onchange="window.location.href=this.value;" >';
							$params['priceFilterFrom'] = $priceFilterFrom;
							$params['priceFilterTo'] = $priceFilterTo;
							unset($params['reviewFilterFrom']);
							unset($params['reviewFilterTo']);
							echo '<option value="'.Util::GetURL($prefix, $params).' selected="selected"> Any Rating </option>';
							foreach ($filterInfo['review'] as $filterInterval => $numProds){
								$interval = explode("_", $filterInterval);
								$params['reviewFilterFrom'] = $interval[0];
								$params['reviewFilterTo'] = $interval[1];
								if($params['reviewFilterTo']>0)
									echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($reviewFilterTo==$params['reviewFilterTo'] && $reviewFilterFrom==$params['reviewFilterFrom'])?'selected="selected"':"").' >'.$params['reviewFilterFrom'].'  -  '.$params['reviewFilterTo'].' ('.$numProds.')</option>';
								else
									echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($reviewFilterTo==$params['reviewFilterTo'] && $reviewFilterFrom==$params['reviewFilterFrom'])?'selected="selected"':"").' >Unrated ('.$numProds.')</option>';
							}
							echo '</select>';
						}
					?>
				</ul>
			</div>
		<?php
	}

	/*
	 * @param prefix: prefix of the url
	 * @param params: extra parameters of the url
	 * @param filterInfo: filter related values
	 * @param extra: extra values if required
	 */
	public static function GetRefineBarForSearchPage($prefix, $params, $filterInfo, $extra=array()) {
		$retailer = $params['retailer'];
		$categoryId = $params['category'];
		$page = $params['page'];
		$sortBy = $params['sortBy'];
		$priceFilterFrom = $params['priceFilterFrom'];
		$priceFilterTo = $params['priceFilterTo'];

		$minPrice = $filterInfo['minPrice'];
		$maxPrice = $filterInfo['maxPrice'];
		$maxPriceDiff = $maxPrice - $minPrice;

		if ($maxPriceDiff<500) {
			$numDiv = 3;
			$diff = ceil($maxPriceDiff/$numDiv);     							 // dividing by no of intervals for filtering	
		} else {
			$numDiv = 5;
			$diff = ceil($maxPriceDiff/$numDiv);
		}
		
	?>
			<div id="refine_bar" >
				<ul>
					<?php
						//TODO logic of filter intervals need to be changed
						echo '<select name="AnyPrice" onchange="window.location.href=this.value;" >';
					
						unset($params['priceFilterFrom']);
						unset($params['priceFilterTo']);
						echo '<option value="'.Util::GetURL($prefix, $params).' selected="selected"> Any Price </option>';
						$params['priceFilterTo'] = $minPrice;
						for ($i=0; $i<$numDiv; $i++){
							$params['priceFilterFrom'] = $params['priceFilterTo'];
							$params['priceFilterTo'] = min($params['priceFilterFrom'] + $diff, $maxPrice);
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($priceFilterTo==$params['priceFilterTo'] && $priceFilterFrom==$params['priceFilterFrom'])?'selected="selected"':"").' > $'.$params['priceFilterFrom'].'  -  $'.$params['priceFilterTo'].' </option>';
						}
						
						echo '</select>';
					

						//Category Filter
						unset($params['priceFilterFrom']);
						unset($params['priceFilterTo']);
						
						echo '<select name="AnyCategory" onchange="window.location.href=this.value;">';
						
						//printing the top option - All Departments
						$params['category'] = -1;
						echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($categoryId==$params['category'])?'selected="selected"':"").'> All Departments </option>';
						
						//printing the category path under which have filtered by categoryId
						$aboveCategories = self::GetAboveCategories($retailer, $categoryId);
						foreach ($aboveCategories as $key => $category) {
							$params['category'] = $category->categoryId;
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($categoryId==$params['category'])?'selected="selected"':"").'>'.$category->categoryName.'</option>';
						}
						
						//printing the subCategories of the categoryId under which filtering has been done
						$belowCategories = self::GetBelowCategories($retailer, $categoryId);
						if(!empty($belowCategories)){
							echo '<optgroup label="SubCategories">';
							foreach ($belowCategories as $key => $category) {
								$params['category'] = $category->categoryId;
								echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($categoryId==$params['category'])?'selected="selected"':"").' padding="6px">'.$category->categoryName.'</option>';
							}
							echo '</optgroup>';
						}

						//if filtered under all departments, can change category from here also
						if($categoryId == -1){
							global $categories;
							foreach ($categories as $key => $category) {
								$params['category'] = $category->categoryId;
								echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($categoryId==$params['category'])?'selected="selected"':"").'>'.$category->name.'</option>';
							}
						}

						echo '</select>';
					?>
				</ul>
			</div>
		<?php
	}
}
?>