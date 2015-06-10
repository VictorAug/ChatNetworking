package br.iesb.servidor.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTransfer {
    
    private static final Integer PORT = 13267;
    private final String fileName;

    public ServerTransfer(String fileName) {
	this.fileName = fileName;
    }

    public static void init(String fileName) {
	// Criando servidor
	ServerTransfer server = new ServerTransfer(fileName);
	
	// Aguardar conexão de cliente para tranferir
	server.waitForClient();
    }

    private void waitForClient() {
	// Checa se a transferência foi completada com sucesso
	OutputStream output = null;
	ServerSocket servsock = null;
	FileInputStream fileIn = null;
	
	try {
	    // Abrindo porta para conexão de clientes
	    servsock = new ServerSocket(PORT);
	    System.out.println("Porta de conexão aberta 13267");
	    
	    // Cliente conectado
	    Socket sock = servsock.accept();
	    System.out.println("Conexão recebida pelo cliente");
	    
	    // Criando tamanho de leitura
	    byte[] cbuffer = new byte[1024];
	    int bytesRead;
	    
	    // Criando arquivo que será transferido pelo servidor
	    File file = new File(System.getProperty("user.dir")+"/database/"+fileName);
	    fileIn = new FileInputStream(file);
	    System.out.println("Lendo arquivo ...");
	    
	    // Criando canal de transferência
	    output = sock.getOutputStream();
	    
	    // Lendo arquivo criado e enviado para o canal de transferência
	    System.out.println("Enviando arquivo ...");
	    while ((bytesRead = fileIn.read(cbuffer)) != -1) {
		output.write(cbuffer, 0, bytesRead);
		output.flush();
	    }
	    System.out.println("Arquivo enviado!");
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (output != null) {
		try {
		    output.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (servsock != null) {
		try {
		    servsock.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (fileIn != null) {
		try {
		    fileIn.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	
    }

}
