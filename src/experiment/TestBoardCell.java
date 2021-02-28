package experiment;

import java.util.HashSet;
import java.util.Set;
/*
 * @author Quinn Vo
 * @author Luc Lafave
 */
public class TestBoardCell {
	//Location of cell on board
	private int row;
	private int column;
	
	private boolean inRoom;
	private Set<TestBoardCell> adjMtx;
	private boolean occupied;
	
	//Give cell location and create its adjacency list
	public TestBoardCell(int row, int column) {
		this.row = row;
		this.column = column;
		
		//Create adjacency list
		adjMtx = new HashSet<TestBoardCell>();


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
	
	
}
