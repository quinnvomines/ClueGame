package clueGame;

import java.util.Map;

public class ComputerPlayer extends Player{
	
	public ComputerPlayer(String name, String color, int row, int col, Board board){
		super(name, color, row, col, board);
	}
	
	//STUB; create a solution
	public Solution createSuggestion() {
		return new Solution(new Card("name", CardType.PERSON), new Card("name", CardType.ROOM), new Card("name", CardType.WEAPON));
	}
	
	//STUB; select targets
	public BoardCell selectTargets(int moveDistance, Board board) {
		return new BoardCell(0, 0, "stub");
	}
	
}
