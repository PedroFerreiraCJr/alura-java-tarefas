package br.com.dotofcodex.tarefas_alura.server;

import java.util.concurrent.ThreadFactory;

public class DefaultThreadFactory implements ThreadFactory {

	private ThreadFactory defaultFactory;

	public DefaultThreadFactory(ThreadFactory defaultFactory) {
		this.defaultFactory = defaultFactory;
	}

	@Override
	public Thread newThread(Runnable tarefa) {
		Thread thread = defaultFactory.newThread(tarefa);
		thread.setUncaughtExceptionHandler(new SimpleThreadExceptionHandler());
		return thread;
	}
}
