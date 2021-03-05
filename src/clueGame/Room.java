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
	
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	//stub; gets cell label
	public BoardCell getLabelCell() {
		return labelCell;
	}
	
	//stub; gets center cell
	public BoardCell getCenterCell() {
		return centerCell;
	}

	@Override
	public String toString() {
		return "Room [name=" + name + "]";
	}
	
	
	
	
}
