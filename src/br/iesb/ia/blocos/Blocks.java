package br.iesb.ia.blocos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class Blocks {

    private JFrame frame;
    private JPanel blocoOrigem;
    private Color blocoDesempilhado;

    private LinkedList<Stack<JPanel>> tableStackPanel = new LinkedList<Stack<JPanel>>();
    private LinkedList<Stack<Color>> tableStackColor = new LinkedList<Stack<Color>>();
    
    private JPanel[][] tablePanel = new JPanel[3][6];
    private Color[][] tableColor = new Color[3][6];

    protected JPanel blocoDestino;
    private JPanel yellowBlock;
    private JPanel blueBlock;
    private JPanel purpleBlock;
    private JPanel redBlock;
    private JPanel greenBlock;
    private JPanel grayBlock;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    Blocks window = new Blocks();
		    window.frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the application.
     */
    public Blocks() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	frame = new JFrame();
	frame.setBounds(100, 100, 810, 518);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	JPanel panel = new JPanel();
	panel.setBackground(Color.black);
	frame.getContentPane().add(panel, BorderLayout.CENTER);
	panel.setLayout(new MigLayout("", "[grow][30][40][30.00][][40][30][][40][30][grow,fill]", "[][][][][][40][40][40][30][100][grow]"));

	purpleBlock = new JPanel();
	purpleBlock.setBackground(Color.magenta);
	addMouseListener(purpleBlock);
	panel.add(purpleBlock, "cell 2 5,grow");

	redBlock = new JPanel();
	redBlock.setBackground(Color.red);
	addMouseListener(redBlock);
	panel.add(redBlock, "cell 5 5,grow");

	blueBlock = new JPanel();
	blueBlock.setBackground(Color.blue);
	addMouseListener(blueBlock);

	JPanel panel_3 = new JPanel();
	panel_3.setBackground(new Color(0, 0, 0));
	panel.add(panel_3, "cell 8 5,grow");
	panel.add(blueBlock, "cell 2 6,grow");

	grayBlock = new JPanel();
	grayBlock.setBackground(Color.lightGray);
	addMouseListener(grayBlock);
	panel.add(grayBlock, "cell 5 6,grow");

	yellowBlock = new JPanel();
	yellowBlock.setBackground(Color.yellow);
	addMouseListener(yellowBlock);

	JPanel panel_2 = new JPanel();
	panel_2.setBackground(new Color(0, 0, 0));
	panel.add(panel_2, "cell 8 6,grow");
	panel.add(yellowBlock, "cell 2 7,grow");

	greenBlock = new JPanel();
	greenBlock.setBackground(Color.green);
	addMouseListener(greenBlock);
	panel.add(greenBlock, "cell 5 7,grow");

	JPanel panel_1 = new JPanel();
	panel_1.setBackground(new Color(0, 0, 0));
	panel.add(panel_1, "cell 8 7,grow");

	JPanel table3 = new JPanel();
	table3.setBackground(new Color(102, 51, 0));
	panel.add(table3, "cell 1 8 9 1,grow");

	JPanel table1 = new JPanel();
	table1.setBackground(new Color(102, 51, 0));
	panel.add(table1, "cell 1 9,grow");

	JPanel table2 = new JPanel();
	table2.setBackground(new Color(102, 51, 0));
	panel.add(table2, "cell 9 9,grow");

	tablePanel[0][0] = yellowBlock;
	tablePanel[0][1] = blueBlock;
	tablePanel[0][2] = purpleBlock;
	
//	tablePanel[1][0] = 
	
//	Stack<JPanel> pilha = new Stack<>();
//	pilha.push(yellowBlock);
//	pilha.push(blueBlock);
//	pilha.push(purpleBlock);
//	tableStackPanel.addFirst(pilha);
//	tableStackColor.addFirst(copyOfFirstStack());
//
//	pilha = new Stack<>();
//	pilha.push(greenBlock);
//	pilha.push(grayBlock);
//	pilha.push(redBlock);
//	tableStackPanel.add(pilha);
//	tableStackColor.add(copyOfSecondStack());
//
//	pilha = new Stack<>();
//	pilha.push(panel_1);
//	pilha.push(panel_2);
//	pilha.push(panel_3);
//	tableStackPanel.add(pilha);
//	tableStackColor.add(copyOfThirdStack());

    }

    private Stack<Color> copyOfThirdStack() {
	Stack<Color> pilha = new Stack<Color>();
	pilha.push(Color.black);
	pilha.push(Color.black);
	pilha.push(Color.black);
	return pilha;
    }

    private Stack<Color> copyOfFirstStack() {
	Stack<Color> pilha = new Stack<Color>();
	pilha.push(Color.yellow);
	pilha.push(Color.blue);
	pilha.push(Color.magenta);
	return pilha;
    }

    private Stack<Color> copyOfSecondStack() {
	Stack<Color> pilha = new Stack<Color>();
	pilha.push(Color.green);
	pilha.push(Color.lightGray);
	pilha.push(Color.red);
	return pilha;
    }

    private void addMouseListener(JPanel block) {
	block.addMouseListener(new MouseListener() {

	    @Override
	    public void mouseReleased(MouseEvent e) {
		if (blocoOrigem == null) {
		    blocoOrigem = block;
		    if (Color.black.equals(blocoOrigem.getBackground())) {
			JOptionPane.showMessageDialog(frame, "Selecione o bloco de origem...");
			blocoOrigem = null;
		    }
		} else {
		    blocoDestino = block;
		    move();
		}
	    }

	    private void move() {
		Color background = blocoOrigem.getBackground();
		for (int i = 0; i < 3; i++) {
		    Color color;
		    if (tableStackPanel.get(i).contains(blocoOrigem)) {
			for (int j = 2; j >= 0; j--) {
			    color = tableStackColor.get(i).get(j);
			    if (!color.equals(blocoOrigem.getBackground())) {
				tableStackColor.get(i).set(j, Color.black);
				popBlock(color);
				repaintAll();
			    } else {
				// TODO Mudando a cor do bloco de origem
				tableStackColor.get(i).set(j, Color.black);
				break;
			    }
			}
			break;
		    }
		}
		for (int i = 0; i < 3; i++) {
		    Color color2;
		    if (tableStackPanel.get(i).contains(blocoDestino)) {
			for (int j = 2; j >= 0; j--) {
			    color2 = tableStackColor.get(i).get(j);
			    if (!color2.equals(blocoDestino.getBackground())) {
				tableStackColor.get(i).set(j, Color.black);
				popBlock(color2);
				repaintAll();
			    } else {
				popBlock(blocoDestino.getBackground());
				tableStackColor.get(i).set(j, background);
				repaintAll();
				break;
			    }
			}
		    }
		}
	    }

	    private void repaintAll() {
		for (int k = 0; k < 3; k++) {
		    for (int l = 0; l < 3; l++) {
			tableStackPanel.get(k).get(l).setBackground(tableStackColor.get(k).get(l));
		    }
		}
	    }

	    private void popBlock(Color color) {
		for (int k = 0; k < 3; k++) {
		    if (!tableStackPanel.get(k).contains(blocoDestino) && !tableStackPanel.get(k).contains(blocoOrigem)
			    && tableStackColor.get(k).contains(Color.black)) {
			for (int l = 0; l < 3; l++) {
			    if (tableStackColor.get(k).get(l).equals(Color.black)) {
				tableStackColor.get(k).set(l, color);
				sleep(250);
				break;
			    }
			}
		    }
		}
	    }
	    
	    private void sleep(long time) {
		try {
		    Thread.sleep(time);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	    }

	    @Override
	    public void mouseEntered(MouseEvent e) {
	    }

	    @Override
	    public void mouseClicked(MouseEvent e) {
	    }
	});
    }
}
