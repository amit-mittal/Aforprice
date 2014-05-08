package work.managers;

import global.exceptions.Bhagte2BandBajGaya;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import work.items.Task;
import db.DbConnection;
import db.DbConnectionPool;
import db.Queries;

public abstract class WorkManagerLockedTasks implements WorkManager {

	private final static Logger logger = Logger.getLogger(WorkManagerLockedTasks.class);
	private final Connection connection;
	private final String name;
	private final PreparedStatement lock;	
	
	private final PreparedStatement updateStartTime;
	private final PreparedStatement updateEndTime;
	private final PreparedStatement updateErrTime;
	
	private final Map<Integer, Task> pendingTasks = new HashMap<Integer, Task>();	

	public WorkManagerLockedTasks(Date runDate, String taskName){
		this.name = taskName.substring(0, Math.min(50, taskName.length()));
		DbConnection pooledConn = DbConnectionPool.get().getConnection();
		connection = pooledConn.getConnection();
		try {							
	
			lock = connection.prepareStatement(Queries.INSERT_TASKS_TO_LOCK);						
			lock.setString(1, name);	
			
			updateStartTime = connection.prepareStatement(Queries.UPDATE_START_TIME);
			updateStartTime.setString(2, name);				
			
			updateEndTime = connection.prepareStatement(Queries.UPDATE_END_TIME);
			updateEndTime.setString(2, name);
				
			updateErrTime = connection.prepareStatement(Queries.UPDATE_ERR_TIME);
			updateErrTime.setString(2, name);
			if(runDate != null)
				setDate(runDate);
	
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new Bhagte2BandBajGaya(e);
		}		
		finally{
			DbConnectionPool.get().releaseConnection(pooledConn);
		}
	}
	
	@Override
	public String getName(){
		return name;
	}
		
	
	public void setDate(Date date){
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());		
		try {
			lock.setDate(2, sqlDate);
			updateStartTime.setDate(3, sqlDate);		
			updateEndTime.setDate(3, sqlDate);		
			updateErrTime.setDate(3, sqlDate);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new Bhagte2BandBajGaya(e);		
		}				
	}
	
	@Override
	public void onStart(Task task) {				
		try {			
			updateStartTime.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			updateStartTime.setInt(4, task.getId());
			updateStartTime.setInt(5, task.attempt());
			updateStartTime.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			// TODO Handle exception
		}		
	}

	@Override
	public void onFinish(Task task) {		
		synchronized(this){			
			removePending(task.getId());
			notifyAll();
		}
		try {
			updateEndTime.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			updateEndTime.setInt(4, task.getId());
			updateEndTime.setInt(5, task.attempt());
			updateEndTime.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			// TODO Auto-generated catch block
		}

	}

	@Override
	public void onError(Task task, Throwable t) {
		synchronized(this){			
			removePending(task.getId());							
			notifyAll();
		}
		try {
			updateErrTime.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			updateErrTime.setInt(4, task.getId());
			updateErrTime.setInt(5, task.attempt());
			updateErrTime.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			// TODO Auto-generated catch block			
		}
		//TODO
		/*
		 * 1. Log the error
		 * 2. Keep track of number of errors and if number of errors is more than the max allowed, abort.
		 */
	}
	
	
	public void waitOnPendingTasks(int max){
		int numUnfinished = getNumPending();
		if(numUnfinished == max){
			synchronized(this){
				numUnfinished = getNumPending();
				while(true)
				{					
					if(numUnfinished < max)
						break;
					try {
						logger.info("waiting on unfinished tasks: count = " + numUnfinished);
						wait();
					} catch (InterruptedException e) {						
					}											
					numUnfinished = getNumPending();					
				}								
			}
		}
	}
	
	protected boolean tryLock(int taskId, int attempt){
		try{
			lock.setInt(3, taskId);
			lock.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			lock.setInt(5, attempt);
			lock.executeUpdate();
		}catch(SQLException e){
			logger.warn("Unable to lock for taskId = " + taskId);
			return false;
		}
		return true;
	}
	
	protected void removePending(int id){
		pendingTasks.remove(id);
	}
	
	protected void addPending(Task task){
		pendingTasks.put(task.getId(), task);
	}
	
	protected int getNumPending(){
		return pendingTasks.size();
	}

}
