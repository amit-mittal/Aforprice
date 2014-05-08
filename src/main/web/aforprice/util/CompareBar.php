<?php
/* 
 *  @Author: Amit Mittal
 * 
 *  This (Static) class deals with compare bar
 */
?>
<?php
chdir(dirname(__FILE__));
	include_once('Util.php');
?>
<?php
class CompareBar {
	
	/* This constructir has been deliberatly made
	 * private so that initialization of this class is not
	 * possible and it could properly server as a static class */
	private function __construct() {
		/* If any initialization required for this class
		 * put it here, and call this construct from every
		 * function necessary */
	}
	
	
	/*
	 * @param prefix: prefix of the url
	 * @param params: extra parameters of the url
	 * @param sort: supported sorts
	 * @param extra: extra values if required
	 */
	public static function GetCompareBar($prefix, $params, $sort, $extra=array()) {
		$retailer = $params['retailer'];
		$categoryId = $params['category'];
		$page = $params['page'];
		$sortBy = $params['sortBy'];
		$priceFilterFrom = $params['priceFilterFrom'];
		$priceFilterTo = $params['priceFilterTo'];

		?>
			<div class="compare_bar">
				<div class="right sort_by">
					Sort By:
					<select class="sort_by_option" onchange="window.location.href=this.value; ">
					<?php
						$params['sortBy'] = 0;
						if(isset($sort[0])) {
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($sortBy==$sort[0])?'selected="selected"':"").'>Price: Low-High</option>';
						}
						$params['sortBy'] = 1;
						if(isset($sort[1])) {
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($sortBy==$sort[1])?'selected="selected"':"").'>Price: High-Low</option>';
						}
						$params['sortBy'] = 2;
						if(isset($sort[2])) {
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($sortBy==$sort[2])?'selected="selected"':"").'>Drop Percentage</option>';
						}
						$params['sortBy'] = 3;
						if(isset($sort[3])) {
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($sortBy==$sort[3])?'selected="selected"':"").'>Best Selling</option>';
						}
						$params['sortBy'] = 4;
						if(isset($sort[4])) {
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($sortBy==$sort[4])?'selected="selected"':"").'>Review Ratings</option>';
						}
						$params['sortBy'] = 5;
						if(isset($sort[5])) {
							echo '<option value="'.Util::GetURL($prefix, $params).'" '.(($sortBy==$sort[5])?'selected="selected"':"").'>Relevance</option>';
						}
					?>
					</select>
				</div>
				<div class="compare_list">
					Compare upto 4 items
					<ul>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
					</ul>
					<button class="compare_button" ></button>
				</div>	
			</div>
		<?php
	}
}
?>
