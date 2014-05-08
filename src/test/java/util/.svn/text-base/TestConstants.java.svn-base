/**
 * 
 */
package util;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Ashish
 *
 */
public class TestConstants extends AbstractTest{
	
	@Before
    public void setUp() {
        System.setProperty("ENVIRONMENT", "QA");
    }

	/**
	 * Test method for {@link util.Constants.CATEGORY_HTML_FILES#path(java.lang.String, java.util.Date)}.
	 */
	@Test
	public void testPath() {
		String root;
		if(Constants.isWindows()){
			root = "C:\\Users\\Anurag\\TheMarketDataProject\\crawler\\category_html_files\\"+ConfigParms.getInstance().getEnvironment();
			_assertEquals(Constants.CATEGORY_HTML_FILES.path("walmart", DateTimeUtils.dateFromyyyyMMdd("20120730")),
									root+"\\20120730\\walmart\\");
			_assertEquals(Constants.CATEGORY_HTML_FILES.path("toysrus", DateTimeUtils.dateFromyyyyMMdd("20120731")),
					root+"\\20120731\\toysrus\\");
		}
		else{
			root = System.getenv("HOME") + "/crawler/category_html_files/"+ConfigParms.getInstance().getEnvironment();
			_assertEquals(Constants.CATEGORY_HTML_FILES.path("walmart", DateTimeUtils.dateFromyyyyMMdd("20120730")),
					root+"/20120730/walmart/");
			_assertEquals(Constants.CATEGORY_HTML_FILES.path("toysrus", DateTimeUtils.dateFromyyyyMMdd("20120731")),
					root+"/20120731/toysrus/");
		}
		
	}

}
