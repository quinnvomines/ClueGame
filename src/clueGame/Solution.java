package clueGame;

public class Solution {
	public Card person;
	public Card room;
	public Card weapon;
	
	//Constructor
	public Solution(Card person, Card room, Card weapon) {
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	//Get person
	public Card getPerson() {
		return person;
	}

	//Get room
	public Card getRoom() {
		return room;
	}

	//Get weapon
	public Card getWeapon() {
		return weapon;
	}
	
}
