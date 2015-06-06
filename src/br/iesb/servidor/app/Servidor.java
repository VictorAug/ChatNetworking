package br.iesb.servidor.app;

import java.io.Serializable;

import br.iesb.servidor.app.service.ServidorService;

public class Servidor implements Serializable {

    private static final long serialVersionUID = 5537429467751528722L;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	ServidorService.getInstance();
//	EventQueue.invokeLater(() -> {
//	    try {
//		new ServidorFrame().setVisible(true);
//	    } catch (Exception e) {
//		e.printStackTrace();
//	    }
//	});
    }

}
