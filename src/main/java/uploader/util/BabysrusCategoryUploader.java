package uploader.util;

import entities.Retailer;

public class BabysrusCategoryUploader extends ToysrusCategoryUploader {

	public BabysrusCategoryUploader() throws UploaderException {
		super(Retailer.BABYSRUS);
	}

	@Override
	protected String getRootCatClass(){
		return "subCatBlockBRU";
	}

	
	public static void main(String[] args) throws UploaderException{
		BabysrusCategoryUploader up = new BabysrusCategoryUploader();
		up.walkAndStore();
		up.terminate();
		
	}
}
