package clueGame;

import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {
	//Location of cell on board
	private int row;
	private int column;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private boolean inRoom;
	private Set<TestBoardCell> adjMtx;
	private boolean occupied;

	public BoardCell(int row, int col) {

	}

	//Add a cell to the adjacency list
	public void addAdjacency( TestBoardCell cell ) {
		adjMtx.add(cell);
	}
	
	public int getRow() {
		return row;
	}

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
		return false;
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
