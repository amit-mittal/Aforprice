package mongo;

import util.concurrent.Executors;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Created by Anurag on 3/5/14.
 */
public class MongoWriter {

    private ExecutorService executor;

    private MongoWriter(boolean bcp){
        executor = Executors.newFixedThreadPoolExecutor(1);
        executor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
