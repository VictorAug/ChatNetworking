package br.iesb.cliente.app.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import br.iesb.app.bean.ChatMessage;

public class SalvarAction extends AbstractAction {

    /** Constante serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Atributo text area. */
    private ChatMessage message;

    private static final String DB_PATH = "database";

    private JTextArea txtAreaReceive;

    /**
     * Instancia um novo salvar action.
     *
     * @param frame
     *            the frame
     * @param txtAreaReceive
     */
    public SalvarAction(JFrame frame, ChatMessage message, JTextArea txtAreaReceive) {
	super("Salvar como...");
	this.putValue(Action.SHORT_DESCRIPTION, "Salvar arquivo");
	this.message = message;
	this.txtAreaReceive = txtAreaReceive;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
	File file = new File(DB_PATH);
	try {
	    if (!Files.exists(Paths.get(DB_PATH)) && file.setReadOnly())
		file.mkdir();
	    escreveArquivo();
	} catch (SecurityException se) {
	    se.printStackTrace();
	} catch (IOException e1) {
	    e1.printStackTrace();
	} 
    }

    /**
     * Escreve arquivo.
     * 
     * @throws IOException
     *             Sinaliza que uma I/O exception ocorreu.
     */
    public void escreveArquivo() throws IOException {
	PrintWriter out = new PrintWriter(new FileWriter(DB_PATH + "/" + message.getName()));
	out.print(txtAreaReceive.getText());
	out.close();
	JOptionPane.showMessageDialog(null, "Arquivo salvo com sucesso!");
    }

}
