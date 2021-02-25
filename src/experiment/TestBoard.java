package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private TestBoardCell[][] board;
	private Set<TestBoardCell> targetsList;
	Set<TestBoardCell> visitedList;
	
	public TestBoard() {
		board = new TestBoardCell[4][4];
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				board[row][col] = new TestBoardCell(row,col);
			}
		}
	}
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		visitedList = new HashSet<TestBoardCell>();
		targetsList = new HashSet<TestBoardCell>();
		visitedList.add(startCell); 
		targetsList = findAllTargets(startCell, pathLength);
		
	}
	
	public Set<TestBoardCell> findAllTargets(TestBoardCell startCell,int pathLength) {
		
		return targetsList;
		
	}
	
	public TestBoardCell getCell( int row, int col ) {
		return board[row][col];
	}
	
	public Set<TestBoardCell> getTargets(){
		return targetsList;
	}
	
}
