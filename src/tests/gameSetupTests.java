package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;


import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import experiment.TestBoard;

public class gameSetupTests {

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

	@Test
	//Load people and weapons from ClueSetup.txt and insure the data was loaded properly
	public void testLoad() {
		ArrayList<Player> players = board.getPlayers();
		assertEquals(6, players.size());

		//Test for Miss Scarlett
		assertEquals(players.get(0).getName(), "Miss Scarlett");
		assertEquals(players.get(0).getRow(), 1);
		assertEquals(players.get(0).getCol(), 7);
		assertEquals(players.get(0).getColor(), Color.RED);

		//Test for Professor Plum
		assertEquals(players.get(5).getName(), "Professor Plum");
		assertEquals(players.get(5).getRow(), 29);
		assertEquals(players.get(5).getCol(), 14);
		assertEquals(players.get(5).getColor(), Color.MAGENTA);

		//Test for Mrs Peacock
		assertEquals(players.get(4).getName(), "Mrs Peacock");
		assertEquals(players.get(4).getRow(), 16);
		assertEquals(players.get(4).getCol(), 23);
		assertEquals(players.get(4).getColor(), Color.BLUE);
	}

	//Create Player class with human and computer child classes.   
	//Use people data to instantiate 6 players (1 human and 5 computer) 
	public void testPlayers() {
		ArrayList<Player> players = board.getPlayers();
		int numHuman = 0;
		int numComputer = 0;
		
		//Loop through players
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i) instanceof HumanPlayer) { //Checks if human player
				numHuman++;
			} else if(players.get(i) instanceof ComputerPlayer) { //Checks if ComputerPlayer
				numComputer++;
			}
		}
		
		if(numHuman != 1 && numComputer != 5) {
			assert(false); //Assert false if there isn't 1 human, 5 computers
		}
		assert(true);//Otherwise assert true
	}

	@Test
	//Create complete deck of cards (weapons, people and rooms)
	public void testDeck() {
		ArrayList<Card> deck = board.getDeck();
		assertEquals(deck.size(), 21);
		assertEquals(deck.get(0).getCardName(), "Classroom");
		assertEquals(deck.get(10).getCardName(), "Colonel Mustard");
		assertEquals(deck.get(17).getCardName(), "Wrench");
	}


	@Test
	//Deal cards to the Answer and the players 
	//(all cards dealt, players have roughly same # of cards, no card dealt twice)
	public void testDealCards() {
		
		ArrayList<Player> players = board.getPlayers();
		if(players.size() == 0) {
			assert(false);
		} else {
			//Test that no card is dealt twice
			board.deal();
			Card testCard = players.get(0).getHand().get(0); //First card in player's hand
			for(int i = 1; i < players.size(); i++) {
				for(int j = 0; j < players.get(i).getHand().size(); j++) {
					//Check other players hands
					if(testCard.equals(players.get(i).getHand().get(j)) || players.size() == 0) {
						assert(false);
					}
				}
			}
			//Test players have same amount of cards
			assertEquals(players.get(0).getHand().size() , 3);
			assertEquals(players.get(2).getHand().size(), players.get(0).getHand().size());
			assert(true);
		}
		
	}
}
