package br.iesb.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import br.iesb.server.ClientThread;

/**
 * Class MultiThreadChatServerSync.
 */
public class MultiThreadChatServerSync {

    /** Atributo server socket. */
    private static ServerSocket serverSocket = null;

    /** Atributo client socket. */
    private static Socket clientSocket = null;

    /** Este servidor só pode comportar 10 clientes ao mesmo tempo. */
    private static final int MAX_CLIENTS_COUNT = 10;
    
    /** Constante THREADS: Vetor de clientes. */
    private static final ClientThread[] THREADS = new ClientThread[MAX_CLIENTS_COUNT];

    /**
     * O método main.
     *
     * @param args os argumentos
     */
    public static void main(String[] args) {

	// Porta padrão.
	int portNumber = 2222;
	if (args.length < 1) {
	    System.out.println("Uso: java server MultiThreadChatServerSync <portNumber>\n" + "Usando número de porta " + portNumber);
	} else {
	    portNumber = Integer.valueOf(args[0]).intValue();
	}

	/*
	 * Abre um socket servidor com a porta (padrão 2222).
	 * Note que nós não podemos escolher um número menor que 1023 para a porta,
	 * pois requer privilégios de admin.
	 */
	try {
	    serverSocket = new ServerSocket(portNumber);
	} catch (IOException e) {
	    System.out.println(e);
	}

	/*
	 * Cria um novo socket cliente para cada conexão e
	 * passa para uma nova thread cliente.
	 */
	while (true) {
	    try {
		int option = JOptionPane.showConfirmDialog(null, "Aguardando requisição do cliente...", "ChatNetworking", JOptionPane.OK_CANCEL_OPTION);
		clientSocket = serverSocket.accept();
		if (option == JOptionPane.CANCEL_OPTION) {
		    JOptionPane.showMessageDialog(null, "Servidor parado, reinicie-o!");
		    return;
		}
		int i = 0;
		for (i = 0; i < MAX_CLIENTS_COUNT; i++) {
		    if (THREADS[i] == null) {
			(THREADS[i] = new ClientThread(clientSocket, THREADS)).start();
			break;
		    }
		}
		if (i == MAX_CLIENTS_COUNT) {
		    PrintStream os = new PrintStream(clientSocket.getOutputStream());
		    os.println("Servidor lotado. Tente mais tarde.");
		    os.close();
		    clientSocket.close();
		}
	    } catch (IOException e) {
		System.out.println(e);
	    }
	}

    }

}
