package util;

public class MyMemoryUsage {
	private long maxMemoryInMB;
	private long freeMemoryInMB;
	private long usedMemoryInMB;
	private long calcTime;
	private static MyMemoryUsage instance = new MyMemoryUsage();
	long start = System.currentTimeMillis();
	int MB = 1024*1024;
	
			
	public static MyMemoryUsage getInstance(){
		return instance;
	}
	
	private MyMemoryUsage(){
		update();
	}

	/**
	 * returns string representations of memory usage
	 */
	public String getMemoryInfo(boolean refreshMemory){
		if(refreshMemory)
			update();
		return toString();
	}
	
	public String toString(){
		return "MemoryUsage: Used/Free/Max "+ usedMemoryInMB + "MB/" + freeMemoryInMB + "MB/" + maxMemoryInMB + 
				"MB" + (calcTime>5 ? ", calc_time_ms=" + calcTime : "");
	}
	
	/**
	 * calculate latest memory usage
	 */
	public void update(){
		long start = System.currentTimeMillis();
		maxMemoryInMB = Runtime.getRuntime().maxMemory()/MB; 
		freeMemoryInMB = Runtime.getRuntime().freeMemory()/MB;
		usedMemoryInMB = Runtime.getRuntime().totalMemory()/MB - freeMemoryInMB;
		calcTime = System.currentTimeMillis() - start;
	}
	
	/**
	 * Returns memory percentage usage
	 */	
	public int usedMemoryInPercentage(){
		return (int) ((usedMemoryInMB*100)/maxMemoryInMB);
	}
}
