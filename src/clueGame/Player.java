package clueGame;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class Player {
	private String name;
	private Color color;
	protected int row;
	protected int col;
	
	private ArrayList<Card> hand;
	
	//Constructor
	public Player(String name, String color, int row, int col) {
		this.name = name;
		this.row = row;
		this.col = col;
		
		hand = new ArrayList<Card>();
		
		//Get color given a string
		try {
			Field f = Color.class.getField(color);
			this.color = (Color)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
			this.color = null;
		}
		
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
