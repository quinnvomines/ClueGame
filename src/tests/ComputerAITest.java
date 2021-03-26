package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.Solution;

public class ComputerAITest {
	
	private static Board board;
	private static Card classroomCard, lectureHallCard, laboratoryCard, bathroomCard, studentCenterCard,
		recCenterCard, dormitoryCard, diningRoomCard, visitorCenterCard, scarlettCard, mustardCard, whiteCard, 
		greenCard, peacockCard, plumCard, candlestickCard, knifeCard, ropeCard, wrenchCard, revolverCard, leadPipeCard;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		
		classroomCard = new Card("Classroom", CardType.ROOM);
		lectureHallCard = new Card("Lecture Hall", CardType.ROOM);
		laboratoryCard = new Card("Laboratory", CardType.ROOM);
		bathroomCard = new Card("Bathroom", CardType.ROOM);
		studentCenterCard = new Card("Student Center", CardType.ROOM);
		recCenterCard = new Card("Recreation Center", CardType.ROOM);
		dormitoryCard = new Card("Dormitory", CardType.ROOM);
		visitorCenterCard = new Card("Visitor Center", CardType.ROOM);
		diningRoomCard = new Card("Dining Room", CardType.ROOM);
		
		scarlettCard = new Card("Miss Scarlett", CardType.PERSON);
		mustardCard = new Card("Colonel Mustard", CardType.PERSON);
		plumCard = new Card("Professor Plum", CardType.PERSON);
		greenCard = new Card("Reverend Green", CardType.PERSON);
		peacockCard = new Card("Mrs Peacock", CardType.PERSON);
		whiteCard = new Card("Mrs White", CardType.PERSON);
		
		candlestickCard = new Card("Candlestick", CardType.WEAPON);
		wrenchCard = new Card("Wrench", CardType.WEAPON);
		ropeCard = new Card("Rope", CardType.WEAPON);
		revolverCard = new Card("Revolver", CardType.WEAPON);
		leadPipeCard = new Card("Leadpipe", CardType.WEAPON);
		knifeCard = new Card("Knife", CardType.WEAPON);
	}
	
	@Test
	/* Computer player create a suggestion 
	Tests include:
		Room matches current location
		If only one weapon not seen, it's selected
		If only one person not seen, it's selected (can be same test as weapon)
		If multiple weapons not seen, one of them is randomly selected
		If multiple persons not seen, one of them is randomly selected */
	public void testSuggestion() {
		//Room matches current location
		ComputerPlayer compPlayer1 = new ComputerPlayer("compPlayer1", "RED", 4, 19, board);
		Solution testSol = compPlayer1.createSuggestion();
		assertTrue(testSol.getRoom().getCardName().equals(compPlayer1.getRoom().getName()));
		
		//If only one weapon not seen, it's selected
		ComputerPlayer compPlayer2 = new ComputerPlayer("compPlayer2", "GREEN", 4, 19, board);
		compPlayer2.updateSeen(knifeCard);
		compPlayer2.updateSeen(leadPipeCard);
		compPlayer2.updateSeen(wrenchCard);
		compPlayer2.updateSeen(ropeCard);
		compPlayer2.updateSeen(revolverCard);
		
		testSol = compPlayer2.createSuggestion();
		assertTrue(testSol.getWeapon().getCardName().equals(candlestickCard.getCardName()));
		
		//If only one person not seen, it's selected (can be same test as weapon)
		ComputerPlayer compPlayer3 = new ComputerPlayer("compPlayer3", "YELLOW", 4, 19, board);
		compPlayer3.updateSeen(plumCard);
		compPlayer3.updateSeen(scarlettCard);
		compPlayer3.updateSeen(peacockCard);
		compPlayer3.updateSeen(mustardCard);
		compPlayer3.updateSeen(whiteCard);
		
		testSol = compPlayer3.createSuggestion();
		assertTrue(testSol.getPerson().getCardName().equals(greenCard.getCardName()));
		
		//If multiple weapons not seen, one of them is randomly selected
		ComputerPlayer compPlayer4 = new ComputerPlayer("compPlayer4", "YELLOW", 4, 19, board);
		compPlayer4.updateSeen(knifeCard);
		compPlayer4.updateSeen(leadPipeCard);
		compPlayer4.updateSeen(wrenchCard);
		compPlayer4.updateSeen(ropeCard);
		
		testSol = compPlayer4.createSuggestion();
		assertTrue(testSol.getWeapon().getCardName().equals(candlestickCard.getCardName()) 
				|| testSol.getWeapon().getCardName().equals(revolverCard.getCardName()));
		
		//If multiple persons not seen, one of them is randomly selected
		ComputerPlayer compPlayer5 = new ComputerPlayer("compPlayer5", "YELLOW", 4, 19, board);
		compPlayer5.updateSeen(scarlettCard);
		compPlayer5.updateSeen(plumCard);
		compPlayer5.updateSeen(peacockCard);
		compPlayer5.updateSeen(mustardCard);
		
		testSol = compPlayer5.createSuggestion();
		assertTrue(testSol.getPerson().getCardName().equals(greenCard.getCardName()) 
				|| testSol.getPerson().getCardName().equals(whiteCard.getCardName()));
		
	}
	
	@Test
	/* Computer player select a target
	Tests include:
		if no rooms in list, select randomly
		if room in list that has not been seen, select it
		if room in list that has been seen, each target (including room) selected randomly */
	public void testTargets() {
		//If no rooms in list, select randomly
		ComputerPlayer compPlayer1 = new ComputerPlayer("compPlayer1", "YELLOW", 15, 5, board);
		BoardCell cell = compPlayer1.selectTargets(1, board);
		assertTrue(cell.getRow() == 14 && cell.getColumn() == 5 || cell.getRow() == 16 && cell.getColumn() == 5
				|| cell.getRow() == 15 && cell.getColumn() == 4 || cell.getRow() == 15 && cell.getColumn() == 6);
		
		//If room in list that has not been seen, select it
		ComputerPlayer compPlayer2 = new ComputerPlayer("compPlayer2", "YELLOW", 10, 8, board);
		cell = compPlayer2.selectTargets(1, board);
		assertTrue(cell.getRow() == 9 && cell.getColumn() == 10);
		
		//If room in list that has been seen, each target (including room) selected randomly
		ComputerPlayer compPlayer3 = new ComputerPlayer("compPlayer3", "YELLOW", 10, 8, board);
		compPlayer3.updateSeen(diningRoomCard);
		cell = compPlayer3.selectTargets(1, board);
		assertTrue(cell.getRow() == 9 && cell.getColumn() == 10 || cell.getRow() == 11 && cell.getColumn() == 8
				|| cell.getRow() == 10 && cell.getColumn() == 7 || cell.getRow() == 9 && cell.getColumn() == 8);
	}
	
}
