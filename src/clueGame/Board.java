package clueGame;

import java.util.Map;

import experiment.TestBoardCell;

public class Board {
	//Instance variables
	private int numCols;
	private int numRows;

	private TestBoardCell[][] board;

	private String layoutConfigFile;
	private String setupConfigFile;

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

	}

	//stub; set config files
	public void setConfigFiles(String csv, String txt) {

	}

	//stub; load config file
	public void loadConfigFile() {

	}

	//stub; load setup config
	public void loadSetupConfig() {

	}

	//stub; load layout config
	public void loadLayoutConfig() {

	}
	
	//stub; returns room given initial
	public Room getRoom(char roomInital) {
		return new Room();
	}

	//stub; returns room given cell
	public Room getRoom(BoardCell cell) {
		return new Room();
	}

	//stub; gets cell
	public BoardCell getCell(int row, int col) {
		return new BoardCell(row, col);
	}
	
	//returns number of columns
	public int getNumColumns() {
		return numCols;
	}
	
	//returns number of rows
	public int getNumRows() {
		return numRows;
	}


}
