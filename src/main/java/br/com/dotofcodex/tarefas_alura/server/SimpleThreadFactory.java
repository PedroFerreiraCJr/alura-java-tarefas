package br.com.dotofcodex.tarefas_alura.server;

import java.util.concurrent.ThreadFactory;

public class SimpleThreadFactory implements ThreadFactory {

	private static int counter;
	private final SimpleThreadExceptionHandler handler;

	public SimpleThreadFactory() {
		super();
		handler = new SimpleThreadExceptionHandler();
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "Thread-" + (counter++));
		t.setUncaughtExceptionHandler(handler);
		t.setDaemon(true);
		return t;
	}
}
