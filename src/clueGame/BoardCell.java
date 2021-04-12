package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {

	//Instance variables
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private boolean isSecretPassage;
	private boolean inRoom;
	private boolean occupied;
	private boolean unused;
	private boolean isDoorway;
	private boolean flagTarget;
	private int x;
	private int y;
	private int width;
	private int height;

	private int row; //Location of cell on board
	private int column; //Location of cell on board

	private Set<BoardCell> adjMtx;

	public BoardCell(int row, int col, String cellValue) {
		adjMtx = new HashSet<BoardCell>();

		this.row = row;
		this.column = col;
		this.initial = cellValue.charAt(0);
		isSecretPassage = false;

		flagTarget = false;

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

		switch(cellValue.charAt(1)) {
		case '^':
			doorDirection = DoorDirection.UP;
			roomCenter = false;
			roomLabel = false;
			inRoom = false;
			isDoorway = true;
			break;
		case '<':
			doorDirection = DoorDirection.LEFT;
			roomCenter = false;
			roomLabel = false;
			inRoom = false;
			isDoorway = true;
			break;
		case '>':
			doorDirection = DoorDirection.RIGHT;
			roomCenter = false;
			roomLabel = false;
			inRoom = false;
			isDoorway = true;
			break;
		case 'v':
			doorDirection = DoorDirection.DOWN;
			roomCenter = false;
			roomLabel = false;
			inRoom = false;
			isDoorway = true;
			break;
		case '*': //Room Center
			doorDirection = DoorDirection.NONE;
			roomCenter = true;
			roomLabel = false;
			inRoom = true;
			break;
		case '#'://Room Label
			doorDirection = DoorDirection.NONE;
			roomCenter = false;
			roomLabel = true;
			inRoom = true;
			break;
		default://If secret passage
			roomCenter = false;
			roomLabel = false;
			inRoom = true;
			doorDirection = DoorDirection.NONE;
			isSecretPassage = true;
			secretPassage = cellValue.charAt(1);
			break;

		}

		occupied = false; //Set all rooms that are longer than 1 to not be occupied or unused
		unused = false;

	}

	public void draw(Graphics g, double height, double width, double startRowLoc, double startColLoc) {
		x = (int)startColLoc;
		y = (int)startRowLoc;
		this.height = (int) height;
		this.width = (int) width;
		if(!inRoom) {
			g.setColor(Color.BLACK);
			if(unused) {
				//Unused
				g.fillRect((int) startColLoc, (int) startRowLoc, (int) width, (int) height);
			} 
			else {
				//Walkway cell
				if(flagTarget) {
					g.setColor(Color.CYAN);
				}
				else {
					g.setColor(Color.YELLOW);
				}
				g.fillRect((int) startColLoc, (int) startRowLoc, (int) width, (int) height); //Fill yellow
				g.setColor(Color.BLACK);
				g.drawRect((int) startColLoc, (int) startRowLoc, (int) width, (int) height); //Draw border
			}
		} else {
			//Room
			if(flagTarget) {
				g.setColor(Color.CYAN);
			}
			else {
				g.setColor(Color.GRAY);
			}
			g.fillRect((int) startColLoc, (int) startRowLoc, (int) width, (int) height); 
		}
	}
	public boolean containsClick(int mouseX, int mouseY) {
		Rectangle rect = new Rectangle(x,y,width,height);
		if(rect.contains(new Point(mouseX,mouseY))) {
			return true;
		}
		return false;
	}

	public boolean isFlagTarget() {
		return flagTarget;
	}

	//Returns whether it is a secret passage
	public boolean isSecretPassage() {
		return isSecretPassage;
	}


	//Gets initial
	public char getInitial() {
		return initial;
	}

	//Getter for unused
	public boolean isUnused() {
		return unused;
	}

	//Add a cell to the adjacency list
	public void addAdjacency( BoardCell cell ) {
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

	//stub; get adjacency list
	public Set<BoardCell> getAdjList(){
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
		return "BoardCell [row=" + row + ", column=" + column + "] ";
	}

	//return if cell secret passage
	public char getSecretPassage() {
		return secretPassage;
	}

	//set cell to secret passage
	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	//return if cell is doorway
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
	public void setTarget(boolean b) {
		flagTarget = b;
	}

}
