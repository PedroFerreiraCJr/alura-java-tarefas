package br.com.dotofcodex.tarefas_alura.server;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dotofcodex.tarefas_alura.server.command.CommandAcessaBanco;
import br.com.dotofcodex.tarefas_alura.server.command.CommandC1;
import br.com.dotofcodex.tarefas_alura.server.command.CommandChamaWS;
import br.com.dotofcodex.tarefas_alura.server.command.JuntaResultaWSDB;

public class DistribuirTarefa implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(DistribuirTarefa.class);

	private final ExecutorService executor;
	private final Socket socket;
	private final ServidorTarefa servidor;
	private final BlockingQueue<String> filaComandos;

	public DistribuirTarefa(ExecutorService executor, BlockingQueue<String> filaComandos, Socket socket, ServidorTarefa servidor) {
		this.executor = executor;
		this.socket = socket;
		this.servidor = servidor;
		this.filaComandos = filaComandos;
	}

	@Override
	public void run() {
		logger.info("--- Atendendo socket do cliente na porta: " + socket.getPort() + " ---");
		try {
			final PrintStream stream = new PrintStream(socket.getOutputStream());
			Scanner scan = new Scanner(socket.getInputStream());
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				switch (line.trim().toLowerCase()) {
				case "c1": {
					executor.execute(new CommandC1(stream));
					break;
				}
				case "c2": {
					Future<String> futureWS = executor.submit(new CommandChamaWS(stream));
					Future<String> futureDB = executor.submit(new CommandAcessaBanco(stream));
					executor.submit(new JuntaResultaWSDB(futureWS, futureDB, stream));
					break;
				}
				case "c3": {
					filaComandos.put(line);
					stream.println("Comando C3 adicionado a fila de processamento");
					break;
				}
				case "bye": {
					servidor.parar();
					break;
				}
				default: {
					logger.info("--- Comando recebido: " + line + " ---");
				}
				}
				stream.println(String.format("Comando recebido pelo Servidor: %s.", line));
				stream.flush();
			}
			scan.close();
			socket.close();
		} catch (final Exception e) {
			logger.error("--- Falha no atendimento socket do cliente ---", e);
			throw new RuntimeException(e);
		}
		logger.info("--- Terminando socket do cliente na porta: " + socket.getPort() + "---");
	}

}
