package br.iesb.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Class MultiThreadChatServerSync.
 */
public class MultiThreadChatServerSync {

    /** Atributo server socket. */
    private static ServerSocket serverSocket = null;

    /** Atributo client socket. */
    private static Socket clientSocket = null;

    /** Este servidor soh pode comportar 10 clientes ao mesmo tempo. */
    private static final int MAX_CLIENTS_COUNT = 10;
    
    /** Constante THREADS: Vetor de clientes. */
    private static final ClientThread[] THREADS = new ClientThread[MAX_CLIENTS_COUNT];

    /**
     * O metodo main.
     *
     * @param args os argumentos
     */
    public static void main(String[] args) {

	// Porta padrao.
	int portNumber = 2222;
	int i;
	if (args.length < 1) {
	    System.out.println("Uso: java server MultiThreadChatServerSync <portNumber>\n" + "Usando numero de porta " + portNumber);
	} else {
	    portNumber = Integer.valueOf(args[0]).intValue();
	}

	/*
	 * Abre um socket servidor com a porta (padrao 2222).
	 * Note que nos nao podemos escolher um numero menor que 1023 para a porta,
	 * pois requer privilegios de admin.
	 */
	try {
	    serverSocket = new ServerSocket(portNumber);
	} catch (IOException e) {
	    System.out.println(e);
	}

	/*
	 * Cria um novo socket cliente para cada conexao e
	 * passa para uma nova thread cliente.
	 */
	while (true) {
	    try {
		int option = JOptionPane.showConfirmDialog(null, "Aguardando requisicao do cliente...", "ChatNetworking", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.CANCEL_OPTION) {
		    JOptionPane.showMessageDialog(null, "Servidor parado, reinicie-o!");
		    return;
		}
		clientSocket = serverSocket.accept();
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
