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
				BoardCell currLoc = board[row][col];
				if(!(currLoc.isRoom() || currLoc.isUnused())) {
					//If it is a doorway
					if(currLoc.isDoorway()) { //If doorway connect adjacencies between door and room center
						BoardCell cellTop = board[row - 1][col];
						BoardCell cellLeft = board[row][col - 1];
						BoardCell cellBottom = board[row + 1][col];
						BoardCell cellRight = board[row][col + 1];
						if(currLoc.getDoorDirection() == DoorDirection.UP) {
							char upChar = cellTop.getInitial();
							currLoc.addAdjacency(getCenter(upChar));
							getCenter(upChar).addAdjacency(currLoc);
							
						} else if(currLoc.getDoorDirection() == DoorDirection.DOWN) {
							
							char downChar = cellBottom.getInitial();
							currLoc.addAdjacency(getCenter(downChar));
							getCenter(downChar).addAdjacency(currLoc);
							
						} else if(currLoc.getDoorDirection() == DoorDirection.RIGHT) {
							
							char rightChar = cellRight.getInitial();
							currLoc.addAdjacency(getCenter(rightChar));
							getCenter(rightChar).addAdjacency(currLoc);
							
						} else if(currLoc.getDoorDirection() == DoorDirection.LEFT) {
							
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
