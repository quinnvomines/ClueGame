package clueGame;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame{

	private GameControlPanel gamePanel;
	
	public ClueGame(Board b) {

		setSize(810, 650); //Size
		setTitle("Clue Game"); //Title

		b.repaint(); //PAINT


		//Add the Board panel to the center of the window
		add(b, BorderLayout.CENTER);

		//Game control panel
		gamePanel = new GameControlPanel(b, this);
		add(gamePanel, BorderLayout.SOUTH);
		b.passGameControlPanel(gamePanel);

		//Known cards panel
		KnownCardsPanel knownPanel = new KnownCardsPanel(b);
		add(knownPanel, BorderLayout.EAST);
		b.passKnownCardsPanel(knownPanel);
		
		knownPanel.updatePanel(b.getPlayers().get(0).getHand(), b.getPlayers().get(0).getSeen());
	}
	
	public void setInitialTurn(Player p, int l) {
		gamePanel.setTurn(p,l);
	}

	
public static void main(String [] args) {
		//Set up board
		Board b = Board.getInstance();
		b.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		b.initialize();

		b.deal();
		
		ClueGame cg = new ClueGame(b);
		cg.setInitialTurn(b.getCurrPlayer(),b.getCurrRoll());
		b.passClueGame(cg);
		cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		cg.setVisible(true);
		
		JOptionPane.showMessageDialog(cg, "You are " + b.getPlayers().get(0).getName() + ". \nCan you"
				+ " find the solution\nbefore the Computer players?", "Welcome to Clue", JOptionPane.PLAIN_MESSAGE);
	}
}
