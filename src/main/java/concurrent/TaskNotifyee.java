package concurrent;

/**
 * Created by Anurag on 5/4/14.
 */
public interface TaskNotifyee<T extends Task> {
    void onStart(T task);
    void onFinish(T task);
    void onError(T task, Throwable t);
}