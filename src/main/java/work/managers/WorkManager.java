package work.managers;

import java.util.List;

import work.items.Task;
import work.items.TaskNotifyee;

public interface WorkManager extends TaskNotifyee{
	
	/**
	 * Return a minimum max or the remaining tasks. This method blocks if the previous
	 * tasks have not been completed and returns an empty list if no tasks remaining.
	 * @param max Maximum tasks which the caller can process at a time.
	 * @return List of unprocessed tasks up to what the caller can process
	 */
	List<Task> getNewTasks(int max);
	/**
	 * WorkManager name
	 * @return
	 */
	String getName();
}
