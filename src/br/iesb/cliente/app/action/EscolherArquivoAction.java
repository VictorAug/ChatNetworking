package br.iesb.cliente.app.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

import br.iesb.app.bean.ChatMessage;

@SuppressWarnings("rawtypes")
public class EscolherArquivoAction extends MouseAdapter {

    private JList listRepoOnline;
    private ChatMessage message;
    private static EscolherArquivoAction uniqueInstance;

    public static synchronized EscolherArquivoAction getInstance(JList listRepoOnline, ChatMessage message) {
	if (uniqueInstance == null) {
	    uniqueInstance = new EscolherArquivoAction(listRepoOnline, message);
	}
	return uniqueInstance;
    }

    private EscolherArquivoAction(JList listRepoOnline, ChatMessage message) {
	this.message = message;
	this.listRepoOnline = listRepoOnline;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	if (e.getClickCount() == 2 && e.getSource() == listRepoOnline) {
	    if (((JList) e.getSource()).locationToIndex(e.getPoint()) >= 0) {
		if (message.getFile().getName().equals(listRepoOnline.getSelectedValue())) {
		    AbrirAction.openFile(message.getFile());
		}
	    }
	}
    }

    public void setMessage(ChatMessage message) {
	this.message = message;
    }

    public ChatMessage getMessage() {
	return message;
    }

}
