package br.com.dotofcodex.tarefas_alura.server.command;

import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JuntaResultaWSDB implements Callable<Void> {

	private final Future<String> futureWS;
	private final Future<String> futureDB;
	private final PrintStream saida;

	public JuntaResultaWSDB(Future<String> futureWS, Future<String> futureDB, PrintStream stream) {
		super();
		this.futureWS = futureWS;
		this.futureDB = futureWS;
		this.saida = stream;
	}

	@Override
	public Void call() {
		System.out.println("Aguardando resultado WS e DB");

		try {
			String resultadoWS = futureWS.get(15, TimeUnit.SECONDS);
			String resultadoDB = futureDB.get(15, TimeUnit.SECONDS);

			saida.println("Resultado comando C2: ");
			saida.println("WS: " + resultadoWS);
			saida.println("DB: " + resultadoDB);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			saida.println("Timeout na execução do comando C2");
			futureWS.cancel(true);
			futureDB.cancel(true);
		}
		return null;
	}
}
