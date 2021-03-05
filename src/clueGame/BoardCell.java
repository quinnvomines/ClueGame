package clueGame;

import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {

	//Instance variables
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private boolean inRoom;
	private boolean occupied;
	private boolean unused;
	private boolean isDoorway;

	private int row; //Location of cell on board
	private int column; //Location of cell on board

	private Set<TestBoardCell> adjMtx;

	public BoardCell(int row, int col, String cellValue) {
		this.row = row;
		this.column = column;
		this.initial = cellValue.charAt(0);

		if(cellValue.length() == 1) {
			doorDirection = DoorDirection.NONE;
			if(initial == 'X') { //Unused
				unused = true;
				inRoom = false;
			}
			else if(initial != 'W') { //If not a walkway, must be in a room
				inRoom = true;
			}
			else {
				inRoom = false;
			}
			roomLabel = false;
			roomCenter = false;
			occupied = false;

			return;
		}

		//Set instance variables accordingly if more than 1 characters
		if(cellValue.charAt(1) == '^') { //Door points up
			doorDirection = DoorDirection.UP;
			roomCenter = false;
			roomLabel = false;
			inRoom = false;
			isDoorway = true;
		} else if(cellValue.charAt(1) == '<') { //Door points left
			doorDirection = DoorDirection.LEFT;
			roomCenter = false;
			roomLabel = false;
			inRoom = false;
			isDoorway = true;
		} else if(cellValue.charAt(1) == '>') { //Door points right
			doorDirection = DoorDirection.RIGHT;
			roomCenter = false;
			roomLabel = false;
			inRoom = false;
			isDoorway = true;
		} else if(cellValue.charAt(1) == 'v') { //Door points down
			doorDirection = DoorDirection.DOWN;
			roomCenter = false;
			roomLabel = false;
			inRoom = false;
			isDoorway = true;
		} else if(cellValue.charAt(1) == '*') { //Room center
			doorDirection = DoorDirection.NONE;
			roomCenter = true;
			roomLabel = false;
			inRoom = true;
		} else if(cellValue.charAt(1) == '#') { //Room label
			doorDirection = DoorDirection.NONE;
			roomCenter = false;
			roomLabel = true;
			inRoom = true;
		} else {
			roomCenter = false;
			roomLabel = false;
			inRoom = true;
			doorDirection = DoorDirection.NONE;
			secretPassage = cellValue.charAt(1);
		}
		occupied = false;
		unused = false;

	}

	//Gets initial
	public char getInitial() {
		return initial;
	}

	//Add a cell to the adjacency list
	public void addAdjacency( TestBoardCell cell ) {
		adjMtx.add(cell);
	}

	//Gets row
	public int getRow() {
		return row;
	}

	//Gets column
	public int getColumn() {
		return column;
	}

	//Returns a set of cells adjacent to this cell
	public Set<TestBoardCell> getAdjList(){
		return adjMtx;
	}

	//update what room the cell is in
	public void setRoom(boolean room) {
		this.inRoom = room;
	}

	//check if cell in room
	public boolean isRoom() {
		return inRoom;
	}

	//check if cell contains player
	public boolean getOccupied() {
		return occupied;
	}

	//set a player into the cell
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	//For testing purposes
	@Override
	public String toString() {
		return "TestBoardCell [row=" + row + ", column=" + column + "] ";
	}

	//return if cell secret passage
	public char getSecretPassage() {
		return secretPassage;
	}

	//set cell to secret passage
	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	//return if cell is doorawy
	public boolean isDoorway() {
		return isDoorway;
	}

	//check door direction of cell
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	//check room label of cell
	public boolean isLabel() {
		return roomLabel;
	}

	//check if cell is room center
	public boolean isRoomCenter() {
		return roomCenter;
	}


}
