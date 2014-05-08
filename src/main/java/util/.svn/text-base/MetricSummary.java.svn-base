package util;

public class MetricSummary {
	
	private long totalProcessTime = 0;
	private long totalProcessCount= 0;
	private long successCount= 0;
	private long failureCount= 0;
	private String name;
	private String period;
	
	public MetricSummary(String name, String period) {
		super();
		this.name = name;
		this.period = period;
	}

	public void addRawMetric(RawMetric rawMetric)
	{
		totalProcessTime += rawMetric.processTime;
		totalProcessCount ++;
		
	}

	public long getTotalProcessTime() {
		return totalProcessTime;
	}

	public long getTotalProcessCount() {
		return totalProcessCount;
	}

	public long getSuccessCount() {
		return successCount;
	}

	public long getFailureCount() {
		return failureCount;
	}

	public String getName() {
		return name;
	}

	public String getPeriod() {
		return period;
	}
	
	public long getAverageProcessTime(){
		return this.totalProcessTime/this.totalProcessCount;
	}
	
}
