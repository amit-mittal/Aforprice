package work.managers;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import util.Metric;
import util.Metrics;
import work.items.ProductDetailsParseTask;
import work.items.Task;
import work.items.TaskNotifyee;

public class ProductDetailsParseTaskNotifyee implements TaskNotifyee{

	private static final Logger LOGGER = Logger.getLogger(ProductDetailsParseTaskNotifyee.class);
	
	private static final ProductDetailsParseTaskNotifyee instance = new ProductDetailsParseTaskNotifyee();

	private ProductDetailsParseTaskNotifyee() {		
	}
	
	public static final ProductDetailsParseTaskNotifyee get(){
		return instance;
	}
	
	@Override
	public void onStart(Task task) {	
		ProductDetailsParseTask t = (ProductDetailsParseTask)task;
		LOGGER.info("Start download of " + t.getURL());
	}

	@Override
	public void onFinish(Task task) {
		ProductDetailsParseTask t = (ProductDetailsParseTask)task;				
		Metric m = Metrics.getMetric(Metrics.getProductDetailsParserMetricKey(t.getRetailer()));
		m.addSuccess(1, t.endTime() - t.startTime(), TimeUnit.MILLISECONDS);
		m.setEndTime(System.currentTimeMillis());
		LOGGER.info("Download took " + (t.endTime() - t.startTime()) + "ms.");
	}

	@Override
	public void onError(Task task, Throwable err) {
		ProductDetailsParseTask t = (ProductDetailsParseTask)task;
		Metric m = Metrics.getMetric(Metrics.getProductDetailsParserMetricKey(t.getRetailer()));
		m.addErrs(1);
		m.setEndTime(System.currentTimeMillis());
		LOGGER.error("Download error ", err );
	}
}