package parsers.util;
import static org.junit.Assert.assertTrue;
import entities.Retailer;
import thrift.genereated.retailer.Product;
import org.junit.Test;

public class ProductA4URITest
{
	@Test
	public void getWalmartA4URITest()
	{
		Product product = new Product();
		product.imageUrl = "http://i.walmartimages.com/i/p/00/88/53/08/10/0088530810366_180X180.jpg";
		assertTrue(ProductA4URI.get(Retailer.WALMART.getId(), product ).equals("0088530810366"));
	}
}
