package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import util.concurrent.Executors;
import db.dao.DAOException;
import db.dao.PerformanceMetricDAO;

public class MetricCollector implements Runnable {
	private static final Logger logger = Logger.getLogger(MetricCollector.class);

	public static final LinkedBlockingQueue<RawMetric> RawMetricQueue = new LinkedBlockingQueue<>();
	public static MetricCollector INSTANCE = new MetricCollector();
	Map<String, Map<String, MetricSummary>> summarizedMetricsMap = new ConcurrentHashMap<>();

	private MetricCollector() {
		ScheduledExecutorService scheduler = Executors.newScheduledDaemonThreadPool(1);
		scheduler.scheduleWithFixedDelay(this, 180, 180, TimeUnit.SECONDS );
	}

	public void generateSaveMetricSummary(boolean shouldSave) {
		logger.info("Generate Save Metrics Called" );
		List<RawMetric> rawMetricsToProcess = new ArrayList<RawMetric>();
		RawMetricQueue.drainTo(rawMetricsToProcess);
		int saveSize = 0;
		for (RawMetric rawMetric : rawMetricsToProcess) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(rawMetric.getStartTime());
			SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHH");
			String period = df.format(calendar.getTime());

			Map<String, MetricSummary> metricSummaryByPeriod;
			if ((metricSummaryByPeriod = summarizedMetricsMap.get(rawMetric
					.getName())) == null)
				metricSummaryByPeriod = new HashMap<>();

			MetricSummary metricSummary;
			if ((metricSummary = metricSummaryByPeriod.get(period)) == null)
				metricSummary = new MetricSummary(rawMetric.getName(), period);

			metricSummary.addRawMetric(rawMetric);
			metricSummaryByPeriod.put(period, metricSummary);
			summarizedMetricsMap
					.put(rawMetric.getName(), metricSummaryByPeriod);
		}
		if( shouldSave )
		{
			logger.info("Time to Save metrics" );
			PerformanceMetricDAO performanceMetricDAO = PerformanceMetricDAO
					.getInstance();
			Set<String> metricNames = summarizedMetricsMap.keySet();
			for (String metricName : metricNames) {
				Set<String> periods = summarizedMetricsMap.get(metricName).keySet();
				for (String period : periods) {
					try {
						performanceMetricDAO
								.storeMetricSummary(summarizedMetricsMap.get(
										metricName).get(period));
						saveSize++;
					} catch (DAOException doe) {
						doe.printStackTrace();
					}
				}
	
			}
			logger.info("Saved total " + saveSize + " performance metric");
			summarizedMetricsMap.clear();
		}	
	}

	public void run() {
		generateSaveMetricSummary(false);
	}
	
	public static MetricShutdownRunner SHUTDOWN_RUNNER = MetricCollector.INSTANCE.new MetricShutdownRunner();
	class MetricShutdownRunner implements Runnable
	{
		public void run() {
			generateSaveMetricSummary(true);
		}
		
	}
}
