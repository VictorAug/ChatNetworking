package br.iesb.cliente.app.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import br.iesb.app.bean.ChatMessage;


public class ClienteService implements Serializable {

    private static final long serialVersionUID = 3527000015250907284L;
    private Socket socket;
    private ObjectOutputStream output;
    
    public Socket connect() {
	try {
	    this.socket = new Socket("localhost", 5555);
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
    
}
