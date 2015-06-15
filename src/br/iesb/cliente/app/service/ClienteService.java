package br.iesb.cliente.app.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private static final String host = "192.168.0.109";

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
	    String string = this.socket.getLocalSocketAddress().toString();
	    message.setIPdoCliente(string.substring(1, string.indexOf(":")));
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

    /**
     * Abre canal e faz upload do arquivo para o servidor.
     *
     * @param file the file
     */
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
	    System.out.println("uploadToServer() → "+file);
	    while ((c = fin.read()) != -1) {
		System.out.println(c);
		out.write(c);
	    }
	    fin.close();
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public static void downloadToClient(File file) {
	try {
	    ServerSocket server = new ServerSocket(12344);
	    Socket socket = server.accept();
	    InputStream in = socket.getInputStream();
	    InputStreamReader isr = new InputStreamReader(in);
	    BufferedReader reader = new BufferedReader(isr);
	    String fName = reader.readLine();
	    System.out.println("Downloading from server: " + fName);
	    new File(System.getProperty("user.dir") + "/database/").mkdir();
	    File f = new File(System.getProperty("user.dir") + "/database/" + file.getName());
	    FileOutputStream out = new FileOutputStream(f);
	    DataInputStream dis = new DataInputStream(in);
	    long tamanho = dis.readLong();
	    int c;
	    Integer count = 0;
	    while ((c = in.read()) != -1 || count < tamanho) {
		count += c;
		out.write(c);
	    }
	    System.out.println("Arquivo recebido!");
	    out.close();
	    socket.close();
	    server.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
