package br.iesb.cliente.app.frame;

import java.awt.Color;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import br.iesb.app.bean.ChatMessage;
import br.iesb.app.bean.ChatMessage.Action;
import br.iesb.cliente.app.action.AbrirAction;
import br.iesb.cliente.app.action.EscolherArquivoAction;
import br.iesb.cliente.app.action.SalvarAction;
import br.iesb.cliente.app.action.SalvarComoAction;
import br.iesb.cliente.app.service.ClienteService;
import br.iesb.servidor.app.service.ServidorService;

import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.JTextPane;

/**
 * Class ClienteFrame.
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class ClienteFrame extends JFrame {

    /** Constante serialVersionUID. */
    private static final long serialVersionUID = -7997090265601989938L;

    /** Atributo socket. */
    private Socket socket;

    /** Atributo message. */
    private ChatMessage message;

    /** Atributo service. */
    private ClienteService service;

    /** Atributo txt name. */
    private JTextField txtName;

    /** Atributo reader. */
    private BufferedReader reader;

    /** Atributo print writer. */
    private PrintWriter printWriter;

    /** Atributo user name. */
    private String userName;

    /** Atributo user list. */
    private ArrayList<String> userList;

    /** Atributo is connected. */
    private boolean isConnected = false;

    /** Atributo server socket. */
    private ServerSocket serverSocket;

    /** Atributo print stream. */
    private PrintStream printStream;

    /** Atributo list onlines. */
    private JList listOnlines;

    /** Atributo txt area receive. */
    private JTextArea txtAreaReceive;

    /** Atributo btn conectar. */
    private JButton btnConectar;

    /** Atributo btn sair. */
    private JButton btnSair;

    /** Atributo txt area send. */
    private JTextArea txtAreaSend;

    /** Atributo btn enviar. */
    private JButton btnEnviar;

    /** Atributo btn limpar. */
    private JButton btnLimpar;

    /** Atributo mntm abrir. */
    private JMenuItem mntmAbrir;

    /** Atributo mntm salvar como. */
    private JMenuItem mntmSalvarComo;

    /** Atributo mntm salvar. */
    private JMenuItem mntmSalvar;
    private JLabel lblUsurio;
    private JButton btnEscolherArquivo;
    private JMenu mnAjuda;
    private JMenuItem mntmInformaesDaRede;

    /**
     * Create the application.
     */
    public ClienteFrame() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    @SuppressWarnings("unchecked")
    private void initialize() {
	getContentPane().setForeground(Color.LIGHT_GRAY);
	getContentPane().setBackground(Color.DARK_GRAY);
	setBounds(100, 100, 980, 720);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	getContentPane().setLayout(new MigLayout("", "[][fill][][16.00][grow][40][20][20][20][40][0.01][50][][][50]", "[20][][][20.00][grow][][][15][15][15][15]"));

	lblUsurio = new JLabel("Usuário: ");
	lblUsurio.setForeground(Color.LIGHT_GRAY);
	getContentPane().add(lblUsurio, "cell 0 0,grow");

	txtName = new JTextField();
	txtName.setForeground(new Color(255, 165, 0));
	txtName.setBackground(Color.BLACK);
	getContentPane().add(txtName, "cell 1 0 7 1,grow");
	txtName.setColumns(10);

	btnSair = new JButton("Sair");
	btnSair.setEnabled(false);
	btnSair.addActionListener(e -> {
	    ChatMessage message = new ChatMessage();
	    message.setName(this.message.getName());
	    message.setAction(Action.DISCONNECT);
	    this.service.send(message);
	    disconnected();
	    System.exit(0);
	});

	btnConectar = new JButton("Conectar");
	btnConectar.addActionListener(e -> {
	    String name = this.txtName.getText();
	    if (!name.isEmpty()) {
		this.message = new ChatMessage();
		this.message.setAction(Action.CONNECT);
		this.message.setName(name);
		this.service = new ClienteService();
		this.socket = this.service.connect();
		new Thread(new ListenerSocket(this.socket)).start();
		this.service.send(message);
	    }
	});
	getContentPane().add(btnConectar, "cell 8 0,grow");
	getContentPane().add(btnSair, "cell 9 0,grow");

	listOnlines = new JList();
	listOnlines.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	listOnlines.setModel(new DefaultListModel<String>());
	listOnlines.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Online", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(192, 192, 192)));
	listOnlines.setBackground(Color.BLACK);
	listOnlines.setForeground(Color.YELLOW);
	getContentPane().add(listOnlines, "cell 11 0 4 11,grow");

	txtAreaReceive = new JTextArea();
	txtAreaReceive.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING, TitledBorder.TOP, null, Color.LIGHT_GRAY));
	txtAreaReceive.setEditable(false);
	txtAreaReceive.setEnabled(false);
	txtAreaReceive.setForeground(Color.CYAN);
	txtAreaReceive.setBackground(Color.BLACK);
	getContentPane().add(txtAreaReceive, "cell 0 1 11 4,grow");

	txtAreaSend = new JTextArea();
	txtAreaSend.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mensagem", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(192, 192, 192)));
	txtAreaSend.setEnabled(false);
	txtAreaSend.setForeground(Color.GREEN);
	txtAreaSend.setBackground(Color.BLACK);
	getContentPane().add(txtAreaSend, "cell 0 5 11 5,grow");

	btnEnviar = new JButton("Enviar");
	btnEnviar.setEnabled(false);
	btnEnviar.addActionListener(e -> {
	    String text = this.txtAreaSend.getText();
	    String name = this.message.getName();

	    this.message = new ChatMessage();

	    if (this.listOnlines.getSelectedIndex() > -1) {
		this.message.setNameReserved((String) this.listOnlines.getSelectedValue());
		this.message.setAction(Action.SEND_ONE);
		this.listOnlines.clearSelection();
	    } else {
		this.message.setAction(Action.SEND_ALL);
	    }

	    if (!text.isEmpty()) {
		this.message.setName(name);
		this.message.setText(text);
		this.txtAreaReceive.append("Você disse: " + text + "\n");
		this.service.send(this.message);
	    }

	    this.txtAreaSend.setText("");
	    mntmSalvarComo.addActionListener(new SalvarComoAction(this, txtAreaReceive));
	    mntmSalvar.addActionListener(new SalvarAction(this, message, txtAreaReceive));
	});
	getContentPane().add(btnEnviar, "cell 0 10,grow");

	btnLimpar = new JButton("Limpar");
	btnLimpar.setEnabled(false);
	btnLimpar.addActionListener(e -> {
	    this.txtAreaSend.setText("");
	});
	getContentPane().add(btnLimpar, "cell 2 10,grow");

	btnEscolherArquivo = new JButton("Escolher arquivo...");
	btnEscolherArquivo.addActionListener(new EscolherArquivoAction());
	getContentPane().add(btnEscolherArquivo, "cell 9 10");

	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);

	JMenu mnArquivo = new JMenu("Arquivo");
	menuBar.add(mnArquivo);

	mntmSalvarComo = new JMenuItem("Salvar como...");
	mntmSalvarComo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
	mnArquivo.add(mntmSalvarComo);

	mntmAbrir = new JMenuItem("Abrir...");
	mntmAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
	mntmAbrir.addActionListener(new AbrirAction());

	mntmSalvar = new JMenuItem("Salvar");
	mntmSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	mnArquivo.add(mntmSalvar);
	mnArquivo.add(mntmAbrir);

	JMenu mnConfiguraes = new JMenu("Configurações");
	menuBar.add(mnConfiguraes);

	mnAjuda = new JMenu("Ajuda");
	menuBar.add(mnAjuda);

	mntmInformaesDaRede = new JMenuItem("Informações da rede");
	mntmInformaesDaRede.addActionListener(e -> JOptionPane.showMessageDialog(null, "IP do Servidor: " + socket.getInetAddress().toString() + "\nIP do cliente: "
		+ socket.getLocalAddress().toString(), "Informações da rede", JOptionPane.DEFAULT_OPTION));
	mnAjuda.add(mntmInformaesDaRede);
    }

    /**
     * Class ListenerSocket.
     */
    private class ListenerSocket implements Runnable {

	/** Atributo input. */
	private ObjectInputStream input;

	/**
	 * Instancia um novo listener socket.
	 *
	 * @param socket
	 *            the socket
	 */
	public ListenerSocket(Socket socket) {
	    try {
		this.input = new ObjectInputStream(socket.getInputStream());
	    } catch (Exception e) {
		JOptionPane.showMessageDialog(null, "Servidor offline...");
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
			    connected(message);
			    break;
			case DISCONNECT:
			    disconnected();
			    socket.close();
			    break;
			case SEND_ONE:
			    System.out.println("::: " + message.getText() + " :::");
			    receive(message);
			    break;
			case USERS_ONLINE:
			    refreshOnlines(message);
			    break;
			default:
			    break;
		    }
		}
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		if (message.getSetOnlines().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Servidor encerrado");
			System.exit(0);
		} else {
			JOptionPane.showMessageDialog(null, "Servidor caiu ou excedeu o tempo de estabelecimento da conexão...");
			disconnected();
		}
	    }
	}
    }

    /**
     * Connected.
     *
     * @param message
     *            the message
     */
    private void connected(ChatMessage message) {
	if (message.getText().equals("NO")) {
	    this.txtName.setText("");
	    JOptionPane.showMessageDialog(this, "Conexão não realizada!\nTente novamente com um novo nome.");
	    return;
	}
	this.message = message;
	this.btnConectar.setEnabled(false);
	this.txtName.setEditable(false);
	this.btnSair.setEnabled(true);
	this.txtAreaSend.setEnabled(true);
	this.txtAreaReceive.setEnabled(true);
	this.btnEnviar.setEnabled(true);
	this.btnLimpar.setEnabled(true);
	JOptionPane.showMessageDialog(this, "Você está conectado no chat!");
    }

    /**
     * Disconnect.
     */
    private void disconnected() {
	this.btnConectar.setEnabled(true);
	this.txtName.setEditable(true);
	this.btnSair.setEnabled(false);
	this.txtAreaSend.setEnabled(false);
	this.txtAreaReceive.setEnabled(false);
	this.btnEnviar.setEnabled(false);
	this.btnLimpar.setEnabled(false);
	this.txtAreaReceive.setText("");
	this.txtAreaSend.setText("");
	JOptionPane.showMessageDialog(this, "Você saiu do chat!");
    }

    /**
     * Receive.
     *
     * @param message
     *            the message
     */
    private void receive(ChatMessage message) {
	this.txtAreaReceive.append(message.getName() + " diz:  " + message.getText() + "\n");
    }

    /**
     * Refresh onlines.
     *
     * @param message
     *            the message
     */
    @SuppressWarnings("unchecked")
    private void refreshOnlines(ChatMessage message) {
	System.out.println(message.getSetOnlines().toString());
	Set<String> names = message.getSetOnlines();
	names.remove(message.getName());
	String[] array = (String[]) names.toArray(new String[names.size()]);
	this.listOnlines.setListData(array);
	this.listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.listOnlines.setLayoutOrientation(JList.VERTICAL);
    }

}
