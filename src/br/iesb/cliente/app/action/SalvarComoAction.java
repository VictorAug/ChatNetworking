package br.iesb.cliente.app.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SalvarComoAction extends AbstractAction {

    /** Constante serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Atributo parent. */
    private JFrame parent;

    /** Atributo text area. */
    private String textMessage;

    /**
     * Instancia um novo salvar action.
     *
     * @param frame
     *            the frame
     */
    public SalvarComoAction(JFrame frame, JTextArea txtAreaReceive) {
	super("Salvar como...");
	this.putValue(Action.SHORT_DESCRIPTION, "Salvar arquivo");
	this.parent = frame;
	this.textMessage = txtAreaReceive.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
	JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
	int res = fileChooser.showSaveDialog(this.parent);
	if (res == JFileChooser.APPROVE_OPTION) {
	    File arq = fileChooser.getSelectedFile();
	    try {
		fileChooser.showOpenDialog(this.parent);
		fileChooser.setFileFilter(new FileNameExtensionFilter("txt files (*txt)", "txt"));
		String path = fileChooser.getSelectedFile().getAbsolutePath();
		System.out.println("Caminho do arquivo: " + path);
		escreveArquivo(textMessage, arq.getPath());
		System.out.println("Nome do arquivo escolhido: " + arq.getPath());
	    } catch (IOException ioe) {
		JOptionPane.showMessageDialog(null, "Não foi possível salvar o arquivo!");
	    }
	}
    }

    /**
     * Escreve arquivo.
     *
     * @param conteudo
     *            the conteudo
     * @param fileName
     *            the file name
     * @throws IOException
     *             Sinaliza que uma I/O exception ocorreu.
     */
    public void escreveArquivo(String conteudo, String fileName) throws IOException {
	PrintWriter out = new PrintWriter(new FileWriter(fileName));
	out.print(conteudo);
	out.close();
	JOptionPane.showMessageDialog(null, "Arquivo salvo com sucesso!");
    }

}
