package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {

	private JTextField turn;
	private JTextField roll;
	private JTextField guess;
	private JTextField guessResult;
	private Board board;

	/**
	 * Constructor for the panel, it does 90% of the work
	 */
	public GameControlPanel(Board b)  {
		setLayout(new GridLayout(2, 0));

		makeTopPanel(); //Top panel
		makeBottomPanel(); //Bottom panel

		board = b;
	}

	//Helper function
	private void makeTopPanel() {
		//Make top panel
		JPanel panel = new JPanel(); 
		panel.setLayout(new GridLayout(1, 4));
		add(panel);

		JPanel panel_1 = new JPanel(); //Panel to contain JLabel and JTextfield for turn
		panel_1.setLayout(new GridLayout(2, 1));
		JPanel panel_2 = new JPanel(); //Panel to contain JLabel and JTextfield for roll
		panel_2.setLayout(new GridLayout(2, 1));
		JButton button1 = new JButton("Make Accusation");
		JButton button2 = new JButton("NEXT!");

		//Add to top panel
		panel.add(panel_1);
		panel.add(panel_2);
		panel.add(button1); //Accusation button
		panel.add(button2); //NEXT button

		//Turn
		JLabel turnMessage = new JLabel("Whose turn?");
		turn = new JTextField(20);
		turn.setEditable(false);
		panel_1.add(turnMessage);
		panel_1.add(turn);

		//Roll
		JLabel rollMessage = new JLabel("Roll:");
		roll = new JTextField(20);
		roll.setEditable(false);
		panel_2.add(rollMessage);
		panel_2.add(roll);

		//Add ActionListener for NEXT button
		button2.addActionListener(new NextButtonListener());

	}

	//Helper function
	private void makeBottomPanel() {
		//Make bottom panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));
		add(panel);

		//Panels to add to bottom panel
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new GridLayout(1, 0));
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new GridLayout(1, 0));

		//Allocate space for text fields
		guess = new JTextField(20);
		guess.setEditable(false);
		guessResult = new JTextField(20);
		guessResult.setEditable(false);

		//Add panels to bottom panel
		panel.add(panel_1);
		panel.add(panel_2);

		//Add JTextFields to appropriate panels
		panel_1.add(guess);
		panel_2.add(guessResult);

		//Borders
		panel_1.setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
		panel_2.setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));

	}

	private class NextButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Check flag
			if(!board.isCurrPlayerFinished()) {
				JOptionPane.showMessageDialog(null, "Current player has not finished with his/her turn!");
			}
			else {
				board.nextPressed(); //Process next pressed
				setTurn(board.getCurrPlayer(), board.getCurrRoll()); //Set game panel
				board.repaint();
			}
		}
	}

	//Sets turn and roll number
	public void setTurn(Player player, int rollNum) {
		turn.setText(player.getName());
		roll.setText(Integer.toString(rollNum));
	}

	//Set guess
	public void setGuess(String g) {
		guess.setText(g);
	}

	//Set guess result
	public void setGuessResult(String gr) {
		guessResult.setText(gr);
	}

	/**
	 * Main to test the panel
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		
		GameControlPanel panel = new GameControlPanel(board);  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(1110, 130);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible



		// test filling in the data
		panel.setTurn(new ComputerPlayer( "Col. Mustard", "orange", 0, 0, board), 5);
		panel.setGuess( "I have no guess! ");
		panel.setGuessResult( "So you have nothing? ");


	}
	
}
