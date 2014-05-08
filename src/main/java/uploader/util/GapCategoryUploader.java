package uploader.util;

import entities.Retailer;

public class GapCategoryUploader extends GapCommonCategoryUploader{
	
	public GapCategoryUploader() throws UploaderException{
		super(Retailer.GAP);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GapCategoryUploader uploader = null;
		try {
			uploader = new GapCategoryUploader();
		} catch (UploaderException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		uploader.walkAndStore();
		uploader.terminate();
	}
}