package br.com.dotofcodex.tarefas_alura.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServidorTarefa {
	private static final Logger logger = LoggerFactory.getLogger(ServidorTarefa.class);

	private ExecutorService threadPool;
	private ServerSocket serverSocket;
	private AtomicBoolean running;
	private BlockingQueue<String> filaComandos;

	public ServidorTarefa() throws IOException {
		super();
		logger.info("--- inicializando servidor ---");
		this.threadPool = Executors.newCachedThreadPool(new SimpleThreadFactory());
		this.serverSocket = new ServerSocket(12345);
		this.filaComandos = new ArrayBlockingQueue<String>(2);
		iniciarConsumidores();
	}

	private void iniciarConsumidores() {
		int quantidadeConsumidores = 2;
		for (int i = 0; i < quantidadeConsumidores; i++) {
			TarefaConsumir t = new TarefaConsumir(filaComandos);
			threadPool.execute(t);
		}
	}

	public void parar() throws IOException {
		running.set(false);
		threadPool.shutdown();
		serverSocket.close();
		logger.info("Parando o servidor");
	}

	public void rodar() throws IOException {
		running.set(true);
		while (running.get()) {
			try {
				Socket client = serverSocket.accept();
				logger.info("--- aceitando novo cliente na porta: " + client.getPort() + " ---");
				threadPool.execute(new DistribuirTarefa(threadPool, filaComandos, client, this));
			} catch (SocketException e) {
				logger.error("Falha na execução do servidor", e);
			}
		}
	}
}
