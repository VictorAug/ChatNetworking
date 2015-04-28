package br.iesb.frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ChatFrame {

    private JFrame frame;
    
    private JTextField userNameField;
    private JTextArea chatTextArea;
    private JTextArea inputTextArea;
    private JTextArea usersList;
    
    private BufferedReader reader;
    private PrintWriter writer;
    
    private int port = 5000;
    private String userName, serverIP = "192.168.5.99";
    private Socket socket;
    private ArrayList<String> userList;
    private boolean isConnected = false;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    ChatFrame window = new ChatFrame();
		    window.frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the application.
     */
    public ChatFrame() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	frame = new JFrame();
	frame.getContentPane().setForeground(Color.LIGHT_GRAY);
	frame.getContentPane().setBackground(Color.DARK_GRAY);
	frame.setBounds(100, 100, 640, 480);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(new MigLayout("", "[][fill][][][grow][grow][grow][40][40][40][50][][][50]", "[][][][][grow][30][30][30.00]"));

	JLabel lblUsurio = new JLabel("Usuário:");
	lblUsurio.setForeground(Color.LIGHT_GRAY);
	frame.getContentPane().add(lblUsurio, "cell 0 1,grow");

	userNameField = new JTextField();
	userNameField.setForeground(new Color(255, 165, 0));
	userNameField.setBackground(Color.BLACK);
	frame.getContentPane().add(userNameField, "cell 1 1 7 1,growx");
	userNameField.setColumns(10);
	lblUsurio.setLabelFor(userNameField);

	JButton btnConectar = new JButton("Conectar");
	btnConectar.addActionListener(e -> {
	    if (!isConnected) {
		userName = userNameField.getText();
		userNameField.setEditable(false);
		try {
		    socket = new Socket(InetAddress.getLocalHost().getHostAddress(), port);
		    InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
		    reader = new BufferedReader(streamReader);
		    writer = new PrintWriter(socket.getOutputStream());
		    writer.println(userName + ":foi conectado.:Conectar");
		    writer.flush();
		    isConnected = true;
		} catch (Exception e2) {
		    chatTextArea.append("Não foi possível conectar! Tente novamente.\n");
		    userNameField.setEditable(true);
		}
		listenThread();
	    } else {
		chatTextArea.append("Você já está conectado.\n");
	    }
	});
	frame.getContentPane().add(btnConectar, "cell 8 1,grow");

	JButton btnDesconectar = new JButton("Desconectar");
	btnDesconectar.addActionListener(e -> {
	    sendDisconnect();
	    disconnect();
	});
	frame.getContentPane().add(btnDesconectar, "cell 9 1,grow");

	JLabel lblUsuriosOnline = new JLabel("Usuários Online");
	lblUsuriosOnline.setForeground(Color.LIGHT_GRAY);
	frame.getContentPane().add(lblUsuriosOnline, "cell 11 2,grow");

	JScrollPane scrollPane_1 = new JScrollPane();
	frame.getContentPane().add(scrollPane_1, "cell 0 3 10 2,grow");

	chatTextArea = new JTextArea();
	chatTextArea.setLineWrap(true);
	chatTextArea.setEditable(false);
	chatTextArea.setBackground(new Color(0, 0, 0));
	scrollPane_1.setViewportView(chatTextArea);

	JScrollPane scrollPane_2 = new JScrollPane();
	frame.getContentPane().add(scrollPane_2, "cell 10 3 4 5,grow");

	usersList = new JTextArea();
	usersList.setEditable(false);
	usersList.setBackground(new Color(0, 0, 0));
	scrollPane_2.setViewportView(usersList);

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBackground(new Color(0, 0, 0));
	frame.getContentPane().add(scrollPane, "cell 0 5 9 3,grow");

	inputTextArea = new JTextArea();
	inputTextArea.setForeground(new Color(255, 215, 0));
	inputTextArea.setBackground(new Color(0, 0, 0));
	scrollPane.setViewportView(inputTextArea);

	JButton sendButton = new JButton("Enviar");
	sendButton.addActionListener(e -> {
	    if ("".equals(inputTextArea.getText())) {
		inputTextArea.setText("");
		inputTextArea.requestFocus();
	    } else {
		try {
		    writer.println(userName+":"+inputTextArea.getText()+":"+"Chat");
		    writer.flush();
		} catch (Exception e2) {
		    chatTextArea.append("Mensagem não foi enviada. \n");
		    e2.printStackTrace();
		}
		inputTextArea.setText("");
		inputTextArea.requestFocus();
	    }
	    inputTextArea.setText("");
	    inputTextArea.requestFocus();
	});
	frame.getContentPane().add(sendButton, "cell 9 5 1 3,grow");
    }

    public class IncommingReader implements Runnable {

	@Override
	public void run() {
	    String[] data;
	    String stream;

	    try {
		while ((stream = reader.readLine()) != null) {
		    data = stream.split(":");
		    switch (data[2]) {
			case "Chat":
			    chatTextArea.append(data[0] + ": " + data[1] + "\n");
			    chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
			    break;
			case "Conectar":
			    chatTextArea.removeAll();
			    userAdd(data[0]);
			    break;
			default:
			    break;
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public void listenThread() {
	Thread incommingReader = new Thread(new IncommingReader());
	incommingReader.start();
    }

    public void userAdd(String data) {
	userList.add(data);
    }

    public void userRemove(String data) {
	chatTextArea.append(data + " foi desconectado.\n");
    }

    public void writeUsers() {
	String[] tempList = new String[userList.size()];
	userList.toArray(tempList);
	for (String token : tempList) {
	    usersList.append(token + "\n");
	}
    }

    public void sendDisconnect() {
	String bye = userName + ": :Desconectado";
	try {
	    writer.println(bye);
	    writer.flush();
	} catch (Exception e) {
	    chatTextArea.append("Could not send Disconnect message.\n");
	}
    }

    public void disconnect() {
	try {
	    chatTextArea.append("Desconectado.\n");
	    socket.close();
	} catch (Exception e) {
	    chatTextArea.append("Failed to disconnect.\n");
	}
	isConnected = false;
	userNameField.setEditable(true);
	usersList.setText("");
    }

}
