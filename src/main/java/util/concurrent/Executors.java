package util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Executors {
	public static ThreadPoolExecutor newThreadPoolExecutor(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue){
		return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue);
	}
	
	/**
	 * Creates a thread pool executor with {@link LinkedBlockingQueue}} as the work queue 
	 * and same core and max pool size and 0 keep alive time.
	 * @param poolSize number of threads
	 * @return ThreadPoolExecutor
	 */
	public static ThreadPoolExecutor newFixedThreadPoolExecutor(int poolSize){
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		return new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.MICROSECONDS, workQueue);
	}
	
	public static ScheduledExecutorService newScheduledDaemonThreadPool(int poolSize){
		return java.util.concurrent.Executors.newScheduledThreadPool(1, daemonThreadFactory());
	}
	
	private static ThreadFactory daemonThreadFactory(){
		return new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		};
	}
}
