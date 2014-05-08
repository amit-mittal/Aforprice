package work.workers;

import global.errorhandling.ErrorCodes;
import helper.Validate;
import helper.concurrent.ThreadFactories;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import work.items.Task;
import work.managers.WorkManager;
import work.workers.specs.ScheduledWorkerSpecs;

public class ScheduledWorker implements Runnable{
	
	private static final Logger logger = Logger.getLogger(ScheduledWorker.class);
	
	private ScheduledWorkerSpecs specs;
	private ExecutorService executors;
	private ScheduledExecutorService scheduler;
	private WorkManager workManager;
	
	public ScheduledWorker(ScheduledWorkerSpecs specs, WorkManager workManager){
		Validate.notNull(specs, ErrorCodes.SCHED_WORK_SPEC_IS_NULL);
		Validate.notNull(workManager, ErrorCodes.WORK_MANAGER_IS_NULL);
		this.specs = specs;
		this.workManager = workManager;
		executors = Executors.newFixedThreadPool(specs.threadPoolSize(), ThreadFactories.newThreadFactory());
		scheduler = Executors.newSingleThreadScheduledExecutor(ThreadFactories.newThreadFactory());		
	}
	
	public void scheduleNow(){
		scheduler.schedule(this, 0, TimeUnit.SECONDS);
		synchronized(this){
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void schedule(){		
	}
	
	@Override
	public void run(){		
		List<Task> tasks = null;
		Date now = new Date();
		if(!specs.onlyOnce()){
			long nextRun = now.getTime() + 24*60*60*1000 + (specs.startHrs() * 60*60 + specs.startMin() * 60 + specs.startSec())* 1000 - System.currentTimeMillis();
			scheduler.schedule(this, nextRun, TimeUnit.MILLISECONDS);
		}
		logger.info("Starting the processing of tasks now...");
		while((tasks = workManager.getNewTasks(specs.threadPoolSize())) != null && tasks.size() > 0)
		{
			for(Task task: tasks){
				executors.execute(task);
			}
		}
		//TODO: Fix the tasks end notification for the recurring process
		if(specs.onlyOnce()){
			executors.shutdown();
			scheduler.shutdown();
			try {
				executors.awaitTermination(12, TimeUnit.HOURS);				
			} catch (InterruptedException e) {
			}
		}
		synchronized(this){
			notifyAll();
		}
		logger.info("Done with the processing of all tasks.");
	}
}