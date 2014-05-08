package entities;

import global.exceptions.Bhagte2BandBajGaya;

import java.util.List;

import util.Constants;

public class ProductCountRecon {

	private int actual;
	private int expected;
	private int catMismatch;
	public ProductCountRecon(List<DailyRecon> recons) throws Bhagte2BandBajGaya{
		for(DailyRecon recon: recons){
			actual += recon.getActualProductCount();
			expected += recon.getexpectedProductCount();
			//If product count more than certain threshold, then do not report category mismatch.
			catMismatch += (recon.getActualProductCount() >= Constants.RECON.maxReportCatMismatch() || recon.getActualProductCount() == recon.getexpectedProductCount())?0:1;
		}
	}
	
	public int getActual(){
		return actual;
	}
	
	public int getExpected(){
		return expected;
	}
	
	public int getCatMismatch(){
		return catMismatch;
	}
	
	public int getCountMismatch(){
		return getExpected() - getActual();
	}
}
