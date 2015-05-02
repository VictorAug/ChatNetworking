package br.iesb.cliente.app;

import java.awt.EventQueue;
import java.io.Serializable;

import br.iesb.cliente.app.frame.ClienteFrame;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    new ClienteFrame().setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

}
