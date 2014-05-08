package uploader.util;

import entities.Retailer;

public class OldNavyCategoryUploader extends GapCommonCategoryUploader{
	
	public OldNavyCategoryUploader() throws UploaderException{
		super(Retailer.OLDNAVY);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OldNavyCategoryUploader uploader = null;
		try {
			uploader = new OldNavyCategoryUploader();
		} catch (UploaderException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}