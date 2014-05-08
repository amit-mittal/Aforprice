/**
 * 
 */
package util;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * @author Ashish
 *
 */
public class MetricsTest extends AbstractTest{

	@Test
	public void test() {
		Metric a = new Metric("ABC", TimeUnit.MILLISECONDS);
		long curTime = System.currentTimeMillis();
		a.setStartTime(curTime);
		a.setEndTime(curTime+5);
		_assertTrue(a.totalTime(TimeUnit.MILLISECONDS) == 5);
		_assertTrue(a.totalTime(TimeUnit.NANOSECONDS) == 5e6);
		a.addProcessTime(1, 5, TimeUnit.MILLISECONDS);
		_assertTrue(a.processTime(TimeUnit.MILLISECONDS) == 5);
		_assertTrue(a.processTime(TimeUnit.NANOSECONDS) == 5e6);
		a.addProcessTime(2, 25, TimeUnit.MILLISECONDS);
		_assertTrue(a.processTime(TimeUnit.MILLISECONDS) == 30);
		_assertTrue(a.average() == 10);
		_assertTrue(a.average(TimeUnit.MICROSECONDS) == 10e3);
		
		//test start() and end() method
		Metric b = new Metric("B");
		long start = System.nanoTime();
		b.start();
		b.end();
		_assertTrue(b.processTime(TimeUnit.NANOSECONDS)<=(System.nanoTime()-start));
		
		Metric c = new Metric("C");
		start = System.nanoTime();
		c.setStartTime(start);
		c.setEndTime(start+2000000);
		_assertEquals(2L, c.totalTime(TimeUnit.MILLISECONDS));
	}

}
