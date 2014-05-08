package concurrent;

import java.util.List;

/**
 * Created by Anurag on 5/4/14.
 */
public class TaskChain implements Runnable{

    private final List<Task> taskList;
    public TaskChain(List<Task> taskList){
        this.taskList = taskList;
    }

    @Override
    public void run() {
        for(Task task: taskList){
            task.run();
        }
    }
}
