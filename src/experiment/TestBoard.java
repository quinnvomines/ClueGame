package experiment;

import java.util.HashSet;
import java.util.Set;
/*
 * @author Quinn Vo
 * @author Luc Lafave
 */
public class TestBoard {
	//Constants
	private final static int COLS = 25;
	private final static int ROWS = 31;
	
	//board is 2d array of cells
	private TestBoardCell[][] board;
	//move targets list
	private Set<TestBoardCell> targetsList;
	//which cells have been visited by player
	Set<TestBoardCell> visitedList;
	
	
	//create a simple 4 by 4 board and fill the board with cells at their correct location on the board
	public TestBoard() {
		board = new TestBoardCell[ROWS][COLS];
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				board[row][col] = new TestBoardCell(row, col);
			}
		}
		
	}
	
	//figure out what locations the player can move to
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		visitedList = new HashSet<TestBoardCell>();
		targetsList = new HashSet<TestBoardCell>();
		
		visitedList.add(startCell);
		findAllTargets(startCell,pathLength);
	}
	
	//recursive function to find the locations the player can move to
	public void findAllTargets(TestBoardCell thisCell,int numSteps) {
		for (TestBoardCell c : thisCell.getAdjList()) {
			if(!(visitedList.contains(c)))
			{
				visitedList.add(c);
				if(numSteps == 1)
				{
					targetsList.add(c);
				}
				else
				{
					findAllTargets(c,numSteps - 1);
				}
			}
			
			visitedList.remove(c);
		}
	}
	
	//get current cell on board
	public TestBoardCell getCell( int row, int col ) {
		return board[row][col];
	}
	
	//returns a set of the possible target cells that the player could move to.
	public Set<TestBoardCell> getTargets(){
		return targetsList;
	}
	
}
