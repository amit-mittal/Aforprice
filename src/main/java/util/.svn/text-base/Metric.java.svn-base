package util;

import java.util.concurrent.TimeUnit;

public class Metric {
	
	private long startTime = 0;
	private long endTime = 0;
	private long processTime = 0;
	private int successCount = 0;
	private int totalCount = 0;
	private int errs = 0;
	private final String name;
	private TimeUnit timeUnit = TimeUnit.NANOSECONDS;
	public Metric(String name){
		this.name = name;
	}
	public Metric(String name, TimeUnit timeUnit){
		this.name = name;
		this.timeUnit = timeUnit;
	}
	public void setStartTime(long startTime){		
		setStartTime(startTime, this.timeUnit);
	}
	public void start(){
		if(timeUnit.equals(TimeUnit.MILLISECONDS))
			startTime = System.currentTimeMillis();
		else //nanoseconds
			startTime = System.nanoTime();
	}

	public void setStartTime(long startTime, TimeUnit unit){		
		this.startTime = this.timeUnit.convert(startTime, unit);
	}
	public long startTime(){
		return startTime;
	}	
	public void setEndTime(long endTime){		
		setEndTime(endTime, this.timeUnit);
	}
	public void end(){
		end(1);
	}
	public void end(int unitOfWork){
		if(timeUnit.equals(TimeUnit.MILLISECONDS))
			endTime = System.currentTimeMillis();
		else //nanoseconds
			endTime = System.nanoTime();
		addProcessTime(unitOfWork, endTime-startTime, this.timeUnit);
	}
	public void setEndTime(long endTime, TimeUnit unit){
		this.endTime = this.timeUnit.convert(endTime, unit);
	}
	public long endTime(){
		return endTime;
	}
	
	public long totalTime(TimeUnit unit){
		long total = endTime - startTime;
		return unit.convert(total, this.timeUnit);
	}
	public void add(int count){
		this.totalCount += count;
	}
	public void addSuccess(int count, long time, TimeUnit unit){
		this.successCount += count;
		this.processTime += this.timeUnit.convert(time, unit);
	}
	public void addProcessTime(int unitOfWork, long time, TimeUnit unit){
		this.successCount += unitOfWork;
		this.processTime += this.timeUnit.convert(time, unit);
	}
	public void reset(){
		this.successCount=0;
		this.processTime=0;
	}
	public int successCount(){
		return successCount;
	}
	public long processTime(TimeUnit unit){
		return unit.convert(processTime, this.timeUnit);
	}
	
	public void addErrs(int num){
		errs += num;
	}
	
	public int errCount(){
		return errs;
	}
	
	public double average(){
		return average(this.timeUnit);
	}
	public double average(TimeUnit unit){
		if(successCount != 0)
			return unit.convert(processTime, this.timeUnit)/successCount;
		return -1;
	}
	
	public double stdDev(){
		return 0;
	}
		
	@Override
	public String toString(){
		return "";
	}
	
	public String toStringHTML(){
		StringBuilder html = new StringBuilder();
		long startTimeMs = TimeUnit.MILLISECONDS.convert(startTime, this.timeUnit);
		long endTimeMs = TimeUnit.MILLISECONDS.convert(endTime, this.timeUnit);
		String[] values = {name, DateTimeUtils.getTime(startTimeMs), DateTimeUtils.getTime(endTimeMs), 
				String.valueOf(processTime(TimeUnit.MILLISECONDS)), String.valueOf(totalCount), String.valueOf(successCount), String.valueOf(errs)};
		html.append("<tr>");
		for(String value: values){
			html.append("<td>").append(value).append("</td>");								
		}				
		html.append("</tr>");
		return html.toString();
	}
	
	public String currentStats(){
		return currentStats(TimeUnit.MILLISECONDS);
	}

	public String currentStats(TimeUnit useTimeUnit){
		return ("PERF: " + this.name + " count:"+ this.successCount()+ 
				", total: " + this.processTime(useTimeUnit) + " " + useTimeUnit + 
				", avg: " +  this.average(useTimeUnit)+ " " + useTimeUnit);
	}

	/*
	 * Returns processing time in 'mins, secs' format
	 */
	public String getProcessingTimeMinsSecs(){
		return this.processTime(TimeUnit.MINUTES) + " mins, " + this.processTime(TimeUnit.SECONDS)%60 + " secs";
	}
}