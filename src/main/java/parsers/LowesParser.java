package parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parsers.html.ClassNames;
import parsers.html.LowesClassNames;
import parsers.util.PriceTypes;
import stores.NullProductStore;
import util.Constants;
import util.ProductUtils;
import util.build.ProductSummaryBuilder;
import entities.ProductSummary;
import entities.Retailer;


public class LowesParser extends ProductsParser {
	
	private ClassNames htmlClasses = new LowesClassNames();
	private static final Logger LOGGER = Logger.getLogger(LowesParser.class);
	
	//Rating 3.79 out of 5 stars
	private static final Pattern reviewPatt = Pattern.compile("Rating (.*) out of 5 stars");
	private static final Pattern numReviewPatt = Pattern.compile("(.*) Reviews.*");

	public LowesParser() {
		super(Retailer.ID.LOWES);
		//Lowes give prices based on your location, below cookie will set your local store and that is used to get price
		cookies.put("selectedStore1", Constants.LOWES_STORE);
		//without this cookie, lowes always return the first page.
		cookies.put("LowesSearchSessionFacade", "%7B%22articlesPerPage%22%3A0%2C%22productsPerPage%22%3A48%7D");
	}
	
	@Override
	protected ClassNames getClassNames(){
		return htmlClasses;
	}
	/*
	 * <div class="productWrapper">
		       					<div class="productImgArea">
		       						<a href="/pd_327356-73823-40368_4294810311__?productId=3274845&Ns=p_product_qty_sales_dollar&pl=1&currentURL=%3FNs%3Dp_product_qty_sales_dollar&facetInfo=" title="View Details" class="productImgLink">
		       							<img class="productImg" src="http://images.lowes.com/product/converted/023169/023169129320sm.jpg" alt="Electrolux 3 Installation Inlet Kit" />
		       						</a>								
		       					</div>
		       					<div class="titleArea">
		       						<h3 class="productTitle">
		       							<a href="/pd_327356-73823-40368_4294810311__?productId=3274845&Ns=p_product_qty_sales_dollar&pl=1&currentURL=%3FNs%3Dp_product_qty_sales_dollar&facetInfo=" title="View Electrolux 3 Installation Inlet Kit" name="listpage_productname">Electrolux 3 Installation Inlet Kit</a>
		       						</h3>
		       					<div class="pricingArea">		       										       					       						
			       						<p class="pricing">
																<span>Price</span>																	
																			<strong>
																				$139.00
																			</strong>
																			<!-- cbc_Contract_Price start --><!-- cbc_Contract_Price end -->
			       						</p>				       						       					
								<div class="product-description">		       						
		       						<ul class="productInfo">
										<li>
											Item #: 358884
										</li>
										<li class="last">
											Model #: FBD2400KW
										</li>
									</ul>
										<ul class="prod-detail">											
													<li>
														A quiet performance every time
													</li>
												
													<li>
														Mechanical controls
													</li>										
										</ul>									
		       					</div>														
																	
																	(non-Javadoc)
	 * 
	 * -------------------------page with single product
	 * <div id="productLeft" class="grid_12 alpha"> 
 <div id="detailCont" class="grid_12 alpha"> 
  <div id="imgCont" class="grid_6 alpha"> 
   <div id="imgTopBlock" class="border"> 
    <div id="zoomWrap"> 
     <div id="regularImage"> 
      <div id="imageNav"></div> 
      <img id="prodPrimaryImg" class="productimage" src="http://images.lowes.com/product/converted/012505/012505224522lg.jpg" alt="Frigidaire 19.53 cu ft Commericial Freezerless Refrigerator (Stainless Steel) ENERGY STAR" /> 
      <span class="energy_star"></span> 
     </div> 
     <div id="zoomPort" class="border"> 
      <div id="zoomedImage"> 
       <img class="productimage" src="http://images.lowes.com/product/converted/012505/012505224522xl.jpg" alt="Zoomed: Frigidaire 19.53 cu ft Commericial Freezerless Refrigerator (Stainless Steel) ENERGY STAR" /> 
      </div> 
     </div> 
    </div> 
    <div id="imageControls"> 
    </div> 
   </div> 
  </div> 
  <div id="descCont" class="grid_6 omega"> 
   <h1>Frigidaire&nbsp;19.53 cu ft Commericial Freezerless Refrigerator (Stainless Steel) ENERGY STAR</h1> 
   <p class="itemmodel"> Item #: <span id="ItemNumber">263551</span>&nbsp;|&nbsp; Model #: <span id="ModelNumber">FCGM201RFB</span> </p> 
   <p id="review"> </p>
   <div id="BVRRSummaryContainer"> 
    <span class="productRating"> <img src="/wcsstore/B2BDirectStorefrontAssetStore/images/stars_review_30.gif" title="3 / 5" alt="3 / 5" class="stars" /> 2 Reviews </span> 
   </div> 
   <p></p> 
   <!--  start CachedItemPrice -->
   <!-- Start - JSP File Name:  CatalogEntryPriceDisplay.jspf -->
   <!-- retrieve Price from paObj      --> 
   <p class="pricing"> <strong>View Price in Cart</strong> <a href="#" rel="suggestedPrice_help" class="contextHelp"> <img src="/images/icon-question_i.png" alt="Help Icon" /> </a> </p>
   <div id="suggestedPrice_help" class="hidden">
    <p>Lowe's is committed to offering the lowest possible prices on the best selection of products. Since we've priced this item lower than the manufacturer suggests, we're not allowed to display the price until the item is in your shopping cart.</p>
    <p>Don't worry, you can still remove the item from your cart if you decided not to buy it.</p>
   </div> 
   <p></p> 
   <div class="priceDescriptors"> 
    <p class="suggestedPrice">Suggested Retail Price:<span>$1,649.00</span></p> 
    <p class="savingsMsg"> Ends 07/25/2012 </p> 
   </div> 
   <!-- End - JSP File Name:  CatalogEntryPriceDisplay.jspf --> 
   <p> <span class="ccfinance"></span> <span class="applynow"> <strong>Get 5%* Off Every Day or Special Financing**</strong><br /><span>Minimum Purchase Required</span> <a title="Apply Now" href="#specialfinancing_applynow" name="detailpage_specialfinancingapplynow" target="_blank" onclick="window.open('https://apply.lowes.com/eapplygen2/load.do?cHash=117440611&amp;subActionId=1000&amp;langId=en&amp;siteCode=LUHPLCCE1'); return false;"> Get Details</a> </span> </p> 
   <!-- Starting ProductDetailMyStore.jspf --> 
   <input type="hidden" value="SOS" id="itemType" /> 
   <input type="hidden" id="itemHasFuture" value="true" /> 
   <div id="priceAvailability" class="hidden"> 
    <form name="OrderItemAddForm" id="OrderItemAddForm" action="/OrderItemAdd" method="post"> 
     <input type="hidden" name="cmCategoryId" id="cmCategoryId" value="" /> 
     <input type="hidden" name="catalogId" id="catalogId" value="10051" /> 
     <input type="hidden" name="langId" id="langId" value="-1" /> 
     <input type="hidden" name="storeId" id="storeId" value="10151" /> 
     <input type="hidden" name="catEntryId_1" id="catEntryId" value="3340592" /> 
     <input type="hidden" name="productId" id="productId" value="3340592" /> 
     <input type="hidden" name="AddToCartInterstitial" id="AddToCartInterstitial" value="1" /> 
     <input type="hidden" name="hasCTSitems" id="hasCTSitems" value="false" /> 
     <input type="hidden" name="eppSelected" id="eppSelected" value="false" /> 
     <input type="hidden" name="errorViewName" id="errorViewName" value="CatalogItemAddErrorView" /> 
     <input type="hidden" name="calculationUsageId" id="calculationUsageId" value="-1" /> 
     <input type="hidden" name="doPrice" id="doPrice" value="N" /> 
     <input type="hidden" name="shouldCachePage" id="shouldCachePage" value="false" /> 
     <input type="hidden" name="updatePrices" id="updatePrices" value="1" /> 
     <input type="hidden" name="InventoryStatus" id="InventoryStatus" value="NALC" /> 
     <input type="hidden" name="URL" id="URL" value="OrderItemDisplay?URL=OrderCalculate" /> 
     <input type="hidden" name="paOfferPrice" id="paOfferPrice" value="1484.1" /> 
     <input type="hidden" name="Ntk" id="Ntk" value="" /> 
     <input type="hidden" name="Ntt" id="Ntt" value="" /> 
     <input type="hidden" name="N" id="N" value="4294789498" /> 
     <input type="hidden" name="fromItemDisplay" id="fromItemDisplay" value="true" /> 
     <input type="hidden" name="published" id="published" value="" /> 
     <input type="hidden" name="parentProductCacheKey" value="ProductDisplay:3340592:1613:1,484.10::false:false:true:true:true:1342986678383" /> 
     <!--  shipmode(s) Start -->
     <!-- To Fix Defect# 2575 -->
     <!-- To Fix Defect#2575 --> 
     <ul id="delivery"> 
      <!--  shipmode(s) elements Start --> 
      <li class="methods 
															   								
																pickup
																	
													 "> <input type="radio" name="shipModeId_1" class="deliveryMethod" id="PL" value="10052" /> <a class="ribbon availHelp" rel="availabilityModalContent" href="#"> 
        <div class="icon"></div> </a> 
       <div class="content"> 
        <p class="freecopy">FREE</p> 
        <label for="storePickUp"> <h3>Store Pickup</h3> <p> Your order will be ready for pickup from <strong>Lowe's Of W Jordan, UT by 08/05/2012</strong>. </p> <span class="ship-by-message"> </span> </label> 
       </div> </li> 
      <li class="methods 
																
																truck
																	
													 "> <input type="radio" name="shipModeId_1" class="deliveryMethod" id="LD" value="10054" /> <a class="ribbon availHelp" rel="availabilityModalContent" href="#"> 
        <div class="icon"></div> </a> 
       <div class="content"> 
        <p class="freecopy">FREE</p> 
        <label for="truckDelivery"> <h3>Lowe's Truck Delivery</h3> <p> Your order will be ready for delivery to you from <strong>Lowe's Of W Jordan, UT by 08/05/2012</strong>. </p> <span class="ship-by-message"> </span> </label> 
       </div> </li> 
      <!--  shipmode(s) End --> 
      <li class="methods parcel disabled"> <input type="radio" class="deliveryMethod" name="shipModeId_1" /> <a class="ribbon availHelp"> <span class="icon"></span> </a> 
       <div class="content"> 
        <h3>Parcel Shipping</h3> 
        <p class="availability">Unavailable for This Order</p> 
        <p>Sent by carriers like UPS, FedEx, USPS, etc.</p> 
       </div> </li> 
      <li class="delivery-message"><span class="help-phone">Need help?</span> <span class="help-phone">Call 1-866-609-3066.</span></li> 
     </ul> 
     <ul id="price"> 
      <li class="line-item"> <strong> Frigidaire 19.53 cu ft Commericial Freezerless Refrigerator (Stainless Steel) ENERGY STAR</strong>  <span id="mystore-item-price" class="line-price"> <p class="suggestedPrice"><span>$1,649.00</span></p> </span></li> 
     </ul> 
     <ul id="warranty"> 
      <li class="line-item"> 
       <div class="inputField"> 
        <input type="checkbox" name="catEntryId_2" value="3444070" class="warranty-checkbox" /> 
        <input type="hidden" id="quantity_2" name="quantity_2" value="1" /> 
        <input type="hidden" id="shipMode_2" name="shipMode_2" value="DUMMY" /> 
        <span class="epp">2-Year Major Appliance Extended Protection Plan ($1500+)</span> 
       </div> <span class="line-price" id="warrantyprice_3444070">$89.97</span> </li> 
      <li class="line-item"> 
       <div class="inputField"> 
        <input type="checkbox" name="catEntryId_2" value="3444078" class="warranty-checkbox" /> 
        <input type="hidden" id="quantity_2" name="quantity_2" value="1" /> 
        <input type="hidden" id="shipMode_2" name="shipMode_2" value="DUMMY" /> 
        <span class="epp">4-Year Major Appliance Extended Protection Plan ($1500+ )</span> 
       </div> <span class="line-price" id="warrantyprice_3444078">$149.97</span> </li> 
     </ul> 
     <ul id="subTotal"> 
      <li id="subtotal">Subtotal : <strong>View Price in Cart</strong></li> 
      <li>
       <div class="addToCart"> 
        <label for="quantity_of_3340592">Qty.:</label> 
        <input id="minQty_3340592" type="hidden" name="ordMinQty" value="-1" /> 
        <input type="hidden" name="ordMulQty" value="-1" /> 
        <input id="quantity_of_3340592" type="text" name="quantity" class="qty-input" value="1" maxlength="5" /> 
        <a title="Add to Cart" id="add_3340592" rel="chooseDelMethod" name="detailpage_addtocartinterstitialpage" href="#addtocartinterstitial_page" class="button add"> <span>Add to Cart</span></a> 
       </div> </li> 
      <!--  shipmode(s) elements End --> 
      <li class="delivery-estimate"> <a href="/ShipEstimatesView?storeId=10151&amp;langId=-1&amp;catalogId=10051&amp;catentryId=3340592&amp;itemType=SOS&amp;availLD=true" class="availHelp deliveryRate left" name="delivery_rate_estimate" title="Delivery Rate Estimate*" target="_blank" rel="nofollow">Delivery Rate Estimate</a> </li> 
     </ul> 
     <!-- Stage 3 - Required Products and EPP - The hidden parameters will be sent if the item is not a bundle --> 
    </form> 
    <div id="epp-help" style="display: none;">
     Benefits of This Extended Protection Plan
     <ul>
      <li>Begins after manufacturer's labor warranty ends</li>
      <li>Includes 100% coverage of parts and labor</li>
      <li>Provides a one-time product replacement if the unit can't be repaired</li>
     </ul>
    </div> 
   </div> 
   <script type="text/javascript">
	
				var foundD...
 id="productLeft" class="grid_12 alpha"
http://www.lowes.com/pd_263551-2251-FCGM201RFB_4294789498__?identifier=Appliances%2FRefrigerators%2FCommercial-Refrigerators&productId=3340592&rpp=16&Ns=p_product_qty_sales_dollar%7C1&searchQueryType=1
[ , <div id="detailCont" class="grid_12 alpha"> 
 <div id="imgCont" class="grid_6 alpha"> 
  <div id="imgTopBlock" class="border"> 
   <div id="zoomWrap"> 
    <div id="regularImage"> 
     <div id="imageNav"></div> 
     <img id="prodPrimaryImg" class="productimage" src="http://images.lowes.com/product/converted/012505/012505224522lg.jpg" alt="Frigidaire 19.53 cu ft Commericial Freezerless Refrigerator (Stainless Steel) ENERGY STAR" /> 
     <span class="energy_star"></span> 
    </div> 
    <div id="zoomPort" class="border"> 
     <div id="zoomedImage"> 
      <img class="productimage" src="http://images.lowes.com/product/converted/012505/012505224522xl.jpg" alt="Zoomed: Frigidaire 19.53 cu ft Commericial Freezerless Refrigerator (Stainless Steel) ENERGY STAR" /> 
     </div> 
    </div> 
   </div> 
   <div id="imageControls"> 
   </div> 
  </div> 
 </div> 
 <div id="descCont" class="grid_6 omega"> 
  <h1>Frigidaire&nbsp;19.53 cu ft Commericial Freezerless Refrigerator (Stainless Steel) ENERGY STAR</h1> 
  <p class="itemmodel"> Item #: <span id="ItemNumber">263551</span>&nbsp;|&nbsp; Model #: <span id="ModelNumber">FCGM201RFB</span> </p> 
  <p id="review"> </p>
  <div id="BVRRSummaryContainer"> 
   <span class="productRating"> <img src="/wcsstore/B2BDirectStorefrontAssetStore/images/stars_review_30.gif" title="3 / 5" alt="3 / 5" class="stars" /> 2 Reviews </span> 
  </div> 
  <p></p> 
  <!--  start CachedItemPrice -->
  <!-- Start - JSP File Name:  CatalogEntryPriceDisplay.jspf -->
  <!-- retrieve Price from paObj      --> 
  <p class="pricing"> <strong>View Price in Cart</strong> <a href="#" rel="suggestedPrice_help" class="contextHelp"> <img src="/images/icon-question_i.png" alt="Help Icon" /> </a> </p>
  <div id="suggestedPrice_help" class="hidden">
   <p>Lowe's is committed to offering the lowest possible prices on the best selection of products. Since we've priced this item lower than the manufacturer suggests, we're not allowed to display the price until the item is in your shopping cart.</p>
   <p>Don't worry, you can still remove the item from your cart if you decided not to buy it.</p>
  </div> 
  <p></p> 
  <div class="priceDescriptors"> 
   <p class="suggestedPrice">Suggested Retail Price:<span>$1,649.00</span></p> 
   <p class="savingsMsg"> Ends 07/25/2012 </p> 
  </div> 
  <!-- End - JSP File Name:  CatalogEntryPriceDisplay.jspf --> 
  <p> <span class="ccfinance"></span> <span class="applynow"> <strong>Get 5%* Off Every Day or Special Financing**</strong><br /><span>Minimum Purchase Required</span> <a title="Apply Now" href="#specialfinancing_applynow" name="detailpage_specialfinancingapplynow" target="_blank" onclick="window.open('https://apply.lowes.com/eapplygen2/load.do?cHash=117440611&amp;subActionId=1000&amp;langId=en&amp;siteCode=LUHPLCCE1'); return false;"> Get Details</a> </span> </p> 
  <!-- Starting ProductDetailMyStore.jspf --> 
  <input type="hidden" value="SOS" id="itemType" /> 
  <input type="hidden" id="itemHasFuture" value="true" /> 
  <div id="priceAvailability" class="hidden"> 
   <form name="OrderItemAddForm" id="OrderItemAddForm" action="/OrderItemAdd" method="post"> 
    <input type="hidden" name="cmCategoryId" id="cmCategoryId" value="" /> 
    <input type="hidden" name="catalogId" id="catalogId" value="10051" /> 
    <input type="hidden" name="langId" id="langId" value="-1" /> 
    <input type="hidden" name="storeId" id="storeId" value="10151" /> 
    <input type="hidden" name="catEntryId_1" id="catEntryId" value="3340592" /> 
    <input type="hidden" name="productId" id="productId" value="3340592" /> 
    <input type="hidden" name="AddToCartInterstitial" id="AddToCartInterstitial" value="1" /> 
    <input type="hidden" name="hasCTSitems" id="hasCTSitems" value="false" /> 
    <input type="hidden" name="eppSelected" id="eppSelected" value="false" /> 
    <input type="hidden" name="errorViewName" id="errorViewName" value="CatalogItemAddErrorView" /> 
    <input type="hidden" name="calculationUsageId" id="calculationUsageId" value="-1" /> 
    <input type="hidden" name="doPrice" id="doPrice" value="N" /> 
    <input type="hidden" name="shouldCachePage" id="shouldCachePage" value="false" /> 
    <input type="hidden" name="updatePrices" id="updatePrices" value="1" /> 
    <input type="hidden" name="InventoryStatus" id="InventoryStatus" value="NALC" /> 
    <input type="hidden" name="URL" id="URL" value="OrderItemDisplay?URL=OrderCalculate" /> 
    <input type="hidden" name="paOfferPrice" id="paOfferPrice" value="1484.1" /> 
    <input type="hidden" name="Ntk" id="Ntk" value="" /> 
    <input type="hidden" name="Ntt" id="Ntt" value="" /> 
    <input type="hidden" name="N" id="N" value="4294789498" /> 
    <input type="hidden" name="fromItemDisplay" id="fromItemDisplay" value="true" /> 
    <input type="hidden" name="published" id="published" value="" /> 
    <input type="hidden" name="parentProductCacheKey" value="ProductDisplay:3340592:1613:1,484.10::false:false:true:true:true:1342986678383" /> 
    <!--  shipmode(s) Start -->
    <!-- To Fix Defect# 2575 -->
    <!-- To Fix Defect#2575 --> 
    <ul id="delivery"> 
     <!--  shipmode(s) elements Start --> 
     <li class="methods 
															   								
																pickup
																	
													 "> <input type="radio" name="shipModeId_1" class="deliveryMethod" id="PL" value="10052" /> <a class="ribbon availHelp" rel="availabilityModalContent" href="#"> 
       <div class="icon"></div> </a> 
      <div class="content"> 
       <p class="freecopy">FREE</p> 
       <label for="storePickUp"> <h3>Store Pickup</h3> <p> Your order will be ready for pickup from <strong>Lowe's Of W Jordan, UT by 08/05/2012</strong>. </p> <span class="ship-by-message"> </span> </label> 
      </div> </li> 
     <li class="methods 
																
																truck
																	
													 "> <input type="radio" name="shipModeId_1" class="deliveryMethod" id="LD" value="10054" /> <a class="ribbon availHelp" rel="availabilityModalContent" href="#"> 
       <div class="icon"></div> </a> 
      <div class="content"> 
       <p class="freecopy">FREE</p> 
       <label for="truckDelivery"> <h3>Lowe's Truck Delivery</h3> <p> Your order will be ready for delivery to you from <strong>Lowe's Of W Jordan, UT by 08/05/2012</strong>. </p> <span class="ship-by-message"> </span> </label> 
      </div> </li> 
     <!--  shipmode(s) End --> 
     <li class="methods parcel disabled"> <input type="radio" class="deliveryMethod" name="shipModeId_1" /> <a class="ribbon availHelp"> <span class="icon"></span> </a> 
      <div class="content"> 
       <h3>Parcel Shipping</h3> 
       <p class="availability">Unavailable for This Order</p> 
       <p>Sent by carriers like UPS, FedEx, USPS, etc.</p> 
      </div> </li> 
     <li class="delivery-message"><span class="help-phone">Need help?</span> <span class="help-phone">Call 1-866-609-3066.</span></li> 
    </ul> 
    <ul id="price"> 
     <li class="line-item"> <strong> Frigidaire 19.53 cu ft Commericial Freezerless Refrigerator (Stainless Steel) ENERGY STAR</strong>  <span id="mystore-item-price" class="line-price"> <p class="suggestedPrice"><span>$1,649.00</span></p> </span></li> 
    </ul> 
    <ul id="warranty"> 
     <li class="line-item"> 
      <div class="inputField"> 
       <input type="checkbox" name="catEntryId_2" value="3444070" class="warranty-checkbox" /> 
       <input type="hidden" id="quantity_2" name="quantity_2" value="1" /> 
       <input type="hidden" id="shipMode_2" name="shipMode_2" value="DUMMY" /> 
       <span class="epp">2-Year Major Appliance Extended Protection Plan ($1500+)</span> 
      </div> <span class="line-price" id="warrantyprice_3444070">$89.97</span> </li> 
     <li class="line-item"> 
      <div class="inputField"> 
       <input type="checkbox" name="catEntryId_2" value="3444078" class="warranty-checkbox" /> 
       <input type="hidden" id="quantity_2" name="quantity_2" value="1" /> 
       <input type="hidden" id="shipMode_2" name="shipMode_2" value="DUMMY" /> 
       <span class="epp">4-Year Major Appliance Extended Protection Plan ($1500+ )</span> 
      </div> <span class="line-price" id="warrantyprice_3444078">$149.97</span> </li> 
    </ul> 
    <ul id="subTotal"> 
     <li id="subtotal">Subtotal : <strong>View Price in Cart</strong></li> 
     <li>
      <div class="addToCart"> 
       <label for="quantity_of_3340592">Qty.:</label> 
       <input id="minQty_3340592" type="hidden" name="ordMinQty" value="-1" /> 
       <input type="hidden" name="ordMulQty" value="-1" /> 
       <input id="quantity_of_3340592" type="text" name="quantity" class="qty-input" value="1" maxlength="5" /> 
       <a title="Add to Cart" id="add_3340592" rel="chooseDelMethod" name="detailpage_addtocartinterstitialpage" href="#addtocartinterstitial_page" class="button add"> <span>Add to Cart</span></a> 
      </div> </li> 
     <!--  shipmode(s) elements End --> 
     <li class="delivery-estimate"> <a href="/ShipEstimatesView?storeId=10151&amp;langId=-1&amp;catalogId=10051&amp;catentryId=3340592&amp;itemType=SOS&amp;availLD=true" class="availHelp deliveryRate left" name="delivery_rate_estimate" title="Delivery Rate Estimate*" target="_blank" rel="nofollow">Delivery Rate Estimate</a> </li> 
    </ul> 
    <!-- Stage 3 - Required Products and EPP - The hidden parameters will be sent if the item is not a bundle --> 
   </form> 
   <div id="epp-help" style="display: none;">
    Benefits of This Extended Protection Plan
    <ul>
     <li>Begins after manufacturer's labor warranty ends</li>
     <li>Includes 100% coverage of parts and labor</li>
     <li>Provides a one-time product replacement if the unit can't be repaired</li>
    </ul>
   </div> 
  </div> 
  <script type="text/javascript">
	
				var foundDifferentItemNumber = "Item #:  | Model #: ";
				
</script> 
  <!-- <div name="PAGE_TIMESTAMP" style="visibility: hidden; display: none;" >pageName=CachedItemPrice:1613,1613:; forma...
[grid_12, alpha]
	 * --------------------------------------------------------------------------------------------
	 * 
	 * @see parsers.ProductsParser#parse(org.jsoup.nodes.Document, int, java.lang.String)
	 */
	@Override
	public List<ProductSummary> parse(String pageUrl, Document doc, int categoryId, String categoryName, int startRank, int max){
		Element helper = null;
		int lookupIndex=0;
		Elements productRows = null;
		for(String productRowLookup : htmlClasses.productRows()){
			if(lookupIndex==1)
				productRows = doc.getElementsByAttributeValue("id",productRowLookup);
			else
				productRows = doc.getElementsByClass(productRowLookup);
			if(productRows.size()>0)
				break;
			lookupIndex++;
		}		
		List<ProductSummary> products = new ArrayList<ProductSummary>();
		for(Element productRow: productRows){
			String name;
			String desc = null;
			double price = PriceTypes.UNKNOWN.getValue();
			String url, imageUrl, model = null;

			//image
			helper = productRow.getElementsByTag(htmlClasses.productImage()).first();
			imageUrl = helper.attr("abs:src");
			
			//name and url
			if(lookupIndex==0){	
				helper = productRow.getElementsByClass(htmlClasses.productName()[lookupIndex]).first();
				name = helper.text();
				url = helper.getElementsByAttribute("href").first().absUrl("href");	
				helper = productRow.getElementsByClass(htmlClasses.price()[0]).first();
			}
			else{//lookupIndex=1
				helper = productRow.getElementsByAttributeValue("id",htmlClasses.productName()[lookupIndex]).first().child(0);
				name = helper.text();
				url = doc.baseUri();
				helper = productRow.getElementsByClass(htmlClasses.price()[0]).first();
				if(helper==null)//it is either id or class.. very inconsistent website!
					helper = productRow.getElementsByAttributeValue("id", htmlClasses.price()[0]).first();
			}
			//price			
			String temp = helper.text();
			int index = temp.indexOf("$");
			if(index==-1 && temp.contains("View Price in Cart"))
				price = PriceTypes.NOT_AVAILABLE.getValue();
			else{
				temp = temp.substring(++index).replace(",", "");
				index = temp.indexOf("/"); //i.e. $0.68 / Sq. Ft.
				if(index!=-1){
					index--;
					temp = temp.substring(0,index).trim();
				}
				price = Double.parseDouble(temp);
			}
			//model
			helper = productRow.getElementsByClass(htmlClasses.model()[lookupIndex]).first();
			if(helper != null)
				model = helper.text(); 
			//description
			helper = productRow.getElementsByClass(htmlClasses.productDesc()).first();
			if(helper != null)
				desc = helper.text();
			ProductSummaryBuilder b = new ProductSummaryBuilder();
			b.retailerId = getRetailerId();
			b.categoryId = categoryId;
			b.categoryName = categoryName;
			b.name = name;
			b.price = price;
			b.url = url;
			b.imageUrl = imageUrl;
			b.desc = desc;
			b.model = model;
			b.reviewRating = getReviewRating(productRow);
			b.numReviews = getNumReviews(productRow);
			b.salesRank = startRank == -1?-1:startRank++;
			b.downloadTime = new Date();
			ProductSummary prod = b.build();		

			if(!ProductUtils.isProductValid(prod)){
				LOGGER.warn("Invalid Product: " + prod);
			}
			products.add(prod);						
		}
		return products;
	}

	@Override
	protected String getNextURL(Document doc){
		Element pagination = doc.getElementsByClass("pagination_wrapper").first();
		if(pagination != null){
			Element curr = pagination.getElementsByClass("currentPage").first();
			Element total = pagination.getElementsByClass("totalPages").first();
			if(curr != null && total != null){
				if(curr.text().equals(total.text()))
					return null;
			}
		}
		Element helper = doc.getElementsByClass(htmlClasses.nextUrl()).first();
		if(helper == null)
			return null;
		else
		{
			String url = helper.absUrl("href");
			return url;
		}
	}
	
	@Override
	protected int getProductCountForCategory(Document doc){
		int recordCount = 0;
		try{
			//39 Results
			String recordCountStr = doc.getElementsByClass("numResults").first().text();
			if(recordCountStr != null){
				recordCount = Integer.parseInt(recordCountStr.trim());
			}
		}
		catch(Exception e){
			LOGGER.warn("Can not find product count for category", e);
		}
		return recordCount;
	}


	@Override
	protected boolean isSortedByBestSeller(Document doc, int categoryId) {
		try{
			Element sortElm = doc.getElementsByClass("bottomBar").first();
			if(sortElm != null){
				Elements listElms = sortElm.getElementsByTag("li");
				for(Element listElm: listElms){
					if(listElm.getElementsByClass("sortBy").first() != null )
						continue;
					if(listElm.getElementsByTag("a").first() == null){
						String txt = listElm.text();
						if(txt != null){
							if(txt.trim().equalsIgnoreCase("Best Sellers"))
								return true;
						}
					}
				}
			}
		}catch(Exception e){
			LOGGER.warn("Can not find if sorted by best seller rank", e);
		}
		return false;
	}

	@Override
	protected double getReviewRating(Element product) {
		try{
			Element reviewElm = product.getElementsByClass("productRating").first();
			if(reviewElm != null){
				Element ratingElm = reviewElm.getElementsByTag("img").first();
				if(ratingElm != null){
					String rating = ratingElm.attr("alt");
					Matcher m = reviewPatt.matcher(rating == null?"":rating);
					if(m.find()){
						return Double.parseDouble(m.group(1).trim());		
					}
				}
			}
		}catch(Exception e){
			LOGGER.warn("Can not get review ratings", e);
		}
		return -1;
	}

	@Override
	protected int getNumReviews(Element product) {
		try{
			Element reviewElm = product.getElementsByClass("productRating").first();
			if(reviewElm != null){
				String numReviews = reviewElm.getElementsByTag("a").text();
				Matcher m = numReviewPatt.matcher(numReviews == null?"":numReviews);
				if(m.find()){
					String numReviewsStr = m.group(1).trim().replaceAll(",", "");
					return Integer.parseInt(numReviewsStr);		
				}
				
			}
		}catch(Exception e){
			LOGGER.warn("Can not get num of reviews", e);
		}
		return -1;
	}

	public static void main(String[] args) throws Exception{
		ProductsParser parser = new LowesParser();
		
		boolean debug = true;
		if(debug){
			String url = "http://www.lowes.com/Windows-Doors/Windows/Storm-Windows/_/N-1z11pon/pl?Ns=p_product_qty_sales_dollar|1";
			url = "http://www.lowes.com/Windows-Doors/Windows/Storm-Windows/_/N-1z11pon/pl?page=2";
			url = "http://www.lowes.com/Bathroom/Bathtubs-Whirlpool-Tubs/Bathtubs/_/N-1z0z4hm/pl?_escaped_fragment_=%26rpp%3D32%26page%3D1";
			url = "http://www.lowes.com/Tools/Power-Saws-Saw-Blades/Table-Saws/_/N-1z0x156/pl?Ns=p_product_qty_sales_dollar|1#!";
			url = "http://www.lowes.com/Appliances/Refrigerators/French-Door-Refrigerators/_/N-1z11pm3/pl?Ns=p_product_qty_sales_dollar|1#!";
			url = "http://www.lowes.com/Appliances/Dishwashers/Built-In-Dishwashers/_/N-1z11pl0/pl?Ns=p_product_qty_sales_dollar|1#!";
			parser.parseAndSave(0, "Unknown", url, new NullProductStore());
		}
	}
}