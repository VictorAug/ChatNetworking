package br.iesb.servidor.app;

import java.io.Serializable;

import br.iesb.servidor.app.service.ServidorService;

public class Servidor implements Serializable {

    private static final long serialVersionUID = 5537429467751528722L;

    public static void main(String[] args) {
	new ServidorService();
    }

}
