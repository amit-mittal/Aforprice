package uploader.util;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.ConfigParms;

public class WalmartCategoryUploaderTest {

	@Before
	public void setUp() throws Exception {
		System.setProperty("ENVIRONMENT", "QA");
		ConfigParms.MODE = ConfigParms.RUNTIME_MODE.UNITTEST;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCategoryParsing() throws UploaderException {
		assertEquals(1,1);
	}
}
