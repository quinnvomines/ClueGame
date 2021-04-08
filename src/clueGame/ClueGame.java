package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ClueGame extends JFrame{
	
	public ClueGame(Board b) {
		setSize(700, 650);
		setTitle("Clue Game");
		
		add(b, BorderLayout.CENTER);
		
		GameControlPanel gamePanel = new GameControlPanel();
		add(gamePanel, BorderLayout.SOUTH);
		
		KnownCardsPanel knownPanel = new KnownCardsPanel();
		add(knownPanel, BorderLayout.EAST);
		
	}
	
	public static void main(String [] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.repaint();
		ClueGame cg = new ClueGame(board);
		cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		cg.setVisible(true);
	}
}
