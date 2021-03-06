package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// Location that is connected by secret passage
		Set<BoardCell> testList = board.getAdjList(30, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(27, 1)));
		assertTrue(testList.contains(board.getCell(4, 19)));

		// Location within rooms not center
		testList = board.getAdjList(6, 4);
		assertEquals(0, testList.size());
	}


	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		//Location that is doorway
		Set<BoardCell> testList = board.getAdjList(10, 8);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(9, 8)));
		assertTrue(testList.contains(board.getCell(10, 7)));
		assertTrue(testList.contains(board.getCell(11, 8)));
		assertTrue(testList.contains(board.getCell(9, 10)));

	}

	// Test a variety of walkway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		//Location with only walkways as adjacent locations
		Set<BoardCell> testList = board.getAdjList(6, 14);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(5, 14)));
		assertTrue(testList.contains(board.getCell(6, 13)));
		assertTrue(testList.contains(board.getCell(6, 15)));
		assertTrue(testList.contains(board.getCell(7, 14)));

		//top edge; Locations that are at each edge of the board
		testList = board.getAdjList(0, 15);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(0, 16)));
		assertTrue(testList.contains(board.getCell(1, 15)));

		// bottom edge; Locations that are at each edge of the board
		testList = board.getAdjList(29, 14);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(29, 13)));
		assertTrue(testList.contains(board.getCell(28, 14)));
		assertTrue(testList.contains(board.getCell(29, 15)));

		// left edge; Locations that are at each edge of the board
		testList = board.getAdjList(15, 0);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(14, 0)));
		assertTrue(testList.contains(board.getCell(15, 1)));
		assertTrue(testList.contains(board.getCell(16, 0)));

		// bottom edge; Locations that are at each edge of the board
		testList = board.getAdjList(15, 23);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(16, 23)));
		assertTrue(testList.contains(board.getCell(15, 22)));

		//Location that is beside a room cell that is not a doorway
		testList = board.getAdjList(7, 17);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(7, 18)));
		assertTrue(testList.contains(board.getCell(7, 16)));

		// Test next to closet
		testList = board.getAdjList(15,13);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(14, 13)));
		assertTrue(testList.contains(board.getCell(15, 14)));
		assertTrue(testList.contains(board.getCell(16, 13)));

	}


	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInClassroom() {
		// test a roll of 1
		board.calcTargets(board.getCell(26, 14), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(21, 17)));

		// test a roll of 3
		board.calcTargets(board.getCell(26, 14), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(21, 15)));
		assertTrue(targets.contains(board.getCell(20, 16)));	
		assertTrue(targets.contains(board.getCell(19, 17)));
		assertTrue(targets.contains(board.getCell(20, 18)));
		assertTrue(targets.contains(board.getCell(21, 19)));	
	}

	@Test
	public void testTargetsInLaboratory() {
		// test a roll of 1
		board.calcTargets(board.getCell(4, 19), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(7, 22)));
		assertTrue(targets.contains(board.getCell(30, 4)));	

		// test a roll of 3
		board.calcTargets(board.getCell(4, 19), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(7, 20)));
		assertTrue(targets.contains(board.getCell(26, 1)));	
		assertTrue(targets.contains(board.getCell(28, 8)));
		assertTrue(targets.contains(board.getCell(29, 9)));

	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(15, 19), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(13, 18)));
		assertTrue(targets.contains(board.getCell(15, 20)));	
		assertTrue(targets.contains(board.getCell(16, 19)));	

		// test a roll of 3
		board.calcTargets(board.getCell(15, 19), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(13, 18)));
		assertTrue(targets.contains(board.getCell(18, 19)));
		assertTrue(targets.contains(board.getCell(17, 20)));	
		assertTrue(targets.contains(board.getCell(16, 21)));
		assertTrue(targets.contains(board.getCell(15, 22)));	

	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 2
		board.calcTargets(board.getCell(15, 3), 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(15, 1)));
		assertTrue(targets.contains(board.getCell(14, 2)));
		assertTrue(targets.contains(board.getCell(13, 3)));
		assertTrue(targets.contains(board.getCell(14, 4)));
		assertTrue(targets.contains(board.getCell(15, 5)));
		assertTrue(targets.contains(board.getCell(16, 4)));
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(16, 2)));

		// test a roll of 1
		board.calcTargets(board.getCell(15, 3), 1);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(14, 3)));
		assertTrue(targets.contains(board.getCell(15, 2)));
		assertTrue(targets.contains(board.getCell(16, 3)));
		assertTrue(targets.contains(board.getCell(15, 4)));

	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(23, 6), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(22, 6)));
		assertTrue(targets.contains(board.getCell(23, 5)));
		assertTrue(targets.contains(board.getCell(23, 7)));
		assertTrue(targets.contains(board.getCell(24, 6)));

		// test a roll of 3
		board.calcTargets(board.getCell(23, 6), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(23, 3)));
		assertTrue(targets.contains(board.getCell(24, 4)));
		assertTrue(targets.contains(board.getCell(22, 4)));
		assertTrue(targets.contains(board.getCell(25, 5)));
		assertTrue(targets.contains(board.getCell(26, 6)));
		assertTrue(targets.contains(board.getCell(25, 7)));
		assertTrue(targets.contains(board.getCell(24, 8)));
		assertTrue(targets.contains(board.getCell(23, 9)));
		assertTrue(targets.contains(board.getCell(22, 8)));
		assertTrue(targets.contains(board.getCell(21, 7)));
		
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(15, 7).setOccupied(true);
		board.calcTargets(board.getCell(15, 3), 2);
		board.getCell(15, 7).setOccupied(false);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(15, 1)));
		assertTrue(targets.contains(board.getCell(14, 2)));
		assertTrue(targets.contains(board.getCell(14, 4)));
		assertTrue(targets.contains(board.getCell(15, 5)));
		assertTrue(targets.contains(board.getCell(16, 4)));
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(16, 2)));

		// we want to make sure we can get into Bathroom, even if flagged as occupied
		board.getCell(2, 11).setOccupied(true);
		board.calcTargets(board.getCell(3, 13), 1);
		board.getCell(2, 11).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(2, 11)));	
		assertTrue(targets.contains(board.getCell(4, 13)));	
		assertTrue(targets.contains(board.getCell(3, 14)));	

		// check leaving a room with a blocked doorway
		board.getCell(7, 19).setOccupied(true);
		board.calcTargets(board.getCell(4, 19), 2);
		board.getCell(7, 19).setOccupied(false);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(7, 18)));
		assertTrue(targets.contains(board.getCell(7, 20)));	
	}
}




