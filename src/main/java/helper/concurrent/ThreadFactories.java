package helper.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadFactories {
	public static ThreadFactory newThreadFactory(){
		return Executors.defaultThreadFactory();
	}
}
