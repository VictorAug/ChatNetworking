package br.iesb.cliente.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientTransfer {

    private static final Integer PORT = 13267;
    private final String fileName;

    public ClientTransfer(String fileName) {
	this.fileName = fileName;
    }

    public static void init(String fileName) {
	// Criando classe cliente para receber arquivo
	ClientTransfer cliente = new ClientTransfer(fileName);

	// Solicitando arquivo
	cliente.getFileFromServer();
    }

    private void getFileFromServer() {
	Socket sockServer = null;
	FileOutputStream fos = null;
	InputStream is = null;

	try {
	    // Criando conexão com o servidor
	    System.out.println("Conectando com Servidor porta 13267");
	    sockServer = new Socket("localhost", PORT);
	    is = sockServer.getInputStream();

	    // Cria arquivo local no cliente
	    fos = new FileOutputStream(new File(System.getProperty("user.dir")) + "/database/" + fileName);
	    System.out.println("Arquivo local criado em " + System.getProperty("user.dir") + "/database/" + fileName);

	    // Prepara variáveis para tranferência
	    byte[] cbuffer = new byte[1024];
	    int bytesRead;

	    // Copia conteudo do canal
	    System.out.println("Recebendo arquivo ...");
	    while ((bytesRead = is.read(cbuffer)) != -1) {
		fos.write(cbuffer, 0, bytesRead);
		fos.flush();
	    }
	    System.out.println("Arquivo recebido!");
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (sockServer != null) {
		try {
		    sockServer.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (fos != null) {
		try {
		    fos.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (is != null) {
		try {
		    is.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

    }
}
