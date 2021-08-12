package br.com.dotofcodex.tarefas_alura.server.command;

import java.io.PrintStream;

public class CommandC1 implements Runnable {

	private PrintStream saida;

	public CommandC1(PrintStream saida) {
		super();
		this.saida = saida;
	}

	@Override
	public void run() {
		System.out.println("Executando comando C2");
		try {
			Thread.sleep(10_000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		saida.println("Comando c2 executado com sucesso!");
	}
}
