/**
 * 
 */
package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import entities.DbCredentials;

/**
 * @author Ashish
 *
 */
public class AbstractTest {
	protected double epsilon = 0.00000001;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/*expected format YYYY-MM-DD HH:MM:SS.sss*/
	public void _assertTime(long expected, long actual){
		try{
			//sybase query doesn't return millis
			assertTrue(Math.abs(expected - actual) <=1000);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: expected=" + expected + ", actual=" + actual;
			log(error);
			fail(error);
		}
	}

	public void _assertEquals(String expected, String actual){
		try{
			assertEquals(expected, actual);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: expected=" + expected + ", actual=" + actual;
			log(error);
			throw new org.junit.ComparisonFailure(error, expected, actual);
		}
	}

	public void _assertEquals(Object expected, Object actual){
		try{
			assertEquals(expected, actual);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: expected=" + expected + ", actual=" + actual;
			log(error);
			fail(error);
		}
	}

	public void _assertEquals(Integer expected, int actual){
		try{
			assertEquals(expected, new Integer(actual));
		}catch(Throwable e){
			String error = "ASSERTION FAILED: expected=" + expected + ", actual=" + actual;
			log(error);
			fail(error);
		}
	}

	public void _assertEquals(DbCredentials expected, DbCredentials actual){
		try{
			assertEquals(expected, actual);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: expected=" + expected + ", actual=" + actual;
			log(error);
			fail(error);
		}
	}

	public void _assertEquals(char expected, char actual){
		_assertEquals(String.valueOf(expected), String.valueOf(actual));
	}

	public void _assertEquals(double expected, double actual){
		try{
			assertEquals(expected, actual, epsilon);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: expected=" + expected + ", actual=" + actual;
			log(error);
			fail(error);
		}
	}

	public void _assertEquals(double expected, double actual, double epsilon){
		try{
			assertEquals(expected, actual, epsilon);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: expected=" + expected + ", actual=" + actual;
			log(error);
			fail(error);
		}
	}
	
	public void _assertEquals(int expected, int actual){
		try{
			assertEquals(expected, actual);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: expected=" + expected + ", actual=" + actual;
			log(error);
			fail(error);
		}
	}
	public void _assertNotNull(Object notNullObject, String message){
		try{
			assertNotNull(notNullObject);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: " + message;
			log(error);
			fail(error);
		}
	}
	
	public void _assertTrue(boolean flag){
		_assertTrue(flag, "");
	}

	public void _assertContains(List<Integer> list, Integer obj){
		if(!list.contains(obj))
			fail("[" + obj+"] does not exist in ["+list+"]");
	}
	public void _assertTrue(boolean flag, String message){
		try{
			assertTrue(flag);
		}catch(Throwable e){
			String error = "ASSERTION FAILED: condition is not true, " + message;
			log(error);
			fail(error);
		}
	}

	private void log(String s){
		System.out.println(s);
	}

}
