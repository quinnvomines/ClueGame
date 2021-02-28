package clueGame;

import java.util.Map;

import experiment.TestBoardCell;

public class Board {
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
     
     public void setConfigFiles(String csv, String txt) {
    	 
     }
     
     public void loadConfigFile() {
    	 
     }
     
     public void loadSetupConfig() {
    	 
     }
     
     public void loadLayoutConfig() {
    	 
     }
	
	public Room getRoom(char roomInital) {
		return new Room();
	}

	public Room getRoom(BoardCell cell) {
		return new Room();
	}
	
	public BoardCell getCell(int row, int col) {
		return new BoardCell(row, col);
	}
	public int getNumColumns() {
		// TODO Auto-generated method stub
		return numCols;
	}
	public int getNumRows() {
		// TODO Auto-generated method stub
		return numRows;
	}
	
	
}
