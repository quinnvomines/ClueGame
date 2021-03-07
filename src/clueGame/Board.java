package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import experiment.TestBoardCell;

public class Board {
	//Instance variables
	private int numCols;
	private int numRows;

	private BoardCell[][] board;

	private String layoutConfigFile;
	private String setupConfigFile;

	//move targets list
	private Set<BoardCell> targetsList;
	//which cells have been visited by player
	Set<BoardCell> visitedList;

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

		} catch (FileNotFoundException e) {

			e.getMessage();

		} catch (BadConfigFormatException b) {

			b.getMessage();

		}
	}

	//stub; set config files
	public void setConfigFiles(String csv, String txt) {
		layoutConfigFile = csv;
		setupConfigFile = txt;
	}


	//load setup config
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {

		roomMap = new HashMap<Character, Room>(); //Initialize map

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

		for(int i = 0; i < input.size(); i++)
		{

			if(input.get(i)[0].equals("Room") || input.get(i)[0].equals("Space"))
			{
				//Make a new room and add to map with the corresponding initial
				Room room = new Room(input.get(i)[1]);
				char charMap = (input.get(i)[2]).charAt(0);
				roomMap.put(charMap, room);
			}
			else {
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
				if(!(board[row][col].isRoom() || board[row][col].isUnused())) {
					//If it is a doorway
					if(board[row][col].isDoorway()) { //If doorway connect adjacencies between door and room center
						if(board[row][col].getDoorDirection() == DoorDirection.UP) {
							board[row][col].addAdjacency(roomMap.get(board[row - 1][col].getInitial()).getCenterCell());
							roomMap.get(board[row - 1][col].getInitial()).getCenterCell().addAdjacency(board[row][col]);
						} else if(board[row][col].getDoorDirection() == DoorDirection.DOWN) {
							board[row][col].addAdjacency(roomMap.get(board[row + 1][col].getInitial()).getCenterCell());
							roomMap.get(board[row + 1][col].getInitial()).getCenterCell().addAdjacency(board[row][col]);
						} else if(board[row][col].getDoorDirection() == DoorDirection.RIGHT) {
							board[row][col].addAdjacency(roomMap.get(board[row][col + 1].getInitial()).getCenterCell());
							roomMap.get(board[row][col + 1].getInitial()).getCenterCell().addAdjacency(board[row][col]);
						} else if(board[row][col].getDoorDirection() == DoorDirection.LEFT) {
							board[row][col].addAdjacency(roomMap.get(board[row][col - 1].getInitial()).getCenterCell());
							roomMap.get(board[row][col - 1].getInitial()).getCenterCell().addAdjacency(board[row][col]);
						}
					}
					//Check and add left neighbor
					if(row - 1 >= 0) {
						if(!(board[row - 1][col].isRoom() || board[row - 1][col].isUnused())) {
							board[row][col].addAdjacency(board[row - 1][col]);
						}
					}
					//Check and add top neighbor
					if(col - 1 >= 0) {
						if(!(board[row][col - 1].isRoom() || board[row][col - 1].isUnused())) {
							board[row][col].addAdjacency(board[row][col - 1]);
						}
					}
					//Check and add right neighbor
					if(col + 1 < numCols) {
						if(!(board[row][col + 1].isRoom() || board[row][col + 1].isUnused())) {
							board[row][col].addAdjacency(board[row][col + 1]);
						}
					}
					//Check and add bottom neighbor
					if(row + 1 < numRows) {
						if(!(board[row + 1][col].isRoom() || board[row + 1][col].isUnused())) {
							board[row][col].addAdjacency(board[row + 1][col]);
						}
					}
				}
				if(board[row][col].isSecretPassage()) {
					roomMap.get(board[row][col].getInitial()).getCenterCell().addAdjacency(roomMap.get(board[row][col].getSecretPassage()).getCenterCell());
				}
			}
		}
	}

	//figure out what locations the player can move to
	public void calcTargets(BoardCell startCell, int pathLength) {
		visitedList = new HashSet<BoardCell>();
		targetsList = new HashSet<BoardCell>();

		visitedList.add(startCell);
		findAllTargets(startCell,pathLength);
	}

	//recursive function to find the locations the player can move to
	public void findAllTargets(BoardCell thisCell,int numSteps) {
		for (BoardCell c : thisCell.getAdjList()) {
			if(!(visitedList.contains(c)) && (!(c.getOccupied()) || c.isRoomCenter()))
			{

				visitedList.add(c);
				if((numSteps == 1 || c.isRoom()))
				{
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


}
