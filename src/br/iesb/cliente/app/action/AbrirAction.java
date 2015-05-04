package br.iesb.cliente.app.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

public class AbrirAction extends AbstractAction {

    /** Constante serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Atributo text area. */
    private JTextArea textArea;

    /**
     * Instancia um novo abrir action.
     */
    public AbrirAction() {
	super("Abrir");
	this.putValue(Action.SHORT_DESCRIPTION, "Abrir arquivo");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent actionEvent) {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setMultiSelectionEnabled(true);
	fileChooser.setSelectedFile(new File("database"));
	fileChooser.setFileFilter(new FileFilter() {
	    
	    private final String[] okFileExtensions = new String[]{"txt", "doc", "docx"};
	    
	    @Override
	    public String getDescription() {
		return okFileExtensions.toString();
	    }
	    
	    @Override
	    public boolean accept(File file) {
		for (String extension : okFileExtensions) {
		    if (file.getName().toLowerCase().endsWith(extension)) {
			return true;
		    }
		}
		return false;
	    }
	});
	int resp = fileChooser.showOpenDialog(textArea);
	if (resp != JFileChooser.APPROVE_OPTION) {
	    return;
	}
	openFile(fileChooser.getSelectedFile());
    }

    /**
     * Open file.
     *
     * @param arquivo
     *            the arquivo
     */
    private void openFile(File arquivo) {
	try {
	    arquivo.setReadOnly();
	    Desktop.getDesktop().open(arquivo);
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	} catch (SecurityException e) {
	    e.printStackTrace();
	}
    }

}
