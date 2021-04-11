package clueGame;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame{

	GameControlPanel gamePanel;
	
	public ClueGame(Board b) {

		setSize(810, 650); //Size
		setTitle("Clue Game"); //Title

		b.repaint(); //PAINT


		//Add the Board panel to the center of the window
		add(b, BorderLayout.CENTER);

		//Game control panel
		gamePanel = new GameControlPanel(b);
		add(gamePanel, BorderLayout.SOUTH);

		//Test update game panel for first player in ClueSetup.txt
		gamePanel.setTurn(b.getPlayers().get(0), 0);

		//Known cards panel
		KnownCardsPanel knownPanel = new KnownCardsPanel();
		add(knownPanel, BorderLayout.EAST);

		//Test update known cards panel for first player in ClueSetup.txt 
		knownPanel.updatePanel(b.getPlayers().get(0).getHand(), new ArrayList<Card>()); 

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
		cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		cg.setVisible(true);

		JOptionPane.showMessageDialog(cg, "You are " + b.getPlayers().get(0).getName() + ". \nCan you"
				+ " find the solution\nbefore the Computer players?", "Welcome to Clue", JOptionPane.PLAIN_MESSAGE);
	}
}
