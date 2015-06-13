package br.iesb.cliente.app.service;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import br.iesb.app.bean.ChatMessage;

public class ClienteService implements Serializable {

    private static final String host = "localhost";

    private static final long serialVersionUID = 3527000015250907284L;

    /** Socket para estabelecer a conexão com o servidor. */
    private Socket socket;

    // private Socket fileSocket;

    /** Objeto com o conteúdo da mensagem a ser enviada ao servidor. */
    private ObjectOutputStream output;

    public Socket connect() {
	try {
	    this.socket = new Socket(host, 5555);
	    this.output = new ObjectOutputStream(socket.getOutputStream());
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return socket;
    }

    public void send(ChatMessage message) {
	try {
	    output.writeObject(message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Retorna o IP do cliente.
     *
     * @return client ip
     */
    public SocketAddress getClientIP() {
	return socket.getLocalSocketAddress();
    }

    /**
     * Retorna o IP do servidor.
     *
     * @return server ip
     */
    public SocketAddress getServerIP() {
	return socket.getRemoteSocketAddress();
    }

    @SuppressWarnings("resource")
    public static void uploadToServer(File file) {
	try {
	    FileInputStream fin = new FileInputStream(file);
	    Socket socket = new Socket(host, 12345);
	    OutputStream out = socket.getOutputStream();
	    OutputStreamWriter osw = new OutputStreamWriter(out);
	    BufferedWriter writer = new BufferedWriter(osw);
	    writer.write(file.getName() + "\n");
	    writer.flush();
	    DataOutputStream dos = new DataOutputStream(out);
	    dos.writeLong(file.length());
	    dos.flush();
	    int c;
	    while ((c = fin.read()) != -1) {
		System.out.println(c);
		out.write(c);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void uploadToServer2(File file) {
	try {
	    // Cria o socket temporário
	    ServerSocket server = new ServerSocket(12345);
	    // while (true) {
	    System.out.println("Esperando conexão");
	    Socket socket = server.accept();
	    System.out.println("Conexão aceita: " + socket);

	    // Envia o arquivo
	    byte[] buffer = new byte[(int) file.length()];
	    FileInputStream fis = new FileInputStream(file);
	    BufferedInputStream bis = new BufferedInputStream(fis);
	    bis.read(buffer, 0, buffer.length);

	    ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
	    System.out.println("Enviando...");
	    os.write(buffer, 0, buffer.length);
	    os.writeLong(file.length());
	    os.flush();
	    bis.close();
	    socket.close();
	    server.close();
	    // }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
