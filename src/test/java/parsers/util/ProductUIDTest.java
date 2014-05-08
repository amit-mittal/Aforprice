package parsers.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProductUIDTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAmazonUid1(){
		assertTrue(ProductUID.getAmazonUID("http://www.amazon.com/Olympic-Sleeve-Conversion-Kit-Chrome/dp/B000KOBGNG%3FSubscriptionId%3DAKIAJWXLVHIMPKIZ346Q%26tag%3Dravede-20%26linkCode%3Dxm2%26camp%3D2025%26creative%3D165953%26creativeASIN%3DB000KOBGNG").equals("B000KOBGNG"));
	}

	@Test
	public void testAmazonBSUid() {
		assertTrue(ProductUID.getAmazonBSUID("http://www.amazon.com/Memory-Light-Wheel-Time-Hardcover/dp/0765325950/ref=zg_bs_books_1").equals("0765325950"));
		assertTrue(ProductUID.getAmazonBSUID("http://www.amazon.com/Memory-Light-Wheel-Time-Hardcover/dp/0765325950/").equals("0765325950"));
		assertTrue(ProductUID.getAmazonBSUID("http://www.amazon.com/Memory-Light-Wheel-Time-Hardcover/dp/0765325950").equals("0765325950"));
		assertTrue(ProductUID.getAmazonBSUID("http://www.amazon.com/gp/product/B001EDB1GY/ref=zg_bs_324544011_26/181-8355871-8255017").equals("B001EDB1GY"));
	}
	
	@Test 
	public void testBananaRepUid(){
		assertTrue(ProductUID.getBananaRepublicUid("http://www.bananarepublic.com/products/straight-fit-charcoal-plaid-cotton-trouser-P323302.jsp").equals("323302"));
	}
	
	@Test
	public void testBestBuyUid(){
		assertTrue(ProductUID.getBestBuyUid("http://www.bestbuy.com/site/100+Year+Anniversary+of+Fenway+Park+-+Various+-+CD/5314941.p?id=2447467&skuId=5314941&cmp=RMX&ky=2efes1Xc0ecwp0jt2NhRyhj6y4Cj1P56J").equals("2447467"));
	}
	@Test
	public void testToysrusUid(){
		assertTrue(ProductUID.getToysrusUID("http://www.toysrus.com/product/index.jsp?productId=16655656").equals("16655656"));
		assertTrue(ProductUID.getToysrusUID("http://www.toysrus.com/product/index.jsp?productId=11123394&cp=2255956.2273442.2256395.3254084.2256459&green").equals("11123394"));
	}
	
	@Test
	public void testBjsUid(){
		assertTrue(ProductUID.getBjsUID("http://www.bjs.com/lg-32-led-hdtv-720p-60hz.product.215905?dimId=2000835").equals("215905"));
		assertTrue(ProductUID.getBjsUID("http://www.bjs.com/lg-32-led-hdtv-720p-60hz.product.215905").equals("215905"));
	}
	
	@Test
	public void testCostcoUid(){
		assertTrue(ProductUID.getCostcoUID("http://www.costco.com/Samsung-55%22-Class-1080p-120Hz-3D-Smart-LED-HDTV-Bundle-UN55ES6580F.product.11756412.html?abc=123").equals("11756412"));
		assertTrue(ProductUID.getCostcoUID("http://www.costco.com/Samsung-55%22-Class-1080p-120Hz-3D-Smart-LED-HDTV-Bundle-UN55ES6580F.product.11756412.html#123").equals("11756412"));
		assertTrue(ProductUID.getCostcoUID("http://www.costco.com/Browse/Product.aspx?Prodid=11657751&whse=BC&Ne=4000000&eCat=BC|50126&N=4045521&Mo=5&No=0&Nr=P_CatalogName:BC&cat=49266&Ns=P_Price").equals("11657751"));
	}
	
	@Test
	public void testCVSUid(){
		assertTrue(ProductUID.getCVSUid("http://www.cvs.com/shop/product-detail/CVS-Diapers-Size-1-8-14-lbs?skuId=890259").equals("890259"));
		assertTrue(ProductUID.getCVSUid("http://www.cvs.com/shop/product-detail/CVS-Diapers-Size-1-8-14-lbs?skuId=890259&foo=bar").equals("890259"));
	}
	
	@Test
	public void testGapUid(){
		assertTrue(ProductUID.getGapUid("http://www.gap.com/products/cotton-cashmere-crewneck-P247857.jsp").equals("247857"));
		assertTrue(ProductUID.getGapUid("http://www.gap.com/products/foldover-pencil-skirt-P453698.jsp?scid=453698002&vid=1").equals("453698"));
	}
	
	@Test
	public void testHomedepotUid(){
		assertTrue(ProductUID.getHomedepotUID("http://www.homedepot.com/Appliances-Refrigeration-Refrigerators-French-Door-Refrigerators/h_d1/N-5yc1vZc3oo/R-202847444/h_d2/ProductDisplay?catalogId=10053&langId=-1&storeId=10051").equals("202847444"));
		assertTrue(ProductUID.getHomedepotUID("http://www.homedepot.com/p/LG-Electronics-30-5-cu-ft-French-Door-Refrigerator-in-Stainless-Steel-Door-In-Door-Design-LFX31945ST/203295339").equals("203295339"));
	}
	
	@Test
	public void testJcpUid(){
		assertTrue(ProductUID.getJCPUID("http://www.jcpenney.com/dotcom//for-the-home/brands/royal-velvet/categories/bedding/royal-velvet-versailles-7-pc-comforter-set-accessories/prod.jump?ppId=ens6002370003&catId=cat100250063&deptId=dept20000012&dimCombo=null&dimComboVal=null").equals("ens6002370003"));
	}
	
	@Test
	public void testKohlsUid(){
		assertTrue(ProductUID.getKohlsUID("http://www.kohls.com/product/prd-912267/nike-attack-razor-training-tee.jsp").equals("912267"));
		assertTrue(ProductUID.getKohlsUID("http://www.kohls.com/kohlsStore/costumes/womensplus/PRD~552716/Pink+Ladies+Costume.jsp").equals("552716"));
	}
	
	@Test
	public void testLowesUid(){
		assertTrue(ProductUID.getLowesUID("http://www.lowes.com/pd_391479-80668-86579_4294701412__?productId=3827415&Ns=p_product_qty_sales_dollar|1&pl=1&currentURL=%3Fpage%3D5%26Ns%3Dp_product_qty_sales_dollar%7C1&facetInfo=").equals("3827415"));
	}
	
	@Test
	public void testMacysUid(){
		assertTrue(ProductUID.getMacysUID("http://www1.macys.com/shop/product/bianca-bathrobes-kids-bubble-size-6-8-bathrobe?ID=757349").equals("757349"));
	}
	
	@Test
	public void testOldNavyUid(){
		assertTrue(ProductUID.getOldNavyUid("http://www.oldnavy.com/products/womens-color-blocked-jersey-tanks-P387076.jsp").equals("387076"));
	}
	
	@Test
	public void testRiteaidUid(){
		assertTrue(ProductUID.getRiteaidUID("http://www.riteaidonlinestore.com/Huggies-Pull-Ups-Learning-Training-Disney-Pixar/dp/B002CA8I3M?field_availability=-1&field_browse=3070378011&field_product_site_launch_date_utc=-1y&id=Huggies+Pull-Ups+Learning+Training+Disney-Pixar&ie=UTF8&refinementHistory=brandtextbin%2Csubjectbin%2Ccolor_map%2Cprice%2Csize_name&searchNodeID=3070378011&searchPage=1&searchRank=salesrank&searchSize=12").equals("B002CA8I3M"));
	}
	
	@Test
	public void testSamsclubUid(){
		assertTrue(ProductUID.getSamsclubUID("http://www.samsclub.com/sams/pampers-baby-dry-size-5-27-lbs-172-ct/prod2900050.ip").equals("prod2900050.ip"));
		assertTrue(ProductUID.getSamsclubUID("http://www.samsclub.com/sams/prod-pampers-baby-dry-size-5-27-lbs-172-ct/prod2900050.ip").equals("prod2900050.ip"));
		assertTrue(ProductUID.getSamsclubUID("http://www.samsclub.com/sams/serta-concierge-suite-pillow-top-mattress-set-twin-xl-2-pk/119250.ip?navAction=push").equals("119250.ip"));
	}
	
	@Test
	public void testStaplesUid(){
		assertTrue(ProductUID.getStaplesUID("http://www.staples.com/Scanners-PCs-Drives-Accessories/cat_CL158948/Perfection-B11B178061-4800-6400-dpi-USB-Flatbed-Color-Image-Scanner/product_IM1J27772").equals("IM1J27772"));
		assertTrue(ProductUID.getStaplesUID("http://www.staples.com/Small-Office-Home-Office/cat_CL158904/Bush-Somerset-71-inch-L-Desk-Hansen-Cherry-Finish/product-nr_BI28421").equals("BI28421"));
		assertTrue(ProductUID.getStaplesUID("http://www.staples.com/Expanding-File-Pockets/cat_CL140619/Smead-Secure-Pockets/StaplesProductDisplay?catalogId=10051&langId=-1&storeId=10001&partNumber=SS1049696").equals("SS1049696"));
	}
	
	@Test 
	public void testTargetUid(){
		assertTrue(ProductUID.getTargetUID("http://m.target.com/p/carson-space-saver-leaning-bookcase-cherry/-/A-14029258").equals("14029258"));
		assertTrue(ProductUID.getTargetUID("http://sites.target.com/site/en/spot/mobile_product_detail.jsp?tcin=14018035").equals("14018035"));
	}
	
	@Test
	public void testWalgreensUid(){
		assertTrue(ProductUID.getWalgreensUID("http://www.walgreens.com/store/c/colgate-cavity-protection-fluoride-toothpaste/ID=prod1635-product").equals("prod1635-product"));
	}
	
	@Test
	public void testWalmartUid(){
		assertTrue(ProductUID.getWalmartUID("http://www.walmart.com/ip/V7-Vantage-Webcam-300-with-Built-In-Microphone/16419432").equals("16419432"));
	}
}