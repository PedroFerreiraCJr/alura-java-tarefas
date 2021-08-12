package br.com.dotofcodex.tarefas_alura.server;

public class SimpleThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println(t.getName() + " - " + e.getMessage());
	}
}
