package br.iesb.servidor.app.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.iesb.servidor.app.bean.ChatMessage;
import br.iesb.servidor.app.bean.ChatMessage.Action;

public class ServidorService implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 67062559888939165L;
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();

    public ServidorService() {
	try {
	    serverSocket = new ServerSocket(5555);
	    System.out.println("Running Server...");
	    while (true) {
		socket = serverSocket.accept();
		new Thread(new ListenerSocket(socket)).start();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private class ListenerSocket implements Runnable {

	private ObjectInputStream input;
	private ObjectOutputStream output;

	public ListenerSocket(Socket socket) {
	    try {
		this.output = new ObjectOutputStream(socket.getOutputStream());
		this.input = new ObjectInputStream(socket.getInputStream());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	@Override
	public void run() {
	    ChatMessage message = null;
	    try {
		while ((message = (ChatMessage) input.readObject()) != null) {
		    Action action = message.getAction();
		    switch (action) {
			case CONNECT:
			    boolean isConnect = connect(message, output);
			    if (isConnect) {
				mapOnlines.put(message.getName(), output);
				sendOnlines();
			    }
			    break;
			case DISCONNECT:
			    disconnect(message, output);
			    return;
			case SEND_ONE:
			    sendOne(message);
			    break;
			case SEND_ALL:
			    sendAll(message);
			    break;
			case USERS_ONLINE:
			    sendOnlines();
			    break;
			default:
			    break;
		    }
		}
	    } catch (IOException e) {
		disconnect(message, output);
		System.out.println(message.getName() + " deixou o chat!");
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    }
	}

    }

    private boolean connect(ChatMessage message, ObjectOutputStream output) {
	if (mapOnlines.size() == 0) {
	    message.setText("YES");
	    send(message, output);
	    return true;

	}
	for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
	    if (kv.getKey().equals(message.getName())) {
		message.setText("NO");
		send(message, output);
		return false;
	    } else {
		message.setText("YES");
		send(message, output);
		return true;
	    }
	}
	return false;
    }

    private void disconnect(ChatMessage message, ObjectOutputStream output) {
	mapOnlines.remove(message.getName());
	message.setText(" deixou o chat!");
	message.setAction(Action.SEND_ONE);
	sendAll(message);
	System.out.println("O usuário " + message.getName() + " saiu da sala");
    }

    private void sendOne(ChatMessage message) {
	for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
	    if (kv.getKey().equals(message.getNameReserved())) {
		try {
		    kv.getValue().writeObject(message);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private void sendAll(ChatMessage message) {
	for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
	    if (!kv.getKey().equals(message.getName())) {
		message.setAction(Action.SEND_ONE);
		try {
		    kv.getValue().writeObject(message);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private void send(ChatMessage message, ObjectOutputStream output) {
	try {
	    output.writeObject(message);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void sendOnlines() {
	Set<String> setNames = new HashSet<String>();
	for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
	    setNames.add(kv.getKey());
	}
	
	ChatMessage message = new ChatMessage();
	message.setAction(Action.USERS_ONLINE);
	message.setSetOnlines(setNames);
	
	for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
	    message.setName(kv.getKey());
	    try {
		System.out.println("name -- " + message.getName());
		kv.getValue().writeObject(message);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

}
