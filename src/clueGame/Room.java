package clueGame;

public class Room {
	//Instance variables
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	//Returns name
	public String getName() {
		return name;
	}
	
	//Sets name
	public void setName(String name) {
		this.name = name;
	}
	
	//stub; gets cell label
	public BoardCell getLabelCell() {
		return new BoardCell(0, 0);
	}
	
	//stub; gets center cell
	public BoardCell getCenterCell() {
		return new BoardCell(0, 0);
	}
	
	
}
