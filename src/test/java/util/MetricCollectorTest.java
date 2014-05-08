package util;

import static org.junit.Assert.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

public class MetricCollectorTest extends AbstractTest
{

	@Test
	public void testGenerateSaveMetric1() {
		MetricCollector INSTANCE = MetricCollector.INSTANCE;
		INSTANCE.summarizedMetricsMap.clear();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		long startTime =cal.getTimeInMillis();
		SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHH");
		String period = df.format(cal.getTime());
		MetricCollector.RawMetricQueue.add(new RawMetric("Test", startTime, startTime + 10000 ));				
		MetricCollector.RawMetricQueue.add(new RawMetric("Test", startTime, startTime + 20000 ));				
		MetricCollector.RawMetricQueue.add(new RawMetric("Test", startTime, startTime + 30000 ));
		INSTANCE.generateSaveMetricSummary(false);
		assertTrue(INSTANCE.summarizedMetricsMap.containsKey("Test"));
		assertTrue(INSTANCE.summarizedMetricsMap.get("Test").containsKey(period));
		//assertEquals(INSTANCE.summarizedMetricsMap.get("Test").get(period).getTotalProcessCount(), 3);
		assertEquals(INSTANCE.summarizedMetricsMap.get("Test").get(period).getTotalProcessTime(), 60000);
		INSTANCE.summarizedMetricsMap.clear();
	}

	@Test
	public void testGenerateSaveMetric2() {
		MetricCollector INSTANCE = MetricCollector.INSTANCE;
		INSTANCE.summarizedMetricsMap.clear();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		long startTime =cal.getTimeInMillis();
		SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHH");
		String period1 = df.format(cal.getTime());
		
		MetricCollector.RawMetricQueue.add(new RawMetric("Test", startTime, startTime + 10000 ));				
		MetricCollector.RawMetricQueue.add(new RawMetric("Test", startTime, startTime + 20000 ));				
		MetricCollector.RawMetricQueue.add(new RawMetric("Test", startTime, startTime + 30000 ));

		startTime = startTime + 3600000;
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(startTime);
		df = new SimpleDateFormat("YYYYMMddHH");
		String period2 = df.format(calendar2.getTime());
		MetricCollector.RawMetricQueue.add(new RawMetric("Test", startTime, startTime + 10000 ));				
		MetricCollector.RawMetricQueue.add(new RawMetric("Test", startTime, startTime + 20000 ));				
		
		INSTANCE.generateSaveMetricSummary(false);
		assertTrue(INSTANCE.summarizedMetricsMap.containsKey("Test"));
		assertTrue(INSTANCE.summarizedMetricsMap.get("Test").containsKey(period1));
		assertEquals(INSTANCE.summarizedMetricsMap.get("Test").get(period1).getTotalProcessCount(), 3);
		assertEquals(INSTANCE.summarizedMetricsMap.get("Test").get(period1).getTotalProcessTime(), 60000);
		assertTrue(INSTANCE.summarizedMetricsMap.containsKey("Test"));
		assertTrue(INSTANCE.summarizedMetricsMap.get("Test").containsKey(period2));
		assertEquals(INSTANCE.summarizedMetricsMap.get("Test").get(period2).getTotalProcessCount(), 2);
		assertEquals(INSTANCE.summarizedMetricsMap.get("Test").get(period2).getTotalProcessTime(), 30000);

	}

}
