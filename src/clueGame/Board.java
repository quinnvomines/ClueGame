package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import experiment.TestBoardCell;

public class Board {
	//Instance variables
	private int numCols;
	private int numRows;

	private BoardCell[][] board;

	private String layoutConfigFile;
	private String setupConfigFile;

	private Map<Character, Room> roomMap = new HashMap<Character, Room>();

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
			loadSetupConfig();
			loadLayoutConfig();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BadConfigFormatException b) {
			b.getMessage();
		}
	}

	//stub; set config files
	public void setConfigFiles(String csv, String txt) {
		layoutConfigFile = csv;
		setupConfigFile = txt;
	}


	//stub; load setup config
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {

		FileReader file = new FileReader(setupConfigFile);
		Scanner scan = new Scanner(file);
		ArrayList<String[]> input = new ArrayList<String[]>();
		String line;
		while(scan.hasNextLine())
		{
			line = scan.nextLine();
			if(!(line.startsWith("//"))) {
				String[] row = line.split(", ");
				input.add(row);
			}
		}

		for(int i = 0; i < input.size(); i++)
		{
			if(input.get(i)[0].equals("Room") || input.get(i)[0].equals("Space"))
			{
				Room room = new Room(input.get(i)[1]);
				char charMap = (input.get(i)[2]).charAt(0);
				roomMap.put(charMap, room);
			}
			else {
				throw new BadConfigFormatException("Bad format in setup file");
			}
		}


	}

	//stub; load layout config
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException{
		FileReader file = new FileReader(layoutConfigFile);
		Scanner scan = new Scanner(file);
		ArrayList<String[]> input = new ArrayList<String[]>();
		String line;
		while(scan.hasNextLine()) {
			line = scan.nextLine();
			String[] row = line.split(",");
			input.add(row);
		}
		board = new BoardCell[input.size()][input.get(0).length];

		numRows = input.size();
		numCols = input.get(0).length;
		for(int i = 0; i < input.size(); i++) {
			if(input.get(i).length != numCols) {
				throw new BadConfigFormatException("Column contains empty values");
			}
			for(int j = 0; j < input.get(i).length; j++) {

				BoardCell cell = new BoardCell(i, j, input.get(i)[j]);
				if(!(roomMap.containsKey(cell.getInitial()))) {
					throw new BadConfigFormatException("Cell initial is not a room");
				}
				else {
					if(cell.isRoomCenter())
					{
						char init = cell.getInitial();
						roomMap.get(init).setCenterCell(cell);
					}
					else if(cell.isLabel()) 
					{
						char init = cell.getInitial();
						roomMap.get(init).setLabelCell(cell);
					}
					board[i][j] = cell;
				}
			}
		}

	}

	//stub; returns room given initial
	public Room getRoom(char roomInitial) {
		return roomMap.get(roomInitial);
	}

	//stub; returns room given cell
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}

	//stub; gets cell
	public BoardCell getCell(int row, int col) {
		if(row >= numRows || col >= numCols) {	
			return null;
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


}
