package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ClueGame extends JFrame{
	
	public ClueGame(Board b) {
		setSize(700, 650); //Size
		setTitle("Clue Game"); //Title
		
		//Add the Board panel to the center of the window
		add(b, BorderLayout.CENTER);
		
		//Game control panel
		GameControlPanel gamePanel = new GameControlPanel();
		add(gamePanel, BorderLayout.SOUTH);
		
		//Known cards panel
		KnownCardsPanel knownPanel = new KnownCardsPanel();
		add(knownPanel, BorderLayout.EAST);
		
	}
	
	public static void main(String [] args) {
		//Set up board
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		
		board.repaint(); //PAINT
		
		ClueGame cg = new ClueGame(board);
		cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		cg.setVisible(true);
	}
}
