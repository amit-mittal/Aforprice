package work.items;

import global.errorhandling.ErrorCodes;
import helper.Validate;

import org.apache.log4j.Logger;

public abstract class Task implements Runnable{
	
	private static Logger logger = Logger.getLogger(Task.class);
	
	private long startTime = -1;;
	private long endTime = -1;
	private TaskNotifyee notifyee;
	private int id;
	private final int attempt;
	public Task(TaskNotifyee notifyee, int id, int attempt){
		Validate.notNull(notifyee, ErrorCodes.WORK_MANAGER_IS_NULL);
		this.attempt = attempt;
		this.id = id;
		this.notifyee = notifyee;
	}	
	
	@Override
	public void run(){
		startTime = System.currentTimeMillis();
		if(notifyee != null)
			notifyee.onStart(this);
		try{
			doWork();
			endTime = System.currentTimeMillis();
			if(notifyee != null)
				notifyee.onFinish(this);
		}catch(Exception e){
			logger.error(e.getMessage(), e);			
			if(notifyee != null)
				notifyee.onError(this, e);
		}
	}
	
	public long startTime(){
		return startTime;
	}
	public long endTime(){
		return endTime;
	}	
	public boolean isCompleted(){
		return endTime() <= System.currentTimeMillis();
	}	
	public TaskNotifyee getNotifyee(){
		return notifyee;
	}
	public int getId(){
		return id;
	}
	public int attempt(){
		return attempt;
	}
	public abstract void doWork();
	
}