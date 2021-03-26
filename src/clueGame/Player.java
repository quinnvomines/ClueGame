package clueGame;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public abstract class Player {
	private String name;
	private Color color;
	private Room room;
	protected int row;
	protected int col;
	
	private ArrayList<Card> seenCards;
	private ArrayList<Card> hand;
	
	//Constructor
	public Player(String name, String color, int row, int col, Board board) {
		this.name = name;
		this.row = row;
		this.col = col;
		
		this.room = null;
		for(Map.Entry<Character, Room> entry: board.getRoomMap().entrySet()) {
			if(entry.getKey() != 'W' && entry.getKey() != 'X') {
				if(entry.getValue().getCenterCell().getRow() == row && entry.getValue().getCenterCell().getColumn() == col) {
					room = entry.getValue();
				}
			}
		}
		
		hand = new ArrayList<Card>();
		seenCards = new ArrayList<Card>();
		
		//Get color given a string
		try {
			Field f = Color.class.getField(color);
			this.color = (Color)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
			this.color = null;
		}
		
	}
	
	public Room getRoom() {
		return room;
	}

	//STUB; disprove a suggestion
	//Takes in a Solution and compares it to each card in the player's hand to find if there are any matches
	//Returns Card that matches if there is one, otherwise returns null
	public Card disproveSuggestion(Solution testSolution) {
		return new Card("name", CardType.ROOM);
	}
	
	//updateSeen
	public void updateSeen(Card seenCard) {
		seenCards.add(seenCard);
	}
	
	//returns hand
	public ArrayList<Card> getHand() {
		return hand;
	}

	//Add card in hand
	public void updateHand(Card card) {
		hand.add(card);
	}
	
	//Get name
	public String getName() {
		return name;
	}
	
	//Get color
	public Color getColor() {
		return color;
	}
	
	//Get row
	public int getRow() {
		return row;
	}
	
	//get col
	public int getCol() {
		return col;
	}
	
	
}
