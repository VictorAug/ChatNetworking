package br.iesb.servidor.app;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import br.iesb.servidor.app.service.ServidorService;

public class ServidorFrame extends JFrame {

    /** Constante serialVersionUID. */
    private static final long serialVersionUID = 3116577263594543784L;

    /**
     * Create the application.
     * @wbp.parser.entryPoint
     */
    public ServidorFrame() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	ServidorService.getInstance();
	getContentPane().setForeground(Color.CYAN);
	getContentPane().setBackground(Color.BLACK);
	getContentPane().setLayout(new MigLayout("", "[][][][][][][][grow][][][]", "[][][][][grow]"));
	
	JLabel lblServidorOnline = new JLabel("Servidor online");
	lblServidorOnline.setHorizontalAlignment(SwingConstants.CENTER);
	lblServidorOnline.setForeground(Color.CYAN);
	lblServidorOnline.setBackground(Color.DARK_GRAY);
	getContentPane().add(lblServidorOnline, "cell 0 0 11 1,grow");
	
	JTextPane textPane = new JTextPane();
	textPane.setForeground(Color.GREEN);
	textPane.setBackground(Color.DARK_GRAY);
	getContentPane().add(textPane, "cell 0 2 11 3,grow");
	
    }

}
