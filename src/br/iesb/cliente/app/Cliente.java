package br.iesb.cliente.app;

import java.awt.EventQueue;
import java.io.Serializable;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.iesb.cliente.app.frame.ClienteFrame;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * O método main.
     *
     * @param args os argumentos
     */
    public static void main(String args[]) {
	try {
	    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		System.out.println(info.getName());
		if ("Nimbus".equals(info.getName())) {
		    UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (ClassNotFoundException ex) {
	    ex.printStackTrace();
	} catch (InstantiationException ex) {
	    ex.printStackTrace();
	} catch (IllegalAccessException ex) {
	    ex.printStackTrace();
	} catch (UnsupportedLookAndFeelException ex) {
	    ex.printStackTrace();
	}

	/* Create and display the form */
	EventQueue.invokeLater(() -> new ClienteFrame().setVisible(true));
    }

}
