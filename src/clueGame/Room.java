package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BoardCell getLabelCell() {
		return new BoardCell(0, 0);
	}
	
	public BoardCell getCenterCell() {
		return new BoardCell(0, 0);
	}
	
	
}
