package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameSolutionTest {

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
	/* Accusation tests
	Tests include: 
	 solution that is correct
	 solution with wrong person
	 solution with wrong weapon
	 solution with wrong room */
	public void testAccusation() {
		board.setAnswer(peacockCard, laboratoryCard, knifeCard);
		//Check if accusation is right (it should be right)
		assertTrue(board.checkAccusation(new Solution(peacockCard, laboratoryCard, knifeCard)));
		
		//Check if accusation is wrong (it should be wrong)
		assertFalse(board.checkAccusation(new Solution(greenCard, laboratoryCard, knifeCard)));
		assertFalse(board.checkAccusation(new Solution(peacockCard, bathroomCard, knifeCard)));
		assertFalse(board.checkAccusation(new Solution(peacockCard, laboratoryCard, ropeCard)));
	}
	
	@Test
	/* Player disproves a suggestion 
	Tests include: 
	  	If player has only one matching card it should be returned
		If players has >1 matching card, returned card should be chosen randomly
		If player has no matching cards, null is returned */
	public void testSuggestion() {
		Player player = new ComputerPlayer("testPlayer", "RED", 0, 0, board);
		player.updateHand(peacockCard);
		player.updateHand(visitorCenterCard);
		player.updateHand(wrenchCard);
		
		//If player has only one matching card it should be returned
		Solution testSol = new Solution(peacockCard, bathroomCard, ropeCard);
		assertTrue(peacockCard.equals(player.disproveSuggestion(testSol)));
		
		//If players has >1 matching card, returned card should be chosen randomly
		testSol = new Solution(peacockCard, visitorCenterCard, ropeCard);
		assertTrue(peacockCard.equals(player.disproveSuggestion(testSol)) || 
				visitorCenterCard.equals(player.disproveSuggestion(testSol)));
		
		//If player has no matching cards, null is returned
		testSol = new Solution(scarlettCard, bathroomCard, ropeCard);
		assertTrue(null == player.disproveSuggestion(testSol));
	}
	
	@Test
	/* Handle a suggestion made, 
	Tests include:
		Suggestion no one can disprove returns null
		Suggestion only accusing player can disprove returns null
		Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
		Suggestion that two players can disprove, correct player (based on starting with next player in list) 
			returns answer */
	public void testCreateSuggestion() {
		//Setting up tests
		Player testPlayer2 = new HumanPlayer("testPlayer2", "RED", 0, 0, board);
		Player testPlayer1 = new ComputerPlayer("testPlayer1", "GREEN", 0, 0, board);
		Player testPlayer3 = new ComputerPlayer("testPlayer3", "BLUE", 0, 0, board);
		Player testPlayer4 = new ComputerPlayer("testPlayer4", "MAGENTA", 0, 0, board);
		Player testPlayer5 = new ComputerPlayer("testPlayer5", "YELLOW", 0, 0, board);
		Player testPlayer6 = new ComputerPlayer("testPlayer6", "WHITE", 0, 0, board);
		
		Solution solution = new Solution(mustardCard, classroomCard, revolverCard);
		
		testPlayer1.updateHand(bathroomCard);
		testPlayer1.updateHand(ropeCard);
		testPlayer1.updateHand(wrenchCard);
		
		testPlayer2.updateHand(visitorCenterCard);
		testPlayer2.updateHand(scarlettCard);
		testPlayer2.updateHand(peacockCard);
		
		testPlayer3.updateHand(laboratoryCard);
		testPlayer3.updateHand(lectureHallCard);
		testPlayer3.updateHand(studentCenterCard);
		
		testPlayer4.updateHand(recCenterCard);
		testPlayer4.updateHand(dormitoryCard);
		testPlayer4.updateHand(diningRoomCard);
		
		testPlayer5.updateHand(candlestickCard);
		testPlayer5.updateHand(leadPipeCard);
		testPlayer5.updateHand(plumCard);

		testPlayer6.updateHand(whiteCard);
		testPlayer6.updateHand(greenCard);
		testPlayer6.updateHand(knifeCard);
		
		ArrayList<Player> testPlayers = new ArrayList<Player>();
		testPlayers.add(testPlayer1);
		testPlayers.add(testPlayer2);
		testPlayers.add(testPlayer3);
		testPlayers.add(testPlayer4);
		testPlayers.add(testPlayer5);
		testPlayers.add(testPlayer6);
		
		//Suggestion no one can disprove returns null
		board.setPlayers(testPlayers);
		assertEquals(null, board.handleSuggestion(0, solution));
		
		//Suggestion only accusing player can disprove returns null
		Solution sol2 = new Solution(mustardCard, bathroomCard, revolverCard);
		assertEquals(null, board.handleSuggestion(0, sol2));
		
		//Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
		Solution sol3 = new Solution(mustardCard, visitorCenterCard, revolverCard);
		assertEquals(visitorCenterCard, board.handleSuggestion(0, sol3));
		
		//Suggestion that two players can disprove, correct player (based on starting with next player in list)
		Solution sol4 = new Solution(scarlettCard, bathroomCard, revolverCard);
		assertEquals(bathroomCard, board.handleSuggestion(4, sol4));
	}
	
	
}
