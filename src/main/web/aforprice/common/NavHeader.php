<!-- Nav+retailer Header -->
	<div id="nav">
		<div id="navigation">
			<div id="nav_button">
				Shop by<br /><span class="bolder">Department</span>
				<div class="down_big"></div>
			</div>
			<div id="categories" class="dropdown">
				<?php
				chdir(dirname(__FILE__));
					include '../category/getCategoryMenu.php';
				?>
			</div>
		</div>
		<div class="right" style="margin-right: 20px;" id="retail_select">
			<div id="retailers">
				<div id="retail_prev_button"></div>
				<div id="retailer_logos">
					<ul>
						<li data-select="amazonbs"><img src="img/logos/amazon.png" height="39" width="50" /></li>
						<li data-select="babysrus"><img src="img/logos/babiesrus.png" height="39" width="50" /></li>
						<li data-select="bananarepublic"><img src="img/logos/bananarepublic.png" height="39" width="50" /></li>
						<li data-select="bestbuy"><img src="img/logos/bestbuy.png" height="39" width="50" /></li>
						<li data-select="bjs"><img src="img/logos/bjs.png" height="39" width="50" /></li>
						<li data-select="costco"><img src="img/logos/costco.png" height="39" width="50" /></li>
						<li data-select="cvs"><img src="img/logos/cvscaremark.png" height="39" width="50" /></li>
						<li data-select="gap"><img src="img/logos/gap.png" height="39" width="50" /></li>
						<li data-select="homedepot"><img src="img/logos/homedepot.png" height="39" width="50" /></li>
						<li data-select="jcpenny"><img src="img/logos/jcp.png" height="39" width="50" /></li>
						<li data-select="kohls"><img src="img/logos/kohls.png" height="39" width="50" /></li>
						<li data-select="lowes"><img src="img/logos/lowes.png" height="39" width="50" /></li>
						<li data-select="macys"><img src="img/logos/macys.png" height="39" width="50" /></li>
						<li data-select="nordstrom"><img src="img/logos/nordstrom.png" height="39" width="50" /></li>
						<li data-select="oldnavy"><img src="img/logos/oldnavy.png" height="39" width="50" /></li>
						<li data-select="riteaid"><img src="img/logos/riteaid.png" height="39" width="50" /></li>
						<li data-select="samsclub"><img src="img/logos/samsclub.png" height="39" width="50" /></li>
						<li data-select="sears"><img src="img/logos/sears.png" height="39" width="50" /></li>
						<li data-select="staples"><img src="img/logos/staples.png" height="39" width="50" /></li>
						<li data-select="target"><img src="img/logos/target.png" height="39" width="50" /></li>
						<li data-select="toysrus"><img src="img/logos/toysrus.png" height="39" width="50" /></li>
						<li data-select="victoriassecret"><img src="img/logos/vs.png" height="39" width="50" /></li>
						<li data-select="walgreens"><img src="img/logos/walgreens.png" height="39" width="50" /></li>
						<li data-select="walmart"><img src="img/logos/walmart.png" height="39" width="50" /></li>
					</ul>
				</div>
				<div id="retail_next_button"></div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
<!-- Nav+retailer Header ends -->