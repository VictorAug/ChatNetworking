package br.iesb.cliente.app.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EscolherArquivoAction extends AbstractAction {

    /** Constante serialVersionUID. */
    private static final long serialVersionUID = -6481964008718249478L;
    private JTextArea textArea;

    public EscolherArquivoAction() {
	super("Escolher Arquivo");
	this.putValue(Action.SHORT_DESCRIPTION, "Upload do arquivo");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
	fileChooser.setMultiSelectionEnabled(true);
	fileChooser.setAcceptAllFileFilterUsed(false);
	fileChooser.setFileFilter(new FileNameExtensionFilter("image files (*jpg)", "jpg"));
	int res = fileChooser.showOpenDialog(textArea);
	if (res != JFileChooser.APPROVE_OPTION) {
	    return;
	}
	uploadFile(fileChooser.getSelectedFile());
    }

    private void uploadFile(File selectedFile) {
	
    }

}
