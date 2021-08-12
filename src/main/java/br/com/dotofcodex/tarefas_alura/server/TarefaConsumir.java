package br.com.dotofcodex.tarefas_alura.server;

import java.util.concurrent.BlockingQueue;

public class TarefaConsumir implements Runnable {

	private final BlockingQueue<String> filaComandos;

	public TarefaConsumir(BlockingQueue<String> filaComandos) {
		super();
		this.filaComandos = filaComandos;
	}

	@Override
	public void run() {
		try {
			String comando = null;
			while ((comando = filaComandos.take()) != null) {
				System.out.println(Thread.currentThread().getName() + ": Consumindo comando - " + comando);
				Thread.sleep(10_000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
