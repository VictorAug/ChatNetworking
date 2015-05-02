package br.iesb.frame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	private static ServerSocket serverSocket;

	public static void main(String args[]) throws IOException {
		final int portNumber = 2222;
		System.out.println("Creating server socket on port " + portNumber);
		serverSocket = new ServerSocket(portNumber);
		while (true) {
			Socket socket = serverSocket.accept();
			socket.close();
		}
	}
}