package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Player {
	private String name;
	private Color color;
	protected int row;
	protected int col;
	
	private ArrayList<Card> hand;
	
	//STUB; returns hand
	public ArrayList<Card> getHand() {
		return new ArrayList<Card>();
	}

	public void updateHand(Card card) {
		
	}
	
	public String getName() {
		return name;
	}
	public Color getColor() {
		return color;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	
	
}
