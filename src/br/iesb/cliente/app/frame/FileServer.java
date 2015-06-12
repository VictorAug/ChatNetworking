package br.iesb.cliente.app.frame;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileServer {

    public static void main(String[] args) {
	while (true) {

	}
    }

    public static void downloadToServer(File file) {
	try {
	    int lidos;
	    int current = 0;
	    Socket socket = new Socket("localhost", 13267);

	    // Recebendo arquivo
	    byte[] buffer = new byte[(int) file.length()];
	    InputStream is = socket.getInputStream();
	    File novo = new File(System.getProperty("user.dir") + "/database/" + file.getName());
	    FileOutputStream fos = new FileOutputStream(novo);
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    lidos = is.read(buffer, 0, buffer.length);
	    current = lidos;
	    do {
		lidos = is.read(buffer, 0, buffer.length-current);
		if (lidos >= 0) {
		    current += lidos;
		} 
	    } while (lidos > -1);
	    
	    bos.write(buffer, 0, current);
	    bos.close();
	    socket.close();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void uploadToClient(File file) {
	try {
	    // Cria o socket temporário
	    ServerSocket server = new ServerSocket(13267);
	    while (true) {
		Socket socket = server.accept();
		System.out.println("Conexão aceita: " + socket);

		// Envia o arquivo
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(buffer, 0, buffer.length);

		OutputStream os = socket.getOutputStream();
		System.out.println("Enviando...");
		os.write(buffer, 0, buffer.length);
		os.flush();
		bis.close();
		socket.close();
		server.close();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
