package br.iesb.cliente.app.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale.Category;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import br.iesb.app.bean.ChatMessage;
import br.iesb.app.bean.ChatMessage.Action;
import br.iesb.app.utils.DocumentFilter;
import br.iesb.app.utils.ImageFilter;
import br.iesb.app.utils.MidiaFilter;
import br.iesb.cliente.app.action.AbrirAction;
import br.iesb.cliente.app.action.EscolherArquivoAction;
import br.iesb.cliente.app.action.SalvarAction;
import br.iesb.cliente.app.action.SalvarComoAction;
import br.iesb.cliente.app.service.ClienteService;

/**
 * Class ClienteFrame.
 */
@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
public class ClienteFrame extends JFrame {

    /** Constante de serialização do objeto. */
    private static final long serialVersionUID = -7997090265601989938L;

    private Socket socket;
    private ChatMessage message;
    private ClienteService clientService;
    private JTextField txtName;
    private BufferedReader reader;
    private PrintWriter printWriter;
    private String userName;
    private ArrayList<String> userList;
    private boolean isConnected = false;
    private ServerSocket serverSocket;
    private PrintStream printStream;
    private JList listOnlines;
    private JScrollPane scrollReceive;
    private JButton btnConectar;
    private JButton btnSair;
    private JScrollPane scrollSend;
    private JButton btnEnviar;
    private JButton btnLimpar;
    private JMenuItem mntmAbrir;
    private JMenuItem mntmSalvarComo;
    private JMenuItem mntmSalvar;
    private JButton btnEscolherArquivo;
    private JMenu mnAjuda;
    private JMenuItem mntmInformaesDaRede;
    private JList listRepoOnline;
    private JScrollPane scrollPane;
    private JTextArea txtAreaSend;
    private JTextArea txtAreaReceive;

    private DefaultListModel listModel;

    private boolean flag = true;

    private Socket fileSocket;

    /**
     * Create the application.
     */
    public ClienteFrame() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	UIManager.put("TitledBorder.border", new LineBorder(Color.lightGray, 1));

	setTitle("ChatNetworking");

	getContentPane().setForeground(Color.LIGHT_GRAY);
	getContentPane().setBackground(Color.black);
	setBounds(100, 100, 980, 680);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	getContentPane().setLayout(new MigLayout("", "[150][fill][][16.00][][grow][40][20][20][20][30][30][0.01][50][][][50]",
						 "[5][15][5][][][20.00][grow][40][][][15][15][15][15]"));

	listModel = new DefaultListModel();

	listOnlines = new JList(listModel);
	listOnlines.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	listOnlines.setModel(new DefaultListModel<String>());
	listOnlines.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Online", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(192, 192, 192)));
	listOnlines.setBackground(Color.BLACK);
	listOnlines.setForeground(new Color(255, 51, 51));
	listOnlines.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
	getContentPane().add(listOnlines, "cell 13 0 4 14,grow");

	scrollReceive = new JScrollPane();
	scrollReceive.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING, TitledBorder.TOP, null, Color.LIGHT_GRAY));
	scrollReceive.setEnabled(false);
	scrollReceive.setForeground(Color.CYAN);
	scrollReceive.setBackground(Color.BLACK);
	getContentPane().add(scrollReceive, "cell 3 3 10 4,grow");

	txtAreaReceive = new JTextArea();
	txtAreaReceive.setEditable(false);
	txtAreaReceive.setCaretColor(Color.CYAN);
	txtAreaReceive.setFont(new Font("Monospaced", Font.ITALIC, 13));
	txtAreaReceive.setForeground(Color.CYAN);
	txtAreaReceive.setBackground(Color.BLACK);
	scrollReceive.setViewportView(txtAreaReceive);

	listRepoOnline = new JList(listModel);
	listRepoOnline.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	listRepoOnline.setForeground(Color.YELLOW);
	listRepoOnline.setModel(new DefaultListModel<String>());
	listRepoOnline.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Reposit\u00F3rio Online", TitledBorder.LEADING, TitledBorder.TOP, null,
						  new Color(192, 192, 192)));
	listRepoOnline.setBackground(Color.BLACK);
	getContentPane().add(listRepoOnline, "cell 0 3 3 11,grow");

	scrollSend = new JScrollPane();
	scrollSend.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mensagem", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(192, 192, 192)));
	scrollSend.setEnabled(false);
	scrollSend.setForeground(Color.GREEN);
	scrollSend.setBackground(Color.BLACK);
	getContentPane().add(scrollSend, "flowx,cell 3 7 10 6,grow");

	txtAreaSend = new JTextArea();
	txtAreaSend.setFont(new Font("Monospaced", Font.BOLD, 13));
	txtAreaSend.setForeground(Color.GREEN);
	txtAreaSend.setCaretColor(Color.GREEN);
	txtAreaSend.setDisabledTextColor(new Color(109, 109, 109));
	txtAreaSend.setBackground(Color.BLACK);
	scrollSend.setViewportView(txtAreaSend);

	txtName = new JTextField();
	txtName.setCaretColor(Color.ORANGE);
	txtName.setFont(new Font("Monospaced", Font.BOLD, 11));
	txtName.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Usu\u00E1rio", TitledBorder.LEADING, TitledBorder.TOP, null, Color.LIGHT_GRAY));
	txtName.setForeground(new Color(255, 165, 0));
	txtName.setBackground(Color.BLACK);
	getContentPane().add(txtName, "cell 0 0 10 3,grow");
	txtName.setColumns(10);

	btnSair = new JButton("Sair");
	btnSair.setEnabled(false);
	btnSair.addActionListener(e -> {
	    ChatMessage message = new ChatMessage();
	    message.setName(this.message.getName());
	    message.setAction(Action.DISCONNECT);
	    this.clientService.send(message);
	    disconnectedClient();
	});

	btnConectar = new JButton("Conectar");
	btnConectar.addActionListener(e -> {
	    String name = this.txtName.getText().trim();
	    if (!name.isEmpty()) {
		this.message = new ChatMessage();
		this.message.setAction(Action.CONNECT);
		this.message.setName(name);
		this.clientService = new ClienteService();
		this.socket = this.clientService.connect();
		new Thread(new ListenerSocket(this.socket)).start();
		this.clientService.send(message);
	    }
	});
	getContentPane().add(btnConectar, "cell 10 1,grow");
	getContentPane().add(btnSair, "cell 11 1,grow");

	btnEscolherArquivo = new JButton("Enviar arquivo...");
	btnEscolherArquivo.setEnabled(false);
	btnEscolherArquivo.addActionListener(e -> {
	    JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
	    fileChooser.setMultiSelectionEnabled(false);
	    fileChooser.setAcceptAllFileFilterUsed(false);
	    fileChooser.addChoosableFileFilter(new ImageFilter());
	    fileChooser.addChoosableFileFilter(new DocumentFilter());
	    fileChooser.addChoosableFileFilter(new MidiaFilter());
	    int res = fileChooser.showOpenDialog(new JTextArea());
	    if (res != JFileChooser.APPROVE_OPTION) {
		return;
	    }
	    String name = this.message.getName();
	    this.message = new ChatMessage();
	    this.message.setAction(Action.SEND_FILE);
	    this.message.setName(name);
	    this.message.setFile(fileChooser.getSelectedFile());
	    this.fileSocket = this.clientService.connectFile();
	    upload();
	    this.txtAreaReceive.append("Você enviou o(s) arquivo(s): " + this.message.getFileNames() + "\n");
	    this.flag = false;
	    this.clientService.send(this.message);
	});

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
		this.clientService.send(this.message);
	    }

	    this.txtAreaSend.setText("");
	    mntmSalvarComo.addActionListener(new SalvarComoAction(this, txtAreaReceive));
	    mntmSalvar.addActionListener(new SalvarAction(this, message, txtAreaReceive));
	});
	getContentPane().add(btnEnviar, "cell 3 13,grow");

	btnLimpar = new JButton("Limpar");
	btnLimpar.setEnabled(false);
	btnLimpar.addActionListener(e -> this.txtAreaSend.setText(""));
	getContentPane().add(btnLimpar, "cell 4 13,grow");
	getContentPane().add(btnEscolherArquivo, "cell 10 13 2 1,grow");

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
	mntmInformaesDaRede.addActionListener(e -> JOptionPane.showMessageDialog(null, "IP do Servidor: " + this.clientService.getServerIP() + "\nIP do cliente: "
		+ this.clientService.getClientIP(), "Informações da rede", JOptionPane.DEFAULT_OPTION));
	mnAjuda.add(mntmInformaesDaRede);
    }

    private void upload() {
	// Checa se a transferência foi completada com sucesso
	OutputStream output = null;
	ServerSocket servsock = null;
	FileInputStream fileIn = null;
	BufferedInputStream bis = null;
	Socket socket = null;
	int tam = 0;

	try {
	    // Criando tamanho de leitura
	    byte[] cbuffer = new byte[(int) message.getFile().length()];
	    int bytesRead;
	    
	    // Criando arquivo que será transferido pelo servidor
	    fileIn = new FileInputStream(message.getFile());
	    bis = new BufferedInputStream(fileIn);
	    bis.read(cbuffer, 0, cbuffer.length);
	    System.out.println("Lendo arquivo ...");

	    // Criando canal de transferência
	    output = socket.getOutputStream();
	    
	    // Lendo arquivo criado e enviado para o canal de transferência
	    System.out.println("Enviando arquivo ...");
	    output.write(cbuffer, 0, cbuffer.length);
	    output.flush();
	    System.out.println("Arquivo enviado!");
	} catch (Exception e1) {
	    e1.printStackTrace();
	}
    }

    /**
     * Classe ListenerSocket.
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
			    connectedClient(message);
			    break;
			case DISCONNECT:
			    disconnectedClient();
			    socket.close();
			    break;
			case SEND_ONE:
			    receiveMessage(message);
			    break;
			case USERS_ONLINE:
			    refreshOnlines(message);
			    break;
			case RECEIVE_FILE:
			    receiveFile(message);
			    break;
			default:
			    break;
		    }
		}
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		if (message.getOnlines().isEmpty()) {
		    JOptionPane.showMessageDialog(null, "Servidor encerrado");
		    System.exit(0);
		} else {
		    JOptionPane.showMessageDialog(null, "Servidor caiu ou excedeu o tempo de estabelecimento da conexão...");
		    disconnectedClient();
		}
	    }
	}
    }

    /**
     * Receive file.
     *
     * @param message
     *            the message
     */
    private void receiveFile(ChatMessage message) {
	System.out.println("receiveFile() → Thread " + txtName.getText());
	System.out.println("message.getName() → " + message.getName() + "\n" + "this.message.getName() → " + this.message.getName());

	Set<String> arrayList = new HashSet<String>();
	for (int i = 0; i < this.listRepoOnline.getModel().getSize(); i++) {
	    arrayList.add((String) this.listRepoOnline.getModel().getElementAt(i));
	}
	arrayList.addAll(message.getFileNames());
	this.listRepoOnline.setListData(arrayList.toArray());
	this.listRepoOnline.addMouseListener(EscolherArquivoAction.getInstance(listRepoOnline));

	if (this.message.getName().equals(message.getName())) {
	    salvar(this.message.getFile());
	} else {
	    // message.getFile().forEach(f -> download(f.getName(), is));
	    // txtAreaReceive.append(message.getName() +
	    // " enviou o(s) arquivo(s): " + message.getFileNames() + "\n");
	}
    }

    private void download(String filename, InputStream is) {
	FileOutputStream fos = null;
	byte[] buffer = new byte[1024];
	int lidos;
	try {
	    fos = new FileOutputStream(new File(System.getProperty("user.dir") + "/database/" + filename));
	    while ((lidos = is.read(buffer)) != -1) {
		fos.write(buffer, 0, lidos);
		fos.flush();
	    }
	    System.out.println("Arquivo " + filename + " recebido!");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @SuppressWarnings("resource")
    private void salvar(File escolhido) {
	try {
	    File novo = new File(System.getProperty("user.dir") + "/database/" + this.message.getName() + "_" + escolhido.getName());
	    FileInputStream fileInputStream = new FileInputStream(escolhido);
	    FileOutputStream fileOutputStream = new FileOutputStream(novo);

	    EscolherArquivoAction.getInstance().addFileName(novo.getName());

	    FileChannel fin = fileInputStream.getChannel();
	    FileChannel fout = fileOutputStream.getChannel();

	    Long size = fin.size();

	    fin.transferTo(0, size, fout);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Receive.
     *
     * @param message
     *            the message
     */
    private void receiveMessage(ChatMessage message) {
	this.listRepoOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	if (message.getText() != null && !message.getName().equals(this.message.getName())) {
	    this.txtAreaReceive.append(message.getName() + " diz:  " + message.getText() + "\n");
	}
    }

    /**
     * Refresh onlines.
     *
     * @param message
     *            the message
     */
    private void refreshOnlines(ChatMessage message) {
	System.out.println("refreshOnlines() → Thread do(a) " + message.getName());
	Set<String> names = message.getOnlines();
	names.remove(message.getName());
	String[] array = (String[]) names.toArray(new String[names.size()]);
	this.listOnlines.setListData(array);
	this.listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.listOnlines.setLayoutOrientation(JList.VERTICAL);
    }

    /**
     * Connected.
     *
     * @param message
     *            the message
     */
    private void connectedClient(ChatMessage message) {
	if (message.getText().equals("NO")) {
	    this.txtName.setText("");
	    JOptionPane.showMessageDialog(this, "Conexão não realizada!\nTente novamente com um novo nome.");
	    return;
	}
	this.message = message;
	this.btnConectar.setEnabled(false);
	this.btnEscolherArquivo.setEnabled(true);
	this.txtName.setEditable(false);
	this.btnSair.setEnabled(true);
	this.scrollSend.setEnabled(true);
	this.txtAreaSend.setEnabled(true);
	this.scrollReceive.setEnabled(true);
	this.txtAreaReceive.setEnabled(true);
	this.btnEnviar.setEnabled(true);
	this.btnLimpar.setEnabled(true);
	this.btnEscolherArquivo.setEnabled(true);
	this.listOnlines.setEnabled(true);
	this.listRepoOnline.setEnabled(true);
	JOptionPane.showMessageDialog(this, "Você está conectado no chat!");
    }

    /**
     * Disconnect.
     */
    private void disconnectedClient() {
	this.btnConectar.setEnabled(true);
	this.txtName.setEditable(true);
	this.btnSair.setEnabled(false);
	this.scrollSend.setEnabled(false);
	this.txtAreaSend.setEditable(false);
	this.scrollReceive.setEnabled(false);
	this.txtAreaReceive.setEditable(false);
	this.btnEnviar.setEnabled(false);
	this.btnLimpar.setEnabled(false);
	this.btnEscolherArquivo.setEnabled(false);
	this.listOnlines.setEnabled(false);
	this.listRepoOnline.setEnabled(false);
	this.txtAreaReceive.setText("");
	this.txtAreaSend.setText("");
	JOptionPane.showMessageDialog(this, "Você saiu do chat!");
    }
}
