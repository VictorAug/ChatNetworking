package br.iesb.servidor.app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import br.iesb.app.bean.ChatMessage;
import br.iesb.app.bean.ChatMessage.Action;

/**
 * Class ServidorService.
 */
public class ServidorService implements Serializable {

    /** Constante serialVersionUID. */
    private static final long serialVersionUID = 67062559888939165L;

    /** Socket Servidor para abrir uma conexão. */
    private ServerSocket serverSocket;

    /** Socket para receber a solicitação do cliente. */
    private Socket socket;

    /**
     * Lista de usuários online no chat. <br>
     * Vai servir p/ redistribuir a mensagem que o usuário digitar p/ todos os
     * outros. </br></br> <code>String</code> Nome do usuário </br></br>
     * <code>ObjectOutputStream</code> Tudo o que o usuário for digitar.
     */
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();

    private ServerSocket fserverSocket;

    private Socket fileSocket;

    /**
     * Lista de arquivos disponíveis no servidor. <br>
     * Vai servir p/ redistribuir o arquivo que o usuário enviar p/ todos os
     * outros. </br></br> <code>String</code> Nome do usuário </br></br>
     * <code>Set<File></code> Tudo o que o usuário for enviar.
     */
    private static Map<String, LinkedHashSet<File>> mapFiles = new HashMap<String, LinkedHashSet<File>>();

    /** Atributo unique instance. */
    private static ServidorService uniqueInstance;

    /**
     * Retorna uma instância de ServidorService.
     *
     * @return uma instância de ServidorService
     */
    public static synchronized ServidorService getInstance() {
	if (uniqueInstance == null) {
	    uniqueInstance = new ServidorService();
	}
	return uniqueInstance;
    }

    /**
     * Instancia um novo servidor service.
     */
    private ServidorService() {
	initMessageChannel();
	initFileChannel();
    }

    private void initFileChannel() {
	try {
	    fserverSocket = new ServerSocket(13267);
	    while (true) {
		System.out.println("Running FileServer...");
		fileSocket = fserverSocket.accept();
	    }
	} catch (IOException e) {
	    System.out.println("initFileChannel() → FAILED");
	    e.printStackTrace();
	}
    }

    private void initMessageChannel() {
	try {
	    serverSocket = new ServerSocket(5555);
	    while (true) {
		System.out.println("Running Server...");
		socket = serverSocket.accept();
		new Thread(new ListenerSocket(socket)).start();
	    }
	} catch (IOException e) {
	    if (!mapOnlines.isEmpty()) {
		JOptionPane.showMessageDialog(null, "Servidor já está rodando...");
	    }
	}
    }

    /**
     * Classe ListenerSocket. <br>
     * </br> É o ouvinte do servidor.
     */
    private class ListenerSocket implements Runnable {

	/** Executa o envio de mensagem p/ o(s) cliente(s). */
	private ObjectOutputStream output;

	/** Recebe a mensagem enviada pelo cliente. */
	private ObjectInputStream input;

	/**
	 * Instancia um novo listener socket.
	 *
	 * @param socket
	 *            socket da comunicação cliente-servidor estabelecida.
	 */
	public ListenerSocket(Socket socket) {
	    try {
		// input output agora pertencem exclusivamente do cliente que se
		// conectou.
		this.output = new ObjectOutputStream(socket.getOutputStream());
		this.input = new ObjectInputStream(socket.getInputStream());
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	    ChatMessage message = null;
	    try {
		while ((message = (ChatMessage) input.readObject()) != null) {
		    switch (message.getAction()) {
			case CONNECT:
			    if (connect(message, output)) {
				mapOnlines.put(message.getName(), output);
				mapFiles.put(message.getName(), new LinkedHashSet<File>());
				sendOnlines();
			    } else {
				message = null;
			    }
			    break;
			case DISCONNECT:
			    if (message != null) {
				disconnect(message, output);
				if (!mapOnlines.isEmpty()) {
				    sendOnlines();
				}
			    }
			    break;
			case SEND_ONE:
			    sendOne(message, output);
			    break;
			case SEND_ALL:
			    sendAll(message, output);
			    break;
			case SEND_FILE:
			    sendFile(message, output);
			    break;
			default:
			    break;
		    }
		}
	    } catch (IOException e) {
		ChatMessage cm = new ChatMessage();
		if (message != null) {
		    cm.setName(message.getName());
		    disconnect(cm, output);
		    sendOnlines();
		    System.out.println(message.getName() + " deixou o chat!");
		}
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Send file.
     *
     * @param message
     *            the message
     * @param output
     *            the output
     */
    public void sendFile(ChatMessage message, ObjectOutputStream output) {
	download(message);

	mapOnlines.entrySet().forEach(kv -> {
	    if (message.getName().equals(kv.getKey())) {
		mapFiles.get(kv.getKey()).add(message.getFile());
		// message.setFile(kv.getKey());
	    }
	    message.setAction(Action.RECEIVE_FILE);
	    try {
		kv.getValue().writeObject(message);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	});
    }

    private void download(ChatMessage message) {
	FileOutputStream fos = null;
	InputStream is = null;
	try {
	    is = socket.getInputStream();

	    // Cria arquivo local no cliente
	    fos = new FileOutputStream(new File(System.getProperty("user.dir")) + "/database/" + message.getFile().getName());
	    System.out.println("Arquivo local criado em " + System.getProperty("user.dir") + "/database/" + message.getFile().getName());

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
	}
    }

    /**
     * Send one.
     *
     * @param message
     *            the message
     * @param output
     *            the output
     */
    private void sendOne(ChatMessage message, ObjectOutputStream output) {
	for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
	    if (kv.getKey().equals(message.getNameReserved())) {
		try {
		    kv.getValue().writeObject(message);
		    send(message, output);
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	    }
	}
    }

    /**
     * Send all.
     *
     * @param message
     *            the message
     * @param output
     *            the output
     */
    private void sendAll(ChatMessage message, ObjectOutputStream output) {
	mapOnlines.entrySet().forEach(kv -> {
	    if (!kv.getKey().equals(message.getName())) {
		message.setAction(Action.SEND_ONE);
		try {
		    kv.getValue().writeObject(message);
		    send(message, output);
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	    }
	});
    }

    /**
     * Envia mensagem para o cliente.
     *
     * @param message
     *            the message
     * @param output
     *            the output
     */
    private void send(ChatMessage message, ObjectOutputStream output) {
	try {
	    output.writeObject(message);
	    System.out.println("send() → Success: mensagem " + message.toString() + " enviada!");
	} catch (IOException ex) {
	    System.out.println("send() → Erro: alguém saiu da sala!");
	}
    }

    /**
     * Envia mensagem a todos usuários onlines.
     */
    private void sendOnlines() {
	Set<String> setNames = new HashSet<String>();
	mapOnlines.entrySet().forEach(kv -> setNames.add(kv.getKey()));

	ChatMessage message = new ChatMessage();
	message.setAction(Action.USERS_ONLINE);
	message.setOnlines(setNames);

	Set<String> fileNames = new HashSet<String>();
	mapFiles.values().forEach(action -> action.forEach(file -> fileNames.add(file.getName())));
	message.addAllFileNames(fileNames);

	mapOnlines.entrySet().forEach(kv -> {
	    message.setName(kv.getKey());
	    try {
		kv.getValue().writeObject(message);
	    } catch (Exception e) {
		System.out.println("sendOnlines() → Exception");
	    }
	});
    }

    /**
     * Connect.
     *
     * @param message
     *            mensagem recebida pelo servidor
     * @param output
     *            objeto que será enviado pelo servidor
     * @return true, se a conexão foi estabelecida
     */
    private boolean connect(ChatMessage message, ObjectOutputStream output) {
	if (mapOnlines.size() == 0) {
	    message.setText("YES");
	    send(message, output);
	    return true;
	}
	if (mapOnlines.containsKey(message.getName())) {
	    message.setText("NO");
	    send(message, output);
	    return false;
	} else {
	    message.setText("YES");
	    send(message, output);
	    return true;
	}
    }

    /**
     * Disconnect.
     *
     * @param message
     *            the message
     * @param output
     *            the output
     */
    private void disconnect(ChatMessage message, ObjectOutputStream output) {
	mapOnlines.remove(message.getName());
	message.setText(" deixou o chat!");
	message.setAction(Action.SEND_ONE);
	sendAll(message, output);
	System.out.println("O usuário " + message.getName() + " saiu da sala");
    }

    public static Map<String, LinkedHashSet<File>> getMapFiles() {
	return mapFiles;
    }

}
