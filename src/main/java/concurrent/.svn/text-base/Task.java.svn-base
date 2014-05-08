package concurrent;

import util.FastLogger;

public abstract class Task implements Runnable{

    private static FastLogger logger = FastLogger.create(Task.class);

    private long startTime = -1;;
    private long endTime = -1;
    private TaskNotifyee notifyee;

    public Task(TaskNotifyee notifyee){
        this.notifyee = notifyee;
    }

    public Task(){
        this(null);
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

    public abstract void doWork();
}