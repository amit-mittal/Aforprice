<?php
/* 
 *  @Author: Amit Mittal
 * 
 *  This (Static) class deals with pagination of a page
 */
?>
<?php
chdir(dirname(__FILE__));
	include_once('Util.php');
?>
<?php
class Pagination {
	public static $rowLimit = 4;                              //no of products per row
	public static $limit = 20;								  //how many items to show per page
	public static $adjacents = 2;                             // How many adjacent pages should be shown on each side?
	
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
	 * @param pageInfo: stuff related to the page
	 * @param extra: extra values if required
	 */
	public static function GetPagination($prefix, $params, $pageInfo, $extra=array()) {
		$retailer = $params['retailer'];
		$categoryId = $params['category'];
		$page = $params['page'];
		$sortBy = $params['sortBy'];
		$priceFilterFrom = $params['priceFilterFrom'];
		$priceFilterTo = $params['priceFilterTo'];

		$rowLimit = self::$rowLimit;
		$limit = self::$limit;
		$adjacents = self::$adjacents;

		$page = $pageInfo['page'];
		$totalProducts = $pageInfo['totalProducts'];
		$start = $pageInfo['start'];
		$end = $pageInfo['end'];

		$num_pages = 1;
		if($totalProducts>$limit)
			$num_pages = ceil($totalProducts/$limit);

		//page variables
		$prev = $page - 1;							//previous page is page - 1
		$next = $page + 1;							//next page is page + 1
		$last = $num_pages;							//last is = total pages / items per page, rounded up.
		$lpm1 = $last - 1;					     	//second last page

		?>
		<div id="page_info">
		<?php
			if($last > 1)
			{
				echo '<div class="right pagination">';
				
				//previous button
				if ($page>1){
					$params['page'] = $prev;
					echo '<a href="'.Util::GetURL($prefix, $params).'">Previous</a>';
				} else {
					echo '<span class="disabled previous">Previous</a></span>';
				}

				if ($last < 7 + ($adjacents * 2)) 	//not enough pages to bother breaking it up
				{
					for ($counter = 1; $counter <= $last; $counter++) {
						if ($counter == $page){
							echo '<span class="current">'.$counter.'</span>';
						} else {
							$params['page'] = $counter;
							echo '<a href="'.Util::GetURL($prefix, $params).'">'.$params['page'].'</a>';
						}
					}
				}					
				elseif($last > 5 + ($adjacents * 2))	//enough pages to hide some
				{
					//close to beginning; only hide later pages
					if($page < 1 + ($adjacents * 2))
					{
						for ($counter = 1; $counter < 4 + ($adjacents * 2); $counter++) {
							if ($counter == $page){
								echo '<span class="current">'.$counter.'</span>';
							} else {
								$params['page'] = $counter;
								echo '<a href="'.Util::GetURL($prefix, $params).'">'.$params['page'].'</a>';
							}
						}
						echo "....";
						$params['page'] = $last;
						echo '<a href="'.Util::GetURL($prefix, $params).'">'.$params['page'].'</a>';
					}
					//in middle; hide some front and some back
					elseif($last - ($adjacents * 2) > $page && $page > ($adjacents * 2))
					{
						$params['page'] = 1;
						echo '<a href="'.Util::GetURL($prefix, $params).'">'.$params['page'].'</a>';
						echo "....";
						for ($counter = $page - $adjacents; $counter <= $page + $adjacents; $counter++)
						{
							if ($counter == $page){
								echo '<span class="current">'.$counter.'</span>';
							} else {
								$params['page'] = $counter;
								echo '<a href="'.Util::GetURL($prefix, $params).'">'.$params['page'].'</a>';
							}
						}
						echo "....";
						
						$params['page'] = $last;
						echo '<a href="'.Util::GetURL($prefix, $params).'">'.$params['page'].'</a>';
					}
					//close to end; only hide early pages
					else
					{
						$params['page'] = 1;
						echo '<a href="'.Util::GetURL($prefix, $params).'">'.$params['page'].'</a>';
						echo "....";
						for ($counter = $last - (2 + ($adjacents * 2)); $counter <= $last; $counter++)
						{
							if ($counter == $page){
								echo "<span class=\"current\">$counter</span>";
							} else {
								$params['page'] = $counter;
								echo '<a href="'.Util::GetURL($prefix, $params).'">'.$params['page'].'</a>';
							}
						}
					}		
				}

				//next button
				if ($page < $counter-1) {
					$params['page'] = $next;
					echo '<a href="'.Util::GetURL($prefix, $params).'">Next</a>';
				} else {
					echo '<span class="disabled next">Next</a></span>';
				}
				echo "</div>\n";
			}
		?>	
				
			<br>
			<div>
			<?php if(isset($start) && isset($end) && isset($totalProducts) && $totalProducts>0){ ?>
				&nbsp;&nbsp;&nbsp;Showing <span class="start"><?php echo $start." - " ?></span>
				<span class="end"><?php echo $end; ?></span> of 
				<span class="num_results"><?php echo $totalProducts." products " ?></span>
			<?php } ?>
			</div>
			<div class="clear"></div>
			<br>
		</div>
		<?php
	}
}
?>
