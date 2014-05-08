package uploader.util;

import java.util.Map;

import junit.framework.Assert;
import util.UtilityLogger;

public class MyAssert extends Assert {
	private int failureCount = 0;
	private int successCount = 0; 
	private double epsilon = 0.000000001;
	/*
	 * assert that difference between expected and actual is less than z
	 */
	public String assertProximity(int expected, String expectedVarName, int actual, String actualVarName, int proximity)
	{
		String result;
		if(Math.abs(expected-actual)>proximity){
			failureCount++;
			result="ASSERT FAILED " + String.format("Difference between %s(%d) and %s(%d) is more than proximity(%d)", expectedVarName, expected, actualVarName, actual, proximity);
			UtilityLogger.logError(result);
		}
		else{
			successCount++;
			result="ASSERT SUCCESS " + String.format("Abs(%s(%d) - %s(%d))<= proximity(%d)", expectedVarName, expected, actualVarName, actual, proximity);
			UtilityLogger.logInfo(result);
		}
		return result;
	}

	/*
	 * assert that expected value is part of actual map
	 */
	public boolean assertContains(String expected, String expectedVarName, Map actual, String actualVarName)
	{
		if(actual.containsKey(expected)){
			successCount++;
			UtilityLogger.logDebug("ASSERT SUCCESS " + String.format("%s(%s) is part of %s", expectedVarName, expected, actualVarName));
			return true;
		}
		else{
			failureCount++;
			UtilityLogger.logError("ASSERT FAILED " + String.format("%s(%s) is NOT part of %s", expectedVarName, expected, actualVarName));
			return false;
		}			
	}

	/*
	 * assert that expected value and actual value is same
	 */
	public boolean assertEquals(Object expected, String expectedVarName, Object actual, String actualVarName)
	{
		if(actual.equals(expected)){
			successCount++;
			UtilityLogger.logDebug("ASSERT SUCCESS " + String.format("%s(%s) == %s(%s)", expectedVarName, expected, actualVarName, actual));
			return true;
		}
		else{
			failureCount++;
			UtilityLogger.logError("ASSERT FAILED " + String.format("%s(%s) != %s(%s)", expectedVarName, expected, actualVarName, actual));
			return false;
		}			
	}

	/*
	 * assert that expected value and actual value is same
	 */
	public boolean assertEquals(String expected, String expectedVarName, String actual, String actualVarName)
	{
		if(expected.equalsIgnoreCase(actual)){
			successCount++;
			UtilityLogger.logDebug("ASSERT SUCCESS " + String.format("%s(%s) == %s(%s)", expectedVarName, expected, actualVarName, actual));
			return true;
		}
		else{
			failureCount++;
			UtilityLogger.logError("ASSERT FAILED " + String.format("%s(%s) != %s(%s)", expectedVarName, expected, actualVarName, actual));
			return false;
		}			
	}

	/*
	 * assert that expected value and actual value is same
	 */
	public boolean assertEquals(Double expected, String expectedVarName, Double actual, String actualVarName)
	{
		if(Math.abs(expected - actual)<epsilon){
			successCount++;
			UtilityLogger.logDebug("ASSERT SUCCESS " + String.format("%s(%f) == %s(%f)", expectedVarName, expected, actualVarName, actual));
			return true;
		}
		else{
			failureCount++;
			UtilityLogger.logError("ASSERT FAILED " + String.format("%s(%f) != %s(%f)", expectedVarName, expected, actualVarName, actual));
			return false;
		}			
	}

	public void printResultSummary(){
		if(isTestFailed())
			UtilityLogger.logError(getResultSummary());
		else
			UtilityLogger.logInfo(getResultSummary());
	}

	public String getResultSummary(){
		String resultSummary;
		if(failureCount==0)
			resultSummary = "ASSERT TEST SUCCEEDED " + String.format("SUCCESS:%d FAILURE:%d", successCount, failureCount);
		else
			resultSummary ="ASSERT TEST FAILED " + String.format("SUCCESS:%d FAILURE:%d", successCount, failureCount);
		return resultSummary;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	
	public boolean isTestFailed(){
		return this.failureCount>0;
	}
}
