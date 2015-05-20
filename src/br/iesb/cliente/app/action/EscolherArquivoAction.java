package br.iesb.cliente.app.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JList;

import br.iesb.app.bean.ChatMessage;

@SuppressWarnings("rawtypes")
public class EscolherArquivoAction extends MouseAdapter {

    private JList listRepoOnline;
    private final ChatMessage message;

    public EscolherArquivoAction(JList listRepoOnline, ChatMessage message) {
	this.message = message;
	this.listRepoOnline = listRepoOnline;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	if (e.getClickCount() == 2 && e.getSource() == listRepoOnline) {
	    if (((JList) e.getSource()).locationToIndex(e.getPoint()) >= 0) {
		for (File file : message.getFiles()) {
		    if (file.getName().equals(listRepoOnline.getSelectedValue())) {
			AbrirAction.openFile(file);
		    }
		}
	    }
	}
    }
}
