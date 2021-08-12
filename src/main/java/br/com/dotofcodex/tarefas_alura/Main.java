package br.com.dotofcodex.tarefas_alura;

import br.com.dotofcodex.tarefas_alura.server.ServidorTarefa;

public class Main {
	public static void main(String[] args) throws Exception {
		ServidorTarefa servidor = new ServidorTarefa();
		servidor.rodar();
		servidor.parar();
	}
}
