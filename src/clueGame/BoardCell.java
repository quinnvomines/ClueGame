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

		public char getSecretPassage() {
			return secretPassage;
		}

		public void setSecretPassage(char secretPassage) {
			this.secretPassage = secretPassage;
		}

		public boolean isDoorway() {
			return false;
		}

		public DoorDirection getDoorDirection() {
			return doorDirection;
		}

		public boolean isLabel() {
			return roomLabel;
		}

		public boolean isRoomCenter() {
			return roomCenter;
		}

		
}
