package uploader.util;

import entities.Retailer;

public class BananaRepublicCategoryUploader extends GapCommonCategoryUploader{
	
	public BananaRepublicCategoryUploader() throws UploaderException{
		super(Retailer.BANANAREPUBLIC);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BananaRepublicCategoryUploader uploader = null;
		try {
			uploader = new BananaRepublicCategoryUploader();
		} catch (UploaderException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}