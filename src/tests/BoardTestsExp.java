package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;

/*
 * @author Quinn Vo
 * @author Luc Lafave
 */
class BoardTestsExp {

	TestBoard board;
	
	@BeforeEach
	//create a new 4x4 board
	public void setUp() {
		board = new TestBoard();
	}
	
	@Test
	public void testAdjacency() {
		//Top left corner
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
		
		//Bottom right corner
		cell = board.getCell(3, 3);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(2, testList.size());
		
		//Right edge
		cell = board.getCell(1, 3);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(3, testList.size());
		
		//Left edge
		cell = board.getCell(3, 0);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(3, 1)));
		assertEquals(2, testList.size());
		
		//Center of board
		cell = board.getCell(2, 2);
		testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(2, testList.size());
		
	}
	
	@Test
	public void testTargetsNormal() {
		//Start at (0,0); calculate targets with 6 steps 
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		
		//Start at (0,1); calculate targets with 2 steps 
		cell = board.getCell(0, 1);
		board.calcTargets(cell, 2);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		
		//Start at (2,1); calculate targets with 1 steps 
		cell = board.getCell(2, 1);
		board.calcTargets(cell, 1);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		
		
	}
	
	@Test
	public void testTargetsMixed() {
		//start at (0, 3); calculate targest within 3 steps with an occupied cell at (0,2)
		board.getCell(0, 2).setOccupied(true);
		board.getCell(1, 2).setRoom(true);
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		
		
		//start at (3, 2); calculate target within 2 steps with an occupied cell at (3,0)
		board.getCell(3, 0).setOccupied(true);
		board.getCell(2, 3).setRoom(true);
		cell = board.getCell(3, 2);
		board.calcTargets(cell, 2);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		
	}

}
