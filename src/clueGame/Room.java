package clueGame;

public class Room {
	//Instance variables
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	//Constructor Room object
	public Room(String name) {
		this.name = name;
		
	}
	
	//Returns name
	public String getName() {
		return name;
	}
	
	//Sets name
	public void setName(String name) {
		this.name = name;
	}
	
	//Set center cell
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	//Set label cell
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	//Gets cell label
	public BoardCell getLabelCell() {
		return labelCell;
	}
	
	//Gets center cell
	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	public boolean equals(Room otherRoom) {
		return this.name.equals(otherRoom.getName());
	}

	//Testing purposes
	@Override
	public String toString() {
		return "Room [name=" + name + "]";
	}
	
	
	
	
}
