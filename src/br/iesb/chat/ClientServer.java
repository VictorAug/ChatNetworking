package br.iesb.chat;

import javax.swing.JOptionPane;

import br.iesb.client.ChatClient;
import br.iesb.server.MultiThreadChatServerSync;

/**
 * Class ClientServer.
 */
public class ClientServer {

    /** Constante CHAT_NETWORKING_TITLE. */
    private static final String CHAT_NETWORKING_TITLE = "ChatNetworking";

    /**
     * O metodo main.
     *
     * @param args os argumentos
     */
    public static void main(String[] args) {
	Object selection = JOptionPane.showInputDialog(null, "Entrar como : ", CHAT_NETWORKING_TITLE, JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Servidor", "Cliente" }, "Servidor");
	
	// INICIA O SERVIDOR
	if ("Servidor".equals(selection))
	    MultiThreadChatServerSync.main(new String[] {});
	// IP CLIENTE
	else if ("Cliente".equals(selection))
	    ChatClient.main(new String[] { JOptionPane.showInputDialog(null, "Digite o endereco IP: ", CHAT_NETWORKING_TITLE, JOptionPane.INFORMATION_MESSAGE) });
    }

}
