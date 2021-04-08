package tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;


public class FileInitTests {
	private final static int COLS = 24;
	private final static int ROWS = 31;

	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}
	
	//Test the first and last cells to ensure data is correct
	@Test
	public void testFirstAndLastCells() {
		//First cell
		BoardCell cell = board.getCell(0, 0);
		assertTrue(cell != null);
		
		//Last cell
		cell = board.getCell(30, 23);
		assertTrue(cell != null);
		
		//Should be null since cell doesn't exist
		cell = board.getCell(31, 24);
		assertTrue(cell == null);
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(ROWS, board.getNumRows());
		assertEquals(COLS, board.getNumColumns());
	}
	
	
	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(12, numDoors);
	}

	@Test
	public void testRoomLabels() {
		// Check all rooms are loaded
		assertEquals("Lecture Hall", board.getRoom('L').getName() );
		assertEquals("Classroom", board.getRoom('C').getName() );
		assertEquals("Bathroom", board.getRoom('B').getName() );
		assertEquals("Laboratory", board.getRoom('A').getName() );
		assertEquals("Student Center", board.getRoom('S').getName() );
		assertEquals("Dormitory", board.getRoom('D').getName() );
		assertEquals("Dining Room", board.getRoom('E').getName() );
		assertEquals("Visitor Center", board.getRoom('O').getName() );
		assertEquals("Recreation Center", board.getRoom('R').getName() );
	}

	@Test
	public void testRooms() {
		
		//test a secret passage from Dormitory to Laboratory
		BoardCell cell = board.getCell(27, 7);
		if(cell == null) {
			System.out.println("null");
		}
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Dormitory" ) ;
		assertTrue( cell.getSecretPassage() == 'A' );

		//test a secret passage from Laboratory to Dormitory
		cell = board.getCell(0, 17);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Laboratory" ) ;
		assertTrue( cell.getSecretPassage() == 'D' );

		// test a closet
		cell = board.getCell(15, 10);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );

		// Test label cell for Student Center
		cell = board.getCell(18, 2);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Student Center" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Student Center
		cell = board.getCell(20, 4);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Student Center" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );

		// Test label cell for Classroom
		cell = board.getCell(25, 14);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Classroom" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Classroom
		cell = board.getCell(26, 14);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Classroom" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );


		// Test label cell for Lecture Hall
		cell = board.getCell(3, 2);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Lecture Hall" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Lecture Hall
		cell = board.getCell(4, 2);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Lecture Hall" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );

		// Test label cell for Bathroom
		cell = board.getCell(1, 11);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Bathroom" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Bathroom
		cell = board.getCell(2, 11);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Bathroom" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );	

		// Test label cell for Laboratory
		cell = board.getCell(3, 19);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Laboratory" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Laboratory
		cell = board.getCell(4, 19);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Laboratory" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );	

		// Test label cell for Recreation Center
		cell = board.getCell(19, 18);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Recreation Center" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Recreation Center
		cell = board.getCell(25, 22);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Recreation Center" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );

		// Test label cell for Dormitory
		cell = board.getCell(29, 3);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Dormitory" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Dormitory
		cell = board.getCell(30, 3);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Dormitory" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );

		// Test label cell for Dining Room
		cell = board.getCell(8, 9);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Dining Room" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Dining Room
		cell = board.getCell(9, 10);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Dining Room" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );

		// Test label cell for Visitor Center
		cell = board.getCell(12, 18);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Visitor Center" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		// Test center cell for Visitor Center
		cell = board.getCell(13, 18);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Visitor Center" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );

	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus
	// two cells that are not a doorway.
	@Test
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(11, 2);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(20, 2);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(21, 17);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCell(24, 20);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(15, 0);
		assertFalse(cell.isDoorway());
		cell = board.getCell(14,10);
		assertFalse(cell.isDoorway());
	}

}
