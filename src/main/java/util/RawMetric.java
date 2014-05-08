package util;

public class RawMetric {
	final String name;
	final long startTime;
	final long endTime;
	final long processTime;
	
 	public RawMetric(String name, long startTime, long endTime) {
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.processTime = endTime - startTime;
	}

	public String getName() {
		return name;
	}


	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getProcessTime() {
		return processTime;
	}
	
	
	
}
