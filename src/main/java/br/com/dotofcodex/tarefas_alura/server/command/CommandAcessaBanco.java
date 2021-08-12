package br.com.dotofcodex.tarefas_alura.server.command;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

public class CommandAcessaBanco implements Callable<String> {

	private PrintStream saida;

	public CommandAcessaBanco(PrintStream saida) {
		super();
		this.saida = saida;
	}

	@Override
	public String call() throws Exception {
		System.out.println("Executando comando C1 - Banco");
		saida.println("Processando comando C2 - Banco");
		Thread.sleep(10_000);
		int numero = new Random().nextInt(100) + 1;
		System.out.println("Finalizou o comando C2 - Banco");
		return Integer.toString(numero);
	}
}
