package clueGame;

import java.awt.Color;
import java.awt.Graphics;
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

import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	Set<BoardCell> visitedList;
	GameControlPanel gameConPan;

	private Map<Character, Room> roomMap;

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
					if(currPlayer instanceof HumanPlayer)
						c.setTarget(true);
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
		int i = location + 1 % players.size();
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
		if(currPlayerLoc == players.size() - 1)
		{
			currPlayer = players.get(0);
			currPlayerLoc = 0;
		}
		else {
			currPlayer = players.get(currPlayerLoc + 1);
			currPlayerLoc = currPlayerLoc + 1;
		}
		Random r = new Random();
		currRoll = r.nextInt(6) + 1;
		if(currPlayer instanceof HumanPlayer) {
			calcTargets(board[currPlayer.getRow()][currPlayer.getCol()], currRoll);
			currPlayerFinished = false;
		}
		else {
			BoardCell cell = ((ComputerPlayer) currPlayer).selectTargets(currRoll, this);
			board[currPlayer.getRow()][currPlayer.getCol()].setOccupied(false);
			currPlayer.setRow(cell.getRow());
			currPlayer.setCol(cell.getColumn());
			board[currPlayer.getRow()][currPlayer.getCol()].setOccupied(true);
		}

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

	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {
		if(currPlayer instanceof HumanPlayer) {
			for(BoardCell c : targetsList) {
				if(c.containsClick(e.getX(),e.getY())){
					//move player
					board[currPlayer.getRow()][currPlayer.getCol()].setOccupied(false);
					currPlayer.setRow(c.getRow());
					currPlayer.setCol(c.getColumn());
					board[currPlayer.getRow()][currPlayer.getCol()].setOccupied(true);
					for(BoardCell bc : targetsList) {
						bc.setTarget(false);
					}
					repaint();
					//check if in room and handle suggestion
					currPlayerFinished = true;
					return;
				}
			}
			JOptionPane.showMessageDialog(null, "Must click on a target!");
		}	
	}
}
