package clueGame;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import experiment.TestBoardCell;

public class Board extends JPanel implements MouseListener{
	//Instance variables
	private int numCols;
	private int numRows;

	private BoardCell[][] board;

	private Solution solution;


	private ArrayList<Card> deck;
	private ArrayList<Player> players;

	private String layoutConfigFile;
	private String setupConfigFile;

	private ArrayList<String[]> playersPre;

	private Player currPlayer;
	private int currPlayerLoc;
	private boolean currPlayerFinished;
	private int currRoll;

	//move targets list
	private Set<BoardCell> targetsList;
	//which cells have been visited by player
	private Set<BoardCell> visitedList;
	private GameControlPanel gameControlPanel;

	private Map<Character, Room> roomMap;

	private boolean winFlag = false;
	private Solution previousGuess;

	//private JDialog suggestionBox;
	//private JComboBox<String> personChoices;
	//private JComboBox<String> weaponChoices;
	private KnownCardsPanel knownCardsPanel;
	private ClueGame clueGame;

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		super() ;
	}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}
	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize()
	{
		try {

			loadSetupConfig(); //Load the setup file (txt)
			loadLayoutConfig(); //Load the layout file (csv)
			addPlayers();
			addMouseListener(this);
			if(players.isEmpty()) {
				return;
			}

			currPlayer = players.get(0);
			currPlayerLoc = 0;
			currPlayerFinished = false;
			Random r = new Random();
			currRoll = r.nextInt(6) + 1;
			calcTargets(board[currPlayer.getRow()][currPlayer.getCol()],currRoll);

		} catch (FileNotFoundException e) {

			e.getMessage();

		} catch (BadConfigFormatException b) {

			b.getMessage();

		}
	}

	//set config files
	public void setConfigFiles(String csv, String txt) {
		//Get data from the data folder
		layoutConfigFile = "data/" + csv;
		setupConfigFile = "data/" + txt;
	}


	//load setup config
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		playersPre = new ArrayList<String[]>();
		roomMap = new HashMap<Character, Room>(); //Initialize map

		deck = new ArrayList<Card>();
		players = new ArrayList<Player>();

		//Variables to prepare reading in from Setup file
		FileReader file = new FileReader(setupConfigFile); 
		Scanner scan = new Scanner(file);

		ArrayList<String[]> input = new ArrayList<String[]>(); //Will hold data from file

		String line;
		while(scan.hasNextLine())
		{
			line = scan.nextLine(); //Read a line from Setup file

			if(!(line.startsWith("//"))) { //Skip if it is a comment
				String[] row = line.split(", "); 
				input.add(row); //Add row of data to ArrayList
			}
		}

		boolean firstPlayer = true;
		for(int i = 0; i < input.size(); i++)
		{

			String name = input.get(i)[1];
			if(input.get(i)[0].equals("Room") || input.get(i)[0].equals("Space"))
			{
				if(input.get(i)[0].equals("Room")) {
					Card tempCard = new Card(name, CardType.ROOM); 
					deck.add(tempCard); //Add Room to the deck of Cards
				}
				//Make a new room and add to map with the corresponding initial
				Room room = new Room(name);
				char charMap = (input.get(i)[2]).charAt(0);
				roomMap.put(charMap, room);
			} else if (input.get(i)[0].equals("Player")) {
				Card tempCard = new Card(name, CardType.PERSON); 
				deck.add(tempCard); //Add Player to deck of Cards

				playersPre.add(input.get(i)); //Add to a temporary players list to add later

			} else if (input.get(i)[0].equals("Weapon")) {
				Card tempCard = new Card(name, CardType.WEAPON);
				deck.add(tempCard); //Add Weapon to deck of Cards
			} else {
				//Throw exception if it is not a Room or a Space
				throw new BadConfigFormatException("Bad format in setup file");
			}
		}


	}

	//load layout config
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException{
		//Variables to prepare file read in
		FileReader file = new FileReader(layoutConfigFile);
		Scanner scan = new Scanner(file);

		ArrayList<String[]> input = new ArrayList<String[]>(); //ArrayList holds data from file

		String line;
		while(scan.hasNextLine()) {
			line = scan.nextLine(); //Read a line from Layout file
			String[] row = line.split(",");
			input.add(row); //Add data to ArrayList
		}

		board = new BoardCell[input.size()][input.get(0).length]; //Initialize board

		numRows = input.size();
		numCols = input.get(0).length;
		for(int i = 0; i < input.size(); i++) {

			//Throw exception if there are missing values 
			if(input.get(i).length != numCols) {
				throw new BadConfigFormatException("Column contains empty values");
			}

			for(int j = 0; j < input.get(i).length; j++) {

				BoardCell cell = new BoardCell(i, j, input.get(i)[j]); //new BoardCell

				//Check if roomMap has initial read in from file
				if(!(roomMap.containsKey(cell.getInitial()))) {
					//Throws Exception if initial isn't valid
					throw new BadConfigFormatException("Cell initial is not a room");
				}
				else {
					if(cell.isRoomCenter()) //Check if it is a center
					{
						//Set as center cell 
						char init = cell.getInitial();
						roomMap.get(init).setCenterCell(cell);
					}
					else if(cell.isLabel()) //Check if it is a label
					{
						//Set as label cell
						char init = cell.getInitial();
						roomMap.get(init).setLabelCell(cell);
					}
					board[i][j] = cell; //Add to board
				}
			}
		}

		//fill out adjacency
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				//gets all walkways.
				BoardCell currLoc = board[row][col];
				if(!(currLoc.isRoom() || currLoc.isUnused())) {
					//If it is a doorway
					if(currLoc.isDoorway()) { //If doorway connect adjacencies between door and room center

						if(currLoc.getDoorDirection() == DoorDirection.UP) {
							//Check if doorway points out of the board
							if(row - 1 < 0) {
								throw new BadConfigFormatException("Doorway points outside of map");
							}

							BoardCell cellTop = board[row - 1][col];
							char upChar = cellTop.getInitial();
							currLoc.addAdjacency(getCenter(upChar));
							getCenter(upChar).addAdjacency(currLoc);

						} else if(currLoc.getDoorDirection() == DoorDirection.DOWN) {
							//Check if doorway points out of the board
							if(row + 1 >= numRows) {
								throw new BadConfigFormatException("Doorway points outside of map");
							}

							BoardCell cellBottom = board[row + 1][col];
							char downChar = cellBottom.getInitial();
							currLoc.addAdjacency(getCenter(downChar));
							getCenter(downChar).addAdjacency(currLoc);

						} else if(currLoc.getDoorDirection() == DoorDirection.RIGHT) {
							//Check if doorway points out of the board
							if(col + 1 >= numCols) {
								throw new BadConfigFormatException("Doorway points outside of map");
							}

							BoardCell cellRight = board[row][col + 1];
							char rightChar = cellRight.getInitial();
							currLoc.addAdjacency(getCenter(rightChar));
							getCenter(rightChar).addAdjacency(currLoc);

						} else if(currLoc.getDoorDirection() == DoorDirection.LEFT) {
							//Check if doorway points out of the board
							if(col - 1 < 0) {
								throw new BadConfigFormatException("Doorway points outside of map");
							}

							BoardCell cellLeft = board[row][col - 1];
							char leftChar = cellLeft.getInitial();
							currLoc.addAdjacency(getCenter(leftChar));
							getCenter(leftChar).addAdjacency(currLoc);

						}
					}
					//Check and add top neighbor
					if(row - 1 >= 0) {
						BoardCell cellTop = board[row - 1][col];
						if(!(cellTop.isRoom() || cellTop.isUnused())) {
							currLoc.addAdjacency(cellTop);
						}
					}
					//Check and add left neighbor
					if(col - 1 >= 0) {
						BoardCell cellLeft = board[row][col - 1];
						if(!(cellLeft.isRoom() || cellLeft.isUnused())) {
							currLoc.addAdjacency(cellLeft);
						}
					}
					//Check and add right neighbor
					if(col + 1 < numCols) {
						BoardCell cellRight = board[row][col + 1];
						if(!(cellRight.isRoom() || cellRight.isUnused())) {
							currLoc.addAdjacency(cellRight);
						}
					}
					//Check and add bottom neighbor
					if(row + 1 < numRows) {
						BoardCell cellBottom = board[row + 1][col];
						if(!(cellBottom.isRoom() || cellBottom.isUnused())) {
							currLoc.addAdjacency(cellBottom);
						}
					}
				}
				//Add secret passage adjacency
				if(currLoc.isSecretPassage()) {
					BoardCell secretPassageCenter = getCenter(currLoc.getSecretPassage());
					getCenter(currLoc.getInitial()).addAdjacency(secretPassageCenter);
				}
			}
		}

	}

	private void addPlayers() {
		//Add players 
		for(int i = 0; i < playersPre.size(); i++) {
			Player tempPlayer;
			String name = playersPre.get(i)[1];
			String color = playersPre.get(i)[4];
			String rowPlayer = playersPre.get(i)[2];
			String colPlayer = playersPre.get(i)[3];

			if(i == 0) {
				tempPlayer = new HumanPlayer(name, color, Integer.parseInt(rowPlayer), Integer.parseInt(colPlayer), this);
			} else {
				tempPlayer = new ComputerPlayer(name, color, Integer.parseInt(rowPlayer), Integer.parseInt(colPlayer), this);
			}

			board[Integer.parseInt(rowPlayer)][Integer.parseInt(colPlayer)].setOccupied(true);
			players.add(tempPlayer); //Add Player to deck of Cards

		}
	}

	private BoardCell getCenter(char initial) {
		return roomMap.get(initial).getCenterCell();
	}

	//figure out what locations the player can move to
	public void calcTargets(BoardCell startCell, int pathLength) {
		visitedList = new HashSet<BoardCell>();
		targetsList = new HashSet<BoardCell>();

		visitedList.add(startCell);
		findAllTargets(startCell,pathLength);
	}

	public void setCurrPlayerFinished(boolean currPlayerFinished) {
		this.currPlayerFinished = currPlayerFinished;
	}
	//recursive function to find the locations the player can move to
	public void findAllTargets(BoardCell thisCell,int numSteps) {
		for (BoardCell c : thisCell.getAdjList()) {
			if(!(visitedList.contains(c)) && (!(c.getOccupied()) || c.isRoomCenter()))
			{

				visitedList.add(c);
				if((numSteps == 1 || c.isRoom()))
				{
					if(currPlayer instanceof HumanPlayer) {
						c.setTarget(true);
						if(c.isRoom()) {
							//loop through roomMap
							for(int row = 0; row < numRows; row++) {
								for(int col = 0; col < numCols; col++) {
									if(board[row][col].getInitial() == c.getInitial()) {
										board[row][col].setTarget(true);
									}
								}
							}
						}
					}
					targetsList.add(c);
				}
				else
				{
					findAllTargets(c,numSteps - 1);
				}
				visitedList.remove(c);
			}

		}
	}

	public void deal() {
		//Split the deck into different ArrayLists
		ArrayList<Card> playersLeft = new ArrayList<Card>();
		ArrayList<Card> weaponsLeft = new ArrayList<Card>();
		ArrayList<Card> roomsLeft = new ArrayList<Card>();
		for(int i = 0; i < deck.size(); i++) {
			if(deck.get(i).getType() == CardType.PERSON) {
				playersLeft.add(deck.get(i));
			} else if(deck.get(i).getType() == CardType.ROOM) {
				roomsLeft.add(deck.get(i));
			} else if(deck.get(i).getType() == CardType.WEAPON) {
				weaponsLeft.add(deck.get(i));
			}
		}

		//Get random position from each 
		Random r = new Random();
		int indexPlayerSol = r.nextInt(playersLeft.size());
		int indexRoomSol = r.nextInt(roomsLeft.size());
		int indexWeaponSol = r.nextInt(weaponsLeft.size());

		//Add to solution
		solution = new Solution(playersLeft.get(indexPlayerSol), roomsLeft.get(indexRoomSol), 
				weaponsLeft.get(indexWeaponSol));
	
		
		//Remove from options
		playersLeft.remove(indexPlayerSol);
		roomsLeft.remove(indexRoomSol);
		weaponsLeft.remove(indexWeaponSol);

		//Recombine the lists 
		ArrayList<Card> dealDeck = new ArrayList<Card>();
		dealDeck.addAll(playersLeft);
		dealDeck.addAll(roomsLeft);
		dealDeck.addAll(weaponsLeft);

		//While deal deck is not empty, hand it out to players
		int playerNum = 0;
		while(!dealDeck.isEmpty()) {
			int randomSpot = r.nextInt(dealDeck.size()); //Get random card from deal deck
			Card tempCard = dealDeck.get(randomSpot);
			dealDeck.remove(randomSpot);
			players.get(playerNum).updateHand(tempCard); //Add to player hand
			players.get(playerNum).updateSeen(tempCard); //Add to seen list

			if(playerNum == players.size() - 1) {
				playerNum = 0;
			} else {
				playerNum++;
			}
		}

	}

	public void setAnswer(Card person, Card room, Card weapon) {
		solution = new Solution(person, room, weapon);
	}

	//Check check if accusation is correct
	public boolean checkAccusation(Solution accuse) {
		if(solution.equals(accuse)) {
			return true;
		}
		return false;
	}

	//Handle suggestion 
	//while loop that loops through players list checks at size of players list, go back to beginning
	//Condition for while loop, while not at location, then increment
	//If card matches suggestion, then break out of loop and return the Card
	public Card handleSuggestion(int location, Solution suggestion) {
		int i = (location + 1) % players.size();
		while(i != location) {

			if(players.get(i).disproveSuggestion(suggestion) != null) {
				return players.get(i).disproveSuggestion(suggestion);
			}

			i++;
			if(i == players.size()) {
				i = 0;
			}
		}
		return null;
	}

	//Draws each individual cell
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int boardWidth = this.getWidth();
		int boardHeight = this.getHeight();
		double cellWidth = boardWidth / numCols;
		double cellHeight = boardHeight / numRows;
		double startRowLoc = 0;
		double startColLoc = 0;
		for(int rows = 0; rows < numRows; rows++) {
			for(int cols = 0; cols < numCols; cols++) {
				board[rows][cols].draw(g, cellHeight, cellWidth, startRowLoc, startColLoc);
				startColLoc = startColLoc + cellWidth; //Move to next cell in column
			}
			startColLoc = 0; //Set column to 0
			startRowLoc = startRowLoc + cellHeight; //Move to next cell in row
		}

		//Add doorways
		startRowLoc = 0;
		startColLoc = 0;
		for(int rows = 0; rows < numRows; rows++) {
			for(int cols = 0; cols < numCols; cols++) {
				if(board[rows][cols].isDoorway()) {
					g.setColor(Color.BLUE); //Doorway color

					switch(board[rows][cols].getDoorDirection()) {
					case UP:
						//Make doorway pointing up
						g.fillRect((int) startColLoc, (int) (startRowLoc - 1.0/5 * cellHeight + 1), (int) cellWidth, (int) ((1.0/5) * cellHeight));
						break;
					case LEFT:
						//Make doorway pointing left
						g.fillRect((int) (startColLoc - (1.0/4) * cellWidth + 1), (int) startRowLoc, (int) ((1.0/4) * cellWidth), (int) cellHeight);
						break;
					case RIGHT:
						//Make doorway pointing right
						g.fillRect((int) (startColLoc + cellWidth), (int) startRowLoc, (int) ((1.0/4) * cellWidth), (int) cellHeight);
						break;
					case DOWN:
						//Make doorway pointing down
						g.fillRect((int) startColLoc, (int) (startRowLoc + cellHeight), (int) cellWidth, (int) ((1.0/4) * cellHeight));
						break;
					default:
						//No doorway
						break;
					}
				}
				startColLoc = startColLoc + cellWidth; //Move to next cell in column
			}
			startColLoc = 0; //Set column to 0
			startRowLoc = startRowLoc + cellHeight; //Move to next cell in row
		}

		//Draw room labels
		startRowLoc = 0;
		startColLoc = 0;
		for(int rows = 0; rows < numRows; rows++) {
			for(int cols = 0; cols < numCols; cols++) {
				if(board[rows][cols].isLabel()) {
					roomMap.get(board[rows][cols].getInitial()).draw(g, cellHeight,startRowLoc, startColLoc);
				}
				startColLoc = startColLoc + cellWidth; //Move to next cell in column
			}
			startColLoc = 0; //Set column to 0
			startRowLoc = startRowLoc + cellHeight; //Move to next cell in row
		}

		//Draw players
		startRowLoc = 0;
		startColLoc = 0;
		for(int rows = 0; rows < numRows; rows++) {
			for(int cols = 0; cols < numCols; cols++) {
				for(int i = 0; i < players.size(); i++) {
					if(players.get(i).getRow() == rows && players.get(i).getCol() == cols) {
						//Draw player
						players.get(i).draw(g, cellWidth, cellHeight, startRowLoc, startColLoc);
					}
				}
				startColLoc = startColLoc + cellWidth; //Move to next cell in column
			}
			startColLoc = 0; //Set column to 0
			startRowLoc = startRowLoc + cellHeight; //Move to next cell in row
		}

	}
	//next pressed
	public void nextPressed() {
		//Advances to next player
		if(currPlayerLoc == players.size() - 1)
		{
			currPlayer = players.get(0);
			currPlayerLoc = 0;
		}
		else {
			currPlayer = players.get(currPlayerLoc + 1);
			currPlayerLoc = currPlayerLoc + 1;
		}

		//Generate roll 
		Random r = new Random();
		currRoll = r.nextInt(6) + 1;
		if(currPlayer instanceof HumanPlayer) {
			//Calculate targets
			calcTargets(board[currPlayer.getRow()][currPlayer.getCol()], currRoll);
			currPlayerFinished = false; //Flag 
		}
		else {

			BoardCell cell = ((ComputerPlayer) currPlayer).selectTargets(currRoll, this);
			//Return if no targets
			if(cell == null) {
				return;
			}
			if(winFlag) {
				if(checkAccusation(previousGuess)) {
					JOptionPane.showMessageDialog(null, "You lose");
					clueGame.dispose();
				}
				else {
					winFlag = false;
				}
			}
			//Move ComputerPlayer
			board[currPlayer.getRow()][currPlayer.getCol()].setOccupied(false);
			currPlayer.setRow(cell.getRow());
			currPlayer.setCol(cell.getColumn());
			board[currPlayer.getRow()][currPlayer.getCol()].setOccupied(true);
			currPlayer.updateRoom();
			if(currPlayer.getRoom() != null) {
				Solution newSuggestion = ((ComputerPlayer)currPlayer).createSuggestion();
				previousGuess = newSuggestion;
				currPlayer.updateSeen(handleSuggestion(currPlayerLoc, newSuggestion));
				for(int i = 0; i < players.size(); i++) {
					if(players.get(i).getName().equals(newSuggestion.getPerson().getCardName())) {
						board[players.get(i).getRow()][players.get(i).getCol()].setOccupied(false);
						players.get(i).setRow(cell.getRow());
						players.get(i).setCol(cell.getColumn());
						board[players.get(i).getRow()][players.get(i).getCol()].setOccupied(true);
						players.get(i).updateRoom();
						break;
					}
				}
				Card c = handleSuggestion(currPlayerLoc, newSuggestion);
				if(c == null) {
					winFlag = true;
				} else {
					currPlayer.updateSeen(c);
				}


			}
			else {
				previousGuess = null;
			}

		}
		repaint();

	}

	public boolean isWinFlag() {
		return winFlag;
	}
	public void setPreviousGuess(Solution previousGuess) {
		this.previousGuess = previousGuess;
	}
	public boolean isCurrPlayerFinished() {
		return currPlayerFinished;
	}
	//returns room given initial
	public Room getRoom(char roomInitial) {
		return roomMap.get(roomInitial);
	}

	//returns room given cell
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	//Gets cell given a row and column
	public BoardCell getCell(int row, int col) {
		if(row >= numRows || col >= numCols) {	
			return null; //Return null if not a valid row or column
		}
		return board[row][col];
	}

	//returns number of columns
	public int getNumColumns() {
		return numCols;
	}

	//returns number of rows
	public int getNumRows() {
		return numRows;
	}

	//get adjacent list
	public Set<BoardCell> getAdjList(int i, int j) {
		return board[i][j].getAdjList();
	}

	//get targets
	public Set<BoardCell> getTargets() {
		return targetsList;
	}

	//Get player list
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	//Get deck
	public ArrayList<Card> getDeck() {
		return deck;
	}

	public Map<Character, Room> getRoomMap() {
		return roomMap;
	}
	public Player getCurrPlayer() {
		return currPlayer;
	}
	public int getCurrRoll() {
		return currRoll;
	}

	public Solution getPreviousGuess() {
		return previousGuess;
	}
	
	public void passKnownCardsPanel(KnownCardsPanel k) {
		knownCardsPanel = k;
	}
	
	public void passGameControlPanel(GameControlPanel gp) {
		gameControlPanel = gp;
	}
	
	public void passClueGame(ClueGame cg) {
		clueGame = cg;
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {
		//If targets list empty, inform player
		if(targetsList.isEmpty() && currPlayer instanceof HumanPlayer) {
			currPlayerFinished = true;
			JOptionPane.showMessageDialog(null, "Current player has no targets! Click NEXT to continue the game");
			return;
		}
		if(currPlayer instanceof HumanPlayer) {
			for(int row = 0; row < numRows; row++) {
				for(int col = 0; col < numCols; col++) {
					if(board[row][col].containsClick(e.getX(),e.getY()) && board[row][col].isFlagTarget()) {
						//move player
						board[currPlayer.getRow()][currPlayer.getCol()].setOccupied(false);
						if(board[row][col].isRoom()) { //Check if in room
							Room testRoom = roomMap.get(board[row][col].getInitial());
							currPlayer.setRow(testRoom.getCenterCell().getRow());
							currPlayer.setCol(testRoom.getCenterCell().getColumn());
						}
						else {
							currPlayer.setRow(row);
							currPlayer.setCol(col);
						}
						currPlayer.updateRoom(); //Update room (null if not in one)
						board[currPlayer.getRow()][currPlayer.getCol()].setOccupied(true);

						//Check all cell flags to false
						for(int i = 0; i < numRows; i++) {
							for(int j = 0; j < numCols; j++) {
								board[i][j].setTarget(false);
							}
						}
						repaint();
						//check if in room and handle suggestion
						if(board[currPlayer.getRow()][currPlayer.getCol()].isRoom()) {
							JDialog suggestionBox = new JDialog();
							suggestionBox.setLayout(new GridLayout(4, 2));
							suggestionBox.setSize(500, 250); 
							suggestionBox.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							suggestionBox.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
							suggestionBox.setTitle("Make a Suggestion");

							JLabel currentRoomLabel = new JLabel("Current room");
							suggestionBox.add(currentRoomLabel);

							JTextField currentRoomTextField = new JTextField(roomMap.get(board[currPlayer.getRow()][currPlayer.getCol()].getInitial()).getName());
							currentRoomTextField.setEditable(false);
							suggestionBox.add(currentRoomTextField);

							JLabel currentPersonLabel = new JLabel("Person");
							suggestionBox.add(currentPersonLabel);

							JComboBox<String> personChoices = new JComboBox<String>();
							for(int i = 0; i < players.size(); i++) {
								personChoices.addItem(players.get(i).getName());
							}
							suggestionBox.add(personChoices);

							JLabel currentWeaponLabel = new JLabel("Weapon");
							suggestionBox.add(currentWeaponLabel);

							JComboBox<String> weaponChoices = new JComboBox<String>();
							for(int i = 0; i < deck.size(); i++) {
								if(deck.get(i).getType() == CardType.WEAPON) {
									weaponChoices.addItem(deck.get(i).getCardName());
								}
							}
							suggestionBox.add(weaponChoices);

							JButton submitButton = new JButton("Submit");
							suggestionBox.add(submitButton);

							JButton cancelButton = new JButton("Cancel");
							suggestionBox.add(cancelButton);
							
							class SubmitButtonListener implements ActionListener{
								public void actionPerformed(ActionEvent e) {
									String roomChoice = roomMap.get(board[currPlayer.getRow()][currPlayer.getCol()].getInitial()).getName();
									String personChoice = (String) personChoices.getSelectedItem();
									String weaponChoice = (String) weaponChoices.getSelectedItem();
									
									Card r = null;
									Card p = null;
									Card w = null;
									for(int i = 0; i < deck.size(); i++) {
										if(deck.get(i).getCardName().equals(roomChoice)) {
											r = deck.get(i);
										}
										else if(deck.get(i).getCardName().equals(personChoice)) {
											p = deck.get(i);
										}
										else if(deck.get(i).getCardName().equals(weaponChoice)) {
											w = deck.get(i);
										}
									}
									//Move person choice
									for(int i = 0; i < players.size(); i++) {
										if(players.get(i).getName().equals(personChoice)) {
											board[players.get(i).getRow()][players.get(i).getCol()].setOccupied(false);
											players.get(i).setRow(currPlayer.getRow());
											players.get(i).setCol(currPlayer.getCol());
											board[players.get(i).getRow()][players.get(i).getCol()].setOccupied(true);
											players.get(i).updateRoom();
											break;
										}
									}
									repaint();
									
									Solution humanSuggestion = new Solution(p,r,w);
									currPlayer.updateSeen(handleSuggestion(0, humanSuggestion));
									knownCardsPanel.updatePanel(currPlayer.getHand(),currPlayer.getSeen());
									knownCardsPanel.revalidate();
									suggestionBox.setVisible(false);
									
									//TODO: update the guess result and guess 
									gameControlPanel.setGuess(humanSuggestion.toString());
									if(handleSuggestion(0, humanSuggestion) != null) {
										gameControlPanel.setGuessResult("Suggestion disproven");
									}
									else {
										gameControlPanel.setGuessResult("Suggestion correct");
									}
									gameControlPanel.revalidate();
								}

							}
							
							class CancelButtonListener implements ActionListener{
								public void actionPerformed(ActionEvent e) {
									suggestionBox.setVisible(false);
								}

							}
							
							submitButton.addActionListener(new SubmitButtonListener());
							cancelButton.addActionListener(new CancelButtonListener());
							suggestionBox.setVisible(true);
							
						}

						currPlayerFinished = true;
						return;
					}
				}
			}
			JOptionPane.showMessageDialog(null, "Must click on a target!");
		}
	}
}
