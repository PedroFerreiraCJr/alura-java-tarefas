package br.com.dotofcodex.tarefas_alura.client;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClienteTarefa {
	private static final Logger logger = LoggerFactory.getLogger(ClienteTarefa.class);

	public static void main(String[] args) throws Exception {
		try (final Socket socket = new Socket("localhost", 12345)) {
			logger.info("conexão estabelecida");
			final Thread rcv = new Thread(() -> {
				try (final Scanner scan = new Scanner(socket.getInputStream())) {
					while (scan.hasNextLine()) {
						String line = scan.nextLine();

						// resposta recebida do servidor
						System.out.println(line);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});

			final Thread snd = new Thread(() -> {
				try (final PrintStream stream = new PrintStream(socket.getOutputStream())) {
					try (final Scanner scan = new Scanner(System.in)) {
						System.out.println("Type a command: ");
						while (scan.hasNextLine()) {
							System.out.println(">>>");
							String line = scan.nextLine();

							if (line.trim().isEmpty()) {
								break;
							}

							// envia o comando para o servidor
							stream.println(line);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});

			rcv.start();
			snd.start();

			rcv.join();
			snd.join();
		}
	}
}
