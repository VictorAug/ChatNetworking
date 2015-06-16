package br.iesb.cliente.app.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JList;

import br.iesb.cliente.app.service.ClienteService;

@SuppressWarnings("rawtypes")
public class EscolherArquivoAction extends MouseAdapter {

    private JList listRepoOnline;
    private static EscolherArquivoAction uniqueInstance;

    public static synchronized EscolherArquivoAction getInstance(JList listRepoOnline) {
	if (uniqueInstance == null) {
	    uniqueInstance = new EscolherArquivoAction(listRepoOnline);
	}
	return uniqueInstance;
    }

    public static synchronized EscolherArquivoAction getInstance() {
	return uniqueInstance;
    }

    private EscolherArquivoAction(JList listRepoOnline) {
	this.listRepoOnline = listRepoOnline;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	if (e.getClickCount() == 2 && e.getSource() == listRepoOnline) {
	    Arrays.asList(ClienteService.listServerDirectory()).stream().filter(name -> name.contains((String) this.listRepoOnline.getSelectedValue()))
		  .forEach(fileName -> AbrirAction.openFileByName(fileName));
	    listRepoOnline.clearSelection();
	}
    }

}
